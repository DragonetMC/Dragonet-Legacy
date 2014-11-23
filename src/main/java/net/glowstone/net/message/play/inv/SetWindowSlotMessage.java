package net.glowstone.net.message.play.inv;

import com.flowpowered.networking.Message;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
public final class SetWindowSlotMessage implements Message {

    public final int id, slot;
    public final ItemStack item;

}
