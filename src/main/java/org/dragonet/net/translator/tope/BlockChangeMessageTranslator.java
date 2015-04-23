/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.game.BlockChangeMessage;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.UpdateBlockPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class BlockChangeMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, BlockChangeMessage> {

    public BlockChangeMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(BlockChangeMessage packet) {
        UpdateBlockPacket pkBC = new UpdateBlockPacket();
        UpdateBlockPacket.UpdateBlockRecord rec = new UpdateBlockPacket.UpdateBlockRecord();
        rec.x = packet.x;
        rec.z = packet.z;
        rec.y = (byte) (packet.y & 0xFF);
        rec.block = (byte) (this.getTranslator().getItemTranslator().translateToPE(packet.type >> 4) & 0xFF);
        rec.meta = (byte) (packet.type & 0xFF);
        pkBC.records = new UpdateBlockPacket.UpdateBlockRecord[]{rec};
        return new PEPacket[]{pkBC};
    }

}
