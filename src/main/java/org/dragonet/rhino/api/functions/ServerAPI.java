 /* GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 */
package org.dragonet.rhino.api.functions;

import net.glowstone.GlowServer;
import org.bukkit.BanList;
import org.bukkit.entity.Player;
import org.dragonet.DragonetServer;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSFunction;

/**
 *
 * @author TheMCPEGamer
 */
public class ServerAPI extends ScriptableObject {

    private static final long serialVersionUID = 438270592527335642L;

    public ServerAPI() {
    }

    @Override
    public String getClassName() {
        return "ServerAPI";
    }

    ////////////////
    //
    // Server Methods
    //
    ////////////////
    @JSFunction
    public static Object getServer() {
        return (GlowServer) (org.dragonet.DragonetServer.instance().getServer());
    }

    @JSFunction
    public static void banPlayer(Object player, String reason) {
        try {
            Player plr = (Player) player;

            if (!plr.isBanned()) {
                org.dragonet.DragonetServer.instance().getServer().getBanList(BanList.Type.NAME).addBan(plr.getName(), reason, null, null);
            } else {
                org.dragonet.DragonetServer.instance().getLogger().warn("Player: \'" + plr.getName() + "\' is already banned!");
            }
        } catch (ClassCastException cce) {
            org.dragonet.DragonetServer.instance().getLogger().warn("[DragonetAPI] Script passed non-player on banPlayer()! Please alert the script author.");
        }
    }

    @JSFunction
    public static void clientMessage(String msg) {
        org.dragonet.DragonetServer.instance().getServer().broadcastMessage(msg);
    }

    @JSFunction
    public static void disconnectPlayer(Object player, String reason) {
        Player plr = (Player) player;

        try {
            if (plr.isOnline()) {
                plr.kickPlayer(reason);
            } else {
                org.dragonet.DragonetServer.instance().getLogger().warn("[DragonetAPI] Script passed an offline player on disconnectPlayer()! Please alert the script author.");
            }
        } catch (ClassCastException cce) {
            org.dragonet.DragonetServer.instance().getLogger().warn("[DragonetAPI] Script passed non-player on disconnectPlayer()! Please alert the script author.");
        }
    }

    @JSFunction
    public static String getName() {
        return org.dragonet.DragonetServer.instance().getServer().getName();
    }

    @JSFunction
    public static void runConsoleCommand(String command) {
        DragonetServer.instance().getServer().dispatchCommand(DragonetServer.instance().getServer().getConsoleSender(), command);
    }

    @JSFunction
    public static void stop(String msg) {
        org.dragonet.DragonetServer.instance().getServer().savePlayers();

        for (Player plr : org.dragonet.DragonetServer.instance().getServer().getOnlinePlayers()) {
            plr.kickPlayer(msg);
        }

        org.dragonet.DragonetServer.instance().getServer().shutdown();
    }

    @JSFunction
    public static void logMessage(String msg, String type) {
        if (type.equalsIgnoreCase("debug")) {
            org.dragonet.DragonetServer.instance().getLogger().debug(msg);
        } else if (type.equalsIgnoreCase("error")) {
            org.dragonet.DragonetServer.instance().getLogger().error(msg);
        } else if (type.equalsIgnoreCase("trace")) {
            org.dragonet.DragonetServer.instance().getLogger().trace(msg);
        } else if (type.equalsIgnoreCase("warn")) {
            org.dragonet.DragonetServer.instance().getLogger().warn(msg);
        } else {
            org.dragonet.DragonetServer.instance().getLogger().info(msg);
        }
    }
}
