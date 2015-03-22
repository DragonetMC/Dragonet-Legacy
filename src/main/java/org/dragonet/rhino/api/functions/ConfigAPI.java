/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */

package org.dragonet.rhino.api.functions;

import java.io.File;
import java.io.IOException;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSFunction;

/**
 *
 * @author TheMCPEGamer
 */
public class ConfigAPI extends ScriptableObject
{
    private static final long serialVersionUID = 438270592527335642L;
    
    public ConfigAPI() {}
    
    @Override
    public String getClassName()
    {
        return "ConfigAPI";
    }

    
    ////////////////
    //
    // Script Methods
    //
    ////////////////
    
    @JSFunction
    public static boolean Exists(String dirName, String fileName)
    {
        boolean result = false;
        
        File dir = new File("plugins/" + dirName); 
        
        File config = new File(dir.getPath() + "/" + fileName);
        
        if(dir.exists() || config.exists())
        {
            result = true;
        }
        
        return result;
    }
    
    @JSFunction
    public static void createConfig(String dirName, String fileName)
    {
        File dir = new File("plugins/" + dirName); 
        
        File config = new File(dir.getPath() + "/" + fileName);
        
        if(!dirName.equals("") && !fileName.equals(""))
        {
            if(!dir.exists() && !config.exists())
            {
                try
                {
                    if(dir.mkdir()) {}
                    else
                    {
                        System.err.println("Could not create config directory...");
                    }
                    
                    if(config.createNewFile()) {}
                    else
                    {
                        System.err.println("Could not create config file...");
                    }
                }
                
                catch(IOException IOe) {}
            }
        }
    }
}
