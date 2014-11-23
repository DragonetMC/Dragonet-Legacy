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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import lombok.Getter;
import org.dragonet.utilities.io.PEBinaryReader;

public class RaknetDataPacket extends BinaryPacket{

    private @Getter ArrayList<EncapsulatedPacket> encapsulatedPackets;
    
    public RaknetDataPacket(byte[] data) {
        super(data);
    }
    
    @Override
    public void encode() {
    }

    @Override
    public void decode() {
        try{
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            while(reader.available() > 3){
                this.encapsulatedPackets.add(EncapsulatedPacket.fromBinary(reader));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
