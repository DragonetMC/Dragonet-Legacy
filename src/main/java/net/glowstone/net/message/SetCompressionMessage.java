package net.glowstone.net.message;

import com.flowpowered.networking.Message;
import lombok.Data;
import lombok.Getter;

@Data
public final class SetCompressionMessage implements Message {

    private final @Getter int threshold;

}

