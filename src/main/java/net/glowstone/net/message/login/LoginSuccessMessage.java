package net.glowstone.net.message.login;

import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class LoginSuccessMessage implements Message {

    public final String uuid;
    public final String username;

}
