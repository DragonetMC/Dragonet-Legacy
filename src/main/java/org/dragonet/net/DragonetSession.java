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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.Setter;
import net.glowstone.EventFactory;
import net.glowstone.GlowServer;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.entity.meta.profile.PlayerProfile;
import net.glowstone.io.PlayerDataService;
import net.glowstone.net.GlowSession;
import net.glowstone.net.message.KickMessage;
import net.glowstone.net.message.play.game.UserListItemMessage;
import net.glowstone.net.protocol.ProtocolType;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.dragonet.ChunkLocation;
import org.dragonet.DragonetServer;
import org.dragonet.entity.DragonetPlayer;
import org.dragonet.net.packet.EncapsulatedPacket;
import org.dragonet.net.packet.RaknetDataPacket;
import org.dragonet.net.packet.minecraft.ClientConnectPacket;
import org.dragonet.net.packet.minecraft.LoginPacket;
import org.dragonet.net.packet.minecraft.LoginStatusPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.PEPacketIDs;
import org.dragonet.net.packet.minecraft.PingPongPacket;
import org.dragonet.net.packet.minecraft.ServerHandshakePacket;
import org.dragonet.net.packet.minecraft.StartGamePacket;
import org.dragonet.net.translator.BaseTranslator;
import org.dragonet.net.translator.TranslatorProvider;
import org.dragonet.utilities.MD5Encrypt;
import org.dragonet.utilities.io.PEBinaryReader;
import org.dragonet.utilities.io.PEBinaryWriter;

public class DragonetSession extends GlowSession {

    private final static Pattern patternUsername = Pattern.compile("^[a-zA-Z0-9_]{3,16}$");
    
    private @Getter
    DragonetServer dServer;

    private SocketAddress remoteAddress;
    private String remoteIP;
    private int remotePort;
    private InetSocketAddress remoteInetSocketAddress;

    private int loginStage;
    
    private @Getter
    long clientID;
    private @Getter
    short clientMTU;

    private @Getter
    long clientSessionID ;

    private @Getter
    int sequenceNum;        //Server->Client
    private @Getter
    int lastSequenceNum;    //Server<-Client

    private @Getter
    @Setter
    int messageIndex;          //Server->Client
    private @Getter
    @Setter
    int splitID;

    private @Getter String username;
    
    private RaknetDataPacket queue;

    private ArrayList<Integer> queueACK = new ArrayList<>();
    private ArrayList<Integer> queueNACK = new ArrayList<>();
    private HashMap<Integer, RaknetDataPacket> cachedOutgoingPacket = new HashMap<>();

    private @Getter BaseTranslator translator;
    
    private @Getter ClientChunkManager chunkManager;

    private boolean statusActive = true;
    
    public DragonetSession(DragonetServer dServer, SocketAddress remoteAddress, long clientID, short clientMTU) {
        super(dServer.getServer());
        this.dServer = dServer;
        this.clientID = clientID;
        this.clientMTU = clientMTU;
        this.remoteAddress = remoteAddress;
        this.remoteIP = this.remoteAddress.toString().substring(1, this.remoteAddress.toString().indexOf(":"));
        this.remotePort = Integer.parseInt(this.remoteAddress.toString().substring(this.remoteAddress.toString().indexOf(":")+1));
        this.remoteInetSocketAddress = new InetSocketAddress(this.remoteIP, this.remotePort);
        this.queue = new RaknetDataPacket(this.sequenceNum);
        this.chunkManager = new ClientChunkManager(this);
        this.loginStage = 0;
    }

