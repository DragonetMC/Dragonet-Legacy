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
package org.dragonet;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import net.glowstone.block.ItemTable;
import net.glowstone.block.itemtype.ItemType;
import org.dragonet.item.ModItemType;

public class CustomItemManager {

    private @Getter
    DragonetServer dServer;
    private Field field;

    /**
     * Identify wether plugins made changes to this
     */
    private @Getter
    boolean madeChanges;

    public CustomItemManager(DragonetServer dServer) {
        this.dServer = dServer;
        try {
            field = ItemTable.class.getDeclaredField("idToType");
        } catch (NoSuchFieldException | SecurityException ex) {
        }
        field.setAccessible(true);
    }

    public void registerMaterial(int id, ModItemType type, boolean override) {
        if (this.getTable().containsKey(id) && !override) {
            throw new IllegalStateException("ID Already taken! ");
        }
        this.madeChanges = true;
        if (this.getTable().containsKey(id)) {
            this.getTable().remove(id);
        }
        type.setId(id);
        this.getTable().put(id, type);
    }

    private HashMap<Integer, ItemType> getTable() {
        try {
            Object obj = this.field.get(ItemTable.instance());
            return (HashMap<Integer, ItemType>) obj;
        } catch (IllegalArgumentException | IllegalAccessException ex) {
        }
        return null;
    }
}
