/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.message.play.entity.RelativeEntityPositionMessage;
import org.bukkit.entity.Entity;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.MovePlayerPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class RelativeEntityPositionMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, RelativeEntityPositionMessage> {

    public RelativeEntityPositionMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(RelativeEntityPositionMessage packet) {
        Entity entity = this.getSession().getPlayer().getWorld().getEntityManager().getEntity(packet.id);
        if (entity == null) {
            return null;
        }
        if (entity instanceof GlowPlayer) {
            boolean isTeleport = Math.sqrt(packet.deltaX ^ 2 + packet.deltaY ^ 2 + packet.deltaZ ^ 2) > 2;
            MovePlayerPacket pkMovePlayer = new MovePlayerPacket(packet.id, (float) entity.getLocation().getX(), (float) entity.getLocation().getY(), (float) entity.getLocation().getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch(), entity.getLocation().getYaw(), isTeleport);
            return new PEPacket[]{pkMovePlayer};
        } else {
            //TODO: Handle other entities
            return null;
        }
    }

}
