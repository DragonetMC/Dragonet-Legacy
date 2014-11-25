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
import net.glowstone.net.message.play.game.ChunkBulkMessage;
import net.glowstone.net.message.play.game.ChunkDataMessage;
import org.dragonet.ChunkLocation;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.translator.BaseTranslator;

public class Translator_v0_10_0 extends BaseTranslator {
    private HashMap<Integer, Integer> blockMap = new HashMap<>();
    
    
    public Translator_v0_10_0(DragonetSession session) {
        super(session);
        for(int i = 0; i <= 24; i++){
            blockMap.put(i, i);
        }
        blockMap.put(26, 26);
        blockMap.put(27, 27);
        blockMap.put(31, 31);
        blockMap.put(50, 50);
    }

    @Override
    public Message[] translateToPC(PEPacket packet) {
        System.out.print("Trnaslating to PC: " + packet.getClass().getSimpleName());
        //TODO
        return null;
    }

    @Override
    public PEPacket[] translateToPE(Message message) {
        //System.out.print("Trnaslating to PE: " + message.getClass().getSimpleName());
        
        /* ==================================================================================== */
        
        /***
         * Chunk Bulk Message
         */
        if (message instanceof ChunkBulkMessage) {
            for (ChunkDataMessage dataMsg : ((ChunkBulkMessage)message).getEntries()) {
                this.getSession().getChunkManager().prepareChunk(new ChunkLocation(dataMsg.x, dataMsg.z));
            }
            return null;
        }
        
        /* ==================================================================================== */
        return null;
    }

    @Override
    public int translateBlockToPE(int pcBlock) {
        if(this.blockMap.containsKey(pcBlock)) return this.blockMap.get(pcBlock);
        return 7;
    }

}
