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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.ArrayUtils;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.EncapsulatedPacket;
import org.dragonet.net.packet.Protocol;
import org.dragonet.net.packet.RaknetDataPacket;
import org.dragonet.net.packet.minecraft.BatchPacket;
import org.dragonet.net.packet.minecraft.ClientConnectPacket;
import org.dragonet.net.packet.minecraft.DisconnectPacket;
import org.dragonet.net.packet.minecraft.FullChunkPacket;
import org.dragonet.net.packet.minecraft.LoginPacket;
import org.dragonet.net.packet.minecraft.LoginStatusPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.PEPacketIDs;
import org.dragonet.net.packet.minecraft.PingPongPacket;
import org.dragonet.net.packet.minecraft.ServerHandshakePacket;
import org.dragonet.net.translator.BaseTranslator;
import org.dragonet.net.translator.TranslatorProvider;
import org.dragonet.utilities.io.PEBinaryReader;
import org.dragonet.utilities.io.PEBinaryWriter;

public final class PENetworkClient {

    @Getter
    private SocketAddress remoteAddress;

    @Getter
    private String remoteIP;

    @Getter
    private int remotePort;

    @Getter
    private InetSocketAddress remoteInetSocketAddress;

    @Getter
    @Setter
    private int loginStage;

    @Getter
    private long clientID;

    @Getter
    private short clientMTU;

    @Getter
    private int sequenceNum;        //Server->Client

    @Getter
    private int lastSequenceNum;    //Server<-Client

    @Getter
    @Setter
    private int messageIndex;          //Server->Client

    @Getter
    @Setter
    private int splitID;

    private RaknetDataPacket queue;

    private final ArrayList<Integer> queueACK = new ArrayList<>();
    private final ArrayList<Integer> queueNACK = new ArrayList<>();
    private final HashMap<Integer, RaknetDataPacket> cachedOutgoingPacket = new HashMap<>();

    //Handle spltted packets. 
    private final ConcurrentHashMap<Integer, ByteArrayOutputStream> splits = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Integer> splitCounter = new ConcurrentHashMap<>();

    @Getter
    @Setter
    private int sentAndReceivedChunks = 0;

    private ArrayList<Integer> chunkPacketIDS = new ArrayList<>();

    @Getter
    private ArrayDeque<PEPacket> queueAfterChunkSent = new ArrayDeque<>();

    @Getter
    private final long timeConnected;

    @Getter
    private long lastPacketReceived;

    private final NetworkHandler handler;

    /**
     * The session which this client binds to.
     */
    @Getter
    private DragonetSession session;

    public PENetworkClient(NetworkHandler handler, SocketAddress remoteAddress, long clientID, short clientMTU) {
        this.handler = handler;
        this.clientID = clientID;
        this.clientMTU = clientMTU;
        this.remoteAddress = remoteAddress;
        this.remoteIP = this.remoteAddress.toString().substring(1, this.remoteAddress.toString().indexOf(":"));
        this.remotePort = Integer.parseInt(this.remoteAddress.toString().substring(this.remoteAddress.toString().indexOf(":") + 1));
        this.remoteInetSocketAddress = new InetSocketAddress(this.remoteIP, this.remotePort);
        this.queue = new RaknetDataPacket(this.sequenceNum);
        this.loginStage = 0;
        this.timeConnected = System.currentTimeMillis();
        this.lastPacketReceived = System.currentTimeMillis();
    }

    public void setSession(DragonetSession session) {
        if (this.session != null) {
            throw new IllegalStateException("There is already a session bound to this session! ");
        }
        this.session = session;
    }

    public void onTick() {
        sendAllACK();
        sendAllNACK();
        if (this.queue.getEncapsulatedPackets().size() > 0) {
            this.fireQueue();
        }
        if (System.currentTimeMillis() - this.lastPacketReceived > 15000) {
            this.disconnect("Timeout! ");
        }
    }

