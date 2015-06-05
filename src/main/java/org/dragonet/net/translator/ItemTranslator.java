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

public interface ItemTranslator {

    /**
     * Translate a pocket block/item into PC block/item
     *
     * @param itemPE PE Item ID
     * @return PC Item ID
     */
    public int translateToPC(int itemPE);

    /**
     * Translate a PC block/item into PE block/item
     *
     * @param itemPC PC Item ID
     * @return PE Item ID
     */
    public int translateToPE(int itemPC);
}
