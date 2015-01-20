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
package org.dragonet.net.translator;

import com.flowpowered.networking.Message;
import lombok.Getter;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PEPacket;

public abstract class BaseTranslator {

    private @Getter
    DragonetSession session;

    public BaseTranslator(DragonetSession session) {
        this.session = session;
    }

    /**
     * Translate a Minecraft PE packet into Minecraft PC message.
     *
     * @param packet The PEPacket
     * @return Single/Multiple message(s)
     */
    public abstract Message[] translateToPC(PEPacket packet);

    /**
     * Translate a Minecraft PC packet into Minecraft PE message.
     *
     * @param message
     * @return Single/Multiple packet(s)
     */
    public abstract PEPacket[] translateToPE(Message message);

    /**
     * Get the specific ItemTranslator for current protocol
     *
     * @return ItemTranslator for current protocol
     */
    public abstract ItemTranslator getItemTranslator();
}
