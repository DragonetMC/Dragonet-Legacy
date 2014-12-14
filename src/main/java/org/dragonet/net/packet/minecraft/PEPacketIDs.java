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

public final class PEPacketIDs {

    /**
     * Actual Minecraft: PE protocol version
     */

    public final static byte PING = (byte) 0x01;
    public final static byte PONG = (byte) 0x1C;
    
    public final static byte CLIENT_CONNECT = (byte) 0x09;
    public final static byte SERVER_HANDSHAKE = (byte) 0x10;
    public final static byte CLIENT_HANDSHAKE = (byte) 0x13;
    public final static byte CLIENT_DISCONNECT = (byte) 0x15;
    
    public final static byte LOGIN_PACKET = (byte) 0x82;
    public final static byte LOGIN_STATUS_PACKET = (byte) 0x83;

    public final static byte MESSAGE_PACKET = (byte) 0x85;
    public final static byte SET_TIME_PACKET = (byte) 0x86;
    public final static byte START_GAME_PACKET = (byte) 0x87;
    public final static byte ADD_MOB_PACKET = (byte) 0x88;
    public final static byte ADD_PLAYER_PACKET = (byte) 0x89;
    public final static byte REMOVE_PLAYER_PACKET = (byte) 0x8a;

    public final static byte ADD_ENTITY_PACKET = (byte) 0x8c;
    public final static byte REMOVE_ENTITY_PACKET = (byte) 0x8d;
    public final static byte ADD_ITEM_ENTITY_PACKET = (byte) 0x8e;
    public final static byte TAKE_ITEM_ENTITY_PACKET = (byte) 0x8f;
    public final static byte MOVE_ENTITY_PACKET = (byte) 0x90;

    public final static byte ROTATE_HEAD_PACKET = (byte) 0x94;
    public final static byte MOVE_PLAYER_PACKET = (byte) 0x95;
    //public final static byte PLACE_BLOCK_PACKET = (byte) 0x96;
    public final static byte REMOVE_BLOCK_PACKET = (byte) 0x97;
    public final static byte UPDATE_BLOCK_PACKET = (byte) 0x98;
    public final static byte ADD_PAINTING_PACKET = (byte) 0x99;
    public final static byte EXPLODE_PACKET = (byte) 0x9a;
    public final static byte LEVEL_EVENT_PACKET = (byte) 0x9b;
    public final static byte TILE_EVENT_PACKET = (byte) 0x9c;
    public final static byte ENTITY_EVENT_PACKET = (byte) 0x9d;

    public final static byte PLAYER_EQUIPMENT_PACKET = (byte) 0xa0;
    public final static byte PLAYER_ARMOR_EQUIPMENT_PACKET = (byte) 0xa1;
    public final static byte INTERACT_PACKET = (byte) 0xa2;
    public final static byte USE_ITEM_PACKET = (byte) 0xa3;
    public final static byte PLAYER_ACTION_PACKET = (byte) 0xa4;

    public final static byte HURT_ARMOR_PACKET = (byte) 0xa6;
    public final static byte SET_ENTITY_DATA_PACKET = (byte) 0xa7;
    public final static byte SET_ENTITY_MOTION_PACKET = (byte) 0xa8;
    //public final static byte SET_ENTITY_LINK_PACKET = (byte) 0xa9;
    public final static byte SET_HEALTH_PACKET = (byte) 0xaa;
    public final static byte SET_SPAWN_POSITION_PACKET = (byte) 0xab;
    public final static byte ANIMATE_PACKET = (byte) 0xac;
    public final static byte RESPAWN_PACKET = (byte) 0xad;
    public final static byte SEND_INVENTORY_PACKET = (byte) 0xae;
    public final static byte DROP_ITEM_PACKET = (byte) 0xaf;
    public final static byte WINDOW_OPEN_PACKET = (byte) 0xb0;
    public final static byte WINDOW_CLOSE_PACKET = (byte) 0xb1;
    public final static byte WINDOW_SET_SLOT_PACKET = (byte) 0xb2;
    public final static byte WINDOW_SET_PROPERTY_PACKET = (byte) 0xb3;
    public final static byte WINDOW_ITEMS_PACKET = (byte) 0xb4;
    //public final static byte WINDOW_ACK_PACKET = (byte) 0xb5;
    public final static byte CHAT_PACKET = (byte) 0xb6;
    public final static byte ADVENTURE_SETTINGS_PACKET = (byte) 0xb7;
    public final static byte ENTITY_DATA_PACKET = (byte) 0xb8;
    //public final static byte PLAYER_INPUT_PACKET = (byte) 0xb9;
    public final static byte FULL_CHUNK_DATA_PACKET = (byte) 0xba;
    public final static byte UNLOAD_CHUNK_PACKET = (byte) 0xbb;
    public final static byte SET_DIFFICULTY_PACKET = (byte) 0xbc;

}
