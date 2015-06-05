package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class CollectItemMessage implements Message {

    public final int id, collector;

}
