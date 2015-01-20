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

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.RejectedExecutionException;
import lombok.Getter;
import org.dragonet.net.DragonetSession;
import org.dragonet.peaddon.DragonetPEAddonServer;

public class PEAddonClient implements Runnable {

    private Socket clientSocket;

    private @Getter
    DragonetPEAddonServer addonServer;

    private @Getter
    boolean running;

    private @Getter
    DragonetSession peSession;

    public PEAddonClient(Socket clientSocket, DragonetPEAddonServer addonServer) {
        this.clientSocket = clientSocket;
        this.addonServer = addonServer;
        this.running = true;
    }

    @Override
    public void run() {
        if (!(this.addonServer.isRunning() && this.isRunning())) {
            try {
                this.clientSocket.close();
            } catch (IOException e) {
            }
            throw new RejectedExecutionException("PEAddon client disconnected! ");
        }
        //TODO
    }

    public void disconnect() {
        if (this.peSession != null) {
            this.peSession.disconnect("PEAddon disconnected! ");
        }
        this.running = false;
    }

    public int getConnectionID() {
        return this.clientSocket.getRemoteSocketAddress().toString().hashCode();
    }
}
