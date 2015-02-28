/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api;

import org.dragonet.rhino.Script;

/**
 *
 * @author TheMCPEGamer__
 */
public class Tick
{
    public static void Tick()
    {
        for(Script s : org.dragonet.DragonetServer.instance().getRhino().Scripts)
        {
            s.runFunction("Tick");
        }
    }
}
