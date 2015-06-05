package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class ChunkDataMessage implements Message {

    public final int x, z;
    public final boolean continuous;
    public final int primaryMask;
    public final byte[] data;

    public static ChunkDataMessage empty(int x, int z) {
        return new ChunkDataMessage(x, z, true, 0, new byte[0]);
    }

}
