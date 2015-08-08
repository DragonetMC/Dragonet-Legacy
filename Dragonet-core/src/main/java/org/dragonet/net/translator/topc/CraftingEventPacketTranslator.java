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
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.CraftingEventPacket;
import org.dragonet.net.translator.PEPacketTranslatorToPC;
import org.dragonet.net.translator.Translator_v0_11;

public class CraftingEventPacketTranslator extends PEPacketTranslatorToPC<Translator_v0_11, CraftingEventPacket>{

    public CraftingEventPacketTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public Message[] handleSpecific(CraftingEventPacket packet) {
        //TODO: From ln.2561
        return null;
    }

}
