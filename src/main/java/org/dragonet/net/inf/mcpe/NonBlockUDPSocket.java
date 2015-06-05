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
package org.dragonet.net.inf.mcpe;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.ArrayDeque;
import java.util.Queue;
import lombok.Getter;
import org.dragonet.net.SessionManager;

public class NonBlockUDPSocket extends Thread {

    @Getter
    private SessionManager manager;
    
    private SocketAddress addr;
    private DatagramSocket socket;
    private Queue<DatagramPacket> receivedPacketQueue = new ArrayDeque<>();
    private boolean running;

    public NonBlockUDPSocket(SessionManager manager, SocketAddress address) throws Exception {
        this.manager = manager;
        addr = address;
        socket = new DatagramSocket(null);
        socket.setBroadcast(true);
        socket.setSendBufferSize(1024 * 1024 * 8); // from PocketMine
        socket.setReceiveBufferSize(1024 * 1024); // from PocketMine
        socket.bind(addr);
    }

    @Override
    public void run() {
        setName("UDPSocket");
        running = true;
        while (running) {
            try {
                DatagramPacket pk = new DatagramPacket(new byte[1024 * 1024], 1024 * 1024);
                socket.receive(pk);
                synchronized (receivedPacketQueue) {
                    receivedPacketQueue.add(pk);
                }
            } catch (IOException e) {
            }
        }
        socket.close();
    }

    public DatagramPacket receive() {
        if (receivedPacketQueue.isEmpty()) {
            return null;
        }
        synchronized (receivedPacketQueue) {
            return receivedPacketQueue.poll();
        }
    }

    public boolean send(byte[] buffer, SocketAddress addr) {
        /*
         if(buffer.length > 1 && (buffer[0] == (byte)0xC0 || buffer[0] == (byte)0xA0)){
         System.out.println("ACK/NACK: " + ByteUtility.bytesToHexString(buffer));
         }
         */
        try {
            socket.send(new DatagramPacket(buffer, buffer.length, addr));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void end() {
        running = false;
        //this.socket.close();
        this.interrupt();
    }

    public InetAddress getServerAddress() {
        return this.socket.getLocalAddress();
    }

    public int getServerPort() {
        return this.socket.getLocalPort();
    }

    public InetAddress getLocalAddress() {
        return this.socket.getLocalAddress();
    }

}
