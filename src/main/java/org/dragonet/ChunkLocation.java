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

import lombok.Getter;
import lombok.Setter;

public class ChunkLocation {

    private @Getter
    @Setter
    int x;
    private @Getter
    @Setter
    int z;

    public ChunkLocation() {
        this.x = 0;
        this.z = 0;
    }

    public ChunkLocation(int x, int z) {
        this.x = x;
        this.z = z;
    }

    /**
     * Caculate the distance to another ChunkLocation.
     *
     * @param loc Another chunk location
     * @return
     */
    public int distanceTo(ChunkLocation loc) {
        return (int) Math.sqrt((double) ((loc.getX() - this.getX()) * (loc.getX() - this.getX()) + (loc.getZ() - this.getZ()) * (loc.getZ() - this.getZ())));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ChunkLocation)) {
            return false;
        }
        ChunkLocation loc = (ChunkLocation) obj;
        if (loc.x == this.x && loc.z == this.z) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "ChunkLocation(" + this.x + ", " + this.z + ")";
    }
}
