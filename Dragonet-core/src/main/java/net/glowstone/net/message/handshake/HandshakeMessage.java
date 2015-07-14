package net.glowstone.net.message.handshake;

import com.flowpowered.networking.AsyncableMessage;
import lombok.Data;

@Data
public final class HandshakeMessage implements AsyncableMessage {

    public final int version;
    public final String address;
    public final int port;
    public final int state;

    @Override
    public boolean isAsync() {
        return true;
    }

}
