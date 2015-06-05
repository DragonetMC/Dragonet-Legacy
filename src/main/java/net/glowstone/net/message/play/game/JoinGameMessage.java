package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class JoinGameMessage implements Message {

    public final int id, mode, dimension, difficulty, maxPlayers;
    public final String levelType;
    public final boolean reducedDebugInfo;

}
