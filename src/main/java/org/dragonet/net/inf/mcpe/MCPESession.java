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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import lombok.Getter;
import net.glowstone.EventFactory;
import net.glowstone.GlowServer;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.GlowSession;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.dragonet.DragonetServer;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.EncapsulatedPacket;
import org.dragonet.net.packet.Protocol;
import org.dragonet.net.packet.RaknetDataPacket;
import org.dragonet.net.packet.minecraft.AdventureSettingsPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.SetTimePacket;
import org.dragonet.net.translator.BaseTranslator;

public class MCPESession extends DragonetSession {

    private @Getter
    DragonetServer dServer;

    private PENetworkClient client;

    private boolean statusActive;


    public MCPESession(DragonetServer dServer, PENetworkClient client) {
        super(dServer, null);
        this.dServer = dServer;
        this.client = client;
    }
    

    @Override
    public String getSessionKey() {
        return "MCPE-" + client.getRemoteAddress().toString();
    }

    /**
     * Trigger a tick update for the session
     */
    @Override
    public void onTick() {
        client.onTick();
        super.onTick();
        if (client.getSentAndReceivedChunks() >= 56 && (this.player instanceof Player)) { //TODO: Change 
            this.getLogger().info("PE player [" + this.player.getName() + "] has spawned. ");
            client.setSentAndReceivedChunks(-1);
            this.sendSettings();
            SetTimePacket pkTime = new SetTimePacket((int) (this.getPlayer().getWorld().getTime() & 0xFFFFFFFF), true);
            this.send(pkTime);
        }
    }

    /**
     * Send a packet to the client with a reliability defined
     *
     * @param packet Packet to send
     * @param reliability Packet reliability
     */
    @Override
    public void send(PEPacket packet, int reliability) {
        client.sendPacket(packet, reliability);
    }

    /**
     * *
     * Send a packet to the client with default packet reliability 2
     *
     * @param packet Packet to send
     */
    @Override
    public void send(PEPacket packet) {
        this.send(packet, 2);
    }



    

    @Override
    public boolean isActive() {
        return this.statusActive;
    }

    public void sendSettings() {
        if (!(this.getPlayer() instanceof GlowPlayer)) {
            return;
        }
        int flags = 0;
        if (this.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
            flags |= 0x01;
        }
        flags |= 0x20;
        AdventureSettingsPacket pkAdventure = new AdventureSettingsPacket();
        pkAdventure.flags = flags;
        this.send(pkAdventure);
    }

    @Override
    public void onDisconnect() {
        this.statusActive = false;
        this.dServer.getSessionManager().removeSessionRaw(this);
        this.getServer().getSessionRegistry().remove((GlowSession) this);
        if (this.player != null) {
            this.player.getWorld().getRawPlayers().remove(this.player);
        }
        super.onDisconnect();
    }

    @Override
    public void disconnect() {
        this.disconnect("Kicked by the server! ");
    }

    /**
     * Disconnects the session with the specified reason. This causes a
     * KickMessage to be sent. When it has been delivered, the channel is
     * closed.
     *
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
            this.player.remove();
        } else {
            GlowServer.logger.info("[" + client.getRemoteIP() + ":" + client.getRemotePort() + "] kicked: " + reason);
        }

        client.disconnect(reason);
        this.statusActive = false;
        this.dServer.getSessionManager().removeSessionRaw(this);
        this.getServer().getSessionRegistry().remove((GlowSession) this);
        if (this.player != null) {
            this.player.getWorld().getRawPlayers().remove(this.player);
        }
        this.player = null;
    }


}
