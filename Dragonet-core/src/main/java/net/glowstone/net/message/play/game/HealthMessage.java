package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class HealthMessage implements Message {

    public final float health;
    public final int food;
    public final float saturation;

}
