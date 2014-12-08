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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.dragonet.utilities.io.PEBinaryReader;

public class UseItemPacket extends PEPacket {

    public int x;
    public int y;
    public int z;
    public int face;
    public short item;
    public short meta;
    public int eid;
    public float fx;
    public float fy;
    public float fz;
    public float posX;
    public float posY;
    public float posZ;

    public UseItemPacket(byte[] data) {
        this.setData(data);
    }
    
    @Override
    public int pid() {
        return PEPacketIDs.USE_ITEM_PACKET;
    }

    @Override
    public void encode() {
    }

    @Override
    public void decode() {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte(); //PID
            this.x = reader.readInt();
            this.y = reader.readInt();
            this.z = reader.readInt();
            this.face = reader.readByte() & 0xF;
            this.item = reader.readShort();
            this.meta = reader.readShort();
            this.eid = reader.readInt();
            this.fx = reader.readFloat();
            this.fy = reader.readFloat();
            this.fz = reader.readFloat();
            this.posX = reader.readFloat();
            this.posY = reader.readFloat();
            this.posZ = reader.readFloat();
        } catch (IOException e) {
        }
    }

}
