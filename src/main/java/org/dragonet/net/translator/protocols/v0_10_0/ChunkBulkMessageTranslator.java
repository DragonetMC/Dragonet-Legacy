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
package org.dragonet.net.translator.protocols.v0_10_0;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import net.glowstone.net.message.play.game.ChunkBulkMessage;
import net.glowstone.net.message.play.game.ChunkDataMessage;
import org.apache.commons.lang.ArrayUtils;
import org.dragonet.net.packet.minecraft.FullChunkPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.utilities.io.PEBinaryUtils;
import org.dragonet.utilities.io.PEBinaryWriter;

public final class ChunkBulkMessageTranslator {

    public static PEPacket[] translate(ChunkBulkMessage message) {
        try {
            ByteArrayInputStream in;
            byte[] buff;
            ArrayList<FullChunkPacket> list = new ArrayList<>();
            FullChunkPacket packetFullChunk;
            for (ChunkDataMessage dataMsg : message.getEntries()) {
                System.out.println("Translating chunk (" + dataMsg.x + ", " + dataMsg.z + ") START ===============");
                System.out.print("Bitmask: ");
                for (int b = 16; b > 0; b--) {
                    System.out.print(((dataMsg.primaryMask >> b) & 1) == 1 ? "1" : "0");
                }
                System.out.print("\n");
                ByteArrayOutputStream bosIDs = new ByteArrayOutputStream();
                Inflater inf = new Inflater();
                inf.reset();
                inf.setInput(dataMsg.data);
                inf.finished();
                byte[] iData = new byte[65535];
                int size = -1;
                try {
                    size = inf.inflate(iData);
                } catch (DataFormatException ex) {
                }
                in = new ByteArrayInputStream(ArrayUtils.subarray(iData, 0, size));
                for (int chunkY = 0; chunkY < 16; chunkY++) {
                    if ((dataMsg.primaryMask & (1 << chunkY)) == 1) {
                        byte[] chunkSection = new byte[4096];
                        in.read(chunkSection);
                        for (int x = 0; x < 16; x++) {
                            for (int z = 0; z < 16; z++) {
                                for (int y = chunkY * 16 * 16; y < chunkY * 16 * 16 + 16; y++) {
                                    bosIDs.write(chunkSection[(y << 8 + z << 4 + x) % (16 * 16 * 16)]);
                                }
                            }
                        }
                        if (message.skyLight) {
                            in.read(new byte[2048]);
                        }
                    } else {
                        for (int x = 0; x < 16; x++) {
                            for (int z = 0; z < 16; z++) {
                                for (int y = 0; y < 16; y++) {
                                    bosIDs.write(0x00);
                                }
                            }
                        }
                    }
                }
                //byte[] bIDs = new byte[16*128*16];
                //byte[] bMetas = new byte[16*128*16/2];

                packetFullChunk = new FullChunkPacket();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                PEBinaryWriter writer = new PEBinaryWriter(bos);
                if (writer.getEndianness() == PEBinaryUtils.BIG_ENDIAN) {
                    writer.switchEndianness();
                }
                writer.writeInt(dataMsg.x);
                writer.writeInt(dataMsg.z);
                writer.write(bosIDs.toByteArray());
                writer.write(new byte[16384]); //TODO: Meta
                writer.write(new byte[16384]);
                for (int i = 0; i < 256; i++) {
                    writer.writeByte((byte) 0x01);
                }
                for (int i = 0; i < 256; i++) {
                    writer.writeInt(0x0085B24A);
                }
                Deflater def = new Deflater(7);
                def.reset();
                def.setInput(bos.toByteArray());
                def.finish();
                byte[] compressedBuffer = new byte[65535];
                size = def.deflate(compressedBuffer);
                byte[] compressed = ArrayUtils.subarray(compressedBuffer, 0, size);
                packetFullChunk.compressedData = compressed;
                list.add(packetFullChunk);
            }
            return list.toArray(new FullChunkPacket[0]);
        } catch (IOException e) {
        }
        return null;
    }
}
