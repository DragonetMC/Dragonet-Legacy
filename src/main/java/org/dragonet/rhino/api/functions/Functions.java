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
<<<<<<< HEAD
    public static String[] Functions = new String[] {"clientMessage", "addItemInventory", "getCurrentWorld", "setBlock", "setArea", "getServerName"};
    
    public static Class[] Classes = new Class[] {clientMessage.class, addItemInventory.class, getCurrentWorld.class, setBlock.class, setArea.class, getServerName.class};
=======
    public static String[] Functions = new String[] {"clientMessage", "addItemInventory", "getCurrentWorld", "setBlock"};
    
    public static Class[] Classes = new Class[] {clientMessage.class, addItemInventory.class, getCurrentWorld.class, setBlock.class};
>>>>>>> c9809ff8faa896943476b654da20442535c08aca
    
    public static void defineFunctions(Scriptable scope)
    {
        Context ctx = Context.enter();
        
        try
        {
            for(int i = 0; i < Functions.length; i++)
            {
                ((ScriptableObject) scope).defineFunctionProperties(new String[] {Functions[i]}, Classes[i], ScriptableObject.DONTENUM);
            }
        }
        
        finally
        {
            ctx.exit();
        }
    }
}
