/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
            new DiggingHandler().handle(getSession(), msgFinishBreak);
        }
        return null;
    }

}
