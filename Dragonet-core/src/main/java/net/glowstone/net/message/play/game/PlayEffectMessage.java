package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class PlayEffectMessage implements Message {

    public final int id;
    public final int x, y, z;
    public final int data;
    public final boolean ignoreDistance;

}
