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

import io.github.jython234.jraklibplus.server.NioSession;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Matcher;
import lombok.Getter;
import net.glowstone.entity.meta.profile.PlayerProfile;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.inf.mcpe.jraklib.JRakLibInterface;
import org.dragonet.net.packet.Protocol;
import org.dragonet.net.packet.minecraft.BatchPacket;
import org.dragonet.net.packet.minecraft.DisconnectPacket;
import org.dragonet.net.packet.minecraft.LoginPacket;
import org.dragonet.net.packet.minecraft.LoginStatusPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.PEPacketIDs;
import org.dragonet.net.translator.BaseTranslator;
import org.dragonet.net.translator.TranslatorProvider;

public final class PENetworkClient {
    
    
    @Getter
    private final NioSession raknetSession;
    
    @Getter
    private String username;

    private final JRakLibInterface inf;

    /**
     * The session which this client binds to.
     */
    @Getter
    private MCPESession session;

    public PENetworkClient(JRakLibInterface inf, NioSession raknetSession) {
        this.inf = inf;
        this.raknetSession = raknetSession;
    }

    public void sendPacket(PEPacket packet) {
        sendPacket(packet, 2);
    }

    public void sendPacket(PEPacket packet, int reliability) {
        if (!(packet instanceof PEPacket)) {
            return;
        }
        //TODO
        inf.sendPacket(this, packet, false);
    }

    public void disconnect(String reason) {
        close(reason);
    }

    public void processPacketBuffer(byte[] buffer) {
        PEPacket packet = Protocol.decode(buffer);
        if (packet == null) {
            return;
        }
        System.out.println("Received Packet: " + packet.getClass().getSimpleName());
        switch (packet.pid()) {
            case PEPacketIDs.LOGIN_PACKET:
                LoginPacket packetLogin = (LoginPacket) packet;
                this.username = packetLogin.username;
                
                BaseTranslator translator = TranslatorProvider.getByPEProtocolID(packetLogin.protocol1, packetLogin.protocol2);
                if (!(translator instanceof BaseTranslator)) {
                    LoginStatusPacket pkLoginStatus = new LoginStatusPacket();
                    pkLoginStatus.status = LoginStatusPacket.LOGIN_FAILED_CLIENT;
                    this.sendPacket(pkLoginStatus);
                    this.disconnect("Unsupported game version! ");
                    break;
                }
                
                session = new MCPESession(inf.getManager().getServer(), this, inf, translator);

                LoginStatusPacket pkLoginStatus = new LoginStatusPacket();
                pkLoginStatus.status = LoginStatusPacket.LOGIN_SUCCESS;
                this.sendPacket(pkLoginStatus);

                inf.getManager().getServer().getLogger().info("Accepted connection by [" + this.username + "]. ");

                Matcher matcher = DragonetSession.PATTERN_USERNAME.matcher(this.username);
                if (!matcher.matches()) {
                    this.disconnect("Bad username! ");
                    break;
                }
                
                session.setPlayer(new PlayerProfile(this.username, UUID.nameUUIDFromBytes(("OfflinePlayer:" + this.username).getBytes(StandardCharsets.UTF_8))));
                break;
            case PEPacketIDs.BATCH_PACKET:
                BatchPacket packetBatch = (BatchPacket) packet;
                if (packetBatch.packets == null || packetBatch.packets.isEmpty()) {
                    return;
                }
                for (PEPacket pk : packetBatch.packets) {
                    this.processPacketBuffer(pk.getData());
                }
                break;
            default:
                if (session == null) {
                    disconnect("Network error! ");
                    return;
                }
                session.onPacketReceived(packet);
        }
    }
    
    private void close(String reason){
        this.sendPacket(new DisconnectPacket(reason));
        if(session != null){
            session.disconnect(reason);
            inf.getManager().removeSessionRaw(session);
        }
    }

}
