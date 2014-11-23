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

import com.google.common.collect.Lists;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import lombok.Getter;
import org.dragonet.utilities.io.PEBinaryReader;
import org.dragonet.utilities.io.PEBinaryWriter;

public class RaknetDataPacket extends BinaryPacket{

    private @Getter int sequenceNumber;
    private @Getter ArrayList<EncapsulatedPacket> encapsulatedPackets = new ArrayList<>();
    
    public RaknetDataPacket(byte[] data) {
        super(data);
    }

    public RaknetDataPacket(EncapsulatedPacket[] packets) {
        for(EncapsulatedPacket pk : packets) this.encapsulatedPackets.add(pk);
    }
    
    @Override
    public void encode() {
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte)0x84);
            writer.writeTriad(this.sequenceNumber);
            for(EncapsulatedPacket packet : this.encapsulatedPackets.toArray(new EncapsulatedPacket[0])){
                writer.write(EncapsulatedPacket.toBinary(packet));
            }
            this.setData(bos.toByteArray());
        }catch(IOException e){
            e.printStackTrace();
            this.setData(new byte[0]);
        }
    }

    @Override
    public void decode() {
        try{
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            this.sequenceNumber = reader.readTriad();
            while(reader.available() > 3){
                this.encapsulatedPackets.add(EncapsulatedPacket.fromBinary(reader));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
