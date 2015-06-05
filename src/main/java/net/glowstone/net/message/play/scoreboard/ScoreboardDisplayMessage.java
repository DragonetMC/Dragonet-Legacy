package net.glowstone.net.message.play.scoreboard;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class ScoreboardDisplayMessage implements Message {

    public final int position;
    public final String objective;

}
