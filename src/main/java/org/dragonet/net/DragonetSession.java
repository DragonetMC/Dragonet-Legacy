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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import javax.crypto.SecretKey;
import lombok.Getter;
import net.glowstone.EventFactory;
import net.glowstone.GlowServer;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.entity.meta.profile.PlayerProfile;
import net.glowstone.io.PlayerDataService;
import net.glowstone.net.GlowSession;
import net.glowstone.net.message.play.game.UserListItemMessage;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.event.player.PlayerLoginEvent;
import org.dragonet.DragonetServer;
import org.dragonet.entity.DragonetPlayer;
import org.dragonet.net.packet.EncapsulatedPacket;
import org.dragonet.net.packet.RaknetDataPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.utilities.io.PEBinaryWriter;

public class DragonetSession extends GlowSession {

    private @Getter DragonetServer dServer;
    
    private SocketAddress remoteAddress;

    private @Getter long clientID;
    private short clientMTU;

    private int sequenceNum;        //Server->Client
    private int lastSequenceNum;    //Server<-Client

    private int messageID;
    private int splitID;

    private ArrayList<Integer> queueACK = new ArrayList<>();
    private ArrayList<Integer> queueNACK = new ArrayList<>();
    private HashMap<Integer, RaknetDataPacket> cachedOutgoingPacket = new HashMap<>();

    public DragonetSession(DragonetServer dServer, SocketAddress remoteAddress, long clientID, short clientMTU) {
        super(dServer.getServer(), null);
        this.dServer = dServer;
        this.clientID = clientID;
        this.clientMTU = clientMTU;
    }

    /**
     * Trigger a tick update for the session
     */
    public void onTick() {
        sendAllACK();
        sendAllNACK();
    }

