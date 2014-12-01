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
package org.dragonet.inventory;

import java.io.IOException;
import org.dragonet.utilities.io.PEBinaryReader;
import org.dragonet.utilities.io.PEBinaryWriter;

public class PEInventorySlot {

    public short id;
    public byte count;
    public short meta;

    public PEInventorySlot() {
        this((short) 0, (byte) 0, (short) 0);
    }

    public PEInventorySlot(short id, byte count, short meta) {
        this.id = id;
        this.count = count;
        this.meta = meta;
    }

    public static PEInventorySlot readSlot(PEBinaryReader reader) throws IOException {
        short id = reader.readShort();
        byte count = reader.readByte();
        short meta = reader.readShort();
        return new PEInventorySlot(id, count, meta);
    }

    public static void writeSlot(PEBinaryWriter writer, PEInventorySlot slot) throws IOException {
        writer.writeShort(slot.id);
        writer.writeByte(slot.count);
        writer.writeShort(slot.meta);
    }
}
