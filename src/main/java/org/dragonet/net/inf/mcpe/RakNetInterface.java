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
import net.glowstone.GlowServer;
import org.bukkit.Bukkit;
import org.dragonet.DragonetServer;
import org.dragonet.net.SessionManager;
import org.dragonet.net.packet.minecraft.BatchPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.raknet.RakNet;
import org.dragonet.raknet.protocol.EncapsulatedPacket;
import org.dragonet.raknet.server.RakNetServer;
import org.dragonet.raknet.server.ServerHandler;
import org.dragonet.raknet.server.ServerInstance;
import org.dragonet.raknet.server.Session;
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
        //this.handler.sendOption("name", name); TODO This
    }
    
    public void onTick(){
        int n = 0;
        //while(this.handler.handlePacket() && n < 4000) n++;
    }
    
    @Override
    public void openSession(Session rakSession) {
        PENetworkClient cli = new PENetworkClient(this, rakSession);
        clients.put(rakSession.getIdentifier(), cli);
    }

    @Override
    public void closeSession(Session rakSession, String reason) {
        PENetworkClient cli = clients.remove(rakSession.getIdentifier());
        if(cli == null) return;
        cli.disconnect(reason);
    }

    @Override
    public void handleEncapsulated(Session rakSession, EncapsulatedPacket packet, int flags) {
        PENetworkClient cli = clients.get(rakSession.getIdentifier());
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
    
    public void sendPacket(Session session, PEPacket packet, boolean needACK, boolean immediate){
        packet.encode();
        byte[] buffer = packet.getData();
        
        if(buffer.length > 1024 && !BatchPacket.class.isAssignableFrom(packet.getClass())){
            BatchPacket batch = new BatchPacket();
            batch.packets.add(packet);
            sendPacket(session, batch, needACK, immediate);
            return;
        }
        //TODO Take some things from this
        EncapsulatedPacket encapsulated = new EncapsulatedPacket();
        encapsulated.buffer = buffer;
        encapsulated.needACK = needACK;
        encapsulated.reliability = needACK ? (byte)2 : (byte)3;
        encapsulated.messageIndex = 0;
	    try {
		    session.addEncapsulatedToQueue(encapsulated); // TODO immediate or not
	    } catch (Exception e) {
		    e.printStackTrace();
	    }
	    //this.handler.sendEncapsulated(identifier, encapsulated, (needACK ? RakNet.FLAG_NEED_ACK : 0) | (immediate ? RakNet.PRIORITY_IMMEDIATE : RakNet.PRIORITY_NORMAL));
    }

	public String getServerName() {

		return "MCPE;"+
		sesMan.getServer().getServer().getServerName().replace(";", "\\;") + ";"+
		DragonetVersioning.MINECRAFT_PE_VERSION + ";"+
		"MCPC " + DragonetVersioning.MINECRAFT_PE_VERSION + ";"+
		Bukkit.getOnlinePlayers().size()+
		"100"; //TODO max players

	}

}
