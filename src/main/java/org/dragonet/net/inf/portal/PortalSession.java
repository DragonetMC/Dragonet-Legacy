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

package org.dragonet.net.inf.portal;

import org.dragonet.DragonetServer;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.translator.BaseTranslator;

public final class PortalSession extends DragonetSession {

    public PortalSession(String username, String remoteIP, int remotePort, DragonetServer dServer, BaseTranslator translator) {
        super(dServer, translator);
    }

    @Override
    public String getSessionKey() {
        return "PORTAL-"; //TODO: Remote address. 
    }

    @Override
    public void send(PEPacket pk) {
    }

    @Override
    public void send(PEPacket pk, int reliability) {
    }

}
