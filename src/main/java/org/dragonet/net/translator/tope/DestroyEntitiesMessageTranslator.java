/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import java.util.ArrayList;
import java.util.UUID;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.message.play.entity.DestroyEntitiesMessage;
import org.apache.commons.lang.ArrayUtils;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.PlayerListPacket;
import org.dragonet.net.packet.minecraft.RemoveEntityPacket;
import org.dragonet.net.packet.minecraft.RemovePlayerPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class DestroyEntitiesMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, DestroyEntitiesMessage> {
    
    public DestroyEntitiesMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(DestroyEntitiesMessage packet) {
        this.getTranslator().cachedEntityIDs.removeAll(packet.ids); //Remove from the list
        int[] ids = ArrayUtils.toPrimitive(packet.ids.toArray(new Integer[0]));
        ArrayList<PEPacket> pkRemoveEntity = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            if (!this.getTranslator().cachedPlayerEntities.contains(ids[i])) {
                PEPacket tmp = new RemoveEntityPacket();
                ((RemoveEntityPacket) tmp).eid = ids[i];
                pkRemoveEntity.add(tmp);
            } else {
                GlowPlayer glow = ((GlowPlayer) this.getSession().getPlayer().getWorld().getEntityManager().getEntity(ids[i]));
                PEPacket tmp = new RemovePlayerPacket();
                ((RemovePlayerPacket) tmp).uuid = glow.getUniqueId();
                ((RemovePlayerPacket) tmp).eid = ids[i];
                this.getTranslator().cachedPlayerEntities.remove(new Integer(ids[i]));
                PlayerListPacket pl = new PlayerListPacket(new PlayerListPacket.PlayerInfo(glow.getUniqueId(), -1, null, true, true, null));
                pl.isAdding = false;
                pkRemoveEntity.add(tmp);
                pkRemoveEntity.add(pl);
            }
        }
        return pkRemoveEntity.toArray(new PEPacket[0]);
    }

}
