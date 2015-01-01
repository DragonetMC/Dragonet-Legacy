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

package org.dragonet.net.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.dragonet.net.RaknetConstants;
import org.dragonet.utilities.io.PEBinaryWriter;

public class ServerInfoPacket extends BinaryPacket{

    public long time;
    public long serverID;
    public String serverName;
    
    @Override
    public void encode() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte(RaknetConstants.ID_UNCONNECTED_PING_OPEN_CONNECTIONS);
            writer.writeLong(this.time);
            writer.writeLong(this.serverID);
            writer.write(RaknetConstants.magic);
            writer.writeString("MCCPP;MINECON;" + this.serverName);
            this.setData(bos.toByteArray());
        } catch (IOException e) {
            this.setData(new byte[0]);
        }
    }

    @Override
    public void decode() {
    }

}