    /**
     * Trigger a tick update for the session
     */
    public void onTick() {
        sendAllACK();
        sendAllNACK();
        if (this.queue.getEncapsulatedPackets().size() > 0) {
            this.fireQueue();
        }
        this.chunkManager.onTick();
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
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) 0xC0);
            writer.writeShort((short) (records & 0xFFFF));
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
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) 0xA0);
            writer.writeShort((short) (records & 0xFFFF));
            writer.write(allRecBos.toByteArray());
            this.dServer.getNetworkHandler().send(bos.toByteArray(), this.remoteAddress);
        } catch (IOException e) {
        }
    }

    /**
     * Process a ACK packet
     *
     * @param buffer The ACK packet binary array
     */
    public void processACKPacket(byte[] buffer) {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(buffer));
            int count = reader.readShort();
            List<Integer> packets = new ArrayList<>();
            for (int i = 0; i < count && reader.available() > 0; ++i) {
                if (reader.readByte() == (byte) 0x00) {
                    int start = reader.readTriad();
                    int end = reader.readTriad();
                    if ((end - start) > 4096) {
                        end = start + 4096;
                    }
                    for (int c = start; c <= end; ++c) {
                        packets.add(c);
                    }
                } else {
                    packets.add(reader.readTriad());
                }
            }
            int[] seqNums = ArrayUtils.toPrimitive(packets.toArray(new Integer[0]));
            for (int seq : seqNums) {
                if (this.cachedOutgoingPacket.containsKey(seq)) {
                    this.cachedOutgoingPacket.remove(seq);
                }
            }
        } catch (IOException e) {
        }
    }

    /**
     * Process a NACK packet
     *
     * @param buffer The NACK packet binary array
     */
    public void processNACKPacket(byte[] buffer) {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(buffer));
            int count = reader.readShort();
            List<Integer> packets = new ArrayList<>();
            for (int i = 0; i < count && reader.available() > 0; ++i) {
                if (reader.readByte() == (byte) 0x00) {
                    int start = reader.readTriad();
                    int end = reader.readTriad();
                    if ((end - start) > 4096) {
                        end = start + 4096;
                    }
                    for (int c = start; c <= end; ++c) {
                        packets.add(c);
                    }
                } else {
                    packets.add(reader.readTriad());
                }
            }
            int[] seqNums = ArrayUtils.toPrimitive(packets.toArray(new Integer[0]));
            for (int seq : seqNums) {
                if (this.cachedOutgoingPacket.containsKey(seq)) {
                    this.dServer.networkHandler.getUdp().send(this.cachedOutgoingPacket.get(seq).getData(), this.remoteAddress);
                }
            }
        } catch (IOException e) {
        }
    }

    /**
     * Send a packet to the client with a reliability defined
     *
     * @param packet Packet to send
     * @param reliability Packet reliability
     */
    public void send(PEPacket packet, int reliability) {
        if(!(packet instanceof PEPacket)) return;
        packet.encode();
        this.fireQueue();
        EncapsulatedPacket[] encapsulatedPacket = EncapsulatedPacket.fromPEPacket(this, packet, reliability);
        for (EncapsulatedPacket ePacket : encapsulatedPacket) {
            ePacket.encode();
            /*
            if (this.queue.getLength() + ePacket.getData().length > this.clientMTU - 24) {
                this.fireQueue();
            }
            */
            this.queue.getEncapsulatedPackets().add(ePacket);
            this.fireQueue();
        }
    }
    
    /***
     * Send a packet to the client with default packet reliability 2
     * @param packet Packet to send
     */
    public void send(PEPacket packet){
        this.send(packet, 2);
    }
    
    

    private synchronized void fireQueue() {
        if(this.queue.getEncapsulatedPackets().isEmpty()) return;
        this.cachedOutgoingPacket.put(this.queue.getSequenceNumber(), this.queue);
        this.queue.encode();
        this.dServer.getNetworkHandler().getUdp().send(this.queue.getData(), this.remoteAddress);
        this.queue = new RaknetDataPacket(this.sequenceNum++);
    }

    /**
     * Send a message to the client
     *
     * @param message Message to send
     */
    @Override
    public void send(Message message) throws ChannelClosedException {
        PEPacket[] packets = this.translator.translateToPE(message);
        if (packets == null) {
            return;
        }
        for (PEPacket packet : packets) {
            if (packet == null) {
                continue;
            }
            this.send(packet);
        }
    }

    /**
     * Send multiple messages
     *
     * @param messages Messages to send
     */
    @Override
    public void sendAll(Message... messages) throws ChannelClosedException {
        for (Message message : messages) {
            this.send(message);
        }
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
        if (dataPacket.getSequenceNumber() - this.lastSequenceNum > 1) {
            for (int i = this.lastSequenceNum + 1; i < dataPacket.getSequenceNumber(); i++) {
                this.queueNACK.add(i);
            }
        }
        this.lastSequenceNum = dataPacket.getSequenceNumber();
        this.queueACK.add(dataPacket.getSequenceNumber());
        if (dataPacket.getEncapsulatedPackets().isEmpty()) {
            return;
        }
        for (EncapsulatedPacket epacket : dataPacket.getEncapsulatedPackets()) {
            PEPacket packet = PEPacket.fromBinary(epacket.buffer);
            if (packet == null) {
                continue;
            }
            switch (packet.pid()) {
                case PEPacketIDs.PING:
                    PingPongPacket pkPong = new PingPongPacket();
                    pkPong.pingID = ((PingPongPacket)packet).pingID;
                    this.send(pkPong, 0);
                    break;
                case PEPacketIDs.CLIENT_CONNECT:
                    if(this.loginStage != 0) break;
                    this.clientSessionID = ((ClientConnectPacket)packet).sessionID;
                    ServerHandshakePacket pkServerHandshake = new ServerHandshakePacket();
                    pkServerHandshake.port = (short)(this.remotePort & 0xFFFF);
                    pkServerHandshake.session = this.clientSessionID;
                    pkServerHandshake.session2 = 0x04440BA9L;
                    this.loginStage = 1;
                    this.send(pkServerHandshake);
                    break;
                case PEPacketIDs.CLIENT_HANDSHAKE:
                    if(this.loginStage != 1) break;
                    this.loginStage = 2;
                    break;
                case PEPacketIDs.LOGIN_PACKET:
                    if(this.loginStage != 2) break;
                    LoginPacket packetLogin = (LoginPacket) packet;
                    this.username = packetLogin.username;
                    
                    this.translator = TranslatorProvider.getByPEProtocolID(this, packetLogin.protocol1);
                    if(!(this.translator instanceof BaseTranslator)){
                        LoginStatusPacket pkLoginStatus = new LoginStatusPacket();
                        pkLoginStatus.status = 2;
                        this.send(pkLoginStatus);
                        this.disconnect("Unsupported game version! ");
                        break;
                    }
                    
                    LoginStatusPacket pkLoginStatus = new LoginStatusPacket();
                    pkLoginStatus.status = 0;
                    this.send(pkLoginStatus);
                    
                    this.getLogger().info("Sent LoginStatusPacket! ");
                    
                    Matcher matcher = patternUsername.matcher(this.username);
                    if(!matcher.matches()){
                        this.disconnect("Bad username! ");
                        break;
                    }
                    
                    this.loginStage = 3;
                    this.setPlayer(new PlayerProfile(this.username, UUID.nameUUIDFromBytes(MD5Encrypt.encryptString(this.username))));
                    break;
                default:
                    if(this.loginStage != 3) break;
                    if(!(this.translator instanceof BaseTranslator)) break;
                    this.dServer.getThreadPool().submit(new ProcessPEPacketTask(this, packet));
                    break;
            }
        }
    }

    @Override
    public void disconnect(String reason) {
        super.disconnect(reason);
        this.dServer.getNetworkHandler().removeSession(this);
    }

    @Override
    public boolean isActive() {
        return this.statusActive;
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

        GlowServer.logger.log(Level.INFO, "{0} [{1}] connected from Minecraft PE, UUID: {2}", new Object[]{player.getName(), this.getAddress(), player.getUniqueId()});

        //Send the StartGamePacket
        StartGamePacket pkStartGame = new StartGamePacket();
        pkStartGame.seed = 0;
        pkStartGame.generator = 1;
        if(this.player.getGameMode().equals(GameMode.CREATIVE)){
            pkStartGame.gamemode = 1;
        }else{
            pkStartGame.gamemode = 0;
        }
        pkStartGame.eid = this.player.getEntityId();
        pkStartGame.spawnX = this.player.getWorld().getSpawnLocation().getBlockX();
        pkStartGame.spawnY = this.player.getWorld().getSpawnLocation().getBlockY();
        pkStartGame.spawnZ = this.player.getWorld().getSpawnLocation().getBlockZ();
        pkStartGame.x = (float)this.player.getLocation().getX();
        pkStartGame.y = (float)this.player.getLocation().getY();
        pkStartGame.z = (float)this.player.getLocation().getZ();
        this.send(pkStartGame);
        
        //Preprare chunks
        this.chunkManager.autoPrepareChunks();
        
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

    
    /**
     * Disconnects the session with the specified reason. This causes a
     * KickMessage to be sent. When it has been delivered, the channel
     * is closed.
     * @param reason The reason for disconnection.
     * @param overrideKick Whether to skip the kick event.
     */
    @Override
    public void disconnect(String reason, boolean overrideKick) {
        if (player != null && !overrideKick) {
            PlayerKickEvent event = EventFactory.onPlayerKick(player, reason);
            if (event.isCancelled()) {
                return;
            }

            reason = event.getReason();

            if (event.getLeaveMessage() != null) {
                this.getServer().broadcastMessage(event.getLeaveMessage());
            }
        }

        // log that the player was kicked
        if (player != null) {
            GlowServer.logger.info(player.getName() + " kicked: " + reason);
        } else {
            GlowServer.logger.info("[" + this.remoteIP + ":" + this.remotePort + "] kicked: " + reason);
        }

        this.send(new KickMessage(reason));
    }

    @Override
    public InetSocketAddress getAddress() {
        return this.remoteInetSocketAddress;
    }
    
    @Override
    public String getHostname() {
        return this.remoteIP;
    }

    @Override
    public void enableCompression(int threshold) {
    }

    @Override
    public void enableEncryption(SecretKey sharedSecret) {
    }
    
    @Override
    public void setProtocol(ProtocolType protocol) {
        //GlowProtocol proto = protocol.getProtocol();
        //super.setProtocol(proto);
    }
}
