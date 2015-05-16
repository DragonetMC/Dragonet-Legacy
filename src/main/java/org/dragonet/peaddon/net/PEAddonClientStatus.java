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
package org.dragonet.peaddon.net;

public class PEAddonClientStatus {

    public boolean isReadingHeader;

    public short packetLength;

    public PEAddonClientStatus() {
        this.isReadingHeader = false;
        this.packetLength = 0;
    }
}
