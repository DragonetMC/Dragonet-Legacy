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

package org.dragonet.world;

import lombok.Getter;

public enum ParticleType {
    EXPLODE(0),
    LARGE_EXPLOSION(1),
    HUGE_EXPLOSION(2),
    FIREWORKS_SPARK(3),
    BUBBLE(4),
    SPLASH(5),
    WAKE(6),
    SUSPENDED(7),
    DEPTH_SUSPEND(8),
    CRIT(9),
    MAGIC_CRIT(10),
    SMOKE(11),
    LARGE_SMOKE(12),
    SPELL(13),
    INSTANT_SPELL(14),
    MOB_SPELL(15),
    MOB_SPELL_AMBIENT(16),
    WITCH_MAGIC(17),
    DRIP_WATER(18),
    DRIP_LAVA(19),
    ANGRY_VILLAGER(20),
    HAPPY_VILLAGER(21),
    TOWNAURA(22),
    NOTE(23),
    PORTAL(24),
    ENCHANTMENT_TABLE(25),
    FLAME(26),
    LAVA(27),
    FOOTSTEP(28),
    CLOUD(29),
    RED_DUST(30),
    SNOWBALL_POOF(31),
    SNOW_SHOVEL(32),
    SLIME(33),
    HEART(34),
    BARRIER(35),
    IRON_CRACK(36), // ironcrack_id_meta
    BLOCK_CRACK(37),// blockcrack_(id + (meta<<12))
    BLOCK_DUST(38), // blockdust_id
    DROPLET(39),
    TAKE(40),
    MOB_APPPERANCE(41);
    
    @Getter
    private int typePC;
    
    @Getter
    private int typePE;
    
    private ParticleType(int typePC/*, int typePE*/){
        this.typePC = typePC;
        //this.typePE = typePE;
    }
    
    
}
