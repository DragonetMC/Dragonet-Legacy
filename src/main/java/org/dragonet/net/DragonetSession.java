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
import com.flowpowered.networking.exception.ChannelClosedException;
import io.netty.channel.ChannelFuture;
import java.net.InetSocketAddress;
import javax.crypto.SecretKey;
import net.glowstone.GlowServer;
import net.glowstone.net.GlowSession;

public class DragonetSession extends GlowSession {
    
    private InetSocketAddress remoteAddress;
    private long clientID;
    
    public DragonetSession(GlowServer server, InetSocketAddress remoteAddress, long clientID) {
        super(server, null);
    }

    @Override
    public void send(Message message) throws ChannelClosedException {
        //TODO
    }

    @Override
    public void sendAll(Message... messages) throws ChannelClosedException {
        //TODO
    }

    @Override
    public ChannelFuture sendWithFuture(Message message) {
        //TODO
        return null;
    }

    @Override
    public void enableCompression(int threshold) {
    }

    @Override
    public void enableEncryption(SecretKey sharedSecret) {
    }

    
}
