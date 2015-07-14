/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.topc;

import com.flowpowered.networking.Message;
import net.glowstone.net.message.play.player.PlayerPositionLookMessage;
import org.bukkit.Location;
import org.dragonet.entity.DragonetPlayer;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.MovePlayerPacket;
import org.dragonet.net.translator.PEPacketTranslatorToPC;
import org.dragonet.net.translator.Translator_v0_11;

public class MovePlayerPacketTranslator extends PEPacketTranslatorToPC<Translator_v0_11, MovePlayerPacket> {

    public MovePlayerPacketTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public Message[] handleSpecific(MovePlayerPacket packet) {
        Location loc = new Location(this.getSession().getPlayer().getWorld(), packet.x, packet.y, packet.z);
        if (!this.getSession().validateMovement(loc)) { //Revert
            this.getSession().sendPosition();
            System.out.println("Reverted movement! ");
            return null;
        }
        //Hack ;P
        ((DragonetPlayer) this.getSession().getPlayer()).setLocation(new Location(((DragonetPlayer) this.getSession().getPlayer()).getWorld(), packet.x, packet.y, packet.z, packet.yaw, packet.pitch));
        return new Message[]{new PlayerPositionLookMessage(false, (double) packet.x, (double) packet.y, (double) packet.z, packet.yaw, packet.pitch)};
    }

}
