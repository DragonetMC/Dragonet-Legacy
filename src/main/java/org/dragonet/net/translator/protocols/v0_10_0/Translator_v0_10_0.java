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
package org.dragonet.net.translator.protocols.v0_10_0;

import com.flowpowered.networking.Message;
import java.util.HashMap;
import net.glowstone.net.message.play.game.ChatMessage;
import net.glowstone.net.message.play.game.ChunkBulkMessage;
import net.glowstone.net.message.play.game.ChunkDataMessage;
import net.glowstone.net.message.play.game.IncomingChatMessage;
import net.glowstone.net.message.play.player.PlayerPositionLookMessage;
import org.dragonet.ChunkLocation;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.MessagePacket;
import org.dragonet.net.packet.minecraft.MovePlayerPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.PEPacketIDs;
import org.dragonet.net.translator.BaseTranslator;

public class Translator_v0_10_0 extends BaseTranslator {

    private HashMap<Integer, Integer> blockMap = new HashMap<>();

    public Translator_v0_10_0(DragonetSession session) {
        super(session);
        for (int i = 0; i <= 24; i++) {
            blockMap.put(i, i);
        }
        blockMap.put(26, 26);
        blockMap.put(27, 27);
        blockMap.put(31, 31);
        blockMap.put(50, 50);
    }

    /* ===== TO PC ===== */
    @Override
    public Message[] translateToPC(PEPacket packet) {
        //System.out.print("Trnaslating to PC: " + packet.getClass().getSimpleName());
        switch (packet.pid()) {
            case PEPacketIDs.MESSAGE_PACKET:
                IncomingChatMessage msgMessage = new IncomingChatMessage(((MessagePacket) packet).message);
                return new Message[]{msgMessage};
            case PEPacketIDs.MOVE_PLAYER_PACKET:
                MovePlayerPacket pkMovePlayer = (MovePlayerPacket) packet;
                return new Message[]{new PlayerPositionLookMessage(true, (double) pkMovePlayer.x, (double) pkMovePlayer.y, (double) pkMovePlayer.z, pkMovePlayer.yaw, pkMovePlayer.pitch)};
        }
        return null;
    }

    /* ===== TO PE ===== */
    @Override
    public PEPacket[] translateToPE(Message message) {
        if (message.getClass().getSimpleName().contains("Player") || message.getClass().getSimpleName().contains("Position")
                || message.getClass().getSimpleName().contains("Chunk")) {
            System.out.print("Trnaslating to PE: " + message.getClass().getSimpleName());
        }

        /* ==================================================================================== */
        /**
         * Chunk Bulk Message
         */
        if (message instanceof ChunkBulkMessage) {
            for (ChunkDataMessage dataMsg : ((ChunkBulkMessage) message).getEntries()) {
                this.getSession().getChunkManager().prepareChunk(new ChunkLocation(dataMsg.x, dataMsg.z));
            }
            return null;
        }

        /**
         * Chat Message
         */
        if (message instanceof ChatMessage) {
            String msg = ((ChatMessage) message).text.asPlaintext();
            MessagePacket pkMessage = new MessagePacket();
            pkMessage.sender = "";
            pkMessage.message = msg;
            return new PEPacket[]{pkMessage};
        }

        /**
         * Position Update
         */
        //if(message instanceof Player)
        /* ==================================================================================== */
        return null;
    }

    @Override
    public int translateBlockToPE(int pcBlock) {
        if (this.blockMap.containsKey(pcBlock)) {
            return this.blockMap.get(pcBlock);
        }
        return 7;
    }

}
