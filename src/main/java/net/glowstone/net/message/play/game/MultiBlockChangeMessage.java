package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;

import java.util.List;

@Data
public final class MultiBlockChangeMessage implements Message {

    public final int chunkX, chunkZ;
    public final List<BlockChangeMessage> records;

}
