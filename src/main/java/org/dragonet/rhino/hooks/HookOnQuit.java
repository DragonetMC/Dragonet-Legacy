 /* GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 */
package org.dragonet.rhino.hooks;

import org.bukkit.entity.Player;
import org.dragonet.rhino.Script;

/**
 *
 * @author TheMCPEGamer
 */
public class HookOnQuit {

    public static void onQuit(Player plr) {
        for (Script s : org.dragonet.DragonetServer.instance().getRhino().getScripts()) {
            s.runFunction("onQuit", new Object[]{plr});
        }
    }
}
