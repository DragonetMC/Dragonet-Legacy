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

package org.dragonet.net.packet;

import java.util.ArrayList;
import lombok.Getter;

public class RaknetDataPacket extends BinaryPacket{

    private @Getter ArrayList<EncapsulatedPacket> encapsulatedPackets;
    
    public RaknetDataPacket(byte[] data) {
        super(data);
    }
    
    @Override
    public void encode() {
        //TODO
    }

    @Override
    public void decode() {
        //TODO
    }

}
