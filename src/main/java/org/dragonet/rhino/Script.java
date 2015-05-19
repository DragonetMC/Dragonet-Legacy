 /* GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 */
package org.dragonet.rhino;

import org.dragonet.plugin.PluginAdapter;
import java.util.*;
import java.io.*;
import lombok.Getter;
import net.glowstone.GlowServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.mozilla.javascript.*;
import org.dragonet.DragonetServer;
import org.dragonet.rhino.api.functions.Functions;

/**
 *
 * @author TheMCPEGamer__ edited by Ash (QuarkTheAwesome)
 */
public class Script extends PluginAdapter {
    
    @Getter
    private Context context;

    //In order for the ScriptAPI to keep track of scripts without relying on filenames (which change)...
    /**
     * Unique name of script (not filename). Set by script.
     */
    @Getter
    private String UID = "";
    
    @Getter
    private final ScriptableObject scope;

    private String fullFilePath = "";

    @Getter
    private File file;

    public Script(GlowServer server, File scriptFile) throws InvalidScriptException {
        super(server);
        context = new ContextFactory().enterContext();
        scope = context.initStandardObjects();
        Functions.defineFunctions(context, scope);
        fullFilePath = scriptFile.getAbsolutePath();
        file = scriptFile;
        //Reads the script and evaluate it
        BufferedReader script = null;
        try {
            script = new BufferedReader(new FileReader(this.getFile()));
        } catch (IOException e) {
        }
        try {
            context.evaluateReader(scope, script, getName(), 1, null);
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        UID = findScriptUID();
        if(UID == null){
            throw new InvalidScriptException(scriptFile.getName(), "Can not find a valid UID defined/returned! ");
        }
        for(Script checkScript : server.getDragonetServer().getRhino().getScripts()){
            if(checkScript.getUID().equals(this.UID)){
                throw new InvalidScriptException(scriptFile.getName(), "There is already a script using that UID, maybe duplicated file? ");
            }
        }
        this.initialize(UID);
    }

    public String getPath() {
        return this.fullFilePath;
    }

    private String findScriptUID() {
        Object name = runFunction("getUID", new Object[]{});
        try {
            if ((String) name == null) {
                throw new ClassCastException();
            }
            return (String) name;
        } catch (ClassCastException e) {
            DragonetServer.instance().getLogger().warn("[DragonetAPI] Script " + this.getName() + " doesn't provide custom name!");
            DragonetServer.instance().getLogger().warn("[DragonetAPI] This script will not be able to use the ScriptAPI.");
            //TODO Link details section for how this works
            //DragonetServer.instance().getLogger().warn("[DragonetAPI] See <URL> for details.");
            return null;
        }
    }

    public Object runFunction(String func, Object[] params) {
        Object result = null;  
        try {
            Object function = scope.get(func, scope);
            if (!(function instanceof UniqueTag)) {
                Function fct = (Function) function;
                result = fct.call(context, scope, scope, params);
            }
        } finally {
        }

        return result;
    }

    @Override
    public void onDisable() {
        runFunction("onDisable", new Object[]{});
    }

    @Override
    public void onLoad() {
        runFunction("onLoad", new Object[]{this});
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
        return false;
    }
    
    public boolean onScriptCommand(String handler, CommandSender cs, Command cmd, String alias, String[] args) {
        Object ret = runFunction(handler, new Object[]{
            cs, cmd.getLabel(), alias, args
        });
        if(boolean.class.isInstance(ret)){
            return (boolean)ret;
        }
        if(Boolean.class.isInstance(ret)){
            return (boolean)ret;
        }
        if(String.class.isInstance(ret)){
            String s = (String)ret;
            if(s.trim().toLowerCase().contains("false") || s.trim().toLowerCase().contains("no")){
                return false;
            }else{
                return true;
            }
        }
        getLogger().warning("Script returns a invalid boolean object in onCommand() hook, treating as true. ");
        return true;
    }

    @Override
    public void onEnable() {
        runFunction("onEnable", new Object[]{});
    }
}
