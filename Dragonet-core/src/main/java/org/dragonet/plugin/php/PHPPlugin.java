/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dragonet.plugin.php;

import com.caucho.quercus.QuercusContext;
import java.io.File;
import net.glowstone.GlowServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.dragonet.plugin.PluginAdapter;

public class PHPPlugin extends PluginAdapter {
    private QuercusContext context;

    public PHPPlugin(GlowServer server, File path) throws IllegalArgumentException {
        super(server);
        if(path.isDirectory()){
            throw new IllegalArgumentException("Input file can not be directory, must be a PHP file. ");
        }
        if(!path.getName().toLowerCase().endsWith(".php")){
            throw new IllegalArgumentException("Input file must be a PHP file. ");
        }
        //TODO: Detect type, load
    }

    @Override
    protected void onScriptEnable() {
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
        return true;
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onLoad() {
    }
}
