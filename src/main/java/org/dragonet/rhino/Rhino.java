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

import org.dragonet.rhino.hooks.HookOnEnchant;
import org.dragonet.rhino.hooks.HookOnQuit;
import org.dragonet.rhino.hooks.HookUseItem;
import org.dragonet.rhino.hooks.HookTick;
import org.dragonet.rhino.hooks.HookOnMove;
import org.dragonet.rhino.hooks.HookOnChatSending;
import org.dragonet.rhino.hooks.HookOnKick;
import org.dragonet.rhino.hooks.HookOnConnect;
import java.io.File;
import java.util.*;
import lombok.Getter;
import net.glowstone.GlowServer;

import org.bukkit.entity.Player;
import org.dragonet.rhino.api.functions.ScriptAPI;
/**
 *
 * @author TheMCPEGamer__ 
 * @author Ash (QuarkTheAwesome) edited
 * @author DefinitlyEvil improved
 */
public class Rhino {

    private final GlowServer server;

    @Getter
    private List<Script> scripts = new ArrayList<>();

    public Rhino(GlowServer server) {
        this.server = server;
        this.startScriptInterpreter();
    }

    public void reload() {
        this.scripts = null;
        //Reset methods in ScriptAPI as scripts will now re-add them
        ScriptAPI.resetMethods();
    }

    public void Tick() {
        HookTick.Tick();
    }

    public void useItem(int blockX, int blockY, int blockZ, String blockFace, String blockName, Player plr) {
        HookUseItem.useItem(blockX, blockY, blockZ, blockFace, blockName, plr);
    }

    public void onConnect(Player plr) {
        HookOnConnect.onConnect(plr);
    }

    public void onQuit(Player plr) {
        HookOnQuit.onQuit(plr);
    }

    public void onKick(Player plr, String msg) {
        HookOnKick.onKick(plr, msg);
    }

    public void onEnchant(Player plr, int enchantID, String itemType, byte itemData) {
        HookOnEnchant.onEnchant(plr, enchantID, itemType, itemData);
    }

    public boolean onChatSending(Player plr, String message) {
        return HookOnChatSending.onChatSending(plr, message);
    }

    public void onMove(Player plr, int x1, int y1, int z1, int x2, int y2, int z2, org.bukkit.util.Vector plrVelocity) {
        HookOnMove.onMove(plr, x1, y1, z1, x2, y2, z2, plrVelocity);
    }

    private void startScriptInterpreter() {
        scripts = loadScripts();
        //Moved this here so Scripts variable has already been initialized if it's needed
        for (Script s : scripts) {
            System.out.println("[DragonetAPI] Starting script " + s.getName() + " with UID " + s.getUID());
            s.runFunction("onInit", new Object[]{});
        }
    }

    private List<Script> loadScripts() {
        List<Script> fileList = new ArrayList<>();
        File dir = new File("plugins");

        if (!dir.isDirectory()) {
            try {
                if (dir.mkdir()) {
                } else {
                    System.err.println("Could not create plugins file...");
                    System.err.println("Please create it yourself");
                    org.dragonet.DragonetServer.instance().shutdown();
                }
            } catch (Exception e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        } else {
            for (File f : dir.listFiles()) {
                if (f.getName().endsWith((".js")) && !fileList.contains(f)) {
                    Script script = new Script(server, f);
                    fileList.add(script);
                    System.out.println("Loaded DragonetAPI Script " + script.getName());
                }
            }

            return fileList;
        }

        return new ArrayList<>();
    }
}
