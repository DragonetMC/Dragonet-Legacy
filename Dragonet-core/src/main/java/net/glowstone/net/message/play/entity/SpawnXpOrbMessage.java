package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class SpawnXpOrbMessage implements Message {

    public final int id, x, y, z;
    public final short count;

}
