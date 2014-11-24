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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.dragonet.utilities.io.PEBinaryWriter;

public class ServerHandshakePacket extends PEPacket {

    public short port;
    public long session;
    public long session2;

    @Override
    public int pid() {
        return PEPacketIDs.SERVER_HANDSHAKE;
    }

    @Override
    public void encode() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte)(this.pid() & 0xFF));
            writer.write(new byte[]{(byte) 0x04, (byte) 0x3F, (byte) 0x57, (byte) 0xFE}); //Cookie
            writer.writeByte((byte) 0xCD);
            writer.writeShort(this.port);
            byte[][] array = new byte[][]{
                {(byte) 0xf5, (byte) 0xff, (byte) 0xff, (byte) 0xf5},
                {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff},
                {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff},
                {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff},
                {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff},
                {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff},
                {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff},
                {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff},
                {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff},
                {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff}
            };
            writeDataArray(array, writer);
            writer.write(new byte[]{(byte)0x00, (byte)0x00});
            writer.writeLong(this.session);
            writer.writeLong(this.session2);
            this.setData(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void decode() {
    }

    private static void writeDataArray(byte[][] array, PEBinaryWriter writer) throws IOException {
        for (byte[] data : array) {
            writer.switchEndianness(); //use this to prevent auto little-endian when writing triad
            writer.writeTriad(data.length);
            writer.switchEndianness();
            writer.write(data);
        }
    }
}
