/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.topc;

import com.flowpowered.networking.Message;
import org.bukkit.entity.Player;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.RemoveBlockPacket;
import org.dragonet.net.translator.PEPacketTranslatorToPC;
import org.dragonet.net.translator.Translator_v0_11;

public class RemoveBlockPacketTranslator extends PEPacketTranslatorToPC<Translator_v0_11, RemoveBlockPacket> {

    public RemoveBlockPacketTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public Message[] handleSpecific(RemoveBlockPacket packet) {
        if (!(this.getSession().getPlayer() instanceof Player)) {
            return null;
        }
        return null;
    }

}
