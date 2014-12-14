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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.dragonet.utilities.io.PEBinaryReader;
import org.dragonet.utilities.io.PEBinaryWriter;
import org.dragonet.net.RaknetConstants;
public class PongPacket extends PEPacket{

    public long pingID;
    public long ServerID;
    public String MOTD;
    @Override
    public int pid() {
        return PEPacketIDs.PONG;
    }

    @Override
    public void encode() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte)(this.pid() & 0xFF));
            writer.writeLong(this.pingID);
            writer.writeLong(this.ServerID);
            writer.write(RaknetConstants.magic);
            writer.writeString(this.MOTD); 
            this.setData(bos.toByteArray());
        }catch(IOException e) {}
    }

    @Override
    public void decode() {
        /*try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte();
            this.pingID = reader.readLong();
        }catch(IOException e) {}*/
    }

}
