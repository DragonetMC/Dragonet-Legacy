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
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.utilities.io.PEBinaryWriter;

public class WindowItemsPacket extends PEPacket {

    public byte windowID;
    public PEInventorySlot[] slots;
    public PEInventorySlot[] hotbar;

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
            writer.writeShort((short)(this.slots.length & 0xFFFF));
            for(PEInventorySlot slot : this.slots){
                PEInventorySlot.writeSlot(writer, slot);
            }
            if(windowID == (byte)0x00){
                writer.writeShort((short)(this.hotbar.length & 0xFFFF));
                for(PEInventorySlot slot : this.hotbar){
                PEInventorySlot.writeSlot(writer, slot);
            }
            }else{
                writer.writeShort((short)0);
            }
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }

}
