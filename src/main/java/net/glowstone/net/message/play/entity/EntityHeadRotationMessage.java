package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class EntityHeadRotationMessage implements Message {

    public final int id;
    public final int rotation;

}

