/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */

package org.dragonet.rhino.api.functions;

import org.bukkit.Material;
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
}