    private synchronized void sendAllACK() {
        if (this.queueACK.isEmpty()) {
            return;
        }
        int[] ackSeqs = ArrayUtils.toPrimitive(this.queueACK.toArray(new Integer[0]));
        Arrays.sort(ackSeqs);
        this.queueACK.clear();
        ByteArrayOutputStream allRecBos = new ByteArrayOutputStream();
        PEBinaryWriter allRecWriter = new PEBinaryWriter(allRecBos);
        try {
            int count = ackSeqs.length;
            int records = 0;
            if (count > 0) {
                int pointer = 1;
                int start = ackSeqs[0];
                int last = ackSeqs[0];
                ByteArrayOutputStream recBos = new ByteArrayOutputStream();
                PEBinaryWriter recWriter = null;
                while (pointer < count) {
                    int current = ackSeqs[pointer++];
                    int diff = current - last;
                    if (diff == 1) {
                        last = current;
                    } else if (diff > 1) { //Forget about duplicated packets (bad queues?)
                        recBos.reset();
                        recWriter = new PEBinaryWriter(recBos);
                        if (start == last) {
                            recWriter.writeByte((byte) 0x01);
                            recWriter.writeTriad(start);
                            start = last = current;
                        } else {
                            recWriter.writeByte((byte) 0x00);
                            recWriter.writeTriad(start);
                            recWriter.writeTriad(last);
                            start = last = current;
                        }
                        records++;
                    }
                }
                if (start == last) {
                    allRecWriter.writeByte((byte) 0x01);
                    allRecWriter.writeTriad(start);
                } else {
                    allRecWriter.writeByte((byte) 0x00);
                    allRecWriter.writeTriad(start);
                    allRecWriter.writeTriad(last);
                }
                records++;
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(allRecBos);
            writer.writeByte((byte)0xC0);
            writer.writeShort((short)(records & 0xFFFF));
            writer.write(allRecBos.toByteArray());
            this.dServer.getNetworkHandler().send(bos.toByteArray(), this.remoteAddress);
        } catch (IOException e) {
        }
    }

    private synchronized void sendAllNACK() {
        if (this.queueNACK.isEmpty()) {
            return;
        }
        int[] ackSeqs = ArrayUtils.toPrimitive(this.queueNACK.toArray(new Integer[0]));
        Arrays.sort(ackSeqs);
        this.queueNACK.clear();
        ByteArrayOutputStream allRecBos = new ByteArrayOutputStream();
        PEBinaryWriter allRecWriter = new PEBinaryWriter(allRecBos);
        try {
            int count = ackSeqs.length;
            int records = 0;
            if (count > 0) {
                int pointer = 1;
                int start = ackSeqs[0];
                int last = ackSeqs[0];
                ByteArrayOutputStream recBos = new ByteArrayOutputStream();
                PEBinaryWriter recWriter;
                while (pointer < count) {
                    int current = ackSeqs[pointer++];
                    int diff = current - last;
                    if (diff == 1) {
                        last = current;
                    } else if (diff > 1) { //Forget about duplicated packets (bad queues?)
                        recBos.reset();
                        recWriter = new PEBinaryWriter(recBos);
                        if (start == last) {
                            recWriter.writeByte((byte) 0x01);
                            recWriter.writeTriad(start);
                            start = last = current;
                        } else {
                            recWriter.writeByte((byte) 0x00);
                            recWriter.writeTriad(start);
                            recWriter.writeTriad(last);
                            start = last = current;
                        }
                        records++;
                    }
                }
                if (start == last) {
                    allRecWriter.writeByte((byte) 0x01);
                    allRecWriter.writeTriad(start);
                } else {
                    allRecWriter.writeByte((byte) 0x00);
                    allRecWriter.writeTriad(start);
                    allRecWriter.writeTriad(last);
                }
                records++;
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(allRecBos);
            writer.writeByte((byte)0xA0);
            writer.writeShort((short)(records & 0xFFFF));
            writer.write(allRecBos.toByteArray());
            this.dServer.getNetworkHandler().send(bos.toByteArray(), this.remoteAddress);
        } catch (IOException e) {
        }
    }

    
    /**
     * Process a ACK packet
     */
    public void processACKPacket(byte[] buffer){
        
    }
    /**
     * Process a NACK packet
     */
    public void processNACKPacket(byte[] buffer){
        
    }
    
    /**
     * Send a message to the client
     *
     * @param message Message to send
     */
    @Override
    public void send(Message message) throws ChannelClosedException {
        //TODO
    }

    /**
     * Send multiple messages
     *
     * @param messages Messages to send
     */
    @Override
    public void sendAll(Message... messages) throws ChannelClosedException {
        //TODO
    }

    /**
     * Send a message to the client
     *
     * @param message Message to send
     * @return Returns nothing, just for implementing the interface
     */
    @Override
    public ChannelFuture sendWithFuture(Message message) {
        this.send(message);
        return null;
    }

    public void processDataPacket(RaknetDataPacket dataPacket) {
        if (dataPacket.getSequenceIndex() - this.lastSequenceNum > 1) {
            for (int i = this.lastSequenceNum + 1; i < dataPacket.getSequenceIndex(); i++) {
                this.queueNACK.add(i);
            }
        }
        this.queueACK.add(dataPacket.getSequenceIndex());
        if (dataPacket.getEncapsulatedPackets().isEmpty()) {
            return;
        }
        for (EncapsulatedPacket epacket : dataPacket.getEncapsulatedPackets()) {
            PEPacket packet = PEPacket.fromBinary(epacket.buffer);

        }
    }

    @Override
    @Deprecated
    public void enableCompression(int threshold) {
    }

    @Override
    @Deprecated
    public void enableEncryption(SecretKey sharedSecret) {
    }

    /**
     * Sets the player associated with this session.
     *
     * @param profile The player's profile with name and UUID information.
     * @throws IllegalStateException if there is already a player associated
     * with this session.
     */
    @Override
    public void setPlayer(PlayerProfile profile) {
        if (this.getPlayer() != null) {
            throw new IllegalStateException("Cannot set player twice");
        }

        // isActive check here in case player disconnected during authentication
        if (!isActive()) {
            // no need to call onDisconnect() since it only does anything if there's a player set
            return;
        }

        // initialize the player
        PlayerDataService.PlayerReader reader = this.getServer().getPlayerDataService().beginReadingData(profile.getUniqueId());
        //this.player = new GlowPlayer(this, profile, reader);
        this.player = new DragonetPlayer(this, profile, reader);

        // isActive check here in case player disconnected after authentication,
        // but before the GlowPlayer initialization was completed
        if (!isActive()) {
            onDisconnect();
            return;
        }

        // login event
        PlayerLoginEvent event = EventFactory.onPlayerLogin(player, this.getHostname());
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            disconnect(event.getKickMessage(), true);
            return;
        }

        // Kick other players with the same UUID
        for (GlowPlayer other : getServer().getOnlinePlayers()) {
            if (other != player && other.getUniqueId().equals(player.getUniqueId())) {
                other.getSession().disconnect("You logged in from another location.", true);
                break;
            }
        }

        player.getWorld().getRawPlayers().add(player);

        GlowServer.logger.info(player.getName() + " [" + this.getAddress() + "] connected, UUID: " + player.getUniqueId());

        // message and user list
        String message = EventFactory.onPlayerJoin(player).getJoinMessage();
        if (message != null && !message.isEmpty()) {
            this.getServer().broadcastMessage(message);
        }

        // todo: display names are included in the outgoing messages here, but
        // don't show up on the client. A workaround or proper fix is needed.
        Message addMessage = new UserListItemMessage(UserListItemMessage.Action.ADD_PLAYER, player.getUserListEntry());
        List<UserListItemMessage.Entry> entries = new ArrayList<>();
        for (GlowPlayer other : this.getServer().getOnlinePlayers()) {
            if (other != player && other.canSee(player)) {
                other.getSession().send(addMessage);
            }
            if (player.canSee(other)) {
                entries.add(other.getUserListEntry());
            }
        }
        send(new UserListItemMessage(UserListItemMessage.Action.ADD_PLAYER, entries));
    }

}
