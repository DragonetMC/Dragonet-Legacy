/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api.functions;

import java.util.ArrayList;

import org.dragonet.DragonetServer;
import org.dragonet.rhino.CustomMethod;
import org.dragonet.rhino.Script;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrappedException;
import org.mozilla.javascript.annotations.JSFunction;

/**
 *
 * @author Ash (QuarkTheAwesome)
 */
public class ScriptAPI extends ScriptableObject {
	private static final long serialVersionUID = 438270592527335642L;
	
	//Rhino compatibility stuff
	public ScriptAPI() {}
	@Override
	public String getClassName() {
		return "ScriptAPI";
	}
	
    ////////////////
    //
    // Cross-Script Methods
    //
    ////////////////
	@JSFunction
	public static void callCustomFunction(String function) {
		for (Script s : DragonetServer.instance().getRhino().Scripts) {
			s.runFunction(function, new Object[] {});
		}
	}
	
	@JSFunction
	public static void addMethod(String method, String handler, String ownerUID) {
		//Check if ownerUID belongs to a valid script
		Script scr = null;
		try {
			for (Script s : DragonetServer.instance().getRhino().Scripts) {
				if (s.UID == ownerUID) {
					scr = s;
				}
			}
		} catch (WrappedException e) {
			DragonetServer.instance().getLogger().error("[DragonetAPI] Script tried to add a method before initialization finished! Please use postInit for this.");
		}

		if (scr == null) {
			DragonetServer.instance().getLogger().error("[DragonetAPI] Script doesn't have a valid UID but is trying to register method " + method + "! Received '" + ownerUID + "', this does not belong to any script!");
			DragonetServer.instance().getLogger().error("[DragonetAPI] Method " + method + " will not be defined. This will cause issues with other scripts.");
			return;
		}
		
		//Check if method name is already taken
		for (CustomMethod m : CustomMethod.methods) {
			if (m.method == method) {
				DragonetServer.instance().getLogger().error("[DragonetAPI] Script " + scr.UID + " (" + scr.getName() + ")" + " tried to reserve method " + method + ", but this has already been reserved by " + m.owner.getName() + "!");
				DragonetServer.instance().getLogger().error("[DragonetAPI] Method " + method + " will not be defined. This will cause issues with other scripts.");
			}
		}
		
		//Finally, add method
		CustomMethod.methods.add(new CustomMethod(method, handler, scr));
		DragonetServer.instance().getLogger().info("[DragonetAPI] Script " + scr.UID + " (" + scr.getName() + ") added API method " + method + " sucessfully.");
	}
	
	@JSFunction
	public static void callCustomMethod(String method) {
		//Cycle through all scripts looking for our method.
		for (CustomMethod m : CustomMethod.methods) {
			if (m.method == method) {
				//Found it! Run.
				m.run();
			}
		}
	}
	
	public static void resetMethods() {
		DragonetServer.instance().getLogger().info("[DragonetAPI] Removing all custom methods...");
		CustomMethod.methods = new ArrayList<CustomMethod>();
	}
}
