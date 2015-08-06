/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.entity.EntityEquipmentMessage;
import org.bukkit.entity.Player;
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.PlayerEquipmentPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class EntityEquipmentMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, EntityEquipmentMessage> {

    public EntityEquipmentMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(EntityEquipmentMessage packet) {
        if (this.getSession().getPlayer() == null) return null;
        if (!(this.getSession().getPlayer().getWorld().getEntityManager().getEntity(packet.id) instanceof Player)) {
            return null; //Only process player's equipments for now. 
        }
        switch (packet.slot) {
            case 0: //Held Item
                PlayerEquipmentPacket pkEquipment = new PlayerEquipmentPacket();
                pkEquipment.eid = packet.id;
                if (packet.stack != null) {
                    pkEquipment.item = new PEInventorySlot((short) (packet.stack.getTypeId() & 0xFFFF)   ,(byte)(packet.stack.getAmount() & 0xFF) ,(short) (packet.stack.getDurability() & 0xFFFF));
                } else {
                    pkEquipment.item = new PEInventorySlot();
                }
                pkEquipment.slot = (byte) 0;
                return new PEPacket[]{pkEquipment};
        }
        return null;
    }

}
