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
public class clearInventory
{
    public static void clearInventory(Object plr)
    {
      if(((Player) plr).isOnline())
      {
        try
        {
            ((Player) plr).getInventory().clear();
        }
        
        catch(ClassCastException cce)
        {
            org.dragonet.DragonetServer.instance().getLogger().warn("[DragonetAPI] Script passed non-player on clearInventory()! Please alert the script author.");
        }
      }
      else
      {
          org.dragonet.DragonetServer.instance().getLogger().warn("[DragonetAPI] Script passed an offline player on clearInventory()! Please alert the script author.");
      }
    }
}
