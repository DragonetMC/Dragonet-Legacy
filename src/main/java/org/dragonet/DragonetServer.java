/* GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 */

package org.dragonet;

import lombok.Getter;
import net.glowstone.GlowServer;
import org.dragonet.utilities.DragonetVersioning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DragonetServer {
    public @Getter GlowServer server;
    public @Getter Logger logger;

    public DragonetServer(GlowServer server) {
        this.server = server;
        this.logger = LoggerFactory.getLogger("DragonetServer");
        this.logger.info("Starting Dragonet Server version " + DragonetVersioning.DRAGONET_VERSION + "... ");
    }
    
    public void initialize(){
        
    }
}
