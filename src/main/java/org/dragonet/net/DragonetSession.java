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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
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
import net.glowstone.net.message.play.game.UserListItemMessage;
import net.glowstone.net.protocol.ProtocolType;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.util.Vector;
import org.dragonet.DragonetServer;
import org.dragonet.entity.DragonetPlayer;
import org.dragonet.inventory.InventoryType;
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.inventory.PEWindowConstantID;
import org.dragonet.net.packet.minecraft.BatchPacket;
import org.dragonet.net.packet.minecraft.MovePlayerPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.PEPacketIDs;
import org.dragonet.net.packet.minecraft.SetDifficultyPacket;
import org.dragonet.net.packet.minecraft.SetHealthPacket;
import org.dragonet.net.packet.minecraft.SetTimePacket;
import org.dragonet.net.packet.minecraft.StartGamePacket;
import org.dragonet.net.packet.minecraft.WindowItemsPacket;
import org.dragonet.net.translator.BaseTranslator;

public abstract class DragonetSession extends GlowSession {

    public final static Pattern PATTERN_USERNAME = Pattern.compile("^[a-zA-Z0-9_]{3,16}$");

    @Getter
    private DragonetServer dServer;

    @Getter
    private long clientSessionID;

    @Getter
    private String username;

    @Getter
    @Setter
    private BaseTranslator translator;

    @Getter
    private ClientChunkManager chunkManager;

    private boolean statusActive = true;

    public DragonetSession(DragonetServer dServer, BaseTranslator translator) {
        super(dServer.getServer());
        this.dServer = dServer;
        this.translator = translator;
        this.chunkManager = new ClientChunkManager(this);
    }

    public abstract String getSessionKey();

    public void onTick() {
        this.chunkManager.onTick();
    }

    public void onPacketReceived(PEPacket packet) {
        switch (packet.pid()) {
            case PEPacketIDs.DISCONNECT_PACKET:
                this.onDisconnect();
                break;
            case PEPacketIDs.BATCH_PACKET:
                BatchPacket packetBatch = (BatchPacket) packet;
                if (packetBatch.packets == null || packetBatch.packets.isEmpty()) {
                    return;
                }
                for (PEPacket pk : packetBatch.packets) {
                    onPacketReceived(pk);
                }
                break;
            default:
                if (!(this.translator instanceof BaseTranslator)) {
                    break;
                }
                this.dServer.getThreadPool().submit(new ProcessPEPacketTask(this, packet));
                break;
        }
    }

    public abstract void send(PEPacket pk);

    public abstract void send(PEPacket pk, int reliability);

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
        if (this.player.getGameMode().equals(GameMode.CREATIVE)) {
            pkStartGame.gamemode = 1;
        } else {
            pkStartGame.gamemode = 0;
        }
        pkStartGame.eid = 0;//this.player.getEntityId();
        pkStartGame.spawnX = this.player.getWorld().getSpawnLocation().getBlockX();
        pkStartGame.spawnY = this.player.getWorld().getSpawnLocation().getBlockY();
        pkStartGame.spawnZ = this.player.getWorld().getSpawnLocation().getBlockZ();
        pkStartGame.x = (float) this.player.getLocation().getX();
        pkStartGame.y = (float) this.player.getLocation().getY();
        pkStartGame.z = (float) this.player.getLocation().getZ();
        this.send(pkStartGame);

        //Send Time
        SetTimePacket pkTime = new SetTimePacket((int) (this.getPlayer().getWorld().getTime() & 0xFFFFFFFF), false);
        this.send(pkTime);

        /*
         //Send Spawn Position
         SetSpawnPositionPacket pkSpawnPos = new SetSpawnPositionPacket();
         pkSpawnPos.x = this.player.getLocation().getBlockX();
         pkSpawnPos.y = this.player.getLocation().getBlockY();
         pkSpawnPos.z = this.player.getLocation().getBlockZ();
         this.send(pkSpawnPos);
         */
        //Send Health
        SetHealthPacket pkHealth = new SetHealthPacket((int) Math.floor(this.getPlayer().getHealth()));
        this.send(pkHealth);

        //Send Difficulty Packet
        SetDifficultyPacket pkDifficulty = new SetDifficultyPacket();
        pkDifficulty.difficulty = this.getServer().getDifficulty().getValue();
        this.send(pkDifficulty);

