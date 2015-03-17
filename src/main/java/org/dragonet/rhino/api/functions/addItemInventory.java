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
 * @author TheMCPEGamer, edited by Ash (QuarkTheAwesome)
 */
public class addItemInventory
{
    public static void addItemInventory(String PlayerName, String MaterialName, int Count)
    {          
        Player plr = org.dragonet.DragonetServer.instance().getServer().getPlayer(PlayerName);

        Material mat = Material.getMaterial(MaterialName);
        if((plr != null) && (mat != null))
        {
            plr.getInventory().addItem(new ItemStack(mat, Count));
            return;
        }
        else if (plr == null)
        {
        	org.dragonet.DragonetServer.instance().getLogger().warn("[DragonetAPI] Script tried to add item to non-existent player! Please alert the script author.");
            return;
        }
        else if (mat == null)
        {
        	org.dragonet.DragonetServer.instance().getLogger().warn("[DragonetAPI] Script tried to add non-existent item to player! Please alret the script author.");
        	return;
        }
    }
}
