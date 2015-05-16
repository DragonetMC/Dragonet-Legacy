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
import java.io.FileOutputStream;
import java.io.IOException;
import org.dragonet.utilities.io.PEBinaryReader;

public class LoginPacket extends PEPacket {

    public String username;
    public int protocol1;
    public int protocol2;
    public int clientIntID;

    public boolean slim;
    public byte[] skin;

    public LoginPacket(byte[] data) {
        this.setData(data);
    }

    @Override
    public int pid() {
        return PEPacketIDs.LOGIN_PACKET;
    }

    @Override
    public void encode() {
    }

    @Override
    public void decode() {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte(); //PID
            this.username = reader.readString();
            this.protocol1 = reader.readInt();
            this.protocol2 = reader.readInt();
            this.clientIntID = reader.readInt();
            this.slim = (reader.readByte() & 0xF) > 0;
            int len = reader.readShort();
            this.skin = reader.read(len);
            this.setLength(reader.totallyRead());
        } catch (IOException e) {
        }
    }

}
