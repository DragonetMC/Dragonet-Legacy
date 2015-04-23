/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.entity.EntityVelocityMessage;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.SetEntityMotionPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class EntityVelocityMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, EntityVelocityMessage> {

    public EntityVelocityMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(EntityVelocityMessage packet) {
        SetEntityMotionPacket pkMotion = new SetEntityMotionPacket();
        SetEntityMotionPacket.EntityMotionData data = new SetEntityMotionPacket.EntityMotionData();
        data.eid = packet.id;
        data.motionX = packet.velocityX;
        data.motionY = packet.velocityY;
        data.motionZ = packet.velocityZ;
        pkMotion.motions = new SetEntityMotionPacket.EntityMotionData[]{data};
        return new PEPacket[]{pkMotion};
    }

}
