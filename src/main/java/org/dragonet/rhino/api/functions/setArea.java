/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api.functions;

/**
 *
 * @author TheMCPEGamer
 */
public class setArea
{
    public static void setArea(String worldName, int x1, int y1, int z1, int x2, int y2, int z2, String materialName, int tileData)
    {
        for(int x = x1; x < x2; x++)
        {
            for(int y = y1; y < y2; y++)
            {
                for(int z = z1; z < z2; z++)
                {
                    setBlock.setBlock(worldName, x, y, z, materialName, java.lang.Byte.parseByte(tileData + ""));
                }
            }
        }
    }
}
