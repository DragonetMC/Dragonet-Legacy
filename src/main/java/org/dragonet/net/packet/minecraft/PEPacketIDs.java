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
    
    public static final byte PING = (byte) 0x00;
    public static final byte PONG = (byte) 0x00;
    public static final byte CLIENT_CONNECT = (byte) 0x09;
    public static final byte SERVER_HANDSHAKE = (byte) 0x10;
    public static final byte CLIENT_HANDSHAKE = (byte) 0x13;

    public static final byte LOGIN_PACKET = (byte) 0x8f;
    public static final byte PLAY_STATUS_PACKET = (byte) 0x90;
    public static final byte DISCONNECT_PACKET = (byte) 0x91;
    public static final byte BATCH_PACKET = (byte) 0x92;
    public static final byte TEXT_PACKET = (byte) 0x93;
    public static final byte SET_TIME_PACKET = (byte) 0x94;
    public static final byte START_GAME_PACKET = (byte) 0x95;
    public static final byte ADD_PLAYER_PACKET = (byte) 0x96;
    public static final byte REMOVE_PLAYER_PACKET = (byte) 0x97;
    public static final byte ADD_ENTITY_PACKET = (byte) 0x98;
    public static final byte REMOVE_ENTITY_PACKET = (byte) 0x99;
    public static final byte ADD_ITEM_ENTITY_PACKET = (byte) 0x9a;
    public static final byte TAKE_ITEM_ENTITY_PACKET = (byte) 0x9b;
    public static final byte MOVE_ENTITY_PACKET = (byte) 0x9c;
    public static final byte MOVE_PLAYER_PACKET = (byte) 0x9d;
    public static final byte REMOVE_BLOCK_PACKET = (byte) 0x9e;
    public static final byte UPDATE_BLOCK_PACKET = (byte) 0x9f;
    public static final byte ADD_PAINTING_PACKET = (byte) 0xa0;
    public static final byte EXPLODE_PACKET = (byte) 0xa1;
    public static final byte LEVEL_EVENT_PACKET = (byte) 0xa2;
    public static final byte BLOCK_EVENT_PACKET = (byte) 0xa3;
    public static final byte ENTITY_EVENT_PACKET = (byte) 0xa4;
    public static final byte MOB_EFFECT_PACKET = (byte) 0xa5;
    public static final byte UPDATE_ATTRIBUTES_PACKET = (byte) 0xa6;
    public static final byte MOB_EQUIPMENT_PACKET = (byte) 0xa7;
    public static final byte MOB_ARMOR_EQUIPMENT_PACKET = (byte) 0xa8;
    public static final byte INTERACT_PACKET = (byte) 0xa9;
    public static final byte USE_ITEM_PACKET = (byte) 0xaa;
    public static final byte PLAYER_ACTION_PACKET = (byte) 0xab;
    public static final byte HURT_ARMOR_PACKET = (byte) 0xac;
    public static final byte SET_ENTITY_DATA_PACKET = (byte) 0xad;
    public static final byte SET_ENTITY_MOTION_PACKET = (byte) 0xae;
    public static final byte SET_ENTITY_LINK_PACKET = (byte) 0xaf;
    public static final byte SET_HEALTH_PACKET = (byte) 0xb0;
    public static final byte SET_SPAWN_POSITION_PACKET = (byte) 0xb1;
    public static final byte ANIMATE_PACKET = (byte) 0xb2;
    public static final byte RESPAWN_PACKET = (byte) 0xb3;
    public static final byte DROP_ITEM_PACKET = (byte) 0xb4;
    public static final byte WINDOW_OPEN_PACKET = (byte) 0xb5;
    public static final byte WINDOW_CLOSE_PACKET = (byte) 0xb6;
    public static final byte WINDOW_SET_SLOT_PACKET = (byte) 0xb7;
    public static final byte WINDOW_SET_DATA_PACKET = (byte) 0xb8;
    public static final byte WINDOW_SET_CONTENT_PACKET = (byte) 0xb9;
    public static final byte CRAFTING_DATA_PACKET = (byte) 0xba;
    public static final byte CRAFTING_EVENT_PACKET = (byte) 0xbb;
    public static final byte ADVENTURE_SETTINGS_PACKET = (byte) 0xbc;
    public static final byte BLOCK_ENTITY_DATA_PACKET = (byte) 0xbd;
    //public static final byte PLAYER_INPUT_PACKET = (byte) 0xbe;
    public static final byte FULL_CHUNK_DATA_PACKET = (byte) 0xbf;
    public static final byte SET_DIFFICULTY_PACKET = (byte) 0xc0;
    //public static final byte CHANGE_DIMENSION_PACKET = (byte) 0xc1;
    //public static final byte SET_PLAYER_GAMETYPE_PACKET = (byte) 0xc2;
    public static final byte PLAYER_LIST_PACKET = (byte) 0xc3;
    //public static final byte TELEMETRY_EVENT_PACKET = (byte) 0xc4;

}
