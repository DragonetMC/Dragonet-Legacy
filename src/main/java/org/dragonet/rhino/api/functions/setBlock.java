/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api.functions;

import org.bukkit.Material;

/**
 *
 * @author TheMCPEGamer
 */
public class setBlock
{
    public static void setBlock(String worldName, int x, int y, int z, String tileName, int tileData)
    {
        org.dragonet.DragonetServer.instance().getServer().getWorld(worldName).getBlockAt(x, y, z).setType(Material.getMaterial(tileName));
        org.dragonet.DragonetServer.instance().getServer().getWorld(worldName).getBlockAt(x, y, z).setData(java.lang.Byte.parseByte(tileData + ""));
    }
}
