package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class PlaySoundMessage implements Message {

    public final String sound;
    public final double x, y, z;
    public final float volume, pitch;

}

