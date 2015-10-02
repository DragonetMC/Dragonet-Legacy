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
package org.dragonet.net.inf.mcpe.jraklib;

import net.beaconpe.jraklib.Logger;

/**
 * JRakLib logging interface.
 *
 * @author jython234
 */
public class JRakLibLogger implements Logger{
    private org.slf4j.Logger logger;

    public JRakLibLogger(org.slf4j.Logger logger){
        this.logger = logger;
    }

    @Override
    public void notice(String s) {
        logger.info("[NOTICE]: "+s);
    }

    @Override
    public void critical(String s) {
        logger.error("[CRITICAL]: "+s);
    }

    @Override
    public void emergency(String s) {
        logger.error("[EMERGENCY]: "+s);
    }
}

