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

package org.dragonet.net;

import com.flowpowered.networking.Message;
import org.dragonet.net.packet.minecraft.PEPacket;

public class ProcessPEPacketTask implements Runnable {
    private final DragonetSession session;
    private final PEPacket packet;

    public ProcessPEPacketTask(DragonetSession session, PEPacket packet) {
        this.session = session;
        this.packet = packet;
    }
    
    @Override
    public void run() {
        if(this.packet == null) return;
        Message[] messages = this.session.getTranslator().translateToPC(this.packet);
        for(Message msg : messages){
            if(msg == null) continue;
            this.session.messageReceived(msg);
        }
    }

}
