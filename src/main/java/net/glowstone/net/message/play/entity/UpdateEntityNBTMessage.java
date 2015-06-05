package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;
import lombok.Data;
import net.glowstone.util.nbt.CompoundTag;

@Data
public final class UpdateEntityNBTMessage implements Message {

    public final int entityId;
    public final CompoundTag tag;

}
