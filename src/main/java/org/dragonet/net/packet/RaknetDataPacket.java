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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import org.dragonet.utilities.io.PEBinaryReader;
import org.dragonet.utilities.io.PEBinaryWriter;

public class RaknetDataPacket extends BinaryPacket {

    private @Getter
    @Setter
    int sequenceNumber;
    private @Getter
    ArrayList<EncapsulatedPacket> encapsulatedPackets = new ArrayList<>();

    public RaknetDataPacket(int sequenceNumber) {
        this(sequenceNumber, new EncapsulatedPacket[0]);
    }

    public RaknetDataPacket(int sequenceNumber, EncapsulatedPacket[] packets) {
        this.sequenceNumber = sequenceNumber;
        for (EncapsulatedPacket pk : packets) {
            this.encapsulatedPackets.add(pk);
        }
    }

    public RaknetDataPacket(byte[] data) {
        super(data);
    }

    @Override
    public void encode() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) 0x84);
            writer.writeTriad(this.sequenceNumber);
            for (EncapsulatedPacket packet : this.encapsulatedPackets.toArray(new EncapsulatedPacket[this.encapsulatedPackets.size()])) {
                writer.write(EncapsulatedPacket.toBinary(packet));
            }
            this.setData(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            this.setData(new byte[0]);
        }
    }

    @Override
    public void decode() {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            this.sequenceNumber = reader.readTriad();
            EncapsulatedPacket pkEncap;
            while (reader.available() > 3) {
                pkEncap = EncapsulatedPacket.fromBinary(reader);
                if (pkEncap == null) {
                    return;
                }
                this.encapsulatedPackets.add(pkEncap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getLength() {
        int len = 4;
        for (EncapsulatedPacket packet : this.encapsulatedPackets) {
            len += packet.buffer.length + 1 + 2;
            if (packet.reliability == 2 || packet.reliability == 3 || packet.reliability == 4 || packet.reliability == 6 || packet.reliability == 7) {
                len += 3;
            }
            if (packet.reliability == 1 || packet.reliability == 3 || packet.reliability == 4 || packet.reliability == 7) {
                len += 3 + 1;
            }
            if (packet.hasSplit) {
                len += 4 + 2 + 4;
            }
        }
        return len;
    }

}