    private synchronized void sendAllACK() {
        if (this.queueACK.isEmpty()) {
            return;
        }
        int[] ackSeqs = ArrayUtils.toPrimitive(this.queueACK.toArray(new Integer[0]));
        Arrays.sort(ackSeqs);
        this.queueACK.clear();
        ByteArrayOutputStream allRecBos = new ByteArrayOutputStream();
        PEBinaryWriter allRecWriter = new PEBinaryWriter(allRecBos);
        try {
            int count = ackSeqs.length;
            int records = 0;
            if (count > 0) {
                int pointer = 1;
                int start = ackSeqs[0];
                int last = ackSeqs[0];
                ByteArrayOutputStream recBos = new ByteArrayOutputStream();
                PEBinaryWriter recWriter;
                while (pointer < count) {
                    int current = ackSeqs[pointer++];
                    int diff = current - last;
                    if (diff == 1) {
                        last = current;
                    } else if (diff > 1) { //Forget about duplicated packets (bad queues?)
                        recBos.reset();
                        recWriter = new PEBinaryWriter(recBos);
                        if (start == last) {
                            recWriter.writeByte((byte) 0x01);
                            recWriter.writeTriad(start);
                            start = last = current;
                        } else {
                            recWriter.writeByte((byte) 0x00);
                            recWriter.writeTriad(start);
                            recWriter.writeTriad(last);
                            start = last = current;
                        }
                        records++;
                    }
                }
                if (start == last) {
                    allRecWriter.writeByte((byte) 0x01);
                    allRecWriter.writeTriad(start);
                } else {
                    allRecWriter.writeByte((byte) 0x00);
                    allRecWriter.writeTriad(start);
                    allRecWriter.writeTriad(last);
                }
                records++;
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) 0xC0);
            writer.writeShort((short) (records & 0xFFFF));
            writer.write(allRecBos.toByteArray());
            handler.send(bos.toByteArray(), this.remoteAddress);
        } catch (IOException e) {
        }
    }

    private synchronized void sendAllNACK() {
        if (this.queueNACK.isEmpty()) {
            return;
        }
        int[] ackSeqs = ArrayUtils.toPrimitive(this.queueNACK.toArray(new Integer[0]));
        Arrays.sort(ackSeqs);
        this.queueNACK.clear();
        ByteArrayOutputStream allRecBos = new ByteArrayOutputStream();
        PEBinaryWriter allRecWriter = new PEBinaryWriter(allRecBos);
        try {
            int count = ackSeqs.length;
            int records = 0;
            if (count > 0) {
                int pointer = 1;
                int start = ackSeqs[0];
                int last = ackSeqs[0];
                ByteArrayOutputStream recBos = new ByteArrayOutputStream();
                PEBinaryWriter recWriter;
                while (pointer < count) {
                    int current = ackSeqs[pointer++];
                    int diff = current - last;
                    if (diff == 1) {
                        last = current;
                    } else if (diff > 1) { //Forget about duplicated packets (bad queues?)
                        recBos.reset();
                        recWriter = new PEBinaryWriter(recBos);
                        if (start == last) {
                            recWriter.writeByte((byte) 0x01);
                            recWriter.writeTriad(start);
                            start = last = current;
                        } else {
                            recWriter.writeByte((byte) 0x00);
                            recWriter.writeTriad(start);
                            recWriter.writeTriad(last);
                            start = last = current;
                        }
                        records++;
                    }
                }
                if (start == last) {
                    allRecWriter.writeByte((byte) 0x01);
                    allRecWriter.writeTriad(start);
                } else {
                    allRecWriter.writeByte((byte) 0x00);
                    allRecWriter.writeTriad(start);
                    allRecWriter.writeTriad(last);
                }
                records++;
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) 0xA0);
            writer.writeShort((short) (records & 0xFFFF));
            writer.write(allRecBos.toByteArray());
            handler.send(bos.toByteArray(), this.remoteAddress);
        } catch (IOException e) {
        }
    }

    /**
     * Process a ACK packet
     *
     * @param buffer The ACK packet binary array
     */
    public void processACKPacket(byte[] buffer) {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(buffer));
            int count = reader.readShort();
            List<Integer> packets = new ArrayList<>();
            for (int i = 0; i < count && reader.available() > 0; ++i) {
                if (reader.readByte() == (byte) 0x00) {
                    int start = reader.readTriad();
                    int end = reader.readTriad();
                    if ((end - start) > 4096) {
                        end = start + 4096;
                    }
                    for (int c = start; c <= end; ++c) {
                        packets.add(c);
                    }
                } else {
                    packets.add(reader.readTriad());
                }
            }
            int[] seqNums = ArrayUtils.toPrimitive(packets.toArray(new Integer[0]));
            for (int seq : seqNums) {
                if (this.cachedOutgoingPacket.containsKey(seq)) {
                    this.cachedOutgoingPacket.remove(seq);
                }
                if (this.chunkPacketIDS.contains(seq)) {
                    this.sentAndReceivedChunks++;
                    this.chunkPacketIDS.remove(new Integer(seq));
                }
            }
        } catch (IOException e) {
        }
    }

    /**
     * Process a NACK packet
     *
     * @param buffer The NACK packet binary array
     */
    public void processNACKPacket(byte[] buffer) {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(buffer));
            int count = reader.readShort();
            List<Integer> packets = new ArrayList<>();
            for (int i = 0; i < count && reader.available() > 0; ++i) {
                if (reader.readByte() == (byte) 0x00) {
                    int start = reader.readTriad();
                    int end = reader.readTriad();
                    if ((end - start) > 4096) {
                        end = start + 4096;
                    }
                    for (int c = start; c <= end; ++c) {
                        packets.add(c);
                    }
                } else {
                    packets.add(reader.readTriad());
                }
            }
            int[] seqNums = ArrayUtils.toPrimitive(packets.toArray(new Integer[0]));
            for (int seq : seqNums) {
                if (this.cachedOutgoingPacket.containsKey(seq)) {
                    handler.getUdp().send(this.cachedOutgoingPacket.get(seq).getData(), this.remoteAddress);
                }
            }
        } catch (IOException e) {
        }
    }

    public void sendPacket(PEPacket packet) {
        sendPacket(packet, 2);
    }

    public void sendPacket(PEPacket packet, int reliability) {
        if (!(packet instanceof PEPacket)) {
            return;
        }
        /*
         if (!(packet instanceof FullChunkPacket) && !(packet instanceof StartGamePacket) && !(packet instanceof SetTimePacket) && !(packet instanceof SetDifficultyPacket)
         && !(packet instanceof LoginStatusPacket) && !(packet instanceof ServerHandshakePacket) && this.sentAndReceivedChunks != -1) {
         this.queueAfterChunkSent.add(packet);
         return;
         }*/
        packet.encode();
        if (packet.getData().length > this.clientMTU + 1 && !(packet instanceof BatchPacket)) {
            //BATCH PACKET
            BatchPacket pk = new BatchPacket();
            pk.packets.add(packet);
            sendPacket(pk, reliability);
            //System.out.println("Using BATCH PACKET for " + packet.getClass().getSimpleName());
            return;
        }
        this.fireQueue();
        //System.out.println(" >>*>> Sending: " + packet.getClass().getSimpleName());
        EncapsulatedPacket[] encapsulatedPacket = EncapsulatedPacket.fromPEPacket(this, packet, reliability);
        for (EncapsulatedPacket ePacket : encapsulatedPacket) {
            ePacket.encode();
            /*
             if (this.queue.getLength() + ePacket.getData().length > this.clientMTU - 24) {
             this.fireQueue();
             }
             */
            this.queue.getEncapsulatedPackets().add(ePacket);
            if (this.sentAndReceivedChunks != -1 && (packet instanceof FullChunkPacket) && !this.chunkPacketIDS.contains(this.queue.getSequenceNumber())) {
                this.chunkPacketIDS.add(this.queue.getSequenceNumber());
            }
            this.fireQueue();
        }
    }

    private synchronized void fireQueue() {
        if (this.queue.getEncapsulatedPackets().isEmpty()) {
            return;
        }
        this.cachedOutgoingPacket.put(this.queue.getSequenceNumber(), this.queue);
        this.queue.encode();
        handler.getUdp().send(this.queue.getData(), this.remoteAddress);
        this.queue = new RaknetDataPacket(this.sequenceNum++);
    }

    public void disconnect(String reason) {
        this.sendPacket(new DisconnectPacket(reason));
        handler.remove(this.remoteAddress);
    }

    public void processDataPacket(RaknetDataPacket dataPacket) {
        this.lastPacketReceived = System.currentTimeMillis();
        if (dataPacket.getSequenceNumber() - this.lastSequenceNum > 1) {
            for (int i = this.lastSequenceNum + 1; i < dataPacket.getSequenceNumber(); i++) {
                this.queueNACK.add(i);
            }
        }
        this.lastSequenceNum = dataPacket.getSequenceNumber();
        this.queueACK.add(dataPacket.getSequenceNumber());
        if (dataPacket.getEncapsulatedPackets().isEmpty()) {
            return;
        }
        for (EncapsulatedPacket epacket : dataPacket.getEncapsulatedPackets()) {
            if (epacket.hasSplit) {
                System.out.println("PROCESSING SPLITTED PACKET ID: " + epacket.splitID + ", Index-" + epacket.splitIndex + ", Count-" + epacket.splitCount);
                //Handle split packet
                if (epacket.splitIndex == epacket.splitCount - 1) {
                    if (splits.containsKey((Integer) epacket.splitID)) {
                        try {
                            splits.get((Integer) epacket.splitID).write(epacket.buffer);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        byte[] buff = splits.get((Integer) epacket.splitID).toByteArray();
                        splits.remove((Integer) epacket.splitID);
                        splitCounter.remove((Integer) epacket.splitID);
                        processPacketBuffer(buff);
                    }
                } else {
                    try {
                        if (epacket.splitIndex == 0) {
                            ByteArrayOutputStream oup = new ByteArrayOutputStream();
                            oup.write(epacket.buffer);
                            splits.put((Integer) epacket.splitID, oup);
                            splitCounter.put((Integer) epacket.splitID, -1);
                        } else {
                            if (splits.containsKey((Integer) epacket.splitID)
                                    && (splitCounter.get((Integer) epacket.splitID) < epacket.splitIndex)) {
                                splits.get((Integer) epacket.splitID).write(epacket.buffer);
                                splitCounter.put((Integer) epacket.splitID, epacket.splitIndex);
                            }
                        }
                    } catch (IOException ex) {
                    }
                }
                continue;
            }
            processPacketBuffer(epacket.buffer);
        }
    }

    private void processPacketBuffer(byte[] buffer) {
        PEPacket packet = Protocol.decode(buffer);
        if (packet == null) {
            return;
        }
        System.out.println("Received Packet: " + packet.getClass().getSimpleName());
        switch (packet.pid()) {
            case PEPacketIDs.PING:
                PingPongPacket pkPong = new PingPongPacket();
                pkPong.pingID = ((PingPongPacket) packet).pingID;
                this.sendPacket(pkPong, 0);
                break;
            case PEPacketIDs.CLIENT_CONNECT:
                if (this.loginStage != 0) {
                    break;
                }
                this.clientID = ((ClientConnectPacket) packet).sessionID;
                ServerHandshakePacket pkServerHandshake = new ServerHandshakePacket();
                pkServerHandshake.addr = remoteInetSocketAddress.getAddress();
                pkServerHandshake.port = (short) 0;
                pkServerHandshake.session = this.clientID;
                pkServerHandshake.session2 = this.clientID + 1000L;
                this.loginStage = 1;
                this.sendPacket(pkServerHandshake);
                break;
            case PEPacketIDs.CLIENT_HANDSHAKE:
                if (this.loginStage != 1) {
                    break;
                }
                this.loginStage = 2;
                break;
            default:
                if (session == null) {
                    disconnect("Network error! ");
                    return;
                }
                session.onPacketReceived(packet);
        }
    }
}
