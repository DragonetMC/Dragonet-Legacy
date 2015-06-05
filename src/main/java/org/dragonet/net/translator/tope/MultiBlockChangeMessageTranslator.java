/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.game.BlockChangeMessage;
import net.glowstone.net.message.play.game.MultiBlockChangeMessage;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.UpdateBlockPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class MultiBlockChangeMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, MultiBlockChangeMessage> {

    public MultiBlockChangeMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(MultiBlockChangeMessage packet) {
        UpdateBlockPacket pkBC = new UpdateBlockPacket();
        pkBC.records = new UpdateBlockPacket.UpdateBlockRecord[packet.records.size()];
        //PEPacket[] packets = new PEPacket[msgMBC.records.size()];
        int i = 0;
        for (BlockChangeMessage msgBC : packet.records) {
            //packets[i] = this.translateToPE(msgBC)[0];
            pkBC.records[i] = new UpdateBlockPacket.UpdateBlockRecord();
            pkBC.records[i].x = msgBC.x;
            pkBC.records[i].z = msgBC.z;
            pkBC.records[i].y = (byte) (msgBC.y & 0xFF);
            pkBC.records[i].block = (byte) (this.getTranslator().getItemTranslator().translateToPE(msgBC.type >> 4) & 0xFF);
            pkBC.records[i].meta = (byte) (msgBC.type & 0xFF);
            i++;
        }
        return new PEPacket[]{pkBC};
    }

}
