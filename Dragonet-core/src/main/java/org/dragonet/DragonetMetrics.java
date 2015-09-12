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

package org.dragonet;

import java.io.File;
import java.io.IOException;
import org.dragonet.utilities.DragonetVersioning;

public class DragonetMetrics extends org.mcstats.Metrics {

    private DragonetServer server;

    public DragonetMetrics(final DragonetServer server) throws IOException {
        super("Dragonet Server", DragonetVersioning.DRAGONET_VERSION);
        this.server = server;
        
        Graph ext = createGraph("Extensions");
        ext.addPlotter(new Plotter("DAPIS") {
            @Override
            public int getValue() {
                return server.isEnabledJs() ? 1 : 0;
            }
        });
        ext.addPlotter(new Plotter("PHP") {
            @Override
            public int getValue() {
                return server.isEnabledPhp() ? 1 : 0;
            }
        });
        ext.addPlotter(new Plotter("DragonPortal Enabled") {
            @Override
            public int getValue() {
                return server.isEnabledDragonPortal() ? 1 : 0;
            }
        });
    }
    
    @Override
    public String getFullServerVersion() {
        return DragonetVersioning.DRAGONET_VERSION;
    }

    @Override
    public int getPlayersOnline() {
        return server.getServer().getOnlinePlayers().size();
    }

    @Override
    public File getConfigFile() {
        return new File("Statistic.properties");
    }

}
