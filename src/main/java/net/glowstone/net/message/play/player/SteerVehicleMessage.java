package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class SteerVehicleMessage implements Message {

    public final float sideways, forward;
    public final boolean jump, unmount;

}

