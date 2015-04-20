 /* GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
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
