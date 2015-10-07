/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.tope;

import net.glowstone.net.message.play.inv.CloseWindowMessage;
import net.glowstone.net.message.play.inv.OpenWindowMessage;
import org.dragonet.inventory.InventoryType;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.WindowOpenPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class OpenWindowMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, OpenWindowMessage> {

    public OpenWindowMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(OpenWindowMessage packet) {
        byte typePE = InventoryType.PEInventory.toPEInventory(InventoryType.PCInventory.fromString(packet.type), packet.slots);
        if (typePE == (byte) 0xFF) {
            //Not supported, close it
            CloseWindowMessage msgCloseWindow = new CloseWindowMessage(packet.id);
            this.getSession().messageReceived(msgCloseWindow);
            return null;
        }
        WindowOpenPacket pkOpenWindow = new WindowOpenPacket();
        pkOpenWindow.windowID = (byte) (packet.id & 0xFF);
        pkOpenWindow.type = typePE;
        pkOpenWindow.slots = (byte) (packet.slots & 0xFFFF);
        pkOpenWindow.x = this.getSession().getPlayer().getLocation().getBlockX();
        pkOpenWindow.y = this.getSession().getPlayer().getLocation().getBlockY();
        pkOpenWindow.z = this.getSession().getPlayer().getLocation().getBlockZ();
        getSession().getOpenedWindows().add(packet.id);
        this.getTranslator().cachedWindowType[packet.id & 0xFF] = typePE;
        return new PEPacket[]{pkOpenWindow};
    }

}
