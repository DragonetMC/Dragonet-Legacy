/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.topc;

import com.flowpowered.networking.Message;
import net.glowstone.net.message.play.entity.AnimateEntityMessage;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.AnimatePacket;
import org.dragonet.net.translator.PEPacketTranslatorToPC;
import org.dragonet.net.translator.Translator_v0_11;

public class AnimatePacketTranslator extends PEPacketTranslatorToPC<Translator_v0_11, AnimatePacket> {

    public AnimatePacketTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public Message[] handleSpecific(AnimatePacket packet) {
        return new Message[]{new AnimateEntityMessage(this.getSession().getPlayer().getEntityId(), 1)};
    }

}
