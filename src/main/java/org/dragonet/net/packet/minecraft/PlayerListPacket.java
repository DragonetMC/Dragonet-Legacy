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
package org.dragonet.net.packet.minecraft;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import lombok.Data;
import org.dragonet.net.inf.mcpe.NetworkChannel;
import org.dragonet.utilities.io.PEBinaryReader;
import org.dragonet.utilities.io.PEBinaryWriter;

public class PlayerListPacket extends PEPacket {

    public boolean isAdding = true;
    
    public ArrayList<PlayerInfo> players;

    public PlayerListPacket() {
        players = new ArrayList<>();
    }
    
    public PlayerListPacket(PlayerInfo... playerArray){
        players = new ArrayList<>();
        for(PlayerInfo info : playerArray){
            players.add(info);
        }
    }
    
    @Override
    public int pid() {
        return PEPacketIDs.PLAYER_LIST_PACKET;
    }

    @Override
    public void encode() {
        try {
            setChannel(NetworkChannel.CHANNEL_TEXT);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            if(isAdding){
                writer.writeByte((byte)0x00);
            }else{
                writer.writeByte((byte)0x01);
            }
            writer.writeInt(players.size());
            for(PlayerInfo info : players){
                info.encode(writer, isAdding);
            }
            this.setData(bos.toByteArray());
        } catch (IOException e) {

        }
    }

    @Override
    public void decode() {
    }

    @Data
    public static class PlayerInfo {

        public UUID uuid;
        public long eid;
        public String name;
        
        public boolean skinSlim;
        public boolean skinTransparent;
        public byte[] skin;

        public PlayerInfo(UUID uuid, long eid, String name, boolean skinSlim, boolean skinTransparent, byte[] skin) {
            this.uuid = uuid;
            this.eid = eid;
            this.name = name;
            this.skinSlim = skinSlim;
            this.skinTransparent = skinTransparent;
            this.skin = skin;
        }
        
        public void encode(PEBinaryWriter out, boolean isAdding) throws IOException {
            out.writeUUID(uuid);
            if(!isAdding) return;
            out.writeLong(eid);
	        out.writeString(name);
            out.writeByte(skinSlim ? (byte)0x01 : (byte)0x00);
	        out.writeByte((byte) 0x00); //TODO get the default skin to work.
            /*out.writeByte(skinTransparent ? (byte)0x01 : (byte)0x00);
	        out.writeShort((short) skin.length);
            out.write(skin);*/
        }
        
        public static PlayerInfo decode(PEBinaryReader reader) throws IOException {
            UUID uuid = reader.readUUID();
            long eid = reader.readLong();
            String name = reader.readString();
            boolean skinSlim = (reader.readByte() & 0xFF) == 0x01;
            boolean skinTransparent = (reader.readByte() & 0xFF) == 0x01;
            short skinLen = reader.readShort();
            byte[] skin = reader.read(skinLen);
            return new PlayerInfo(uuid, eid, name, skinSlim, skinTransparent, skin);
        }
    }

}
