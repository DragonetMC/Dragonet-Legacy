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

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemList {

    private ArrayList<ItemStack> items;

    public ItemList() {
        this.items = new ArrayList<>();
    }

    public ItemList(ArrayList<ItemStack> items) {
        this.items = items;
    }

    public ItemList(ItemStack[] items) {
        this.items = Lists.newArrayList(items);
    }

    public ItemList(Inventory inventory) {
        this();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) != null) {
                this.items.add(inventory.getItem(i));
            }
        }
    }

    public boolean tryToRemove(ItemStack item) {
        ArrayList<ItemStack> original = this.cloneList();
        if (item == null || item.getTypeId() == 0) {
            return true;
        }
        int toRemove = item.getAmount();
        for (int i = 0; i < items.size(); i++) {
            if (toRemove == 0) {
                break;
            }
            if (items.get(i) == null) {
                continue;
            }
            int typeID = items.get(i).getTypeId();
            int damage = items.get(i).getDurability();
            int amount = items.get(i).getAmount();
            if (typeID == item.getTypeId() && damage == item.getDurability()) {
                //We found the item
                if (amount > toRemove) {
                    //SUCCESS
                    items.get(i).setAmount(amount - toRemove);
                    return true;
                } else {
                    items.set(i, null);
                    toRemove -= amount;
                }
            }
        }
        if (toRemove <= 0) {
            return true;
        } else {
            this.items = original;
            return false;
        }
    }

    private ArrayList<ItemStack> cloneList() {
        ArrayList<ItemStack> cloned = new ArrayList<>();
        for (ItemStack item : this.items) {
            cloned.add(item.clone());
        }
        return cloned;
    }

    public ArrayList<ItemStack> getItems() {
        return items;
    }

    public ItemStack[] getContents() {
        return this.items.toArray(new ItemStack[0]);
    }

}
