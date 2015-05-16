/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.inv.SetWindowSlotMessage;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class SetWindowSlotMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, SetWindowSlotMessage> {

    public SetWindowSlotMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(SetWindowSlotMessage packet) {
        if (this.getTranslator().cachedWindowType[packet.id & 0xFF] == -1) {
            return null;
        }
        //NOT WORKING YET
            /*
         //System.out.println("Updating slot: WID=" + msgSetSlot.id + ", ITEM=" + msgSetSlot.item + ", SLOTID=" + msgSetSlot.slot);
         //byte typePE = (byte) (this.cachedWindowType[msgSetSlot.id & 0xFF] & 0xFF);
         int targetSlot = 0;
         if (msgSetSlot.id == 0) {
         if (msgSetSlot.slot >= 9 && msgSetSlot.slot <= 35) {
         targetSlot = (short) (msgSetSlot.slot - 9);
         } else if (msgSetSlot.slot >= 36 && msgSetSlot.slot <= 44) {
         targetSlot = (short) (msgSetSlot.slot - 36);
         } else {
         targetSlot = (short) (msgSetSlot.slot & 0xFFFF);
         }
         } else {
         targetSlot = (short) (msgSetSlot.slot & 0xFFFF);
         }
         WindowSetSlotPacket pkSetSlot = new WindowSetSlotPacket();
         pkSetSlot.windowID = (byte) (msgSetSlot.id & 0xFF);
         pkSetSlot.slot = (short) (targetSlot & 0xFFFF);
         pkSetSlot.item = new PEInventorySlot((short) (msgSetSlot.item.getTypeId() & 0xFFFF), (byte) (msgSetSlot.item.getAmount() & 0xFF), msgSetSlot.item.getDurability());
         return new PEPacket[]{pkSetSlot};
         */
        if (packet.id == 0) {
            //Player Inventory
            this.getSession().sendInventory();
            return null;
        } else {
            //TODO
            //this.getSession().getPlayer().getOpenInventory().getTopInventory()
        }
        return null;
    }

}
