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
package org.dragonet.net.translator.tope;

import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.message.play.entity.EntityHeadRotationMessage;
import org.bukkit.entity.Entity;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.MovePlayerPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class EntityHeadRotationTranslator extends MessageTranslatorToPE<Translator_v0_11, EntityHeadRotationMessage> {

    public EntityHeadRotationTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(EntityHeadRotationMessage packet) {
        Entity entity = this.getSession().getPlayer().getWorld().getEntityManager().getEntity(packet.id);
        if (entity == null) {
            return null;
        }
        if(GlowPlayer.class.isAssignableFrom(packet.getClass())){
            MovePlayerPacket pk = new MovePlayerPacket(packet.id, 0f, 0f, 0f, packet.getRotation(), 0f, 0f, false);
            return new PEPacket[]{pk};
        }
        return null;
    }

}
