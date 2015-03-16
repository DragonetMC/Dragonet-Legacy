/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api.functions;

/**
 *
 * @author TheMCPEGamer
 */
public class getServerName
{
    public static String getServerName()
    {
        return org.dragonet.DragonetServer.instance().getServer().getName();
    }
}
