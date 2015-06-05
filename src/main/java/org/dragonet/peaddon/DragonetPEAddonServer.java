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
package org.dragonet.peaddon;

import java.io.IOException;
import org.dragonet.peaddon.net.ClientPool;
import lombok.Getter;
import org.dragonet.DragonetServer;
import org.dragonet.peaddon.net.PEAddonSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DragonetPEAddonServer {

    private @Getter
    DragonetServer server;

    private @Getter
    Logger logger;

    private @Getter
    int port;

    private @Getter
    boolean running;

    private @Getter
    ClientPool clientPool;

    private @Getter
    PEAddonSocket serverSocket;

    public DragonetPEAddonServer(DragonetServer server) {
        this.server = server;
        this.logger = LoggerFactory.getLogger("PEAddon");
        this.clientPool = new ClientPool(this);
    }

    /**
     * Initialize the PEAddon Server when Dragonet starts. DO NOT CALL IT
     * YOURSELF, IT'S AN INTERNAL-ONLY FUNCTION!
     *
     * @throws IOException Thrown if faild to bind on the PEAddon TCP port.
     */
    public void initialize() throws IOException {
        this.port = this.server.getNetworkHandler().getUdp().getServerPort(); //TCP port, so it's fine
        this.running = true;
        this.serverSocket = new PEAddonSocket(this);
        this.serverSocket.start();
    }

    public void stop() {
        this.running = false;
    }
}
