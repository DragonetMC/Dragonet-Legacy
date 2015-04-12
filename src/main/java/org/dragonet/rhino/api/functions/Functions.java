/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api.functions;

import java.lang.reflect.InvocationTargetException;
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
        
        Class[] clazz = {PlayerAPI.class, ServerAPI.class, WorldAPI.class, ScriptAPI.class, ConfigAPI.class};
        
        try
        {
            for(Class c : clazz)
            {
                ScriptableObject.defineClass(scope, c);
            }
        }
        
        catch(IllegalAccessException | InstantiationException | InvocationTargetException e)
        {
        	org.dragonet.DragonetServer.instance().getLogger().warn(e.getMessage());
        }
        
        finally
        {
            Context.exit();
        }
    }
}
