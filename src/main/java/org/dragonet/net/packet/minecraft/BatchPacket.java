/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.packet.minecraft;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.dragonet.net.packet.Protocol;
import org.dragonet.utilities.io.PEBinaryReader;
import org.dragonet.utilities.io.PEBinaryWriter;

public class BatchPacket extends PEPacket {

    public ArrayList<PEPacket> packets;

    public BatchPacket() {
        packets = new ArrayList<>();
    }

    public BatchPacket(byte[] data) {
        this.setData(data);
    }

    @Override
    public int pid() {
        return PEPacketIDs.BATCH_PACKET;
    }

    @Override
    public void encode() {
        try {
            //Combine all packets
            ByteArrayOutputStream packetCombiner = new ByteArrayOutputStream();
            for (PEPacket pk : packets) {
                pk.encode();
                packetCombiner.write(pk.getData());
            }
            Deflater def = new Deflater(7);
            def.reset();
            def.setInput(packetCombiner.toByteArray());
            def.finish();
            byte[] deflateBuffer = new byte[65535];
            int size = def.deflate(deflateBuffer);
            deflateBuffer = Arrays.copyOfRange(deflateBuffer, 0, size);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeInt(deflateBuffer.length);
            writer.write(deflateBuffer);
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
        try {
            packets = new ArrayList<>();
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte(); //PID
            int size = reader.readInt();
            byte[] payload = reader.read(size);
            Inflater inf = new Inflater();
            inf.setInput(payload);
            byte[] decompressedPayload = new byte[1024 * 1024 * 64];
            int decompressedSize = 0;
            try {
                decompressedSize = inf.inflate(decompressedPayload);
            } catch (DataFormatException ex) {
                this.setLength(reader.totallyRead());
                return;
            }
            inf.end();
            int offset = 0;
            while (offset < decompressedSize) {
                PEPacket pk = Protocol.decode(Arrays.copyOfRange(decompressedPayload, offset, decompressedSize));
                if (pk == null || pk.getLength() == 0) {
                    packets.clear();
                    return;
                }
                offset += pk.getLength();
                packets.add(pk);
            }
            this.setLength(reader.totallyRead());
        } catch (IOException e) {
        }
    }

}
