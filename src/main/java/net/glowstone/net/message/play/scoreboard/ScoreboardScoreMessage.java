package net.glowstone.net.message.play.scoreboard;

import com.flowpowered.networking.Message;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ScoreboardScoreMessage implements Message {

    public final String target;
    public final boolean remove;
    public final String objective;
    public final int value;

    public ScoreboardScoreMessage(String target, String objective, int value) {
        this(target, false, objective, value);
    }

    public static ScoreboardScoreMessage remove(String target, String objective) {
        return new ScoreboardScoreMessage(target, true, objective, 0);
    }
}
