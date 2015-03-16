/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api;

import org.bukkit.entity.Player;
import org.dragonet.rhino.Script;

/**
 *
 * @author TheMCPEGamer
 */
public class onEnchant
{
    public static void onEnchant(Player plr, int enchantID, String itemType, byte itemData)
    {
        for(Script s : org.dragonet.DragonetServer.instance().getRhino().Scripts)
        {
            s.runFunction("onEnchant", new Object[] {plr, enchantID, itemType, itemData});
        }
    }
}
