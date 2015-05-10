/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.glowstone.entity.passive;

import net.glowstone.entity.GlowAnimal;
import org.bukkit.Location;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;

/**
 *
 * @author TheMCPEGamer
 */
public class GlowMooshroom extends GlowAnimal implements Cow
{
    public GlowMooshroom(Location loc) {
        super(loc, EntityType.MUSHROOM_COW);
    }
}
