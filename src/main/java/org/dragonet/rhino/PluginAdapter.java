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
package org.dragonet.rhino;

import com.avaje.ebean.EbeanServer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import net.glowstone.GlowServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginLogger;

public abstract class PluginAdapter implements Plugin {

    private final String name;
    
    private final GlowServer server;
    private final PluginLogger logger;
    private final File dataFolder;

    private FileConfiguration config;

    private boolean enabled;
    private boolean naggable;

    public PluginAdapter(GlowServer server, String name) throws IllegalStateException {
        this.name = name;
        this.server = server;
        dataFolder = new File(server.getDragonetServer().getPluginFolder(), this.getName().replace(".", "_").concat("-data"));
        if (dataFolder.isFile()) {
            server.getLogger().warning("Faild to load plugin [" + getName() + "] due to plugin folder is occupied by a regular file. ");
            throw new IllegalStateException("Plugin folder for [" + getName() + "] is occupied by a regular file. ");
        }
        config = new YamlConfiguration();
        try {
            config.load(new File(dataFolder, "config.yml"));
        } catch (IOException | InvalidConfigurationException ex) {
        }
        logger = new PluginLogger(this);
    }

    @Override
    public File getDataFolder() {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        return dataFolder;
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public PluginDescriptionFile getDescription() {
        return new PluginDescriptionFile(getName(), "DAPIS Script", "");
    }

    @Override
    public FileConfiguration getConfig() {
        return new YamlConfiguration();
    }

    @Override
    public InputStream getResource(String string) {
        return null; //Not supported. 
    }

    @Override
    public final void saveConfig() {
        try {
            this.config.save(new File(dataFolder, "config.yml"));
        } catch (IOException ex) {
            logger.warning("Faild to saveConfig() due to: " + ex.getMessage());
        }
    }

    @Override
    public final void saveDefaultConfig() {
        saveConfig();
    }

    @Override
    public void saveResource(String string, boolean bln) {
        return; //Not supported. 
    }

    @Override
    public final void reloadConfig() {
        config = new YamlConfiguration();
        try {
            config.load(new File(dataFolder, "config.yml"));
        } catch (IOException ex) {
        } catch (InvalidConfigurationException ex) {
        }
    }

    @Override
    public PluginLoader getPluginLoader() {
        return null; //Not supported. 
    }

    @Override
    public final GlowServer getServer() {
        return server;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public abstract void onDisable();

    @Override
    public abstract void onLoad();

    @Override
    public boolean isNaggable() {
        return naggable;
    }

    @Override
    public void setNaggable(boolean naggable) {
        this.naggable = naggable;
    }

    @Override
    public EbeanServer getDatabase() {
        return null; //Not supported. 
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String string, String string1) {
        return null; //Not supported. 
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmnd, String string, String[] strings) {
        return new ArrayList<String>();
    }

    @Override
    public abstract boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args);

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!Plugin.class.isInstance(obj)) {
            return false;
        }
        return getName().equals(((Plugin) obj).getName());
    }

}
