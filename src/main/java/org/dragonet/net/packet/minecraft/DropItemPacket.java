/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.packet.minecraft;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.utilities.io.PEBinaryReader;

public class DropItemPacket extends PEPacket {

    public DropItemPacket(byte[] data) {
        this.setData(data);
    }

    @Override
    public int pid() {
        return PEPacketIDs.DROP_ITEM_PACKET;
    }

    public long eid;
    public byte unknown;
    public PEInventorySlot slot;

    @Override
    public void encode() {
    }

    @Override
    public void decode() {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte(); //PID
            this.eid = reader.readLong();
            this.unknown = reader.readByte();
            this.slot = PEInventorySlot.readSlot(reader);
            this.setLength(reader.totallyRead());
        } catch (IOException e) {
        }
    }

}
