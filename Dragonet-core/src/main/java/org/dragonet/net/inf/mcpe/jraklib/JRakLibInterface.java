package org.dragonet.net.inf.mcpe.jraklib;

import net.beaconpe.jraklib.JRakLib;
import net.beaconpe.jraklib.protocol.EncapsulatedPacket;
import net.beaconpe.jraklib.server.JRakLibServer;
import net.beaconpe.jraklib.server.ServerHandler;
import net.beaconpe.jraklib.server.ServerInstance;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.SessionManager;
import org.dragonet.net.inf.mcpe.NetworkChannel;
import org.dragonet.net.packet.minecraft.BatchPacket;
import org.dragonet.net.packet.minecraft.PEPacket;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * The Interface for communicating with JRakLib.
 *
 * @author jython234
 */
public class JRakLibInterface implements ServerInstance{
    private JRakLibServer rakLibServer;
    private ServerHandler handler;

    @Getter
    private SessionManager manager;
    
    private Map<DragonetSession, String> identifierMap = new HashMap<>();

    public JRakLibInterface(SessionManager manager, InetSocketAddress address){
        this.manager = manager;
        this.rakLibServer = new JRakLibServer(new JRakLibLogger(manager.getServer().getLogger()), address.getPort(), address.getHostString());
        this.handler = new ServerHandler(rakLibServer, this);
        manager.getServer().getLogger().info("JRakLib Server started on: "+address.toString());
    }

    @Override
    public void openSession(String identifier, String address, int port, long clientID) {
        manager.getServer().getLogger().debug("("+identifier+") New session with clientID: "+clientID);
        //TODO: Add your DragonetSession's, players here
    }

    @Override
    public void closeSession(String identifier, String reason) {
        manager.getServer().getLogger().debug("("+identifier+") Session closed with reason: "+reason);
        //TODO: Remove dragonet sessions, players here.
    }

    @Override
    public void handleEncapsulated(String identifier, EncapsulatedPacket encapsulatedPacket, int flags) {
        manager.getServer().getLogger().debug("("+identifier+") PACKET IN: "+dumpHexFromBytes(encapsulatedPacket.buffer));
        //TODO: Pass on these packets to the correct session.
    }

    @Override
    public void handleRaw(String address, int port, byte[] payload) {
        manager.getServer().getLogger().debug("("+address+":"+port+", RAW) PACKET IN: "+dumpHexFromBytes(payload));
        //TODO: Handle strange raw packets? Only recieved if the client is a custom client.
    }

    @Override
    public void notifyACK(String identifier, int identifierACK) {
        //Not needed
    }

    @Override
    public void handleOption(String option, String value) {
        //TODO: Handle bandwith usage here
    }

    /**
     * Wraps <code>bytes</code> into an encapsulated packet and sends it.
     * @param session The Session this packet is being sent from.
     * @param packet The Packet being sent.
     * @param immediate If the packet should be sent immediately (no compression, skips packet queues)
     */
    public void sendPacket(DragonetSession session, PEPacket packet, boolean immediate){
        if(identifierMap.containsKey(session)){
            if(packet.getData() == null){
                packet.encode();
            }
            if(!immediate && !(packet instanceof BatchPacket) && (packet.getData().length >= 512)){ //TODO: Compression threshold config
                BatchPacket bp = new BatchPacket();
                bp.packets.add(packet);
                bp.encode();
                sendPacket(session, bp, false);
            }
            EncapsulatedPacket pk = new EncapsulatedPacket();
            pk.buffer = packet.getData();
            pk.messageIndex = 0;
            if(packet.getChannel() != NetworkChannel.CHANNEL_NONE){
                pk.reliability = 3;
                pk.orderChannel = packet.getChannel().getAsByte();
                pk.orderIndex = 0;
            } else {
                pk.reliability = 3;
            }
            handler.sendEncapsulated(identifierMap.get(session), pk, (byte) ((byte) 0 | (immediate ? JRakLib.PRIORITY_IMMEDIATE : JRakLib.PRIORITY_NORMAL)));
        } else {
            throw new IllegalArgumentException("Invalid session: "+session.toString());
        }
    }

    public static String dumpHexFromBytes(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(String.format("%02X", b)+" ");
        }
        return sb.toString();
    }
}
