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
package org.dragonet.net.translator.topc;

import com.flowpowered.networking.Message;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.CraftingEventPacket;
import org.dragonet.net.packet.minecraft.WindowClosePacket;
import org.dragonet.net.translator.PEPacketTranslatorToPC;
import org.dragonet.net.translator.Translator_v0_11;

public class CraftingEventPacketTranslator extends PEPacketTranslatorToPC<Translator_v0_11, CraftingEventPacket>{

    public CraftingEventPacketTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public Message[] handleSpecific(CraftingEventPacket packet) {
        //TODO: From ln.2561
        if(!getSession().getOpenedWindows().contains(packet.windowId & 0xFF)){
            WindowClosePacket pkClose = new WindowClosePacket();
            pkClose.windowID = packet.windowId;
            getSession().send(pkClose);
            return null;
        }
        int targetID = (int)((packet.uuid.getMostSignificantBits() >> 32) & 0xFFFFFFFF);
        short targetDura = (short)((packet.uuid.getMostSignificantBits() >> 16) | 0xFFFF);
        int amount = (int)(packet.uuid.getMostSignificantBits() & 0xFFFF);
        ItemStack target = new ItemStack(targetID, amount, targetDura);             //Client wants to craft this
        Recipe[] r = getSession().getServer().getCraftingManager().getRecipesFor(target).toArray(new Recipe[0]);
        //TODO: Check crafting type and recipes. 
        //If faild, resend the inventory. 
        return null;
    }

}
