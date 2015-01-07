package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;

import java.util.Collection;

@Data
public class ExplosionMessage implements Message {
    public final float x;
    public final float y;
    public final float z;
    public final float radius;
    public final float playerMotionX;
    public final float playerMotionY;
    public final float playerMotionZ;
    public final Collection<Record> records;

    @Override
    public String toString() {
        return "ExplosionMessage{x=" + x + ",y=" + y + ",z=" + z +
                ",radius=" + radius +
                ",motX=" + playerMotionX + ",motY=" + playerMotionY + ",motZ=" + playerMotionZ +
                ",recordCount=" + records.size() + "}";
    }

    @Data
    public static class Record {
        public final byte x, y, z;
    }
}
