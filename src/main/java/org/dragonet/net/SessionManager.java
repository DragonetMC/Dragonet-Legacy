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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import org.dragonet.DragonetServer;

public final class SessionManager {

    public final static long serverID = 0x0000000012345678L;

    private @Getter
    DragonetServer server;

    @Getter
    private final ConcurrentMap<String, DragonetSession> sessions = new ConcurrentHashMap<>();

    public SessionManager(DragonetServer server) {
        this.server = server;
    }
    
    public void onTick(){
        for(DragonetSession session : sessions.values()){
            session.onTick();
        }
    }

    public void removeSessionRaw(DragonetSession session) {
        sessions.remove(session.getSessionKey());
    }
}
