/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.game.PositionRotationMessage;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.MovePlayerPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class PositionRotationMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, PositionRotationMessage> {

    public PositionRotationMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(PositionRotationMessage packet) {
        if (this.getSession().getPlayer() == null) {
            return null;
        }
        MovePlayerPacket pkMovePlayer = new MovePlayerPacket(
                this.getSession().getPlayer().getEntityId(), 
                (packet.flags & 0x01) > 0 ? (float)(this.getSession().getPlayer().getLocation().getX() + packet.x) : (float) packet.x, 
                (packet.flags & 0x02) > 0 ? (float)(this.getSession().getPlayer().getLocation().getY() + packet.y) : (float) packet.y, 
                (packet.flags & 0x04) > 0 ? (float)(this.getSession().getPlayer().getLocation().getX() + packet.z) : (float) packet.z, 
                (packet.flags & 0x08) > 0 ? this.getSession().getPlayer().getLocation().getYaw() + packet.rotation : packet.rotation, 
                (packet.flags & 0x10) > 0 ? this.getSession().getPlayer().getLocation().getPitch() + packet.pitch : packet.pitch,  
                (packet.flags & 0x08) > 0 ? this.getSession().getPlayer().getLocation().getYaw() + packet.rotation : packet.rotation, 
                true);
        return new PEPacket[]{pkMovePlayer};
    }

}
