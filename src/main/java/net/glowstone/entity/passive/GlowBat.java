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
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;

/**
 *
 * @author TheMCPEGamer
 */
public class GlowBat extends GlowAnimal implements Bat
{
    private Boolean isAwake = true;
    
    public GlowBat(Location loc)
    {
        super(loc, EntityType.BAT);
        setSize(0.3F, 0.3F);
    }

    @Override
    public boolean isAwake()
    {
        return isAwake;
    }

    @Override
    public void setAwake(boolean awake)
    {
        this.isAwake = awake;
    }
}
