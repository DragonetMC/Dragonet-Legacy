package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class ResourcePackStatusMessage implements Message {

    public final String hash;
    public final int result;

}

