package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class TimeMessage implements Message {

    public final long worldAge, time;

}
