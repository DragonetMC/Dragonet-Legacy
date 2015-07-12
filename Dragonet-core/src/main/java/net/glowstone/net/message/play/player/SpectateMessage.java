package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;
import lombok.Data;

import java.util.UUID;

@Data
public final class SpectateMessage implements Message {

    public final UUID target;

}

