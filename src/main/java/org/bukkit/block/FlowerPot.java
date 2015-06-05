package org.bukkit.block;

import org.bukkit.material.MaterialData;

/**
 * Represents a flower pot.
 */
public interface FlowerPot extends BlockState {

    /**
     * Gets the contents of the flower pot.
     *
     * @return the contents.
     */
    MaterialData getContents();

    /**
     * Sets the contents of the flower pot.
     *
     * @param contents the new contents.
     */
    void setContents(MaterialData contents);
}
