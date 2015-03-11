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
public class onQuit
{
    public static void onQuit(String playerName)
    {
        for(Script s : org.dragonet.DragonetServer.instance().getRhino().Scripts)
        {
            s.runFunction("onQuit", new Object[] {playerName});
        }
    }
}
