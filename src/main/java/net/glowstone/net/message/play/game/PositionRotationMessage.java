package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

@Data
@RequiredArgsConstructor
public final class PositionRotationMessage implements Message {

    public final double x, y, z;
    public final float rotation, pitch;
    public final int flags;

    public PositionRotationMessage(double x, double y, double z, float rotation, float pitch) {
        this(x, y, z, rotation, pitch, 0);
    }

    public PositionRotationMessage(Location location) {
        this(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

}
