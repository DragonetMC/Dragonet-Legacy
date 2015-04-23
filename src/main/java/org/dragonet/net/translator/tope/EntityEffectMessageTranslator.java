/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.entity.EntityEffectMessage;
import org.dragonet.PocketPotionEffect;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.MobEffectPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class EntityEffectMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, EntityEffectMessage> {

    public EntityEffectMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(EntityEffectMessage packet) {
        MobEffectPacket pk = new MobEffectPacket();
        pk.eid = packet.id;
        pk.action = MobEffectPacket.EffectAction.ADD;
        PocketPotionEffect effect = PocketPotionEffect.getByID(packet.effect);
        effect.setAmpilifier(packet.amplifier);
        effect.setParticles(!packet.hideParticles);
        effect.setDuration(packet.duration);
        return new PEPacket[]{pk};
    }

}
