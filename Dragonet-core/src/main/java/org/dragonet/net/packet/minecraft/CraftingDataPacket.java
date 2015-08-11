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
import java.util.ArrayList;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.net.inf.mcpe.NetworkChannel;
import org.dragonet.utilities.io.PEBinaryWriter;

public class CraftingDataPacket extends PEPacket {

    public final static int ENTRY_SHAPELESS = 0;
    public final static int ENTRY_SHAPED = 1;
    public final static int ENTRY_FURNACE = 2;
    public final static int ENTRY_FURNACE_DATA = 3;
    public final static int ENTRY_ENCHANT = 4;

    public ArrayList<Recipe> recipies;

    public boolean cleanRecipies;

    public CraftingDataPacket(boolean cleanRecipies) {
        this.cleanRecipies = cleanRecipies;
    }

    @Override
    public int pid() {
        return PEPacketIDs.CRAFTING_DATA_PACKET;
    }

    @Override
    public void encode() {
        try {
            setChannel(NetworkChannel.CHANNEL_WORLD_EVENTS);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));

            for (Recipe r : recipies) {
                if (ShapelessRecipe.class.isInstance(r)) {
                    writer.writeInt(ENTRY_SHAPELESS);
                    ByteArrayOutputStream ebos = new ByteArrayOutputStream();
                    PEBinaryWriter ewriter = new PEBinaryWriter(ebos);
                    /* Write data */
                    {
                        ewriter.writeInt(((ShapelessRecipe) r).getIngredientList().size());
                        for (ItemStack stack : ((ShapelessRecipe) r).getIngredientList()) {
                            PEInventorySlot.writeSlot(ewriter, PEInventorySlot.fromItemStack(stack));
                        }
                        ewriter.writeInt(1);
                        PEInventorySlot.writeSlot(ewriter, PEInventorySlot.fromItemStack(r.getResult()));
                    }
                    writer.writeInt(ebos.toByteArray().length);
                    writer.write(ebos.toByteArray());
                } else if (FurnaceRecipe.class.isInstance(r)) {
                    FurnaceRecipe f = (FurnaceRecipe) r;
                    if (f.getResult().getDurability() != 0) {
                        writer.writeInt(ENTRY_FURNACE_DATA);
                        ByteArrayOutputStream ebos = new ByteArrayOutputStream();
                        PEBinaryWriter ewriter = new PEBinaryWriter(ebos);
                        /* Write data */
                        {
                            ewriter.writeInt(f.getInput().getTypeId() << 16 | f.getInput().getDurability());
                            PEInventorySlot.writeSlot(ewriter, PEInventorySlot.fromItemStack(f.getResult()));
                        }
                        writer.writeInt(ebos.toByteArray().length);
                        writer.write(ebos.toByteArray());
                    } else {
                        writer.writeInt(ENTRY_FURNACE);
                        ByteArrayOutputStream ebos = new ByteArrayOutputStream();
                        PEBinaryWriter ewriter = new PEBinaryWriter(ebos);
                        /* Write data */
                        {
                            ewriter.writeInt(f.getInput().getTypeId());
                            PEInventorySlot.writeSlot(ewriter, PEInventorySlot.fromItemStack(f.getResult()));
                        }
                        writer.writeInt(ebos.toByteArray().length);
                        writer.write(ebos.toByteArray());
                    }
                //}else if(){
                }else{
                    writer.writeInt(-1);
                    writer.writeInt(0);
                }
            }

            writer.writeInt(recipies.size());
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }

}
