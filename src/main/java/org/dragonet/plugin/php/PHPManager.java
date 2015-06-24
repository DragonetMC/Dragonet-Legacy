/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dragonet.plugin.php;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.glowstone.GlowServer;

public final class PHPManager {
    private GlowServer server;
    
    private final ConcurrentMap<String, PHPPlugin> plugins = new ConcurrentHashMap<>();
    
    public PHPManager(GlowServer server) {
        this.server = server;
    }
}
