/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */

package org.dragonet.rhino.api.functions;

import com.google.common.io.Files;
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
    
    @JSFunction
    public static void create(String name)
    {      
        File configDir = new File("plugins/" + name);
        
        File configFile = new File(configDir.getPath() + "/" + name + ".yml");
        
        try
        {
            if(!configDir.exists())
            {
                if(configDir.mkdir()) { if(configFile.createNewFile()) {}}
            }
        }
        catch(IOException IOe) {System.err.println(IOe.getMessage());}
    }
    
    @JSFunction
    public static boolean exists(String name)
    {
        boolean doesExist = false;
        
        if(new File("plugins/" + name + "/" + name + ".yml").exists())
        {
            doesExist = true;
        }
        
        return doesExist;
    }
    
    @JSFunction
    public static String readEntry(String name, String entry)
    {
        File config = new File("plugins/" + name + "/" + name + ".yml");
        
        String content = "";
        
        try
        {
            for(String s : Files.readLines(config, null))
            {
                if(s.contentEquals(new StringBuffer(entry)))
                {
                    content = s.substring(s.indexOf(entry), s.lastIndexOf(entry));
                }
            }
        }
        catch(IOException IOe) {System.err.println(IOe.getMessage());}
        
        return content;
    }
    
    @JSFunction
    public static void addNewEntry(String name, String entry, String value)
    {
        File config = new File("plugins/" + name + "/" + name + ".yml");
        
        String fullEntry = entry + ": " + value;
        
        try
        {
            Files.write(fullEntry.getBytes(), config);
        }
        catch(IOException IOe) {System.err.print(IOe.getMessage());}
    }
    
    @JSFunction
    public static void changeEntry(String name, String entry, String newValue)
    {
        //TODO
    }
}
