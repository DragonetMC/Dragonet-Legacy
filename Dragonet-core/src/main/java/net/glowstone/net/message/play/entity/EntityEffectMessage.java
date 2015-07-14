package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class EntityEffectMessage implements Message {

    public final int id;
    public final int effect, amplifier;
    public final int duration;
    public final boolean hideParticles;

}
