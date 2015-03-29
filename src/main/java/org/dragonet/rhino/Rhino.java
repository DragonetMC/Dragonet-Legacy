/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino;

import java.io.File;
import java.util.*;
import org.bukkit.entity.Player;
import org.dragonet.rhino.api.*;

/**
 *
 * @author TheMCPEGamer__ edited by Ash (QuarkTheAwesome)
 */
public class Rhino
{
    public List<Script> Scripts = new ArrayList<>();
    
    public Rhino()
    {
        this.startScriptInterpreter();
    }
    
    public void reload()
    {
        this.Scripts = null;
        this.startScriptInterpreter();
    }
    
    public void Tick()
    {
        Tick.Tick();
    }
    
    public void useItem(int blockX, int blockY, int blockZ, String blockFace, String blockName, Player plr)
    {
        useItem.useItem(blockX, blockY, blockZ, blockFace, blockName, plr);
    }
    
    public void onConnect(Player plr)
    {
        onConnect.onConnect(plr);
    }
    
    public void onQuit(Player plr)
    {
        onQuit.onQuit(plr);
    }
    
    public void onKick(Player plr, String msg)
    {
        onKick.onKick(plr, msg);
    }
    
    public void onEnchant(Player plr, int enchantID, String itemType, byte itemData)
    {
        onEnchant.onEnchant(plr, enchantID, itemType, itemData);
    }
    
    public boolean onChatSending(Player plr, String message)
    {
        return onChatSending.onChatSending(plr, message);
    }
    
    public void onMove(Player plr, int x1, int y1, int z1, int x2, int y2, int z2, org.bukkit.util.Vector plrVelocity)
    {
        onMove.onMove(plr, x1, y1, z1, x2, y2, z2, plrVelocity);
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
                    script.runFunction("onInit", new Object[] {});
               }
            }

            return fileList;
        }
        
        return new ArrayList<>();
    }
}
