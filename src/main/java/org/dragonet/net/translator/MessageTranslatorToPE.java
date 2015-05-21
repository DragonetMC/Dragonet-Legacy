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

    @Getter
    private T translator;

    @Getter
    private DragonetSession session;

    public MessageTranslatorToPE(T translator, DragonetSession session) {
        this.translator = translator;
        this.session = session;
    }

    public abstract PEPacket[] handleSpecific(P packet);

    public PEPacket[] handle(Message message) {
        return handleSpecific((P) message);
    }
}
