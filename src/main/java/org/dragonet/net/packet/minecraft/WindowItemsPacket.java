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
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.inventory.PEWindowConstantID;
import org.dragonet.utilities.io.PEBinaryReader;
import org.dragonet.utilities.io.PEBinaryWriter;

public class WindowItemsPacket extends PEPacket {

    public byte windowID;
    public PEInventorySlot[] slots;
    public int[] hotbar;

    @Override
    public int pid() {
        return PEPacketIDs.WINDOW_ITEMS_PACKET;
    }

    @Override
    public void encode() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeByte(this.windowID);
            writer.writeShort((short) (this.slots.length & 0xFFFF));
            for (PEInventorySlot slot : this.slots) {
                PEInventorySlot.writeSlot(writer, slot);
            }
            if (windowID == PEWindowConstantID.PLAYER_INVENTORY && this.hotbar.length > 0) {
                writer.writeShort((short) (this.hotbar.length & 0xFFFF));
                for (int slot : this.hotbar) {
                    writer.writeInt(slot);
                }
            } else {
                writer.writeShort((short) 0);
            }
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte();
            this.windowID = reader.readByte();
            short cnt = reader.readShort();
            slots = new PEInventorySlot[cnt];
            for (int i = 0; i < cnt; i++) {
                slots[i] = PEInventorySlot.readSlot(reader);
            }
            if (this.windowID == PEWindowConstantID.PLAYER_INVENTORY) {
                short hcnt = reader.readShort();
                hotbar = new int[hcnt];
                for (int i = 0; i < hcnt; i++) {
                    hotbar[i] = reader.readInt();
                }
            }
            this.setLength(reader.totallyRead());
        } catch (IOException e) {
        }
    }

}
