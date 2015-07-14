 /* GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 */

package net.glowstone.entity.passive;

import net.glowstone.entity.GlowAnimal;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Squid;

/**
 *
 * @author TheMCPEGamer
 */
public class GlowSquid extends GlowAnimal implements Squid
{
    public GlowSquid(Location loc)
    {
        super(loc, EntityType.SQUID);
        setSize(0.9F, 0.9F);
    }
}
