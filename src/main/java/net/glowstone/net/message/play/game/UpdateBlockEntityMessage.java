package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;
import net.glowstone.util.nbt.CompoundTag;

@Data
public final class UpdateBlockEntityMessage implements Message {

    public final int x, y, z, action;
    public final CompoundTag nbt;

}
