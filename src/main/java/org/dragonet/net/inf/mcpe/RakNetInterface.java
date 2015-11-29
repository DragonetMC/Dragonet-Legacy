/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.net.inf.mcpe;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.dragonet.net.SessionManager;
import org.dragonet.net.packet.minecraft.BatchPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.raknet.RakNet;
import org.dragonet.raknet.protocol.EncapsulatedPacket;
import org.dragonet.raknet.server.RakNetServer;
import org.dragonet.raknet.server.ServerHandler;
import org.dragonet.raknet.server.ServerInstance;
import org.dragonet.utilities.DragonetVersioning;

public class RakNetInterface implements ServerInstance {

    @Getter
    private final SessionManager sesMan;
    
    private final RakNetServer raknetServer;
    
    private final ServerHandler handler;

    private final Map<String, PENetworkClient> clients = Collections.synchronizedMap(new HashMap<String, PENetworkClient>());
    
    public RakNetInterface(SessionManager sesMan, String bindAddress, int port) {
        this.sesMan = sesMan;
        this.raknetServer = new RakNetServer(port, bindAddress);
        this.handler = new ServerHandler(raknetServer, this);
        
        String name = "MCPE;";
        name += sesMan.getServer().getServer().getServerName().replace(";", "\\;") + ";";
        name += DragonetVersioning.MINECRAFT_PE_VERSION + ";";
        name += "MCPC " + DragonetVersioning.MINECRAFT_PC_VERSION + ", MCPE " + DragonetVersioning.MINECRAFT_PE_VERSION + ";";
        name += "-1;-1";
        this.handler.sendOption("name", name);
    }
    
    public void onTick(){
        int n = 0;
        while(this.handler.handlePacket() && n < 4000) n++;
    }
    
    @Override
    public void openSession(String identifier, String address, int port, long clientID) {
        InetSocketAddress remoteAddress = new InetSocketAddress(address, port);
        PENetworkClient cli = new PENetworkClient(this, identifier, remoteAddress, clientID);
        clients.put(identifier, cli);
    }

    @Override
    public void closeSession(String identifier, String reason) {
        PENetworkClient cli = clients.remove(identifier);
        if(cli == null) return;
        cli.disconnect(reason);
    }

    @Override
    public void handleEncapsulated(String identifier, EncapsulatedPacket packet, int flags) {
        PENetworkClient cli = clients.get(identifier);
        if(cli == null) return;
        cli.processPacketBuffer(packet.buffer);
    }

    @Override
    public void handleRaw(String address, int port, byte[] payload) {
    }

    @Override
    public void notifyACK(String identifier, int identifierACK) {
    }

    @Override
    public void handleOption(String option, String value) {
    }

    public void shutdown(){
        raknetServer.shutdown();
        handler.shutdown();
    }
    
    public void sendPacket(String identifier, PEPacket packet, boolean needACK, boolean immediate){
        packet.encode();
        byte[] buffer = packet.getData();
        
        if(buffer.length > 1024 && !BatchPacket.class.isAssignableFrom(packet.getClass())){
            BatchPacket batch = new BatchPacket();
            batch.packets.add(packet);
            sendPacket(identifier, batch, needACK, immediate);
            return;
        }
        
        EncapsulatedPacket encapsulated = new EncapsulatedPacket();
        encapsulated.buffer = buffer;
        encapsulated.needACK = needACK;
        encapsulated.reliability = needACK ? (byte)2 : (byte)3;
        encapsulated.messageIndex = 0;
        this.handler.sendEncapsulated(identifier, encapsulated, (needACK ? RakNet.FLAG_NEED_ACK : 0) | (immediate ? RakNet.PRIORITY_IMMEDIATE : RakNet.PRIORITY_NORMAL));
    }
}
