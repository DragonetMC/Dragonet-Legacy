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
