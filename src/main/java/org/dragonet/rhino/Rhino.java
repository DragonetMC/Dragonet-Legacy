/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino;

import java.io.File;
import java.util.*;
import org.mozilla.javascript.*;
import org.dragonet.rhino.api.*;

/**
 *
 * @author TheMCPEGamer__
 */
public class Rhino
{
    public List<Script> Scripts = new ArrayList<>();
    
    public Rhino()
    {
        this.startJSInterpreter();
    }
    
    public void Tick()
    {
        Tick.Tick();
    }
    
    public void startJSInterpreter()
    {
        Scripts = loadJSFiles();
    }
    
    public List<Script> loadJSFiles()
    {
        List<Script> fileList = new ArrayList<>();
        File dir = new File("./plugins");
        
        for(File f : dir.listFiles())
        {
           if(f.getName().endsWith((".js")) && !fileList.contains(f))
           {
            Script script = new Script(f);
            fileList.add(script);
            System.out.println("Loaded DragonetAPI Script " + script.name);
           }
        }
        
        return fileList;
    }
}
