 /* GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 */
package org.dragonet.rhino.api.functions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.dragonet.entity.DragonetPlayer;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSFunction;

/**
 *
 * @author TheMCPEGamer
 */
public class PlayerAPI extends ScriptableObject {

    private static final long serialVersionUID = 438270592527335642L;

    public PlayerAPI() {
    }

    @Override
    public String getClassName() {
        return "PlayerAPI";
    }

    ////////////////
    //
    // Player Methods
    //
    ////////////////
    @JSFunction
    public static void kill(Object plr) {
        ((Player) plr).setHealth(EMPTY);
    }

    @JSFunction
    public static void addItemInventory(Object player, String MaterialName, int Count) {
        Player plr = (Player) player;

        Material mat = Material.getMaterial(MaterialName);
        if ((plr != null) && (mat != null)) {
            plr.getInventory().addItem(new ItemStack(mat, Count));
            return;
        } else if (plr == null) {
            org.dragonet.DragonetServer.instance().getLogger().warn("[DragonetAPI] Script tried to add item to non-existent player! Please alert the script author.");
            return;
        } else if (mat == null) {
            org.dragonet.DragonetServer.instance().getLogger().warn("[DragonetAPI] Script tried to add non-existent item to player! Please alret the script author.");
            return;
        }
    }

    @JSFunction
    public static void clearInventory(Object plr) {
        if (((Player) plr).isOnline()) {
            try {
                ((Player) plr).getInventory().clear();
            } catch (ClassCastException cce) {
                org.dragonet.DragonetServer.instance().getLogger().warn("[DragonetAPI] Script passed non-player on clearInventory()! Please alert the script author.");
            }
        } else {
            org.dragonet.DragonetServer.instance().getLogger().warn("[DragonetAPI] Script passed an offline player on clearInventory()! Please alert the script author.");
        }
    }

    @JSFunction
    public static String getCurrentWorld(Object player) {
        Player plr = (Player) player;

        return plr.getWorld().getName();
    }

    @JSFunction
    public static void setHelth(Object player, double Health) {
        Player plr = ((Player) player);

        plr.setHealth(Health);
    }

    @JSFunction
    public static double getHealth(Object player) {
        Player plr = ((Player) player);

        return plr.getHealth();
    }

    @JSFunction
    public static int getX(Object player) {
        Player plr = ((Player) player);

        return plr.getLocation().getBlockX();
    }

    @JSFunction
    public static int getY(Object player) {
        Player plr = ((Player) player);

        return plr.getLocation().getBlockY();
    }

    @JSFunction
    public static int getZ(Object player) {
        Player plr = ((Player) player);

        return plr.getLocation().getBlockZ();
    }

    @JSFunction
    public static void teleport(Object player, int x, int y, int z, String worldName) {
        Player plr = (Player) player;

        if (!worldName.equals("")) {
            plr.teleport(new Location(org.dragonet.DragonetServer.instance().getServer().getWorld(worldName), x, y, z));
        } else {
            plr.teleport(new Location(org.dragonet.DragonetServer.instance().getServer().getWorld(plr.getWorld().getName()), x, y, z));
        }
    }

    @JSFunction
    public static void addPotionEffect(Object player, int effectID, int duration, int amp, boolean areParticles) {
        if (player instanceof DragonetPlayer) {
            //TODO for MC:PE
        } else {
            PotionEffect effectPC = new PotionEffect(PotionEffectType.getById(effectID), duration, amp, !areParticles);

            Player plr = (Player) player;

            plr.addPotionEffect(effectPC);
        }
    }
}
