/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api.functions;

import org.dragonet.DragonetServer;

/**
 * @author Ash (QuarkTheAwesome)
 */
public class runConsoleCommand
{
    public static void runConsoleCommand(String command)
    {
    	DragonetServer.instance().getServer().dispatchCommand(DragonetServer.instance().getServer().getConsoleSender(), command);
    }
}
