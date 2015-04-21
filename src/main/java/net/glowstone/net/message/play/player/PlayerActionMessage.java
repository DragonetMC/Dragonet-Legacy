package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class PlayerActionMessage implements Message {

    public final int id, action, jumpBoost;

}

