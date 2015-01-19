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

import lombok.Getter;
import org.dragonet.DragonetServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DragonetPEAddonServer {

    private @Getter
    DragonetServer server;
    
    private @Getter
    Logger logger;
    
    //public ArrayList<AddonSession> sessions;

    public DragonetPEAddonServer(DragonetServer server) {
        this.server = server;
        this.logger = LoggerFactory.getLogger("PEAddon");
    }

    /**
     * Initialize the PEAddon Server when Dragonet starts. 
     * DO NOT CALL IT YOURSELF, IT'S AN INTERNAL-ONLY FUNCTION! 
     */
    public void initialize() {
        int addonPort = this.server.getNetworkHandler().getUdp().getServerPort();
        //TODO
    }
}
