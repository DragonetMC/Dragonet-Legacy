/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api.functions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSFunction;

/**
 *
 * @author TheMCPEGamer
 */
public class PlayerAPI extends ScriptableObject
{
    private static final long serialVersionUID = 438270592527335642L;
    
    public PlayerAPI() {}
    
    @Override
    public String getClassName()
    {
        return "PlayerAPI";
    }
    
    
    ////////////////
    //
    // Player Methods
    //
    ////////////////
    
    @JSFunction
    public static void kill(Object plr)
    {
        ((Player) plr).setHealth(EMPTY);
    }
    
    @JSFunction
    public static void addItemInventory(Object player, String MaterialName, int Count)
    {          
        Player plr = (Player) player;

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
    
    @JSFunction
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
    
    @JSFunction
    public static String getCurrentWorld(Object player)
    {
        Player plr = (Player) player;
        
        return plr.getWorld().getName();
    }
}
