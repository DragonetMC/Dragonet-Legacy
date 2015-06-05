package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class ServerDifficultyMessage implements Message {

    public final int difficulty;

}
