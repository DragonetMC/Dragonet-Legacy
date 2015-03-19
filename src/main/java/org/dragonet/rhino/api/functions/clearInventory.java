/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api.functions;

import org.bukkit.entity.Player;

/**
 *
 * @author TheMCPEGamer
 */
public class clearInventory
{
    public static void clearInventory(Player plr)
    {
        plr.getInventory().clear();
    }
}
