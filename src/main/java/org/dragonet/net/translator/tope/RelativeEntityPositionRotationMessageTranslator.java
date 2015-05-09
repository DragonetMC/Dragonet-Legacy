/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.message.play.entity.RelativeEntityPositionRotationMessage;
import org.bukkit.entity.Entity;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.MoveEntitiesPacket;
import org.dragonet.net.packet.minecraft.MovePlayerPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class RelativeEntityPositionRotationMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, RelativeEntityPositionRotationMessage> {

    public RelativeEntityPositionRotationMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(RelativeEntityPositionRotationMessage packet) {
        Entity entity = this.getSession().getPlayer().getWorld().getEntityManager().getEntity(packet.id);
        if (entity == null) {
            return null;
        }
        if (entity instanceof GlowPlayer) {
            boolean isTeleport = Math.sqrt(packet.deltaX ^ 2 + packet.deltaY ^ 2 + packet.deltaZ ^ 2) > 2;
            MovePlayerPacket pkMovePlayer = new MovePlayerPacket(packet.id, (float) entity.getLocation().getX(), (float) entity.getLocation().getY(), (float) entity.getLocation().getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch(), entity.getLocation().getYaw(), isTeleport);
            return new PEPacket[]{pkMovePlayer};
        } else {
            MoveEntitiesPacket.MoveEntityData data = new MoveEntitiesPacket.MoveEntityData();
            data.eid = packet.id;
            data.x = (float) entity.getLocation().getX();
            data.y = (float) entity.getLocation().getY();
            data.z = (float) entity.getLocation().getZ();
            data.yaw = entity.getLocation().getYaw();
            data.pitch = entity.getLocation().getPitch();
            MoveEntitiesPacket pk = new MoveEntitiesPacket(new MoveEntitiesPacket.MoveEntityData[]{data});
            return new PEPacket[]{pk};
        }
    }

}
