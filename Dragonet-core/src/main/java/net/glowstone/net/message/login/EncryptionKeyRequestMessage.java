package net.glowstone.net.message.login;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class EncryptionKeyRequestMessage implements Message {

    public final String sessionId;
    public final byte[] publicKey;
    public final byte[] verifyToken;

}
