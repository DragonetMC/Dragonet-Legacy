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

package org.dragonet.net.packet.minecraft;

public class DisconnectPacket extends PEPacket{

    @Override
    public int pid() {
        return PEPacketIDs.CLIENT_DISCONNECT;
    }

    @Override
    public void encode() {
    }

    @Override
    public void decode() {
    }

}
