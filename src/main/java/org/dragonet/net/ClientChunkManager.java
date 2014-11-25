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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.zip.Deflater;
import lombok.Getter;
import net.glowstone.GlowChunkSnapshot;
import org.apache.commons.lang.ArrayUtils;
import org.dragonet.ChunkLocation;
import org.dragonet.net.packet.minecraft.FullChunkPacket;
import org.dragonet.utilities.io.PEBinaryUtils;
import org.dragonet.utilities.io.PEBinaryWriter;

public class ClientChunkManager {

    private final @Getter
    DragonetSession session;

    private final ArrayList<ChunkLocation> chunksLoaded; //Already sent
    private final Deque<ChunkLocation> chunksQueue;  //Awaiting sending

    public ClientChunkManager(DragonetSession session) {
        this.session = session;
        this.chunksLoaded = new ArrayList<>();
        this.chunksQueue = new ArrayDeque<>();
    }

    /**
     * Check whether a full chunk is loaded(prepared/sent)
     *
     * @param location Chunk location
     * @return The status, true=prepared/sent, false=not prepared/not sent
     */
    public boolean isChunkLoaded(ChunkLocation location) {
        for (ChunkLocation value : this.chunksLoaded) {
            if (value.equals(location)) {
                return true;
            }
        }
        for (ChunkLocation value : this.chunksQueue) {
            if (value.equals(location)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether a full chunk is loaded(prepared/sent)
     *
     * @param x The chunk X coordinate
     * @param z The chunk Z coordinate
     * @return The status, true=prepared/sent, false=not prepared/not sent
     */
    public boolean isChunkLoaded(int x, int z) {
        return this.isChunkLoaded(new ChunkLocation(x, z));
    }

    /**
     * Add a chunk location to the chunk sending queue
     *
     * @param location Chunk location
     */
    public void prepareChunk(ChunkLocation location) {
        if (this.isChunkLoaded(location)) {
            return;
        }
        this.chunksQueue.add(location);
    }

    /**
     * Send all queued chunks to the client and mark them as sent
     */
    public synchronized void sendChunks() {
        ChunkLocation chunkLocation;
        while ((chunkLocation = this.chunksQueue.poll()) != null) {
            this.sendChunk(chunkLocation.getX(), chunkLocation.getZ());
            this.chunksLoaded.add(chunkLocation);
        }
    }

    /**
     * Send a single chunk to the client
     *
     * @param chunkX The chunk X coordinate
     * @param chunkZ The chunk Z coordinate
     */
    private synchronized void sendChunk(int chunkX, int chunkZ) {
        try {
            GlowChunkSnapshot chunk = this.getSession().getPlayer().getWorld().getChunkAt(chunkX, chunkZ).getChunkSnapshot();
            ByteArrayOutputStream totalData = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(totalData);
            if (writer.getEndianness() == PEBinaryUtils.BIG_ENDIAN) {
                writer.switchEndianness();
            }
            writer.writeInt(chunkX);
            writer.writeInt(chunkZ);
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 128; y++) {
                        writer.writeByte((byte) (this.getSession().getTranslator().translateBlockToPE(chunk.getBlockTypeId(x, y, z)) & 0xFF));
                    }
                }
            }
            writer.write(new byte[16384]);
            for (int i = 0; i < 16384; i++) {
                writer.writeByte((byte) 0xF0);
            }
            for (int i = 0; i < 16384; i++) {
                writer.writeByte((byte) 0x11);
            }
            for (int i = 0; i < 256; i++) {
                writer.writeByte((byte) 0x00);
            }
            for (int i = 0; i < 256; i++) {
                writer.writeByte((byte) 0x00);
                writer.writeByte((byte) 0x85);
                writer.writeByte((byte) 0xB2);
                writer.writeByte((byte) 0x4A);
            }
            Deflater deflater = new Deflater(2);
            deflater.reset();
            deflater.setInput(totalData.toByteArray());
            deflater.finish();
            byte[] bufferDeflate = new byte[65536];
            int deflatedSize = deflater.deflate(bufferDeflate);
            FullChunkPacket packet = new FullChunkPacket();
            packet.compressedData = ArrayUtils.subarray(bufferDeflate, 0, deflatedSize);
            this.getSession().send(packet);
        } catch (IOException e) {
        }
    }
}
