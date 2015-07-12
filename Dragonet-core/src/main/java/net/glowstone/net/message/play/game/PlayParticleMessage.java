package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class PlayParticleMessage implements Message {

    public final int particle;
    public final boolean longDistance;
    public final float x, y, z;
    public final float ofsX, ofsY, ofsZ;
    public final float data;
    public final int count;
    public final int[] extData;

}

