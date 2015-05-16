 /* GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 */
package org.dragonet.rhino.api;

import org.dragonet.rhino.Script;

/**
 *
 * @author TheMCPEGamer__
 */
public class Tick {

    public static void Tick() {
        for (Script s : org.dragonet.DragonetServer.instance().getRhino().Scripts) {
            s.runFunction("Tick", new Object[]{});
        }
    }
}
