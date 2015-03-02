/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api.functions;

/**
 *
 * @author TheMCPEGamer
 */
public class clientMessage
{
    public static void clientMessage(String msg)
    {
        org.dragonet.DragonetServer.instance().getServer().broadcastMessage(msg);
    }
}
