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
import java.net.UnknownHostException;
import java.util.EnumMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Getter;
import net.glowstone.GlowServer;
import net.glowstone.util.ServerConfig;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.dragonet.net.inf.mcpe.NetworkHandler;
import org.dragonet.net.SessionManager;
import org.dragonet.net.inf.portal.DragonPortalServer;
import org.dragonet.net.inf.portal.PasswordNotSetException;
import org.dragonet.plugin.php.PHPManager;
import org.dragonet.rhino.Script;
import org.dragonet.utilities.DragonetVersioning;
import org.dragonet.rhino.Rhino;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DragonetServer {

    private final static boolean RELEASE = true;

    private static DragonetServer INSTANCE;

    public static DragonetServer instance() {
        return INSTANCE;
    }

    @Getter
    private GlowServer server;

    @Getter
    private Logger logger;

    @Getter
    private SessionManager sessionManager;

    //---------------------------------
    @Getter
    private Rhino rhino;

    @Getter
    private PHPManager php;

    @Getter
    private boolean enabledJs;

    @Getter
    private boolean enabledPhp;

    //---------------------------------
    @Getter
    private NetworkHandler networkHandler;

    @Getter
    private ExecutorService threadPool;

    @Getter
    private DragonPortalServer portalServer;

    @Getter
    private boolean enabledDragonPortal;

    @Getter
    private CustomItemManager customMaterialManager;

    @Getter
    private int playerSpawnThreshold;

    @Getter
    private final File pluginFolder;

    private org.mcstats.Metrics metrics;

    public DragonetServer(GlowServer server) {
        INSTANCE = this;
        this.server = server;
        ServerConfig serverConfig = new ServerConfig(server.getConfigDir(), new File(server.getConfigDir(), "glowstone.yml"), new EnumMap<ServerConfig.Key, Object>(ServerConfig.Key.class));
        pluginFolder = new File(serverConfig.getString(ServerConfig.Key.PLUGIN_FOLDER));
        this.logger = LoggerFactory.getLogger("DragonetServer");
        this.customMaterialManager = new CustomItemManager(this);
        this.sessionManager = new SessionManager(this);
    }

    /**
     * Initialize the server, DO NOT CALL IT YOURSELF. Only called by Glowstone
     * main class.
     */
    public void initialize() {
        this.logger.info("Starting Dragonet Server version " + (RELEASE ? "Release " : "") + DragonetVersioning.DRAGONET_VERSION + "... ");
        if (!RELEASE) {
            this.logger.warn("** This is a snapshot or an un-official build of Dragonet, you may expirence bugs and errors! ");
        }
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
                    if (inp != null) {
                        inp.close();
                    }
                }
            } catch (IOException e) {
            }
        }
        Configuration config = YamlConfiguration.loadConfiguration(fileConfig);

        enabledJs = config.getBoolean("plugin-support.dapis", true);
        enabledPhp = config.getBoolean("plugin-support.php", true);
        enabledDragonPortal = config.getBoolean("dragonportal.enabled", false);
        
        if (RELEASE) {
            try {
                metrics = new DragonetMetrics(this);
                metrics.start();
            } catch (IOException ex) {
                this.logger.error("Faild to send statistics! ");
            }
        }

        this.logger.info("Current Minecraft PC Version: " + DragonetVersioning.MINECRAFT_PC_VERSION);
        this.logger.info("Current Minecraft: Pocket Edition Version: " + DragonetVersioning.MINECRAFT_PE_VERSION);
        this.threadPool = Executors.newFixedThreadPool(64);
        String ip = config.getString("server-ip", "0.0.0.0");
        int port = config.getInt("server-port", 19132);
        this.logger.info("Trying to bind on UDP address " + ip + ":" + port + "... ");
        try {
            this.networkHandler = new NetworkHandler(sessionManager, new InetSocketAddress(ip, port));
        } catch (Exception ex) {
            this.getLogger().error("FAILD TO BIND ON THE Minecraft: Pocket Edition PORT " + port + "(UDP)! ");
            this.getLogger().error("CLOSE THE PROGRAM USING THAT PORT OR CHANGE THE PORT TO SOLVE THIS PROBLEM! ");
            this.getServer().shutdown();
            return;
        }

        this.rhino = new Rhino(this.getServer());
        if (enabledJs) {
            this.logger.info("Loading DAPIS scripts... ");
            this.rhino.loadScripts();
        } else {
            this.logger.info("DAPIS Javascript plugins disabled! ");
        }

        this.php = new PHPManager(this);
        if (enabledPhp) {
            this.logger.info("Loading PHP scripts");
            this.php.loadScripts();
        } else {
            this.logger.info("PHP plugins disabled! ");

        }

        //DragonPortal server
        if (enabledDragonPortal) {
            this.getLogger().info("Enabling DragonPortal server... ");
            try {
                this.portalServer = new DragonPortalServer(this,
                        config.getString("dragonportal.bind-address", "127.0.0.1"),
                        config.getInt("dragonportal.bind-port", 25590),
                        config.getString("dragonportal.password", "NOT_SET"));
                this.portalServer.initialize();
            } catch (UnknownHostException ex) {
                this.getLogger().error("Faild to create DragonPoral server. ", ex);
            } catch (IOException ex) {
                this.getLogger().error("Faild to bind DragonPoral server on ["
                        + config.getString("dragonportal.bind-address", "127.0.0.1") + ":"
                        + config.getInt("dragonportal.bind-port", 25590) + "]. ", ex);
            } catch (PasswordNotSetException ex) {
                this.getLogger().error(ex.getMessage());
            }
            enabledDragonPortal = true;
        } else {
            enabledDragonPortal = false;
        }

        //This exists because ScriptAPI.addMethod() must be called AFTER Dragonet initialization
        for (Script s : rhino.getScripts()) {
            this.getLogger().info("[DragonetAPI] Loading script " + s.getUID());
            s.runFunction("onLoad", new Object[]{s});
        }
        this.playerSpawnThreshold = config.getInt("player-spawn-chunk-threshold", 36);
        this.logger.info("Dragonet successfully initialized! ");
    }

    /**
     * Trigger a tick update
     */
    public void tickUpdate() {
        this.networkHandler.onTick();
        this.sessionManager.onTick();
        this.rhino.Tick();
    }

    public void shutdown() {
        this.logger.info("Stopping Dragonet server... ");
        this.networkHandler.getUdp().end();
        this.threadPool.shutdown();
    }

    /**
     * Reload the server. Currently only used to re-scan Rhino scripts, but
     * could be useful later.
     */
    public void reload() {
        rhino.reload();
    }
}
