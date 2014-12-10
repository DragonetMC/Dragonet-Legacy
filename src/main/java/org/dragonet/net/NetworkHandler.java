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
package org.dragonet.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import lombok.Getter;
import org.apache.commons.lang.ArrayUtils;
import org.dragonet.DragonetServer;
import org.dragonet.net.packet.RaknetDataPacket;
import org.dragonet.utilities.io.PEBinaryReader;
import org.dragonet.utilities.io.PEBinaryWriter;

public class NetworkHandler {

    public final static long serverID = 0x0000000012345678L;

    private @Getter
    DragonetServer server;
    private @Getter
    NonBlockUDPSocket udp;

    private HashMap<String, DragonetSession> sessions;

    public NetworkHandler(DragonetServer server, InetSocketAddress address) {
        this.server = server;
        this.sessions = new HashMap<>();
        this.udp = new NonBlockUDPSocket(this.server, address);
        this.udp.start();
    }

    public synchronized void onTick() {
        DatagramPacket packet = null;
        while ((packet = this.udp.receive()) != null) {
            this.processPacket(packet);
        }
        for (DragonetSession session : this.sessions.values()) {
            session.onTick();
        }
    }

    private void processPacket(DatagramPacket packet) {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(packet.getData()));
            int raknetPID = reader.readByte() & 0xFF;
            switch (raknetPID) {
                case RaknetConstants.ID_OPEN_CONNECTION_REQUEST_1:
                    reader.read(16); //MAGIC
                    reader.readByte(); //RakNet Protocol
                    short mtu = (short) ((packet.getLength() - 18) & 0xFFFF);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    PEBinaryWriter writer = new PEBinaryWriter(bos);
                    writer.writeByte(RaknetConstants.ID_OPEN_CONNECTION_REPLY_1);
                    writer.write(RaknetConstants.magic);
                    writer.writeLong(NetworkHandler.serverID);
                    writer.writeByte((byte) 0x00);
                    writer.writeShort(mtu);
                    this.udp.send(bos.toByteArray(), packet.getSocketAddress());
                    break;
                case RaknetConstants.ID_OPEN_CONNECTION_REQUEST_2:
                    reader.read(16);
                    reader.read(5);
                    reader.readShort();
                    short clientMTU = reader.readShort();
                    long clientID = reader.readLong();
                    ByteArrayOutputStream bos8 = new ByteArrayOutputStream();
                    PEBinaryWriter writer8 = new PEBinaryWriter(bos8);
                    writer8.writeByte(RaknetConstants.ID_OPEN_CONNECTION_REPLY_2);
                    writer8.write(RaknetConstants.magic);
                    writer8.writeLong(NetworkHandler.serverID);
                    writer8.writeShort((short) (packet.getPort() & 0xFFFF));
                    writer8.writeShort(clientMTU);
                    writer8.writeByte((byte) 0x00);
                    this.send(bos8.toByteArray(), packet.getSocketAddress());
                    DragonetSession session = new DragonetSession(this.server, packet.getSocketAddress(), clientID, clientMTU);
                    this.sessions.put(packet.getSocketAddress().toString(), session);
                    //this.server.getServer().getSessionRegistry().add(session);
                    break;
                case 0x80:
                case 0x81:
                case 0x82:
                case 0x83:
                case 0x84:
                case 0x85:
                case 0x86:
                case 0x87:
                case 0x88:
                case 0x89:
                case 0x8A:
                case 0x8B:
                case 0x8C:
                case 0x8D:
                case 0x8E:
                case 0x8F:
                    if (this.sessions.containsKey(packet.getSocketAddress().toString())) {
                        RaknetDataPacket dataPacket = new RaknetDataPacket(ArrayUtils.subarray(packet.getData(), 1, packet.getLength()));
                        dataPacket.decode();
                        this.sessions.get(packet.getSocketAddress().toString()).processDataPacket(dataPacket);
                    }
                    break;
                case 0xC0:
                    if (this.sessions.containsKey(packet.getSocketAddress().toString())) {
                        this.sessions.get(packet.getSocketAddress().toString()).processACKPacket(ArrayUtils.subarray(packet.getData(), 1, packet.getData().length));
                    }
                    break;
                case 0xA0:
                    if (this.sessions.containsKey(packet.getSocketAddress().toString())) {
                        this.sessions.get(packet.getSocketAddress().toString()).processNACKPacket(ArrayUtils.subarray(packet.getData(), 1, packet.getData().length));
                    }
                    break;
            }
        } catch (IOException e) {
        }
    }

    public void send(byte[] buffer, SocketAddress remoteAddr) {
        this.udp.send(buffer, remoteAddr);
    }

    public synchronized void removeSession(DragonetSession session) {
        this.sessions.remove(session.getAddress().toString());
    }
}
