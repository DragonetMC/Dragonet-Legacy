/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino;

import java.io.File;
import java.io.IOException;
import java.util.*;
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
        this.startScriptInterpreter();
    }
    
    public void reload() {
    	Scripts = null;
    	this.startScriptInterpreter();
    }
    
    public void Tick()
    {
        Tick.Tick();
    }
    
    public void useItem(int blockX, int blockY, int blockZ, String blockFace, String blockName, String playerName)
    {
        useItem.useItem(blockX, blockY, blockZ, blockFace, blockName, playerName);
    }
    
    public void onChatSent(String playerName, String message)
    {
    	onChatSent.onChatSent(playerName, message);
    }
    
    public void onConnect(String playerName)
    {
        onConnect.onConnect(playerName);
    }
    
    public void onQuit(String playerName)
    {
        onQuit.onQuit(playerName);
    }
    
    public void onKick(String playerName, String msg)
    {
        onKick.onKick(playerName, msg);
    }
    
    private void startScriptInterpreter()
    {
        Scripts = loadScripts();
    }
    
    private List<Script> loadScripts()
    {
        List<Script> fileList = new ArrayList<>();
        File dir = new File("./plugins");
        
        if(!dir.isDirectory())
        {
            try
            {
                if(dir.mkdir()) {}
                else
                {
                    System.err.println("Could not create plugins file...");
                    System.err.println("Please create it yourself");
                    org.dragonet.DragonetServer.instance().shutdown();
                }
            }
            
            catch(Exception e)
            {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
        else
        {
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
        
        return new ArrayList<>();
    }
}
