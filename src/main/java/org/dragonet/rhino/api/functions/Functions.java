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
    public static String[] Functions = new String[] {"clientMessage", "addItemInventory"};
    
    public static Class[] Classes = new Class[] {clientMessage.class, addItemInventory.class};
    
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
