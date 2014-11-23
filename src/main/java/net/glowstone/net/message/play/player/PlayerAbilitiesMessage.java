package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class PlayerAbilitiesMessage implements Message {

    public final int flags;
    public final float flySpeed, walkSpeed;

}

