/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api.functions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author TheMCPEGamer
 */
public class addItemInventory
{
    public static void addItemInventory(String PlayerName, String MaterialName, int Count)
    {          
        Player plr = org.dragonet.DragonetServer.instance().getServer().getPlayer(PlayerName);

        if(plr != null)
        {
            plr.getInventory().addItem(new ItemStack(Material.getMaterial(MaterialName), Count));
            return;
        }
        else
        {
            return;
        }
    }
}
