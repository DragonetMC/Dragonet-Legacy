package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;
import lombok.Data;

import java.util.List;

@Data
public final class TabCompleteResponseMessage implements Message {

    public final List<String> completions;

}

