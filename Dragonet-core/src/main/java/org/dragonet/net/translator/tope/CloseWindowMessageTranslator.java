/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.inv.CloseWindowMessage;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.WindowClosePacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class CloseWindowMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, CloseWindowMessage> {

    public CloseWindowMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(CloseWindowMessage packet) {
        if (packet.id != 0) {
            this.getTranslator().cachedWindowType[packet.id & 0xFF] = -1;
        }
        WindowClosePacket pkCloseWindow = new WindowClosePacket();
        pkCloseWindow.windowID = (byte) (packet.id & 0xFF);
        getSession().getOpenedWindows().remove(packet.id);
        return new PEPacket[]{pkCloseWindow};
    }

}
