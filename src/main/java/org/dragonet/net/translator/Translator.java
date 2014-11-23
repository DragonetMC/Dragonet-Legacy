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
package org.dragonet.net.translator;

import com.flowpowered.networking.Message;
import org.dragonet.net.packet.minecraft.PEPacket;

public interface Translator {
    /**
     * Translate a packet into PC packet(s). 
     * @return Minecraft PC Packet(s)
     */
    public Message[] translateToPC();
    
    /**
     * Translate a packet into PE packet(s). 
     * @return Minecraft PE Packet(s)
     */
    public PEPacket[] translateToPE();
}
