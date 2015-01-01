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
import org.dragonet.utilities.io.PEBinaryWriter;

public class RemovePlayerPacket extends PEPacket {

    public int eid;
    public long clientID;
    
    @Override
    public int pid() {
        return PEPacketIDs.REMOVE_PLAYER_PACKET;
    }

    @Override
    public void encode() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeInt(this.eid);
            writer.writeLong(this.clientID);
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }

}
