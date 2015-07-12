package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class SpawnPaintingMessage implements Message {

    public final int id;
    public final String title;
    public final int x, y, z, facing;

}
