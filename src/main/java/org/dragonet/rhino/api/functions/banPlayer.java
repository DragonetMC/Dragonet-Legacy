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
    public static void banPlayer(Object player, String reason)
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
}
