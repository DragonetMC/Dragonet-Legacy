/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.NativeObject;

/**
 * Stores info about methods defined with the ScriptAPI.
 * @author Ash (QuarkTheAwesome)
 */
public class CustomMethod {
	/** Name of method scripts can call. **/
	public String method;
	/** Name of function in {@link owner} to be called when other scripts call {@link method} **/
	public String handler;
	/** Owner script. Should contain {@link handler} function **/
	public Script owner;
	
	/**
	 * Global methods list.
	 * This should NEVER be used in a non-static way!
	 */
	public static List<CustomMethod> methods = new ArrayList<CustomMethod>();
	
	/**
	 * @param method Name of method scripts can call.
	 * @param handler Name of function in {@link owner} to be called when other scripts call {@link method}
	 * @param owner Owner script. Should contain {@link handler} function
	 */
	public CustomMethod(String method, String handler, Script owner) {
		this.method = method;
		this.handler = handler;
		this.owner = owner;
	}
	/**
	 * Runs function {@link handler} in script {@link owner}
	 */
	public void run(Object[] arguments) {
		if (!(arguments == null)) {
			owner.runFunction(handler, arguments);
		} else {
			owner.runFunction(handler, new Object[] {null});
		}
	}
}
