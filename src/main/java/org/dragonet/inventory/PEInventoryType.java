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

public final class PEInventoryType {

    public final static byte CHEST = (byte) 0x0;
    public final static byte DOUBLE_CHEST = (byte) 0x1;
    public final static byte PLAYER = (byte) 0x2;
    public final static byte FURNACE = (byte) 0x3;
    public final static byte CRAFTING = (byte) 0x4;
    public final static byte WORKBENCH = (byte) 0x5;
    public final static byte STONECUTTER = (byte) 0x6;

    public final static class SlotSize {
        public final static int CHEST = 27;
        public final static int DOUBLE_CHEST = 27 + 27;
        public final static int PLAYER = 36;
        public final static int FURNACE = 3;
        public final static int CRAFTING = 5;
        public final static int WORKBENCH = 10;
        public final static int STONECUTTER = 10;
    }
}
