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
package org.dragonet.net.translator;

import java.util.HashMap;

public class ItemTranslator_v0_11 implements ItemTranslator {

    private final HashMap<Integer, Integer> itemMap_PE_to_PC = new HashMap<>();
    private final HashMap<Integer, Integer> itemMap_PC_to_PE = new HashMap<>();
    private final int DEFAULT_BLOCK_TO_PE = 248; //Bedrock

    public ItemTranslator_v0_11() {
        /* ===== PC to PE ===== */
        for (int i = 0; i <= 22; i++) {
            itemMap_PC_to_PE.put(i, i);
        }
        itemMap_PC_to_PE.put(24, 24);
        for (int i = 26; i <= 27; i++) {
            itemMap_PC_to_PE.put(i, i);
        }
        for (int i = 30; i <= 68; i++) {
            itemMap_PC_to_PE.put(i, i);
        }
        itemMap_PC_to_PE.put(71, 71);
        itemMap_PC_to_PE.put(73, 73);
        itemMap_PC_to_PE.put(74, 74);
        for (int i = 78; i <= 83; i++) {
            itemMap_PC_to_PE.put(i, i);
        }
        for (int i = 85; i <= 87; i++) {
            itemMap_PC_to_PE.put(i, i);
        }
        itemMap_PC_to_PE.put(89, 89);
        for (int i = 91; i <= 92; i++) {
            itemMap_PC_to_PE.put(i, i);
        }
        //itemMap_PC_to_PE.put(95, 20); //Not sure for STAINED_GLASS -> GLASS
        for (int i = 95; i <= 96; i++) {
            itemMap_PC_to_PE.put(i, i);
        }
        for (int i = 99; i <= 112; i++) {
            itemMap_PC_to_PE.put(i, i);
        }
        itemMap_PC_to_PE.put(114, 114);
        for (int i = 120; i <= 121; i++) {
            itemMap_PC_to_PE.put(i, i);
        }
        for (int i = 127; i <= 129; i++) {
            itemMap_PC_to_PE.put(i, i);
        }
        for (int i = 133; i <= 136; i++) {
            itemMap_PC_to_PE.put(i, i);
        }
        itemMap_PC_to_PE.put(139, 139);
        for (int i = 141; i <= 142; i++) {
            itemMap_PC_to_PE.put(i, i);
        }
        for (int i = 155; i <= 159; i++) {
            itemMap_PC_to_PE.put(i, i);
        }
        for (int i = 163; i <= 164; i++) {
            itemMap_PC_to_PE.put(i, i);
        }
        itemMap_PC_to_PE.put(169, 89); //SEA_LANTERN -> GLOWSTONE
        for (int i = 170; i <= 174; i++) {
            itemMap_PC_to_PE.put(i, i);
        }
        for (int i = 243; i <= 249; i++) {
            itemMap_PC_to_PE.put(i, i);
        }

        itemMap_PC_to_PE.put(8, 8);
        itemMap_PC_to_PE.put(9, 9);
        itemMap_PC_to_PE.put(10, 10);
        itemMap_PC_to_PE.put(11, 11);
        for (int i = 1; i <= 12; i++) {
            itemMap_PC_to_PE.put(i, i);
        }
        //TODO: More blocks/items

        /* ===== PE to PC ===== */
        //TODO: Initialize the PE-to-PC map
    }

    /**
     * Translate a pocket block/item into PC block/item
     *
     * @param itemPE PE Item ID
     * @return PC Item ID
     */
    @Override
    public int translateToPC(int itemPE) {
        /*
         if (itemMap_PE_to_PC.containsKey(itemPE)) {
         return itemMap_PE_to_PC.get(itemPE);
         } else {
         return DEFAULT_BLOCK_TO_PC;
         }
         */
        return itemPE; //TODO
    }

    /**
     * Translate a PC block/item into PE block/item
     *
     * @param itemPC PC Item ID
     * @return PE Item ID
     */
    @Override
    public int translateToPE(int itemPC) {
        if (itemMap_PC_to_PE.containsKey(itemPC)) {
            return itemMap_PC_to_PE.get(itemPC);
        } else {
            return DEFAULT_BLOCK_TO_PE;
        }
    }
}
