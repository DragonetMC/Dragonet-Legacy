/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.player.PlayerPositionLookMessage;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.MovePlayerPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class PlayerPositionLookMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, PlayerPositionLookMessage> {

    public PlayerPositionLookMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(PlayerPositionLookMessage packet) {
        MovePlayerPacket pkMovePlayer = new MovePlayerPacket();
        pkMovePlayer.eid = this.getSession().getPlayer().getEntityId();
        pkMovePlayer.x = (float) packet.x;
        pkMovePlayer.y = (float) packet.y;
        pkMovePlayer.z = (float) packet.z;
        pkMovePlayer.yaw = packet.yaw;
        pkMovePlayer.pitch = packet.pitch;
        pkMovePlayer.teleport = true;
        return new PEPacket[]{pkMovePlayer};
    }

}
