package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;

import java.util.List;

@Data
public final class ChunkBulkMessage implements Message {

    public final boolean skyLight;
    public final List<ChunkDataMessage> entries;

}
