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

public class SetTimePacket extends PEPacket {

    public int time;
    public boolean isTimeFreezed;

    public SetTimePacket() {
    }

    public SetTimePacket(int time, boolean isTimeFreezed) {
        this.time = time;
        this.isTimeFreezed = isTimeFreezed;
    }

    @Override
    public int pid() {
        return PEPacketIDs.SET_TIME_PACKET;
    }

    @Override
    public void encode() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            //writer.writeInt((int)(((this.time / GlowWorld.DAY_LENGTH) * 19200) & 0xFFFFFFFF));
            //We hack for now :P
            writer.writeInt(20 * 60);
            if (this.isTimeFreezed) {
                writer.writeByte((byte) 0);
            } else {
                writer.writeByte((byte) 1);
            }
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }

}
