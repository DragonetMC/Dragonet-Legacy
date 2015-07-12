/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.hooks;

import org.bukkit.entity.Player;
import org.dragonet.rhino.Script;

/**
 *
 * @author TheMCPEGamer
 */
public class HookOnKick {

    public static void onKick(Player plr, String msg) {
        for (Script s : org.dragonet.DragonetServer.instance().getRhino().getScripts()) {
            s.runFunction("onKick", new Object[]{plr, msg});
        }
    }
}
