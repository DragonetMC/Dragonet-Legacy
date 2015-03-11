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
public class onKick
{
    public static void onKick(String playerName, String msg)
    {
        for(Script s : org.dragonet.DragonetServer.instance().getRhino().Scripts)
        {
            s.runFunction("onKick", new Object[] {playerName, msg});
        }
    }
}
