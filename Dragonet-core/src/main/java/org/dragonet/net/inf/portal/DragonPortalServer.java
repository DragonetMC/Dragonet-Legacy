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

package org.dragonet.net.inf.portal;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import org.dragonet.DragonetServer;

public final class DragonPortalServer {
    
    @Getter
    private final DragonetServer server;
    
    @Getter
    private final InetAddress bindAddress;
    
    @Getter
    private final int bindPort;
    
    @Getter
    protected final String password;
    
    /**
     * Key = SocketAddress.toString(), PortalClient
     *       ^- \1.2.3.4:12345
     */
    @Getter
    private final ConcurrentMap<String, PortalClient> clients = new ConcurrentHashMap<>();
    
    private ServerSocket socket;

    public DragonPortalServer(DragonetServer server, String ip, int port, String password) throws UnknownHostException, PasswordNotSetException {
        this.server = server;
        this.bindAddress = Inet4Address.getByName(ip);
        this.bindPort = port;
        this.password = password;
        if(this.password.equalsIgnoreCase("NOT_SET")){
            throw new PasswordNotSetException();
        }
    }
    
    
    public void initialize() throws IOException, SocketException{
        socket = new ServerSocket(bindPort, 0, bindAddress);
        server.getLogger().info("DragonPortal server successfully started! ");
    }
}
