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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.glowstone.util.nbt.CompoundTag;
import net.glowstone.util.nbt.NBTInputStream;
import net.glowstone.util.nbt.NBTOutputStream;
import org.bukkit.inventory.ItemStack;
import org.dragonet.utilities.io.PEBinaryReader;
import org.dragonet.utilities.io.PEBinaryWriter;

public class PEInventorySlot {

    public short id;
    public byte count;
    public short meta;
    public CompoundTag nbt;

    public PEInventorySlot() {
        this((short) 0, (byte) 0, (short) 0);
    }

    public PEInventorySlot(short id, byte count, short meta) {
        this.id = id;
        this.count = count;
        this.meta = meta;
        nbt = new CompoundTag();
    }

    public PEInventorySlot(short id, byte count, short meta, CompoundTag nbt) {
        this.id = id;
        this.count = count;
        this.meta = meta;
        this.nbt = nbt;
    }
    
    

    public static PEInventorySlot readSlot(PEBinaryReader reader) throws IOException {
        short id = (short)(reader.readShort() & 0xFFFF); //Unsigned
        if(id <= 0){
            return new PEInventorySlot((short)0, (byte)0, (short)0);
        }
        byte count = reader.readByte();
        short meta = reader.readShort();
        short lNbt = reader.readShort();
        if(lNbt <= 0){
            return new PEInventorySlot(id, count, meta);
        }
        byte[] nbtData = reader.read(lNbt);
        NBTInputStream nbtin = new NBTInputStream(new ByteArrayInputStream(nbtData));
        CompoundTag nbt = nbtin.readCompound();
        nbtin.close();
        return new PEInventorySlot(id, count, meta, nbt);
    }

    public static void writeSlot(PEBinaryWriter writer, PEInventorySlot slot) throws IOException {
        if(slot == null || (slot != null && slot.id == 0)){
            writer.writeShort((short)0);
            return;
        }
        writer.writeShort(slot.id);
        writer.writeByte(slot.count);
        writer.writeShort(slot.meta);
        if(slot.nbt == null){
            writer.writeShort((short) 0);
        }else{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            NBTOutputStream nos = new NBTOutputStream(bos);
            nos.writeTag(slot.nbt);
            byte[] nbtdata = bos.toByteArray();
            writer.writeShort((short)(nbtdata.length & 0xFFFF));
            writer.write(nbtdata);
        }
    }
    
    public static PEInventorySlot fromItemStack(ItemStack item){
        PEInventorySlot slot = new PEInventorySlot();
        slot.id = (short)(item.getTypeId() & 0xFFFF);
        if(slot.id <= 0){
            return slot;
        }
        slot.count = (byte)(item.getAmount() & 0xFF);
        slot.meta = (short)(item.getDurability() & 0xFFFF);
        
        if(item.getItemMeta().getDisplayName() != null){
            slot.nbt = new CompoundTag();
            
            CompoundTag display = new CompoundTag();
            display.putString("Name", item.getItemMeta().getDisplayName());
            
            slot.nbt.putCompound("display", display);
        }
        return slot;
    }

    @Override
    public String toString() {
        return "{PE Item: ID=" + this.id + ", Count=" + (this.count & 0xFF) + ", Data=" + this.meta + "}";
    }
}
