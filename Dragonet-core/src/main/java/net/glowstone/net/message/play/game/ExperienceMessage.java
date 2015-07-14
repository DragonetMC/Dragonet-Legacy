package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class ExperienceMessage implements Message {

    public final float barValue;
    public final int level, totalExp;

}
