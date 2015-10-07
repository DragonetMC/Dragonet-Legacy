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
package org.dragonet;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

public class PocketPotionEffect {

    public final static int SPEED = 1;
    public final static int SLOWNESS = 2;
    public final static int HASTE = 3;
    public final static int SWIFTNESS = 3;
    public final static int FATIGUE = 4;
    public final static int MINING_FATIGUE = 4;
    public final static int STRENGTH = 5;
//TODO: public final static int HEALING = 6;
//TODO: public final static int HARMING = 7;
    public final static int JUMP = 8;
    public final static int NAUSEA = 9;
    public final static int CONFUSION = 9;
    public final static int REGENERATION = 10;
    public final static int DAMAGE_RESISTANCE = 11;
    public final static int FIRE_RESISTANCE = 12;
    public final static int WATER_BREATHING = 13;
    public final static int INVISIBILITY = 14;
//public final static int BLINDNESS = 15;
//public final static int NIGHT_VISION = 16;
//public final static int HUNGER = 17;
    public final static int WEAKNESS = 18;
    public final static int POISON = 19;
    public final static int WITHER = 20;
    public final static int HEALTH_BOOST = 21;
//public final static int  ABSORPTION = 22;
//public final static int SATURATION = 23;

    private final static HashMap<Integer, PocketPotionEffect> effects = new HashMap<>();

    static {
        effects.put(SPEED, new PocketPotionEffect(SPEED));
        effects.put(SLOWNESS, new PocketPotionEffect(SLOWNESS));
        effects.put(HASTE, new PocketPotionEffect(HASTE));
        effects.put(SWIFTNESS, new PocketPotionEffect(SWIFTNESS));
        effects.put(FATIGUE, new PocketPotionEffect(FATIGUE));
        effects.put(MINING_FATIGUE, new PocketPotionEffect(MINING_FATIGUE));
        effects.put(STRENGTH, new PocketPotionEffect(STRENGTH));
        effects.put(JUMP, new PocketPotionEffect(JUMP));
        effects.put(NAUSEA, new PocketPotionEffect(NAUSEA));
        effects.put(CONFUSION, new PocketPotionEffect(CONFUSION));
        effects.put(REGENERATION, new PocketPotionEffect(REGENERATION));
        effects.put(DAMAGE_RESISTANCE, new PocketPotionEffect(DAMAGE_RESISTANCE));
        effects.put(FIRE_RESISTANCE, new PocketPotionEffect(FIRE_RESISTANCE));
        effects.put(WATER_BREATHING, new PocketPotionEffect(WATER_BREATHING));
        effects.put(INVISIBILITY, new PocketPotionEffect(INVISIBILITY));
        effects.put(WEAKNESS, new PocketPotionEffect(WEAKNESS));
        effects.put(POISON, new PocketPotionEffect(POISON));
        effects.put(WITHER, new PocketPotionEffect(WITHER));
        effects.put(HEALTH_BOOST, new PocketPotionEffect(HEALTH_BOOST));
    }

    public static PocketPotionEffect getByID(int id) {
        if (effects.containsKey(id)) {
            return effects.get((Integer) id).clone();
        } else {
            return null;
        }
    }

    @Getter
    private int effect;

    @Getter
    @Setter
    private int ampilifier;

    @Getter
    @Setter
    private int duration;

    @Getter
    @Setter
    private boolean particles;

    public PocketPotionEffect(int effect) {
        this.effect = effect;
    }

    @Override
    protected PocketPotionEffect clone() {
        PocketPotionEffect eff = new PocketPotionEffect(effect);
        eff.setAmpilifier(ampilifier);
        eff.setParticles(particles);
        eff.setDuration(duration);
        return eff;
    }

}
