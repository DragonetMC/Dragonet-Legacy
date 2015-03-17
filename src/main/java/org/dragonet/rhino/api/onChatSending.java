/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api;

import org.bukkit.entity.Player;
import org.dragonet.DragonetServer;
import org.dragonet.rhino.Script;

/**
 * @author Ash (QuarkTheAwesome)
 */
public class onChatSending {
	public static boolean cancelled = false;
	public static boolean onChatSending(Player plr, String message) {
		cancelled = false;
		for (Script s : org.dragonet.DragonetServer.instance().getRhino().Scripts) {
			Object result = s.runFunction("onChatSending", new Object[] {plr, message});
			if (result != null) {
				try {
					boolean res = (boolean) result;
					if (res == true) {
						cancelled = res;
					}
				} catch (ClassCastException e) {
					DragonetServer.instance().getLogger().warn("[DragonetAPI] Script " + s.getName() + " returned a non-boolean on onChatSent! Please alert the script author. Returning false for now.");
				}
			}
		}
		return cancelled;
	}
}