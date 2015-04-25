/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.packet.minecraft;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import org.dragonet.net.packet.Protocol;
import org.dragonet.utilities.io.PEBinaryReader;

public class BatchPacket extends PEPacket {

    public ArrayList<PEPacket> packets;

    @Override
    public int pid() {
        return PEPacketIDs.BATCH_PACKET;
    }

    @Override
    public void encode() {

    }

    @Override
    public void decode() {
        try {
            packets = new ArrayList<>();
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte(); //PID
            int size = reader.readInt();
            System.out.println("PAYLOAD SIZE=" + (this.getData().length - 5) + ", BATCH SIZE=" + size);
            byte[] payload = reader.read(size);
            Inflater inf = new Inflater();
            inf.setInput(payload);
            byte[] decompressedPayload = new byte[1024 * 1024 * 64];
            inf.end();
            int decompressedSize = 0;
            try {
                decompressedSize = inf.inflate(decompressedPayload);
            } catch (DataFormatException ex) {
                ex.printStackTrace();
                this.setLength(reader.totallyRead());
                return;
            }
            System.out.println("Batch Packet decompressed length: " + decompressedSize);
            decompressedPayload = Arrays.copyOfRange(decompressedPayload, 0, decompressedSize);
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
