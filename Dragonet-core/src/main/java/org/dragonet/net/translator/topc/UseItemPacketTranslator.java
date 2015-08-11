/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.topc;

import com.flowpowered.networking.Message;
import net.glowstone.net.handler.play.player.BlockPlacementHandler;
import net.glowstone.net.message.play.player.BlockPlacementMessage;
import net.glowstone.net.message.play.player.PlayerSwingArmMessage;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PlayerEquipmentPacket;
import org.dragonet.net.packet.minecraft.UpdateBlockPacket;
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
        System.out.println("FACE=" + (pkUseItem.face & 0xFF) + ", ITEM=" + packet.item);
        if(pkUseItem.face == 0xFF){
            //Air touch
            PlayerSwingArmMessage msg = new PlayerSwingArmMessage();   //Left click air
            return new Message[]{msg};
        }
        if (!(pkUseItem.face >= 0 && pkUseItem.face < 6)) {
            return null;
        }

        //Check the slot
        ItemStack test_holding = this.getSession().getPlayer().getInventory().getItemInHand();
        if (packet.item.id != this.getTranslator().getItemTranslator().translateToPE(test_holding.getTypeId())
                || packet.item.meta != test_holding.getDurability()) {
            //Not same, resend slot
            PlayerEquipmentPacket pkRet = new PlayerEquipmentPacket();
            pkRet.eid = this.getSession().getPlayer().getEntityId();
            pkRet.item = new PEInventorySlot((short)0, (byte)0, (short)0);
            pkRet.item.id = (short) (this.getTranslator().getItemTranslator().translateToPE(test_holding.getTypeId()) & 0xFFFF);
            pkRet.item.count = (byte)(test_holding.getAmount() & 0xFF);
            pkRet.item.meta = test_holding.getDurability();
            pkRet.selectedSlot = this.getSession().getPlayer().getInventory().getHeldItemSlot();
            //Resend block
            UpdateBlockPacket pkUpdateBlock = new UpdateBlockPacket();
            UpdateBlockPacket.UpdateBlockRecord blockRec = new UpdateBlockPacket.UpdateBlockRecord();
            blockRec.x = packet.x;
            blockRec.z = packet.z;
            blockRec.y = (byte) (packet.y & 0xFF);
            blockRec.block = (byte) (this.getSession().getPlayer().getWorld().getBlockAt(pkUseItem.x, pkUseItem.y, pkUseItem.z).getTypeId() & 0xFF);
            blockRec.meta = (byte) (this.getSession().getPlayer().getWorld().getBlockAt(pkUseItem.x, pkUseItem.y, pkUseItem.z).getData() & 0xFF);
            pkUpdateBlock.records = new UpdateBlockPacket.UpdateBlockRecord[]{blockRec};
            getSession().send(pkRet);
            getSession().send(pkUpdateBlock);
            return null;
        }


        //Copied from Glowstone class BlockPlacementHandler
        new BlockPlacementHandler().handle(getSession(), new BlockPlacementMessage(packet.x, packet.y, packet.z, packet.face, test_holding, 0, 0, 0));

        return null;
    }

}
