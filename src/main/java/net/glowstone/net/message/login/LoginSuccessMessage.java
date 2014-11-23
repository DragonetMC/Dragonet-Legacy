package net.glowstone.net.message.login;

import com.flowpowered.networking.Message;
import lombok.Data;
import lombok.Getter;

@Data
public final class LoginSuccessMessage implements Message {

    private final @Getter String uuid;
    private final @Getter String username;

}
