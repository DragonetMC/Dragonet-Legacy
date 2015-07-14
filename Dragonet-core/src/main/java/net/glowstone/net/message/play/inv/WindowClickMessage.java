package net.glowstone.net.message.play.inv;

import com.flowpowered.networking.Message;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
public final class WindowClickMessage implements Message {

    public final int id, slot, button, transaction, mode;
    public final ItemStack item;

}
