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
import java.net.ServerSocket;
import java.net.Socket;
import lombok.Getter;
import org.dragonet.peaddon.DragonetPEAddonServer;

public class PEAddonSocket extends Thread {

    private @Getter
    DragonetPEAddonServer addonServer;

    private ServerSocket socket;

    public PEAddonSocket(DragonetPEAddonServer addonServer) throws IOException {
        this.addonServer = addonServer;
        this.socket = new ServerSocket(this.getAddonServer().getPort());
    }

    @Override
    public void run() {
        while (this.addonServer.isRunning()) {
            try {
                Socket clientSocket = this.socket.accept();
                PEAddonClient client = new PEAddonClient(clientSocket, this.getAddonServer());
                this.getAddonServer().getClientPool().addClient(client);
            } catch (IOException ex) {
            }
        }
    }

}
