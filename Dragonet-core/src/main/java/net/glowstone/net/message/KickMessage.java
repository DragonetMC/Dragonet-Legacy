package net.glowstone.net.message;

import com.flowpowered.networking.Message;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.glowstone.util.TextMessage;

@Data
@RequiredArgsConstructor
public final class KickMessage implements Message {

    public final TextMessage text;

    public KickMessage(String text) {
        this(new TextMessage(text));
    }

}
