/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.entity.SpawnObjectMessage;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class SpawnObjectMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, SpawnObjectMessage> {

    public SpawnObjectMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(SpawnObjectMessage packet) {
        switch(packet.type){
        case 2:
            this.getTranslator().cachedSpawnObjects.put(packet.id, packet); //Cache it first :P 
            break;
        }
        return null;
    }

}
