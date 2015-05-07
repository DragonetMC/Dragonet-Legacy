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
import net.glowstone.block.entity.TileEntity;
import net.glowstone.block.itemtype.ItemType;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PlayerEquipmentPacket;
import org.dragonet.net.packet.minecraft.UseItemPacket;
import org.dragonet.net.translator.PEPacketTranslatorToPC;
import org.dragonet.net.translator.Translator_v0_11;

public class UseItemPacketTranslator extends PEPacketTranslatorToPC<Translator_v0_11, UseItemPacket> {

    public UseItemPacketTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }
    
        static BlockFace convertFace(int direction) {
        if (direction >= 0 && direction < faces.length) {
            return faces[direction];
        } else {
            return BlockFace.SELF;
        }
    }

    private static final BlockFace[] faces = {
        BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST
    };

    @Override
    public Message[] handleSpecific(UseItemPacket packet) {
        UseItemPacket pkUseItem = (UseItemPacket) packet;
        if (!(pkUseItem.face > 0 && pkUseItem.face < 6)) {
            return null;
        }
        
        //Check the slot
        ItemStack test_holding = this.getSession().getPlayer().getInventory().getItemInHand();
        if(packet.item != this.getTranslator().getItemTranslator().translateToPE(test_holding.getTypeId()) ||
           packet.meta != test_holding.getDurability()){
            //Not same, resend
            PlayerEquipmentPacket pkRet = new PlayerEquipmentPacket();
            pkRet.eid = this.getSession().getPlayer().getEntityId();
            pkRet.item = (short)(this.getTranslator().getItemTranslator().translateToPE(test_holding.getTypeId()) & 0xFFFF);
            pkRet.meta = test_holding.getDurability();
            pkRet.selectedSlot = this.getSession().getPlayer().getInventory().getHeldItemSlot();
            getSession().send(pkRet);
            return null;
        }
        
        //Copied from Glowstone class BlockPlacementHandler

        GlowBlock clicked = this.getSession().getPlayer().getWorld().getBlockAt(pkUseItem.x, pkUseItem.y, pkUseItem.z);
        GlowPlayer player = this.getSession().getPlayer();
        ItemStack holding = this.getSession().getPlayer().getInventory().getItemInHand();
        // check that a block-click wasn't against air
        if (clicked != null && clicked.getType() == Material.AIR) {
            // inform the player their perception of reality is wrong
            player.sendBlockChange(clicked.getLocation(), Material.AIR, (byte) 0);
            return null;
        }

        // call interact event
        PlayerInteractEvent event = EventFactory.onPlayerInteract(player, Action.RIGHT_CLICK_BLOCK, clicked, convertFace(pkUseItem.face));

                // attempt to use interacted block
        // DEFAULT is treated as ALLOW, and sneaking is always considered
        boolean useInteractedBlock = event.useInteractedBlock() != Event.Result.DENY;
        if (useInteractedBlock && clicked != null && (!player.isSneaking() || this.getSession().getPlayer().getInventory().getItemInHand() == null)) {
            BlockType useBlockType = ItemTable.instance().getBlock(clicked.getType());
            useInteractedBlock = useBlockType.blockInteract(player, clicked, convertFace(pkUseItem.face), new Vector(1, 1, 1));
        } else {
            useInteractedBlock = false;
        }

                // attempt to use item in hand
        // follows ALLOW/DENY: default to if no block was interacted with
        if ((event.useItemInHand() == Event.Result.DEFAULT ? !useInteractedBlock : event.useItemInHand() == Event.Result.ALLOW) && holding != null) {
            // call out to the item type to determine the appropriate right-click action
            ItemType type = ItemTable.instance().getItem(this.getSession().getPlayer().getInventory().getItemInHand().getType());
            if (clicked == null) {
                type.rightClickAir(player, this.getSession().getPlayer().getInventory().getItemInHand());
            } else {
                type.rightClickBlock(player, clicked, convertFace(pkUseItem.face), this.getSession().getPlayer().getInventory().getItemInHand(), new Vector(1, 1, 1));
            }
        }

                // if anything was actually clicked, make sure the player's up to date
        // in case something is unimplemented or otherwise screwy on our side
        if (clicked != null) {
            player.sendBlockChange(clicked.getLocation(), clicked.getType(), clicked.getData());
            TileEntity entity = clicked.getTileEntity();
            if (entity != null) {
                entity.update(player);
            }
            player.sendBlockChange(clicked.getRelative(convertFace(pkUseItem.face)).getLocation(), clicked.getRelative(convertFace(pkUseItem.face)).getType(), clicked.getRelative(convertFace(pkUseItem.face)).getData());
            TileEntity entity2 = clicked.getTileEntity();
            if (entity2 != null) {
                entity2.update(player);
            }
        }

        // if there's been a change in the held item, make it valid again
        if (holding != null) {
            if (holding.getType().getMaxDurability() > 0 && holding.getDurability() > holding.getType().getMaxDurability()) {
                holding.setAmount(holding.getAmount() - 1);
                holding.setDurability((short) 0);
            }
            if (holding.getAmount() <= 0) {
                holding = null;
            }
        }
        
        return null;
    }

}
