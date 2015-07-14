/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.game.StateChangeMessage;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.StartGamePacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class StateChangeMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, StateChangeMessage> {

    public StateChangeMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(StateChangeMessage packet) {
        if (packet.reason == StateChangeMessage.Reason.GAMEMODE.ordinal()) {
            if (this.getSession().getPlayer() == null) {
                return null;
            }
            StartGamePacket pkStartGame = new StartGamePacket();
            pkStartGame.eid = this.getSession().getPlayer().getEntityId();
            pkStartGame.gamemode = ((int) packet.value) & 0x1;
            pkStartGame.seed = 0;
            pkStartGame.generator = 1;
            pkStartGame.spawnX = this.getSession().getPlayer().getWorld().getSpawnLocation().getBlockX();
            pkStartGame.spawnY = this.getSession().getPlayer().getWorld().getSpawnLocation().getBlockY();
            pkStartGame.spawnZ = this.getSession().getPlayer().getWorld().getSpawnLocation().getBlockZ();
            pkStartGame.x = (float) this.getSession().getPlayer().getLocation().getX();
            pkStartGame.y = (float) this.getSession().getPlayer().getLocation().getY();
            pkStartGame.z = (float) this.getSession().getPlayer().getLocation().getZ();
            return new PEPacket[]{pkStartGame};
        }
        return null;
    }

}
