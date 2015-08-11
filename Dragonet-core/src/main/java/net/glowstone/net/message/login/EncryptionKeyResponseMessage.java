package net.glowstone.net.message.login;

import com.flowpowered.networking.AsyncableMessage;
import lombok.Data;

@Data
public final class EncryptionKeyResponseMessage implements AsyncableMessage {

    public final byte[] sharedSecret;
    public final byte[] verifyToken;

    @Override
    public boolean isAsync() {
        return true;
    }

}
