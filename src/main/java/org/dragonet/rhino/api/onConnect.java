/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api;

import org.dragonet.rhino.Script;

/**
 *
 * @authorTheMCPEGamer
 */
public class onConnect
{
    public static void onConnect(String playerName)
    {
        for(Script s : org.dragonet.DragonetServer.instance().getRhino().Scripts)
        {
            s.runFunction("onConnect", new Object[] {playerName});
        }
    }
}
