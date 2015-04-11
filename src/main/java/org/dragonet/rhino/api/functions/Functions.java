/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api.functions;

import org.mozilla.javascript.*;

/**
 *
 * TheMCPEGamer
 */
public class Functions
{   
    public static void defineFunctions(Scriptable scope)
    {
        Context ctx = Context.enter();
        
        try
        {
            ScriptableObject.defineClass(scope, PlayerAPI.class);
            ScriptableObject.defineClass(scope, ServerAPI.class);
            ScriptableObject.defineClass(scope, WorldAPI.class);
            ScriptableObject.defineClass(scope, ScriptAPI.class);
            ScriptableObject.defineClass(scope, ConfigAPI.class);
        }
        
        catch(Exception e) {
        	e.printStackTrace();
        }
        
        finally
        {
            Context.exit();
        }
    }
}
