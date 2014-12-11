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
package org.dragonet.net.translator.protocols.v0_10_0;

import java.util.HashMap;
import org.dragonet.net.translator.ItemTranslator;

public class ItemTranslator_v0_10_0 implements ItemTranslator{

    private final HashMap<Integer, Integer> itemMap_PE_to_PC = new HashMap<>();
    private final HashMap<Integer, Integer> itemMap_PC_to_PE = new HashMap<>();
    private final int DEFAULT_BLOCK_TO_PE = 7; //Bedrock

    public ItemTranslator_v0_10_0() {
        /* ===== PC to PE ===== */
        itemMap_PC_to_PE.put(1, 1);
        itemMap_PC_to_PE.put(2, 2);
        itemMap_PC_to_PE.put(3, 3);
        for (int i = 8; i <= 11; i++) {
            itemMap_PC_to_PE.put(i, i);
        }
        //for (int i = 1; i <= 12; i++) {
        //    itemMap_PC_to_PE.put(i, i);
        //}
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
        if (itemMap_PE_to_PC.containsKey(itemPE)) {
            return itemMap_PE_to_PC.get(itemPE);
        } else {
            return DEFAULT_BLOCK_TO_PE;
        }
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
