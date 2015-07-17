/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
