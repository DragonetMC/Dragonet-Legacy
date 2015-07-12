package net.glowstone.net.message.play.game;

import com.flowpowered.networking.AsyncableMessage;
import lombok.Data;

@Data
public final class IncomingChatMessage implements AsyncableMessage {

    public final String text;

    @Override
    public boolean isAsync() {
        return true;
    }

}
