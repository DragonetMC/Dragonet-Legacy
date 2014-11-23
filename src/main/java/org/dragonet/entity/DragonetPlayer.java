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

package org.dragonet.entity;

import net.glowstone.entity.GlowPlayer;
import net.glowstone.entity.meta.profile.PlayerProfile;
import net.glowstone.io.PlayerDataService;
import net.glowstone.net.GlowSession;

public class DragonetPlayer extends GlowPlayer {

    public DragonetPlayer(GlowSession session, PlayerProfile profile, PlayerDataService.PlayerReader reader) {
        super(session, profile, reader);
    }

}
