package org.dragonet.net.inf.mcpe.jraklib;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.dragonet.net.SessionManager;
import org.dragonet.net.inf.mcpe.NetworkChannel;
import org.dragonet.net.packet.minecraft.BatchPacket;
import org.dragonet.net.packet.minecraft.PEPacket;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import net.beaconpe.jraklib.JRakLib;
import net.beaconpe.jraklib.protocol.EncapsulatedPacket;
import net.beaconpe.jraklib.server.JRakLibServer;
import net.beaconpe.jraklib.server.ServerHandler;
import net.beaconpe.jraklib.server.ServerInstance;
import org.dragonet.net.inf.mcpe.PENetworkClient;
import org.dragonet.net.packet.minecraft.UpdateBlockPacket;
import org.dragonet.utilities.DragonetVersioning;

/**
 * The Interface for communicating with JRakLib.
 *
 * @author jython234
 */
public class JRakLibInterface implements ServerInstance {

    private JRakLibServer rakLibServer;

    private ServerHandler handler;

    @Getter
    private SessionManager manager;

    private final Map<String, PENetworkClient> clientMap = new ConcurrentHashMap<>();

    public JRakLibInterface(SessionManager manager, InetSocketAddress address) throws Exception {
        this.manager = manager;

        this.rakLibServer = new JRakLibServer(new JRakLibLogger(manager.getServer().getLogger()), address.getPort(), address.getHostString());
        this.handler = new ServerHandler(rakLibServer, this);
        if (rakLibServer.isAlive() == false || rakLibServer.isInterrupted() || rakLibServer.isShutdown()) {
            //DEAD
            throw new Exception("Faild to bind on port! ");
        }

        manager.getServer().getLogger().info("JRakLibPlus Server started on: " + address.toString());
    }
    
    private long lastTimeSendOption;

    public void onTick() {
        if(System.currentTimeMillis() - lastTimeSendOption > 1000){
            handler.sendOption("name", getServerName());
            lastTimeSendOption = System.currentTimeMillis();
        }
        while(handler.handlePacket());
    }

    private String getServerName() {
        return "MCPE;" + manager.getServer().getServer().getServerName() + " (Dragonet " + DragonetVersioning.DRAGONET_VERSION + ");" + DragonetVersioning.MINECRAFT_PE_PROTOCOL + ";" + DragonetVersioning.MINECRAFT_PE_VERSION + ";" + manager.getServer().getServer().getOnlinePlayers().size() + ";" + manager.getServer().getServer().getMaxPlayers();
    }

    public void shutdown() {
        rakLibServer.shutdown();
    }

    @Override
    public void openSession(String identifier, String address, int port, long clientID) {
        manager.getServer().getLogger().info("(" + identifier + ") New session with clientID: " + clientID);
        PENetworkClient client = new PENetworkClient(this, identifier, clientID, new InetSocketAddress(address, port));
        clientMap.put(identifier, client);
    }

    @Override
    public void closeSession(String identifier, String reason) {
        manager.getServer().getLogger().info("(" + identifier + ") Session closed with reason: " + reason);
        if (!clientMap.containsKey(identifier)) {
            return;
        }
        PENetworkClient client = clientMap.get(identifier);
        client.disconnect(reason);

        clientMap.remove(identifier);
    }

    @Override
    public void handleEncapsulated(String identifier, EncapsulatedPacket encapsulatedPacket, int flags) {
        if (!clientMap.containsKey(identifier)) {
            return;
        }
        byte[] data = encapsulatedPacket.buffer.array();
        manager.getServer().getLogger().info("(" + identifier + ") PACKET IN: " + dumpHexFromBytes(data));
        PENetworkClient client = clientMap.get(identifier);
        client.processPacketBuffer(data);
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
        if(packet == null) return;
        
        //DEBUG: BLOCK ALL UPDATEBLOCKPACKETS
        if(packet.getClass().equals(UpdateBlockPacket.class)) return;
        
        System.out.println("Sending packet: " + packet.getClass().getSimpleName());
        if (packet.getData() == null) {
            packet.encode();
        }
        if(packet.getData() == null){ //Faild to encode?
            return;
        }
        if (!(packet instanceof BatchPacket) && (packet.getData().length >= 512)) { //TODO: Compression threshold config
            BatchPacket bp = new BatchPacket();
            bp.packets.add(packet);
            bp.encode();
            sendPacket(session, bp, true);  //We don't f**cking those kinds of packets, memory will be fucked up! 
            return;
        }
        EncapsulatedPacket pk = new EncapsulatedPacket();
        pk.buffer = Unpooled.copiedBuffer(packet.getData());
        pk.messageIndex = 0;
        pk.reliability = 2;
        handler.sendEncapsulated(session.getRaknetSession(), pk, (byte) ((byte) 0 | (immediate || packet.isShouldSendImmidate() ? JRakLib.PRIORITY_IMMEDIATE : JRakLib.PRIORITY_NORMAL)));
    }

    public static String dumpHexFromBytes(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
            sb.append(" ");
        }
        return sb.toString();
    }

    @Override
    public void notifyACK(String string, int i) {
    }

    @Override
    public void exceptionCaught(String string, String string1) {
    }

    @Override
    public void handleOption(String string, String string1) {
    }

    @Override
    public void handleRaw(String string, int i, ByteBuf bb) {
    }
}
