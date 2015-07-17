/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dragonet.plugin.php;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.dragonet.DragonetServer;

public final class PHPManager {
    private DragonetServer server;
    
    private final ConcurrentMap<String, PHPPlugin> plugins = new ConcurrentHashMap<>();
    
    public PHPManager(DragonetServer server) {
        this.server = server;
    }
    
    public void loadScripts(){
        //TODO
        server.getLogger().warn("** PHP plugin support isn't finished yet, plugins written in PHP won't load. ");
    }
}
