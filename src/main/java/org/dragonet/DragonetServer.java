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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.Getter;
import net.glowstone.GlowServer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.dragonet.net.NetworkHandler;
import org.dragonet.statistic.StatisticSender;
import org.dragonet.utilities.DragonetVersioning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DragonetServer {

    public @Getter
    GlowServer server;
    public @Getter
    Logger logger;

    public @Getter
    NetworkHandler networkHandler;

    public @Getter
    ExecutorService threadPool;

    public DragonetServer(GlowServer server) {
        this.server = server;
        this.logger = LoggerFactory.getLogger("DragonetServer");
        this.logger.info("Starting Dragonet Server version " + DragonetVersioning.DRAGONET_VERSION + "... ");
    }

    /**
     * Initialize the server, DO NOT CALL IT YOURSELF. Only called by Glowstone
     * main class.
     */
    public void initialize() {
        /* Uncomment following 3 lines when release */
        //this.logger.info("Sending statistic... ");
        //StatisticSender statSender = new StatisticSender(DragonetVersioning.DRAGONET_VERSION, System.currentTimeMillis());
        //statSender.sendStatistic();
        File fileConfig = new File(this.server.getConfigDir() + File.separator + "dragonet.yml");
        if (!fileConfig.exists()) {
            try {
                InputStream inp = DragonetServer.class.getResourceAsStream("/defaults/dragonet.yml");
                try {
                    FileOutputStream oup = new FileOutputStream(fileConfig);
                    try {
                        int data = -1;
                        while ((data = inp.read()) != -1) {
                            oup.write(data);
                        }
                    } finally {
                        oup.close();
                    }
                } finally {
                    inp.close();
                }
            } catch (IOException e) {
            }
        }
        Configuration config = YamlConfiguration.loadConfiguration(fileConfig);
        this.threadPool = Executors.newFixedThreadPool(64);
        String ip = config.getString("server-ip", "0.0.0.0");
        int port = config.getInt("server-port", 19132);
        this.logger.info("Trying to bind on UDP address " + ip + ":" + port + "... ");
        this.networkHandler = new NetworkHandler(this, new InetSocketAddress(ip, port));
        this.logger.info("Dragonet successfully initialized! ");
    }

    /**
     * Trigger a tick update
     */
    public void tickUpdate() {
        this.networkHandler.onTick();
    }
    
    public void shutdown(){
        this.logger.info("Stopping Dragonet server... ");
        this.networkHandler.getUdp().end();
        this.threadPool.shutdown();
    }
}
