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

public abstract class PEPacketTranslatorToPC<T extends BaseTranslator, P extends PEPacket> {

    @Getter
    private T translator;

    @Getter
    private DragonetSession session;

    public PEPacketTranslatorToPC(T translator, DragonetSession session) {
        this.translator = translator;
        this.session = session;
    }

    public abstract Message[] handleSpecific(P packet);

    public Message[] handle(PEPacket packet) {
        return handleSpecific((P) packet);
    }
}
