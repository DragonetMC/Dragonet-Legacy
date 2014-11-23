package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class RespawnMessage implements Message {

    public final int dimension, difficulty, mode;
    public final String levelType;

}
