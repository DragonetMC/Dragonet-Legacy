/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator;

import com.flowpowered.networking.Message;
import lombok.Getter;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PEPacket;

public abstract class MessageTranslatorToPE<T extends BaseTranslator, P extends Message> {

    private @Getter
    T translator;
    
    private @Getter
    DragonetSession session;

    public MessageTranslatorToPE(T translator, DragonetSession session) {
        this.translator = translator;
        this.session = session;
    }

    public abstract PEPacket[] handle(P packet);
}
