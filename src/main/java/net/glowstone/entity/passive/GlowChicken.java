package net.glowstone.entity.passive;

import net.glowstone.entity.GlowAnimal;
import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;

/**
 *
 * @author gabizou (Taken from his PR found here: https://github.com/GlowstoneMC/Glowstone/pull/599/files)
 */
public class GlowChicken extends GlowAnimal implements Chicken {

    public GlowChicken(Location location) {
        super(location, EntityType.CHICKEN);
        setSize(0.3F, 0.7F);
    }
}