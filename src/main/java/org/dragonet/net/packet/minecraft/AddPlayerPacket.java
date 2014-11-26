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
import org.dragonet.entity.metadata.EntityMetaData;
import org.dragonet.utilities.io.PEBinaryWriter;

public class AddPlayerPacket extends PEPacket {

    public long clientID;
    public String username;
    public int eid;
    public float x;
    public float y;
    public float z;
    public int yaw;
    public int pitch;
    public short unknown1;
    public short unknown2;
    public EntityMetaData metadata;

    @Override
    public int pid() {
        return PEPacketIDs.ADD_PLAYER_PACKET;
    }

    @Override
    public void encode() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PEBinaryWriter writer = new PEBinaryWriter(bos);
        try {
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeLong(this.clientID);
            writer.writeString(this.username);
            writer.writeInt(this.eid);
            writer.writeFloat(this.x);
            writer.writeFloat(this.y);
            writer.writeFloat(this.z);
            writer.writeByte((byte) ((this.yaw * (256 / 360)) & 0xFF));
            writer.writeByte((byte) ((this.pitch * (256 / 360)) & 0xFF));
            writer.writeShort(this.unknown1);
            writer.writeShort(this.unknown2);
            writer.write(this.metadata.encode());
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }
}
