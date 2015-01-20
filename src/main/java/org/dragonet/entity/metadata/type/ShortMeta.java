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
package org.dragonet.entity.metadata.type;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.dragonet.entity.metadata.EntityMetaDataObject;

public class ShortMeta implements EntityMetaDataObject {

    public short data;

    public ShortMeta(short data) {
        this.data = data;
    }

    @Override
    public int type() {
        return 1;
    }

    @Override
    public byte[] encode() {
        ByteBuffer buff = ByteBuffer.allocate(2);
        buff.order(ByteOrder.LITTLE_ENDIAN);
        buff.putShort(this.data);
        return buff.array();
    }

}
