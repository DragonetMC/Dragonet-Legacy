package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public final class EntityRotationMessage implements Message {

    public final int id, rotation, pitch;
    public final boolean onGround;

    public EntityRotationMessage(int id, int rotation, int pitch) {
        this(id, rotation, pitch, true);
    }

}
