/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.topc;

import com.flowpowered.networking.Message;
import org.dragonet.inventory.PEWindowConstantID;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.WindowSetSlotPacket;
import org.dragonet.net.translator.PEPacketTranslatorToPC;
import org.dragonet.net.translator.Translator_v0_11;

public class WindowSetSlotPacketTranslator extends PEPacketTranslatorToPC<Translator_v0_11, WindowSetSlotPacket> {

    public WindowSetSlotPacketTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public Message[] handleSpecific(WindowSetSlotPacket packet) {
        WindowSetSlotPacket pkSetSlot = (WindowSetSlotPacket) packet;
        if (pkSetSlot.windowID == PEWindowConstantID.PLAYER_INVENTORY) {
            //Crafting! 
            this.getTranslator().processCrafting(pkSetSlot);
        }
        return null;
    }

}
