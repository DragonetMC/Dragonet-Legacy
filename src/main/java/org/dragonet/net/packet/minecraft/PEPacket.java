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

import org.dragonet.net.packet.BinaryPacket;

public abstract class PEPacket extends BinaryPacket{

    public abstract int pid();
    
    @Override
    public abstract void encode();

    @Override
    public abstract void decode();

    
    public static PEPacket fromBinary(byte[] buffer){
        if(buffer.length == 0) return null;
        //System.out.print("Got PEPacket 0x" + Integer.toHexString(buffer[0] & 0xFF));
        PEPacket packet;
        switch(buffer[0]){
            case PEPacketIDs.CLIENT_CONNECT:
                packet = new ClientConnectPacket(buffer);
                break;
            case PEPacketIDs.CLIENT_HANDSHAKE:
                packet = new ClientHandshakePacket(buffer);
                break;
            case PEPacketIDs.LOGIN_PACKET:
                packet = new LoginPacket(buffer);
                break;
            case PEPacketIDs.MESSAGE_PACKET:
                packet = new MessagePacket(buffer);
                break;
            case PEPacketIDs.MOVE_PLAYER_PACKET:
                packet = new MovePlayerPacket(buffer);
                break;
            default:
                return null;
        }
        packet.decode();
        return packet;
    }
}
