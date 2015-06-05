package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class AttachEntityMessage implements Message {

    public final int id, vehicle;
    public final boolean leash;

}
