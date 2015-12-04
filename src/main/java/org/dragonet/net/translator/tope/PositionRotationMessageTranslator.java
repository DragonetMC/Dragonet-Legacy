/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.game.PositionRotationMessage;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.MoveEntitiesPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class PositionRotationMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, PositionRotationMessage> {

    public PositionRotationMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    //Memo: Not working yet, idk why. :( 
    @Override
    public PEPacket[] handleSpecific(PositionRotationMessage packet) {
        if (this.getSession().getPlayer() == null) {
            return null;
        }
        //Hack: Yaw and pitch set to 0.0f
        //MovePlayerPacket pkMovePlayer = new MovePlayerPacket(this.getSession().getPlayer().getEntityId(), (float) msgPosRot.x, (float) msgPosRot.y, (float) msgPosRot.z, 0.0f, 0.0f, 0.0f, true);
        MoveEntitiesPacket.MoveEntityData d = new MoveEntitiesPacket.MoveEntityData();
        d.eid = this.getSession().getPlayer().getEntityId();
        d.x = (float) packet.x;
        d.y = (float) packet.y;
        d.z = (float) packet.z;
        d.yaw = 0.0f;
        d.pitch = 0.0f;
        MoveEntitiesPacket pkMoveEntity = new MoveEntitiesPacket(new MoveEntitiesPacket.MoveEntityData[]{d});
        return new PEPacket[]{pkMoveEntity};
    }

}
