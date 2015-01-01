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
package org.dragonet.net;

public final class RaknetConstants {

    public static final byte[] magic = new byte[]{(byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0xfd, (byte) 0xfd, (byte) 0xfd, (byte) 0xfd, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78};

    public static final byte ID_PING_OPEN_CONNECTIONS = (byte) 0x01;
    public static final byte ID_UNCONNECTED_PING_OPEN_CONNECTIONS = (byte) 0x1c;

    public static final byte ID_OPEN_CONNECTION_REQUEST_1 = (byte) 0x05;
    public static final byte ID_OPEN_CONNECTION_REPLY_1 = (byte) 0x06;
    public static final byte ID_OPEN_CONNECTION_REQUEST_2 = (byte) 0x07;
    public static final byte ID_OPEN_CONNECTION_REPLY_2 = (byte) 0x08;

    public static final byte ACK = (byte) 0xA0;
    public static final byte NACK = (byte) 0xC0;
}
