package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
public final class BlockPlacementMessage implements Message {

    public final int x, y, z, direction;
    public final ItemStack heldItem;
    public final int cursorX, cursorY, cursorZ;

}
