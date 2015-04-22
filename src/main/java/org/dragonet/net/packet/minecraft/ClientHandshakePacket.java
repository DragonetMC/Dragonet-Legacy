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
package org.dragonet.net.packet.minecraft;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.dragonet.utilities.io.PEBinaryReader;

public class ClientHandshakePacket extends PEPacket {

    public byte[] cookie;
    public byte security;
    public short port;
    public byte[] dataArray0;
    public byte[][] dataArray;
    public short timestamp;
    public long session;
    public long session2;

    public ClientHandshakePacket(byte[] data) {
        this.setData(data);
    }

    @Override
    public int pid() {
        return PEPacketIDs.CLIENT_HANDSHAKE;
    }

    @Override
    public void encode() {
    }

    @Override
    public void decode() {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte(); //PID
            this.cookie = reader.read(4);
            this.security = reader.readByte();
            this.port = reader.readShort();
            this.dataArray0 = reader.read(reader.readByte() & 0xFF);
            this.dataArray = getDataArray(reader, 9);
            this.timestamp = reader.readShort();
            this.session2 = reader.readLong();
            this.session = reader.readLong();
            this.setLength(reader.totallyRead());
        } catch (IOException e) {
        }
    }

    public static byte[][] getDataArray(PEBinaryReader reader, int len) throws IOException {
        byte[][] dataArray = new byte[len][];
        int arrayLen;
        for (int i = 0; i < len; i++) {
            reader.switchEndianness();
            arrayLen = reader.readTriad();
            reader.switchEndianness();
            dataArray[i] = reader.read(arrayLen);
        }
        return dataArray;
    }
}
