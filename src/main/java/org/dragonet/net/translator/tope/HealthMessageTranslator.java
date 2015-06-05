/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.game.HealthMessage;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.RespawnPacket;
import org.dragonet.net.packet.minecraft.SetHealthPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class HealthMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, HealthMessage> {

    public HealthMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(HealthMessage packet) {
        int h = (int) packet.health;
        if (h <= 0) {
            //DEAD
            SetHealthPacket pk1 = new SetHealthPacket(h);
            RespawnPacket pk2 = new RespawnPacket();
            pk2.x = (float) getTranslator().getSession().getServer().getWorlds().get(0).getSpawnLocation().getX();
            pk2.y = (float) getTranslator().getSession().getServer().getWorlds().get(0).getSpawnLocation().getY();
            pk2.z = (float) getTranslator().getSession().getServer().getWorlds().get(0).getSpawnLocation().getZ();
            return new PEPacket[]{pk1, pk2};
        } else {
            SetHealthPacket pk = new SetHealthPacket(h);
            return new PEPacket[]{pk};
        }
    }

}
