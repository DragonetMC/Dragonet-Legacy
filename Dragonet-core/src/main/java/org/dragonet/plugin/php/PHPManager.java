/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dragonet.plugin.php;

import com.caucho.quercus.QuercusContext;
import com.caucho.quercus.env.Env;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import org.dragonet.DragonetServer;

public final class PHPManager {
    
    
    @Getter
    private DragonetServer server;
    
    @Getter
    private final Env env;
    
    @Getter
    private final QuercusContext context;
    
    private final ConcurrentMap<String, PHPPlugin> plugins = new ConcurrentHashMap<>();
    
    public PHPManager(DragonetServer server) {
        this.server = server;
        context = new QuercusContext();
        env = new Env(context);
        env.start();
        env.setTimeLimit(-1);
        //env.addAutoloadFunction(  , true);
        context.start();
    }
    
    public void loadScripts(){
        //TODO
        server.getLogger().warn("** PHP plugin support isn't finished yet, plugins written in PHP won't load. ");
    }
}
