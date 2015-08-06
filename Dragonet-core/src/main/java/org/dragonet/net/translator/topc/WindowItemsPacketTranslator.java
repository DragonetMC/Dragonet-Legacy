/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.topc;

import com.flowpowered.networking.Message;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.dragonet.inventory.ItemList;
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.inventory.PEWindowConstantID;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.WindowItemsPacket;
import org.dragonet.net.translator.PEPacketTranslatorToPC;
import org.dragonet.net.translator.Translator_v0_11;

public class WindowItemsPacketTranslator extends PEPacketTranslatorToPC<Translator_v0_11, WindowItemsPacket> {

    public WindowItemsPacketTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public Message[] handleSpecific(WindowItemsPacket packet) {
        //Since MCPE sends crafting packets, this packet only indicates item moving/transfering. 
        return null;
        /*
        if (packet.slots.length < 9) {
            emptyCrafting();
            return null;
        }
        ItemStack[] map = new ItemStack[9];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                map[x * 3 + y] = new ItemStack(this.getTranslator().getItemTranslator().translateToPC(packet.slots[x * 3 + y].id & 0xFFFF),
                        packet.slots[x * 3 + y].count & 0xFF, packet.slots[x * 3 + y].meta);
            }
        }
        Recipe recipe = this.getSession().getServer().getCraftingManager().getCraftingRecipe(map);
        if (recipe == null) {
            emptyCrafting();
            return null;
        }
        this.getSession().getPlayer().getInventory().addItem(recipe.getResult());
        this.removeItems(this.getSession().getPlayer().getInventory().getContents(), recipe);
        emptyCrafting();
        return null;
        */
    }
    
    /**
     * Remove enough items from the given item list to form the given recipe.
     * @param items The items to remove the ingredients from.
     * @param recipe A recipe known to match the items.
     */
    public void removeItems(ItemStack[] items, Recipe recipe) {
        ItemList lst = new ItemList(items);
        if (recipe instanceof ShapedRecipe) {
            ShapedRecipe shaped = (ShapedRecipe) recipe;
            for (String itemChar : shaped.getShape()) {
                ItemStack ingredient = shaped.getIngredientMap().get(new Character(itemChar.charAt(0)));
                if (ingredient == null) {
                    continue;
                }
                lst.tryToRemove(ingredient);
            }
        }
        for(int i = 0; i < items.length; i++){
            items[i] = lst.getItems().get(i);
        }
    }
}
