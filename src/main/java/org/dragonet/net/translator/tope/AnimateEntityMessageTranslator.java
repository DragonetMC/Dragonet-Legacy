/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.entity.AnimateEntityMessage;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.AnimatePacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class AnimateEntityMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, AnimateEntityMessage> {

    public AnimateEntityMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(AnimateEntityMessage packet) {
        AnimatePacket pkAnimate = new AnimatePacket();
        pkAnimate.eid = packet.id;
        pkAnimate.action = (byte) 0x01; //(msgAnimate.animation & 0xFF);
        return new PEPacket[]{pkAnimate};
    }

}
