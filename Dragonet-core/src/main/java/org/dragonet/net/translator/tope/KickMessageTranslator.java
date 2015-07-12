/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.KickMessage;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.DisconnectPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class KickMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, KickMessage> {

    public KickMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(KickMessage packet) {
        String msg;
        try {
            //String msg = ((ChatMessage) message).text.asPlaintext();
            Object json = new JSONParser().parse(packet.text.encode());
            if (json instanceof JSONObject) {
                msg = this.getTranslator().translateChatMessage((JSONObject) json);
            } else {
                msg = packet.text.asPlaintext();
            }
        } catch (ParseException ex) {
            return null;
        }
        return new PEPacket[]{new DisconnectPacket(msg)};
    }

}
