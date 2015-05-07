 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.entity.EntityMetadataMessage;
import net.glowstone.net.message.play.entity.SpawnObjectMessage;
import org.bukkit.inventory.ItemStack;
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.AddItemEntityPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class EntityMetadataMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, EntityMetadataMessage> {

    public EntityMetadataMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(EntityMetadataMessage packet) {
        if (!this.getTranslator().cachedEntityIDs.contains(packet.id)) { //Not spawned yet, let's create them
            if (this.getTranslator().cachedSpawnObjects.containsKey(packet.id)) {
                //Spawn it :) 
                SpawnObjectMessage msgObj = this.getTranslator().cachedSpawnObjects.get(packet.id);
                switch (msgObj.type) {
                    case 2: //Dropped Item
                        AddItemEntityPacket pkAddItemEntity = new AddItemEntityPacket();
                        pkAddItemEntity.eid = msgObj.id;
                        pkAddItemEntity.item = new PEInventorySlot((short) (((ItemStack) packet.entries.get(0).value).getTypeId() & 0xFFFF), (byte) (((ItemStack) packet.entries.get(0).value).getAmount() & 0xFF), (short) (((ItemStack) packet.entries.get(0).value).getDurability() & 0xFFFF));
                        pkAddItemEntity.x = (float) msgObj.velX / 32;
                        pkAddItemEntity.y = (float) msgObj.velY / 32;
                        pkAddItemEntity.z = (float) msgObj.velZ / 32;
                        return new PEPacket[]{pkAddItemEntity};
                }
                this.getTranslator().cachedSpawnObjects.remove(packet.id); //Remove it
            } else {
                return null;
            }
        }
        return null;
    }

}
