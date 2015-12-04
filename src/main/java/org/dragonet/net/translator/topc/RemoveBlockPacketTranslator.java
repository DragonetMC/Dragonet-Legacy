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
package org.dragonet.net.translator.topc;

import com.flowpowered.networking.Message;
import net.glowstone.EventFactory;
import net.glowstone.GlowWorld;
import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.handler.play.player.DiggingHandler;
import net.glowstone.net.message.play.player.DiggingMessage;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.RemoveBlockPacket;
import org.dragonet.net.translator.PEPacketTranslatorToPC;
import org.dragonet.net.translator.Translator_v0_11;

public class RemoveBlockPacketTranslator extends PEPacketTranslatorToPC<Translator_v0_11, RemoveBlockPacket> {

    public RemoveBlockPacketTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public Message[] handleSpecific(RemoveBlockPacket packet) {
        if (!(this.getSession().getPlayer() instanceof Player)) {
            return null;
        }

        if (getSession().getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            final GlowPlayer player = getSession().getPlayer();
            GlowWorld world = player.getWorld();
            GlowBlock block = world.getBlockAt(packet.x, packet.y, packet.z);
            PlayerInteractEvent interactEvent = EventFactory.onPlayerInteract(player, Action.LEFT_CLICK_BLOCK, block, BlockFace.UP);
            if(interactEvent.isCancelled()){
                player.sendBlockChange(block.getLocation(), block.getType(), block.getData());
                return null;
            }
            block.setType(Material.AIR);
        } else {
            //Not Creative
            DiggingMessage msgFinishBreak = new DiggingMessage(DiggingMessage.FINISH_DIGGING, packet.x, packet.y, packet.z, 1);
            return new Message[]{msgFinishBreak};
        }
        return null;
    }

}
