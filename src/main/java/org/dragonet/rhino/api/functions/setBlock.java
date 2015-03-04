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
    public static void setBlock(String WORLD_NAME, int x, int y, int z, String TILE_NAME, int TILE_DATA)
    {
        org.dragonet.DragonetServer.instance().getServer().getWorld(WORLD_NAME).getBlockAt(x, y, z).setType(Material.getMaterial(TILE_NAME));
    }
}
