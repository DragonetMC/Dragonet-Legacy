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
import org.dragonet.net.inf.mcpe.NetworkChannel;
import org.dragonet.utilities.io.PEBinaryReader;
import org.dragonet.utilities.io.PEBinaryWriter;

public class PlayerActionPacket extends PEPacket {

    public final static int ACTION_START_BREAK = 0;
    public final static int ACTION_CANCEL_BREAK = 1;
    public final static int ACTION_FINISH_BREAK = 2;
    public final static int ACTION_CONSUME_ITEM = 5;
    public final static int ACTION_WAKE_UP = 6;
    public final static int ACTION_RESPAWN = 7;
    
    public long eid;
    public int action;
    public int x;
    public int y;
    public int z;
    public int face;

    public PlayerActionPacket(byte[] data) {
        this.setData(data);
    }

    @Override
    public int pid() {
        return PEPacketIDs.PLAYER_ACTION_PACKET;
    }

    @Override
    public void encode() {
        try {
            setChannel(NetworkChannel.CHANNEL_WORLD_EVENTS);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeLong(eid);
            writer.writeInt(action);
            writer.writeInt(x);
            writer.writeInt(y);
            writer.writeInt(z);
            writer.writeInt(face);
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte(); //PID
            eid = reader.readLong();
            action = reader.readInt();
            x = reader.readInt();
            y = reader.readInt();
            z = reader.readInt();
            face = reader.readInt();
            this.setLength(reader.totallyRead());
        } catch (IOException e) {
        }
    }

}
