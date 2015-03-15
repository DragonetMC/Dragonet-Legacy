/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api.functions;

import org.dragonet.DragonetServer;

/**
 * @author Ash (QuarkTheAwesome)
 */
public class disconnectPlayer
{
    public static void disconnectPlayer(String player, String reason){
    	DragonetServer.instance().getServer().getPlayer(player).kickPlayer(reason);
    }
}
