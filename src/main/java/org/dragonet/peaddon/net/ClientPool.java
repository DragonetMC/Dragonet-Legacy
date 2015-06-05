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

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.dragonet.peaddon.DragonetPEAddonServer;

public class ClientPool {

    private @Getter
    DragonetPEAddonServer addonServer;

    private ScheduledExecutorService pool;

    private HashMap<Integer, PEAddonClient> clients;

    public ClientPool(DragonetPEAddonServer addonServer) {
        this.addonServer = addonServer;
        this.pool = Executors.newScheduledThreadPool(32);
        this.clients = new HashMap<>();
    }

    public synchronized void addClient(PEAddonClient client) {
        if (this.clients.containsKey(client.getConnectionID())) {
            return;
        }
        this.clients.put(client.getConnectionID(), client);
        this.pool.scheduleAtFixedRate(client, 1, 10, TimeUnit.MICROSECONDS);
    }

    public synchronized void removeClient(int connID) {
        if (this.clients.containsKey(connID)) {
            this.clients.get(connID).disconnect();
            this.clients.remove(connID);
        }
    }

}
