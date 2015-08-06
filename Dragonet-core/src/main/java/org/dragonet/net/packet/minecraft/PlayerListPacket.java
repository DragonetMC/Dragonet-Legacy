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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import lombok.Data;
import org.dragonet.utilities.io.PEBinaryWriter;

public class PlayerListPacket extends PEPacket {

    
    public ArrayList<PlayerInfo> players;
    public boolean isAdding;

    @Override
    public int pid() {
        return PEPacketIDs.PLAYER_LIST_PACKET;
    }

    @Override
    public void encode() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            if(isAdding){
                writer.writeByte((byte)0x00);
            }else{
                writer.writeByte((byte)0x01);
            }
            writer.writeInt(players.size());
            for(PlayerInfo info : players){
                writer.write(info.encode(isAdding));
            }
            this.setData(bos.toByteArray());
        } catch (IOException e) {

        }
    }

    @Override
    public void decode() {
    }

    @Data
    public static class PlayerInfo {

        //REMOVE: UUID
        //ADD: UUID, entity id, name, isSlim, skin 
        
        public byte[] encode(boolean adding) {
            return new byte[0]; //TODO
        }
    }

}
