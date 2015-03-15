/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api;

import org.dragonet.rhino.Script;

/**
 *
 * @author TheMCPEGamer
 */
public class useItem 
{
    public static void useItem(int blockX, int blockY, int blockZ, String blockFace, String blockName, String playerName)
    {   
        for(Script s : org.dragonet.DragonetServer.instance().getRhino().Scripts)
        {
            s.runFunction("useItem", new Object[] {blockX, blockY, blockZ, blockFace, blockName, playerName});
        }
    }
}
