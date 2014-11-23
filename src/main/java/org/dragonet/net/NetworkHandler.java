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

import java.net.InetSocketAddress;
import lombok.Getter;
import org.dragonet.DragonetServer;

public class NetworkHandler {
    public final static long serverID = 0x0000000012345678L;
    
    private @Getter DragonetServer server;
    private @Getter NonBlockUDPSocket socket;

    private NonBlockUDPSocket udp;
    
    public NetworkHandler(DragonetServer server, InetSocketAddress address) {
        this.server = server;
        this.udp = new NonBlockUDPSocket(this.server, address);
        this.udp.start();
    }
    
    public void onTick(){
        while(this.udp.receive() != null){
            //TODO
        }
    }

}
