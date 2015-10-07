/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */

package org.dragonet.rhino;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

public class ScriptCommand extends Command {

    private String handler;
    private Script script;
    
    public ScriptCommand(String name, Script script, String handler, String requiredPermissions){
        super(name);
        this.script = script;
        this.handler = handler;
        this.setPermission(requiredPermissions);
    }
    
    @Override
    public boolean execute(CommandSender cs, String string, String[] strings) {
        boolean success = false;
        if(!script.isEnabled()) return false;
        if(!testPermission(cs)) return false;
        try{
            success = script.onScriptCommand(handler, cs, this, string, strings);
        }catch(Exception e){
            throw new CommandException("Unhandled exception executing command '" + getLabel() + "' in script [" + script.getName() + "]. ", e);
        }
        return success;
    }

}
