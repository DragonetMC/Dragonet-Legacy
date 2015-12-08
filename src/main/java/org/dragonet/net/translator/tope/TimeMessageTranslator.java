/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.game.TimeMessage;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.SetTimePacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class TimeMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, TimeMessage> {

    public TimeMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(TimeMessage packet) {
        SetTimePacket pkTime = new SetTimePacket(packet.time, false); //Because of the hack, we use 0 here. 
        return new PEPacket[]{pkTime};
    }

}
