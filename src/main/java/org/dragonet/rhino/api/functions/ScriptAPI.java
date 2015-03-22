/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */

package org.dragonet.rhino.api.functions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSFunction;
import org.dragonet.rhino.Script;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.UniqueTag;

/**
 *
 * @author TheMCPEGamer
 */
public class ScriptAPI extends ScriptableObject
{
    private static final long serialVersionUID = 438270592527335642L;
    
    public ScriptAPI() {}
    
    @Override
    public String getClassName()
    {
        return "ScriptAPI";
    }
    
    
    ////////////////
    //
    // Script Methods
    //
    ////////////////
    
    @JSFunction
    public static Script[] getScripts()
    {
        Script[] scripts = Arrays.copyOf(org.dragonet.DragonetServer.instance().getRhino().Scripts.toArray(), org.dragonet.DragonetServer.instance().getRhino().Scripts.toArray().length, Script[].class);
        
        return scripts;
    }
}