        //Preprare chunks
        this.chunkManager.prepareLoginChunks();

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

    public void sendInventory() {
        if (this.getPlayer() == null) {
            return;
        }
        if (this.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        WindowItemsPacket pkItems = new WindowItemsPacket();
        pkItems.windowID = PEWindowConstantID.PLAYER_INVENTORY;
        pkItems.slots = new PEInventorySlot[InventoryType.SlotSize.PLAYER];
        pkItems.hotbar = new int[9];
        for (int i = 9; i <= 35; i++) {
            if (this.getPlayer().getInventory().getContents()[i] != null) {
                pkItems.slots[i - 9] = new PEInventorySlot((short) (this.getPlayer().getInventory().getContents()[i].getTypeId() & 0xFFFF), (byte) (this.getPlayer().getInventory().getContents()[i].getAmount() & 0xFF), this.getPlayer().getInventory().getContents()[i].getDurability());
            } else {
                pkItems.slots[i - 9] = new PEInventorySlot();
            }
        }
        for (int i = 0; i <= 8; i++) {
            if (this.getPlayer().getInventory().getContents()[i] != null) {
                pkItems.slots[i + 27] = new PEInventorySlot((short) (this.getPlayer().getInventory().getContents()[i].getTypeId() & 0xFFFF), (byte) (this.getPlayer().getInventory().getContents()[i].getAmount() & 0xFF), this.getPlayer().getInventory().getContents()[i].getDurability());
            } else {
                pkItems.slots[i + 27] = new PEInventorySlot();
            }
        }
        for (int i = 0; i <= 8; i++) {
            pkItems.hotbar[i] = 44 - 8 + i;
        }
        this.send(pkItems);
    }

    /**
     * Send the server side position to the client, used to correct the position
     */
    public void sendPosition() {
        if (this.getPlayer() == null) {
            return;
        }
        /*
         MoveEntitiesPacket.MoveEntityData d = new MoveEntitiesPacket.MoveEntityData();
         d.eid = this.getPlayer().getEntityId();
         d.x = (float) this.getPlayer().getLocation().getX();
         d.y = (float) this.getPlayer().getLocation().getY();
         d.z = (float) this.getPlayer().getLocation().getZ();
         d.yaw = this.getPlayer().getLocation().getYaw();
         d.pitch = this.getPlayer().getLocation().getPitch();
         MoveEntitiesPacket pkMovePlayer = new MoveEntitiesPacket(new MoveEntitiesPacket.MoveEntityData[]{d});
         */
        MovePlayerPacket pkMovePlayer = new MovePlayerPacket();
        pkMovePlayer.eid = this.getPlayer().getEntityId();
        pkMovePlayer.x = (float) this.getPlayer().getLocation().getX();
        pkMovePlayer.y = (float) this.getPlayer().getLocation().getY();
        pkMovePlayer.z = (float) this.getPlayer().getLocation().getZ();
        pkMovePlayer.yaw = this.getPlayer().getLocation().getYaw();
        pkMovePlayer.bodyYaw = this.getPlayer().getLocation().getYaw();
        pkMovePlayer.pitch = this.getPlayer().getLocation().getPitch();
        pkMovePlayer.teleport = true;
        this.send(pkMovePlayer);
    }

    /**
     * Check wether a player can go to the new location
     *
     * @param newLoc The New location
     * @return New location is okay or not
     */
    public boolean validateMovement(Location newLoc) {
        if (this.getPlayer() == null) {
            return false;
        }
        //Check block stucking
        if (!newLoc.getWorld().getName().equals(this.getPlayer().getLocation().getWorld().getName())) {
            newLoc.setWorld(this.getPlayer().getLocation().getWorld());
        }
        Block block = this.getPlayer().getWorld().getBlockAt(newLoc);
        Block blockUp = this.getPlayer().getWorld().getBlockAt(newLoc.clone().add(new Vector(0, 1, 0)));
        if (block != null) {
            if (block.getType().isSolid()) {
                return false;
            }
        }
        if (blockUp != null) {
            if (blockUp.getType().isSolid()) {
                return false;
            }
        }
        return true;
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
