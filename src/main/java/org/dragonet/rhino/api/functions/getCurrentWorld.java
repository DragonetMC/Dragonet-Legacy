/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api.functions;

/**
 *
 * @author TheMCPEGamer
 */
public class getCurrentWorld
{
    public static String getCurrentWorld(String playerName)
    {
        return org.dragonet.DragonetServer.instance().getServer().getPlayer(playerName).getWorld().getName();
    }
}
