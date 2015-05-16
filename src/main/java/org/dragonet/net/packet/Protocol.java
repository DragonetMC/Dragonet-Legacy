/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.packet;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import org.dragonet.net.packet.minecraft.*;

public final class Protocol {

    private static HashMap<Byte, Class<? extends PEPacket>> protocol;

    static {
        protocol = new HashMap<>();
        registerDecoder(PEPacketIDs.DISCONNECT_PACKET, DisconnectPacket.class);
        registerDecoder(PEPacketIDs.BATCH_PACKET, BatchPacket.class);
        registerDecoder(PEPacketIDs.TEXT_PACKET, ChatPacket.class);
        registerDecoder(PEPacketIDs.CLIENT_CONNECT, ClientConnectPacket.class);
        registerDecoder(PEPacketIDs.CLIENT_HANDSHAKE, ClientHandshakePacket.class);
        registerDecoder(PEPacketIDs.DISCONNECT_PACKET, DisconnectPacket.class);
        registerDecoder(PEPacketIDs.DROP_ITEM_PACKET, DropItemPacket.class);
        registerDecoder(PEPacketIDs.LOGIN_PACKET, LoginPacket.class);
        registerDecoder(PEPacketIDs.MOVE_PLAYER_PACKET, MovePlayerPacket.class);
        registerDecoder(PEPacketIDs.PING, PingPongPacket.class);
        registerDecoder(PEPacketIDs.PLAYER_EQUIPMENT_PACKET, PlayerEquipmentPacket.class);
        registerDecoder(PEPacketIDs.REMOVE_BLOCK_PACKET, RemoveBlockPacket.class);
        registerDecoder(PEPacketIDs.UPDATE_BLOCK_PACKET, UpdateBlockPacket.class);
        registerDecoder(PEPacketIDs.USE_ITEM_PACKET, UseItemPacket.class);
        registerDecoder(PEPacketIDs.WINDOW_SET_SLOT_PACKET, WindowSetSlotPacket.class);
        registerDecoder(PEPacketIDs.INTERACT_PACKET, InteractPacket.class);
    }

    private static void registerDecoder(byte id, Class<? extends PEPacket> clazz) {
        if (protocol.containsKey(id)) {
            return;
        }
        try {
            clazz.getConstructor(byte[].class);
        } catch (NoSuchMethodException | SecurityException ex) {
            return;
        }
        protocol.put(id, clazz);
    }

    public static PEPacket decode(byte[] data) {
        if (data == null || data.length < 1) {
            return null;
        }
        byte pid = data[0];
        //System.out.println("Trying to decode 0x" + Integer.toHexString(pid & 0xFF) + "... ");
        if (protocol.containsKey(pid)) {
            Class<? extends PEPacket> c = protocol.get(pid);
            try {
                PEPacket pk = c.getDeclaredConstructor(byte[].class).newInstance((Object) data);
                pk.decode();
                //System.out.println("Decode 0x" + Integer.toHexString(pid & 0xFF) + " as " + pk.getClass().getSimpleName() + ". ");
                return pk;
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            }
        }
        return null;
    }
}
