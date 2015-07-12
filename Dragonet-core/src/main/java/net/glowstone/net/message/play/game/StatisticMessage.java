package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;

import java.util.Map;

@Data
public final class StatisticMessage implements Message {

    public final Map<String, Integer> values;

}
