/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */

package org.dragonet.rhino.api.functions;

import org.bukkit.BanList;
import org.bukkit.entity.Player;
import org.dragonet.DragonetServer;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSFunction;

/**
 *
 * @author TheMCPEGamer
 */
public class ServerAPI extends ScriptableObject
{
    private static final long serialVersionUID = 438270592527335642L;
    
    public ServerAPI() {}
    
    @Override
    public String getClassName()
    {
        return "ServerAPI";
    }
    
    
    ////////////////
    //
    // Server Methods
    //
    ////////////////
    
    @JSFunction
    public static void banPlayer(Object player, String reason)
    {
        try
        {
            Player plr = (Player) player;

            if(!plr.isBanned())
            {
                org.dragonet.DragonetServer.instance().getServer().getBanList(BanList.Type.NAME).addBan(plr.getName(), reason, null, null);
            }
            else
            {
                org.dragonet.DragonetServer.instance().getLogger().warn("Player: \'" + plr.getName() + "\' is already banned!");
            }
        }    
        
        catch(ClassCastException cce)
        {
            org.dragonet.DragonetServer.instance().getLogger().warn("[DragonetAPI] Script passed non-player on banPlayer()! Please alert the script author.");
        }
    }
    
    @JSFunction
    public static void clientMessage(String msg)
    {
        org.dragonet.DragonetServer.instance().getServer().broadcastMessage(msg);
    }
    
    @JSFunction    
    public static void disconnectPlayer(Object player, String reason)
    {
        Player plr = (Player) player;
        
        try
        {
            if(plr.isOnline())
            {
                plr.kickPlayer(reason);
            }
            else
            {
                org.dragonet.DragonetServer.instance().getLogger().warn("[DragonetAPI] Script passed an offline player on disconnectPlayer()! Please alert the script author.");
            }
        }
        
        catch(ClassCastException cce)
        {
            org.dragonet.DragonetServer.instance().getLogger().warn("[DragonetAPI] Script passed non-player on disconnectPlayer()! Please alert the script author.");
        }
    }
    
    @JSFunction
    public static String getName()
    {
        return org.dragonet.DragonetServer.instance().getServer().getName();
    }
    
    @JSFunction
    public static void runConsoleCommand(String command)
    {
    	DragonetServer.instance().getServer().dispatchCommand(DragonetServer.instance().getServer().getConsoleSender(), command);
    }
    
    @JSFunction
    public static void stop(String msg)
    {
        org.dragonet.DragonetServer.instance().getServer().savePlayers();
        
        for(Player plr : org.dragonet.DragonetServer.instance().getServer().getOnlinePlayers())
        {
            plr.kickPlayer(msg);
        }
        
        org.dragonet.DragonetServer.instance().getServer().shutdown();
    }
}
