/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api.functions;

import org.dragonet.DragonetServer;
import org.dragonet.rhino.Script;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSFunction;

/**
 *
 * @author Ash (QuarkTheAwesome)
 */
public class ScriptAPI extends ScriptableObject{
	//Copy-pasted from PlayerAPI - Do I have to change this?
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
}
