/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.entity.CollectItemMessage;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.PickUpItemPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class CollectItemMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, CollectItemMessage> {

    public CollectItemMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(CollectItemMessage packet) {
        PickUpItemPacket pkPickUp = new PickUpItemPacket();
        pkPickUp.target = packet.collector;
        pkPickUp.eid = packet.id;
        return new PEPacket[]{pkPickUp};
    }

}
