package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;
import lombok.Data;
import net.glowstone.entity.meta.MetadataMap;

import java.util.List;
import java.util.UUID;

@Data
public final class SpawnPlayerMessage implements Message {

    public final int id;
    public final UUID uuid;
    public final int x, y, z;
    public final int rotation, pitch;
    public final int item;
    public final List<MetadataMap.Entry> metadata;

}
