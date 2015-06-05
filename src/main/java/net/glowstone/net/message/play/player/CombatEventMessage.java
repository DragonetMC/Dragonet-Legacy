package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class CombatEventMessage implements Message {

    public final Event event;
    public final int duration;
    public final int entityID, playerID;
    public final String message;

    // BEGIN_COMBAT
    public CombatEventMessage(Event event) {
        this(event, 0, 0, 0, null);
    }

    // END_COMBAT
    public CombatEventMessage(Event event, int duration, int entityID) {
        this(event, duration, entityID, 0, null);
    }

    // ENTITY_DEAD
    public CombatEventMessage(Event event, int entityID, int playerID, String message) {
        this(event, 0, entityID, playerID, message);
    }

    public static enum Event {
        ENTER_COMBAT,
        END_COMBAT,
        ENTITY_DEAD;

        public static Event getAction(int id) {
            Event[] values = values();
            return id < 0 || id >= values.length ? null : values[id];
        }
    }

}
