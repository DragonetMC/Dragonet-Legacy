/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.inv.SetWindowContentsMessage;
import org.dragonet.inventory.InventoryType;
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.inventory.PEWindowConstantID;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.WindowItemsPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class SetWindowContentsMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, SetWindowContentsMessage> {

    public SetWindowContentsMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(SetWindowContentsMessage packet) {
        if (packet.id == 0) {
            //Inventory Items(Included hotbar)
            WindowItemsPacket pkInventory = new WindowItemsPacket();
            pkInventory.windowID = PEWindowConstantID.PLAYER_INVENTORY;
            pkInventory.slots = new PEInventorySlot[InventoryType.SlotSize.PLAYER];
            for (int i = 9; i <= 44; i++) {
                if (packet.items[i] != null) {
                    pkInventory.slots[i - 9] = new PEInventorySlot((short) (packet.items[i].getTypeId() & 0xFFFF), (byte) (packet.items[i].getAmount() & 0xFF), packet.items[i].getDurability());
                } else {
                    pkInventory.slots[i - 9] = new PEInventorySlot();
                }
            }
            pkInventory.hotbar = new int[9];
            for (int i = 36; i <= 44; i++) {
                pkInventory.hotbar[i - 36] = i - 9;
            }
            //Armor
            WindowItemsPacket pkArmorInv = new WindowItemsPacket();
            pkArmorInv.windowID = PEWindowConstantID.PLAYER_ARMOR;
            pkArmorInv.slots = new PEInventorySlot[4];
            for (int i = 5; i <= 8; i++) {
                if (packet.items[i] != null) {
                    pkArmorInv.slots[i - 5] = new PEInventorySlot((short) (packet.items[i].getTypeId() & 0xFFFF), (byte) (packet.items[i].getAmount() & 0xFF), packet.items[i].getDurability());
                } else {
                    pkArmorInv.slots[i - 5] = new PEInventorySlot();
                }
            }
            if (this.getSession().getSentAndReceivedChunks() != -1) {
                //Not fully loaded
                this.getSession().getQueueAfterChunkSent().add(pkInventory);
                this.getSession().getQueueAfterChunkSent().add(pkArmorInv);
            } else {
                return new PEPacket[]{pkInventory, pkArmorInv};
            }
            return null;
        }
        //TODO: Implement other types of inventory
        //switch(this.getSession().getPlayer().)
        System.out.println("Updating window content for " + packet.id + ", which has " + packet.items.length + " slots. ");
        return null;
    }

}
