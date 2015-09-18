package org.dragonet.net.inf.mcpe.jraklib;

import io.github.jython234.jraklibplus.protocol.raknet.EncapsulatedPacket;
import io.github.jython234.jraklibplus.protocol.raknet.Reliability;
import io.github.jython234.jraklibplus.server.NioSession;
import io.github.jython234.jraklibplus.server.RakNetServer;
import io.github.jython234.jraklibplus.server.ServerInterface;
import org.dragonet.net.SessionManager;
import org.dragonet.net.inf.mcpe.NetworkChannel;
import org.dragonet.net.packet.minecraft.BatchPacket;
import org.dragonet.net.packet.minecraft.PEPacket;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.dragonet.net.inf.mcpe.PENetworkClient;
import org.dragonet.utilities.DragonetVersioning;
import org.slf4j.LoggerFactory;

/**
 * The Interface for communicating with JRakLib.
 *
 * @author jython234
 */
public class JRakLibInterface implements ServerInterface {

    private RakNetServer rakLibServer;

    @Getter
    private SessionManager manager;

    private final Map<String, PENetworkClient> clientMap = new ConcurrentHashMap<>();

    public JRakLibInterface(SessionManager manager, InetSocketAddress address) throws Exception {
        RakNetServer.ServerOptions options = new RakNetServer.ServerOptions();
        options.maxPacketsPerTick = 1024;
        options.workerThreads = 16;
        options.disconnectInvalidProtocol = true;
        options.sendBufferSize = 8192;
        options.portChecking = true;
        options.name = "MCPE;" + manager.getServer().getServer().getServerName() + " (Dragonet " + DragonetVersioning.DRAGONET_VERSION + ");" + DragonetVersioning.MINECRAFT_PE_PROTOCOL + ";" + DragonetVersioning.MINECRAFT_PE_VERSION + ";-1;" + manager.getServer().getServer().getMaxPlayers();
        this.manager = manager;
        this.rakLibServer = new RakNetServer(LoggerFactory.getLogger("JRakLibPlus"), address, options, this);
        //rakLibServer.setName("MCPE;" + manager.getServer().getServer().getServerName() + " (Dragonet " + DragonetVersioning.DRAGONET_VERSION + ");" + DragonetVersioning.MINECRAFT_PE_PROTOCOL + ";MCPC " + DragonetVersioning.MINECRAFT_PC_VERSION + ", MCPE " + DragonetVersioning.MINECRAFT_PE_VERSION + ";-1;" + manager.getServer().getServer().getMaxPlayers());
        //^^^ Set the JRakLib Thread name to the Server list MOTD lol
        
        rakLibServer.start();
        manager.getServer().getLogger().info("JRakLibPlus Server started on: " + address.toString());
    }
    
    public void shutdown() {
        try {
            rakLibServer.shutdown();
        } catch (InterruptedException ex) {
        }
    }

    @Override
    public void sessionOpened(NioSession session) {
        String identifier = session.getAddress().toString();
        manager.getServer().getLogger().info("(" + identifier + ") New session with clientID: " + session.getID());
        PENetworkClient client = new PENetworkClient(this, session);
        clientMap.put(identifier, client);
    }

    @Override
    public void sessionClosed(NioSession session, String reason) {
        String identifier = session.getAddress().toString();
        manager.getServer().getLogger().info("(" + identifier + ") Session closed with reason: " + reason);
        if (!clientMap.containsKey(identifier)) {
            return;
        }
        PENetworkClient client = clientMap.get(identifier);
        client.disconnect(reason);

        clientMap.remove(identifier);
    }

    @Override
    public void handleEncapsulatedPacket(EncapsulatedPacket encapsulatedPacket, NioSession session) {
        String identifier = session.getAddress().toString();
        if (!clientMap.containsKey(identifier)) {
            return;
        }
        manager.getServer().getLogger().info("(" + identifier + ") PACKET IN: " + dumpHexFromBytes(encapsulatedPacket.payload));
        PENetworkClient client = clientMap.get(identifier);
        client.processPacketBuffer(encapsulatedPacket.payload);
    }

    /**
     * Wraps <code>bytes</code> into an encapsulated packet and sends it.
     *
     * @param session The Session this packet is being sent from.
     * @param packet The Packet being sent.
     * @param immediate If the packet should be sent immediately (no
     * compression, skips packet queues)
     */
    public void sendPacket(PENetworkClient session, PEPacket packet, boolean immediate) {
        if (packet.getData() == null) {
            packet.encode();
        }
        if (!immediate && !(packet instanceof BatchPacket) && (packet.getData().length >= 512)) { //TODO: Compression threshold config
            BatchPacket bp = new BatchPacket();
            bp.packets.add(packet);
            bp.encode();
            sendPacket(session, bp, false);
        }
        EncapsulatedPacket pk = new EncapsulatedPacket();
        pk.payload = packet.getData();
        pk.messageIndex = 0;
        if (packet.getChannel() != NetworkChannel.CHANNEL_NONE) {
            pk.reliability = Reliability.RELIABLE_ORDERED;
            pk.orderChannel = packet.getChannel().getAsByte();
            pk.orderIndex = 0;
        } else {
            pk.reliability = Reliability.RELIABLE;
        }
        session.getRaknetSession().addEncapsulatedToQueue(pk, immediate);
    }

    public static String dumpHexFromBytes(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b) + " ");
        }
        return sb.toString();
    }
}
