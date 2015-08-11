package net.glowstone.net.message.play.scoreboard;

import com.flowpowered.networking.Message;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.scoreboard.RenderType;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ScoreboardObjectiveMessage implements Message {

    public final String name;
    public final String displayName;
    public final int action;
    public final RenderType renderType;

    public enum Action {
        CREATE,
        REMOVE,
        UPDATE
    }

    public static ScoreboardObjectiveMessage create(String name, String displayName) {
        return new ScoreboardObjectiveMessage(name, displayName, Action.CREATE.ordinal(), RenderType.INTEGER);
    }

    public static ScoreboardObjectiveMessage create(String name, String displayName, RenderType renderType) {
        return new ScoreboardObjectiveMessage(name, displayName, Action.CREATE.ordinal(), renderType);
    }

    public static ScoreboardObjectiveMessage remove(String name) {
        return new ScoreboardObjectiveMessage(name, null, Action.REMOVE.ordinal(), null);
    }

    public static ScoreboardObjectiveMessage update(String name, String displayName) {
        return new ScoreboardObjectiveMessage(name, displayName, Action.UPDATE.ordinal(), RenderType.INTEGER);
    }

    public static ScoreboardObjectiveMessage update(String name, String displayName, RenderType renderType) {
        return new ScoreboardObjectiveMessage(name, displayName, Action.UPDATE.ordinal(), renderType);
    }
}
