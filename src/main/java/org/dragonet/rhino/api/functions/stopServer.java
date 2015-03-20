/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api.functions;

import org.bukkit.entity.Player;

/**
 *
 * @author TheMCPEGamer
 */
public class stopServer
{
    public static void stopServer(String msg)
    {
        org.dragonet.DragonetServer.instance().getServer().savePlayers();
        
        for(Player plr : org.dragonet.DragonetServer.instance().getServer().getOnlinePlayers())
        {
            plr.kickPlayer(msg);
        }
        
        org.dragonet.DragonetServer.instance().getServer().shutdown();
    }
}
