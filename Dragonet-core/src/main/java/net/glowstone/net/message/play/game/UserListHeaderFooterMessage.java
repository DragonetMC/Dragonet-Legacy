package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;
import net.glowstone.util.TextMessage;

@Data
public final class UserListHeaderFooterMessage implements Message {

    public final TextMessage header, footer;

}
