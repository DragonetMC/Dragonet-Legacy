package net.glowstone.net.message.play.inv;

import com.flowpowered.networking.Message;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.glowstone.util.TextMessage;

@Data
@RequiredArgsConstructor
public final class OpenWindowMessage implements Message {

    public final int id;
    public final String type;
    public final TextMessage title;
    public final int slots, entityId;

    public OpenWindowMessage(int id, String type, String title, int slots) {
        this(id, type, new TextMessage(title), slots, 0);
    }

}
