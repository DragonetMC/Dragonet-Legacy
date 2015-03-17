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
import java.util.logging.Level;
import lombok.Getter;
import net.glowstone.GlowServer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.dragonet.net.NetworkHandler;
import org.dragonet.peaddon.DragonetPEAddonServer;
import org.dragonet.rhino.Rhino;
import org.dragonet.statistic.StatisticSender;
import org.dragonet.utilities.DragonetVersioning;
import org.dragonet.rhino.Rhino;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DragonetServer {

    private static DragonetServer INSTANCE;

    public static DragonetServer instance() {
        return INSTANCE;
    }

    private @Getter
    GlowServer server;

    private @Getter
    Logger logger;

    private @Getter
    Rhino rhino;
    
    private @Getter
    NetworkHandler networkHandler;

    private @Getter
    ExecutorService threadPool;

    private @Getter
    DragonetPEAddonServer addonServer;

    private @Getter
    boolean addonSupported;

    private @Getter
    CustomItemManager customMaterialManager;
    
    public DragonetServer(GlowServer server) {
        INSTANCE = this;
        this.server = server;
        this.logger = LoggerFactory.getLogger("DragonetServer");
        this.customMaterialManager = new CustomItemManager(this);
        this.logger.info("Starting Dragonet Server version " + DragonetVersioning.DRAGONET_VERSION + "... ");
        this.rhino = new Rhino();
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
        this.logger.info("Current Minecraft PC Version: " + DragonetVersioning.MINECRAFT_PC_VERSION);
        this.logger.info("Current Minecraft: Pocket Edition Version: " + DragonetVersioning.MINECRAFT_PE_VERSION);
        this.threadPool = Executors.newFixedThreadPool(64);
        String ip = config.getString("server-ip", "0.0.0.0");
        int port = config.getInt("server-port", 19132);
        this.logger.info("Trying to bind on UDP address " + ip + ":" + port + "... ");
        try {
            this.networkHandler = new NetworkHandler(this, new InetSocketAddress(ip, port));
        } catch (Exception ex) {
            this.getLogger().error("FAILD TO BIND ON THE Minecraft: Pocket Edition PORT " + port + "(UDP)! ");
            this.getLogger().error("CLOSE THE PROGRAM USING THAT PORT OR CHANGE THE PORT TO SOLVE THIS PROBLEM! ");
            this.getServer().shutdown();
            return;
        }
        if (config.getBoolean("enable-addon", true)) {
            this.getLogger().info("Enabling DragonetPE Android Addon server... ");
            this.addonServer = new DragonetPEAddonServer(this);
            try {
                this.addonServer.initialize();
            } catch (IOException ex) {
                this.getLogger().error("FAILD TO BIND ON THE PEAddon PORT " + this.getNetworkHandler().getUdp().getServerPort() + "(TCP), OTHER PROGRAM MAY USING IT. ");
                this.getLogger().error("CLOSE THE PROGRAM USING THAT PORT OR DISABLE PEAddon SUPPORT TO FIX THIS PROBLEM! ");
                this.getServer().shutdown();
                return;
            }
            this.addonSupported = true;
        } else {
            this.addonSupported = false;
            this.getLogger().info("DragonetPE Android Addon support is disabled! ");
        }
        this.logger.info("Dragonet successfully initialized! ");
    }

    /**
     * Trigger a tick update
     */
    public void tickUpdate() {
        this.networkHandler.onTick();
        this.rhino.Tick();
    }

    public void shutdown() {
        this.logger.info("Stopping Dragonet server... ");
        this.networkHandler.getUdp().end();
        this.threadPool.shutdown();
    }
    
     /**
     * Reload the server.
     * Currently only used to re-scan Rhino scripts, but could be useful later.
     */
    public void reload() {
    	rhino.reload();
    }
}
