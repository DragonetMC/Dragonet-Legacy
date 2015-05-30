/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.topc;

import com.flowpowered.networking.Message;
import net.glowstone.EventFactory;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.ItemTable;
import net.glowstone.block.blocktype.BlockType;
import net.glowstone.entity.objects.GlowItem;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.RemoveBlockPacket;
import org.dragonet.net.translator.PEPacketTranslatorToPC;
import org.dragonet.net.translator.Translator_v0_11;
import static org.dragonet.net.translator.topc.UseItemPacketTranslator.convertFace;

public class RemoveBlockPacketTranslator extends PEPacketTranslatorToPC<Translator_v0_11, RemoveBlockPacket> {

    public RemoveBlockPacketTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public Message[] handleSpecific(RemoveBlockPacket packet) {
        RemoveBlockPacket pkRemoveBlock = (RemoveBlockPacket) packet;
        if (!(this.getSession().getPlayer() instanceof Player)) {
            return null;
        }
        GlowBlock block = this.getSession().getPlayer().getWorld().getBlockAt(pkRemoveBlock.x, pkRemoveBlock.y, pkRemoveBlock.z);

        if(this.getSession().getPlayer().getItemInHand() == null ||
                this.getSession().getPlayer().getItemInHand().getType().equals(Material.AIR)){
            // Client won't send UseItemPacket if holding nothing. 
            PlayerInteractEvent event = EventFactory.onPlayerInteract(this.getSession().getPlayer(), Action.LEFT_CLICK_BLOCK, block, BlockFace.UP);
            if(event.isCancelled()){
                this.getSession().getPlayer().sendBlockChange(block.getLocation(), block.getType(), block.getData());
                return null;
            }
        }
        
        // fire the block break event
        BlockBreakEvent breakEvent = EventFactory.callEvent(new BlockBreakEvent(block, this.getSession().getPlayer()));
        if (breakEvent.isCancelled()) {
            return null;
        }

        BlockType blockType = ItemTable.instance().getBlock(block.getType());
        if (blockType != null) {
            blockType.blockDestroy(this.getSession().getPlayer(), block, BlockFace.UP);
        }

        // destroy the block
        if (!block.isEmpty() && !block.isLiquid() && this.getSession().getPlayer().getGameMode() != GameMode.CREATIVE) {
            for (ItemStack drop : block.getDrops(this.getSession().getPlayer().getInventory().getItem(this.getSession().getPlayer().getInventory().getHeldItemSlot()))) {
                GlowItem item = this.getSession().getPlayer().getWorld().dropItemNaturally(block.getLocation(), drop);
                item.setPickupDelay(30);
                item.setBias(this.getSession().getPlayer());
            }
        }
        // STEP_SOUND actually is the block break particles
        this.getSession().getPlayer().getWorld().playEffectExceptTo(block.getLocation(), Effect.STEP_SOUND, block.getTypeId(), 64, this.getSession().getPlayer());
        block.setType(Material.AIR);
        return null;
    }

}
