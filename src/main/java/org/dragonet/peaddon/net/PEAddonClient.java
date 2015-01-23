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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.dragonet.net.DragonetSession;
import org.dragonet.peaddon.DragonetPEAddonServer;
import org.dragonet.utilities.io.DataIOPair;

public class PEAddonClient implements Runnable {

    private Socket clientSocket;

    private @Getter
    DragonetPEAddonServer addonServer;

    private @Getter
    boolean running;

    private @Getter
    DragonetSession peSession;

    private DataIOPair io;

    private PEAddonClientStatus status;

    public PEAddonClient(Socket clientSocket, DragonetPEAddonServer addonServer) {
        this.clientSocket = clientSocket;
        this.addonServer = addonServer;
        try {
            this.io = new DataIOPair(new DataInputStream(this.clientSocket.getInputStream()), new DataOutputStream(this.clientSocket.getOutputStream()));
        } catch (IOException ex) {
            this.disconnect();
            return;
        }
        this.status = new PEAddonClientStatus();
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
        try {
            if (!this.status.isReadingHeader) {
                if (this.io.getInput().available() < 2) {
                    return; //Waiting for the length
                }
                this.status.packetLength = this.io.getInput().readShort();
                this.status.isReadingHeader = false;
            } else {
                //Reading packet body
                if (this.io.getInput().available() < this.status.packetLength) {
                    return;
                }
                byte[] packetData = new byte[this.status.packetLength];
                this.io.getInput().read(packetData);
                //TODO
            }
        } catch (IOException e) {
            this.disconnect();
        }
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
