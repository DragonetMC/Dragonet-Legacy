package net.glowstone.net.message.play.inv;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class TransactionMessage implements Message {

    public final int id, transaction;
    public final boolean accepted;

}
