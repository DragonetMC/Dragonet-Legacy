/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.net.translator.topc;

import com.flowpowered.networking.Message;
import net.glowstone.net.handler.play.player.DiggingHandler;
import net.glowstone.net.message.play.player.DiggingMessage;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PlayerActionPacket;
import org.dragonet.net.translator.PEPacketTranslatorToPC;
import org.dragonet.net.translator.Translator_v0_11;

public class PlayerActionPacketTranslator extends PEPacketTranslatorToPC<Translator_v0_11, PlayerActionPacket> {

    public PlayerActionPacketTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    private PlayerActionPacket cachedStart;
    
    @Override
    public Message[] handleSpecific(PlayerActionPacket packet) {
        System.out.println("ACTION = " + packet.action + " @ (" + packet.x + ", " + packet.y + ", " + packet.z + ")");
        Message ret = null;
        switch (packet.action) {
            case PlayerActionPacket.ACTION_START_BREAK:
                ret = new DiggingMessage(DiggingMessage.START_DIGGING, packet.x, packet.y, packet.z, packet.face);
                cachedStart = packet;
                break;
            case PlayerActionPacket.ACTION_FINISH_BREAK:
                if(cachedStart != null){
                    ret = new DiggingMessage(DiggingMessage.FINISH_DIGGING, cachedStart.x, cachedStart.y, cachedStart.z, cachedStart.face);
                    cachedStart = null;
                }
                break;
        }
        return new Message[]{ret};
    }

}
