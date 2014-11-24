package org.dragonet.net.translator.protocols.v0_10_0;


import com.flowpowered.networking.Message;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.translator.Translator;

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

public class Translator_v0_10_0 implements Translator{

    @Override
    public Message[] translateToPC(PEPacket packet) {
        System.out.print("Trnaslating to PC: " + packet.getClass().getSimpleName());
        //TODO
        return null;
    }

    @Override
    public PEPacket[] translateToPE(Message message) {
        System.out.print("Trnaslating to PE: " + message.getClass().getSimpleName());
        //TODO
        return null;
    }
}
