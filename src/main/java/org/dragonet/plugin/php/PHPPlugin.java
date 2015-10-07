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
    private final PHPManager manager;

    public PHPPlugin(GlowServer server, File path, PHPManager manager) throws IllegalArgumentException {
        super(server);
        this.manager = manager;
        if(path.isDirectory()){
            throw new IllegalArgumentException("Input file can not be directory, must be a PHP file. ");
        }
        if(!path.getName().toLowerCase().endsWith(".zip")){
            throw new IllegalArgumentException("Input file must be a ZIP file. ");
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
