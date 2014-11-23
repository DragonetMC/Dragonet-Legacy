package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class ClientSettingsMessage implements Message {

    public final String locale;
    public final int viewDistance, chatFlags;
    public final boolean chatColors;
    public final int skinFlags;

}
