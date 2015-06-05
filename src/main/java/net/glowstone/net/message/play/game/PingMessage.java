package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class PingMessage implements Message {

    public final int pingId;

}
