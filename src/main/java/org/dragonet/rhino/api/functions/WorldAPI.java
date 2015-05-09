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

import net.glowstone.entity.passive.GlowChicken;
import net.glowstone.entity.passive.GlowPig;
import net.glowstone.entity.passive.GlowSheep;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSFunction;

/**
 *
 * @author TheMCPEGamer
 */
public class WorldAPI extends ScriptableObject
{
    private static final long serialVersionUID = 438270592527335642L;
    
    public WorldAPI() {}
    
    @Override
    public String getClassName()
    {
        return "WorldAPI";
    }
    
    
    ////////////////
    //
    // World Methods
    //
    ////////////////
    
    @JSFunction
    public static void setArea(String worldName, int x1, int y1, int z1, int x2, int y2, int z2, String materialName, int tileData)
    {
        for(int x = x1; x < x2; x++)
        {
            for(int y = y1; y < y2; y++)
            {
                for(int z = z1; z < z2; z++)
                {
                    setBlock(worldName, x, y, z, materialName, java.lang.Byte.parseByte(tileData + ""));
                }
            }
        }
    }
    
    @JSFunction
    public static void setBlock(String worldName, int x, int y, int z, String tileName, int tileData)
    {
        org.dragonet.DragonetServer.instance().getServer().getWorld(worldName).getBlockAt(x, y, z).setType(Material.getMaterial(tileName));
        org.dragonet.DragonetServer.instance().getServer().getWorld(worldName).getBlockAt(x, y, z).setData(java.lang.Byte.parseByte(tileData + ""));
    }
    
    @JSFunction
    public static void dropItem(String worldName, int x, int y, int z, String materialName, int Count)
    {
        org.dragonet.DragonetServer.instance().getServer().getWorld(worldName).dropItem(new Location(Bukkit.getWorld(worldName), x, y, z), new ItemStack(Material.getMaterial(materialName), Count));
    }
    
    @JSFunction
    public static Object getBlockAt(String worldName, int x, int y, int z)
    {
        return org.dragonet.DragonetServer.instance().getServer().getWorld(worldName).getBlockAt(x, y, z);
    }
    
    @JSFunction
    public static void spawnMob(String worldName, int x, int y, int z, String entityType)
    {
            if(entityType.equalsIgnoreCase("Pig"))
            {
                GlowPig pig = new GlowPig(new Location(org.dragonet.DragonetServer.instance().getServer().getWorld(worldName), x, y, z));
            }
            else if(entityType.equalsIgnoreCase("Sheep"))
            {
                GlowSheep sheep = new GlowSheep(new Location(org.dragonet.DragonetServer.instance().getServer().getWorld(worldName), x, y, z));
            }
            else if(entityType.equalsIgnoreCase("Chicken"))
            {
                GlowChicken chicken = new GlowChicken(new Location(org.dragonet.DragonetServer.instance().getServer().getWorld(worldName), x, y, z));
            }
            else
            {
                throw new UnsupportedOperationException("Entity provided either does not exist or is not implemented!");
            }
    }
}