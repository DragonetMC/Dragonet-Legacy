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
            ((ScriptableObject) scope).defineClass(scope, PlayerAPI.class);
            ((ScriptableObject) scope).defineClass(scope, ServerAPI.class);
            ((ScriptableObject) scope).defineClass(scope, WorldAPI.class);
            ((ScriptableObject) scope).defineClass(scope, ScriptAPI.class);
            ((ScriptableObject) scope).defineClass(scope, ConfigAPI.class);
        }
        
        catch(Exception e) {}
        
        finally
        {
            ctx.exit();
        }
    }
}
