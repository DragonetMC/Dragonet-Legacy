/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.player.PlayerPositionMessage;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.MovePlayerPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class PlayerPositionMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, PlayerPositionMessage> {

    public PlayerPositionMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(PlayerPositionMessage packet) {
        MovePlayerPacket pkMovePlayer = new MovePlayerPacket();
        pkMovePlayer.eid = this.getSession().getPlayer().getEntityId();
        pkMovePlayer.x = (float) packet.x;
        pkMovePlayer.y = (float) packet.y;
        pkMovePlayer.z = (float) packet.z;
        pkMovePlayer.yaw = this.getSession().getPlayer().getLocation().getYaw();
        pkMovePlayer.pitch = this.getSession().getPlayer().getLocation().getPitch();
        pkMovePlayer.teleport = true;
        return new PEPacket[]{pkMovePlayer};
    }

}
