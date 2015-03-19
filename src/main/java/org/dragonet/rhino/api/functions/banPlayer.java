/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api.functions;

import java.util.Date;
import org.bukkit.BanList;
import org.bukkit.entity.Player;

/**
 *
 * @author TheMCPEGamer
 */
public class banPlayer
{
    public static void banPlayer(Player plr, String reason, int[] DateUnbanned)
    {
        if(!plr.isBanned())
        {
            org.dragonet.DragonetServer.instance().getServer().getBanList(BanList.Type.NAME).addBan(plr.getName(), reason, new Date(DateUnbanned[0], DateUnbanned[1], DateUnbanned[2]), null);
        }
        else
        {
            org.dragonet.DragonetServer.instance().getLogger().warn("Player: \'" + plr.getName() + "\' is already banned!");
        }
    }
    
    public static void banPlayer(Player plr, String reason)
    {
        if(!plr.isBanned())
        {
            org.dragonet.DragonetServer.instance().getServer().getBanList(BanList.Type.NAME).addBan(plr.getName(), reason, null, null);
        }
        else
        {
            org.dragonet.DragonetServer.instance().getLogger().warn("Player: \'" + plr.getName() + "\' is already banned!");
        }
    }
    
    public static void banPlayer(Player plr)
    {
        if(!plr.isBanned())
        {
            org.dragonet.DragonetServer.instance().getServer().getBanList(BanList.Type.NAME).addBan(plr.getName(), "[Banned] The Ban Hammer has spoken!", null, null);
        }
        else
        {
            org.dragonet.DragonetServer.instance().getLogger().warn("Player: \'" + plr.getName() + "\' is already banned!");
        }
    }
}
