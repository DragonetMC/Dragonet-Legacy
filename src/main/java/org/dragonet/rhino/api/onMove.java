 /* GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 */

package org.dragonet.rhino.api;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.dragonet.rhino.Script;

/**
 *
 * @author TheMCPEGamer
 */
public class onMove
{
    public static void onMove(Player plr, int x1, int y1, int z1, int x2, int y2, int z2, Vector plrVelocity)
    {
        for(Script s : org.dragonet.DragonetServer.instance().getRhino().Scripts)
        {
            s.runFunction("onMove", new Object[] {plr, x1, y1, z1, x2, y2, z2, plrVelocity});
        }
    }
}
