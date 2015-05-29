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

public class LevelEventPacket extends PEPacket {

    private final static byte TAME_FAIL = (byte) 6;
    private final static byte TAME_SUCCESS = (byte) 7;
    private final static byte SHAKE_WET = (byte) 8;
    private final static byte USE_ITEM = (byte) 9;
    private final static byte EAT_GRASS_ANIMATION = (byte) 10;
    private final static byte FISH_HOOK_BUBBLE = (byte) 11;
    private final static byte FISH_HOOK_POSITION = (byte) 12;
    private final static byte FISH_HOOK_HOOK = (byte) 13;
    private final static byte FISH_HOOK_TEASE = (byte) 14;
    private final static byte SQUID_INK_CLOUD = (byte) 15;
    private final static byte AMBIENT_SOUND = (byte) 16;
    private final static byte RESPAWN = (byte) 17;

    private short eventID;
    private float x;
    private float y;
    private float z;
    private int data;

    @Override
    public int pid() {
        return PEPacketIDs.LEVEL_EVENT_PACKET;
    }

    @Override
    public void encode() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeShort(eventID);
            writer.writeFloat(x);
            writer.writeFloat(y);
            writer.writeFloat(z);
            writer.writeInt(data);
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }

}
