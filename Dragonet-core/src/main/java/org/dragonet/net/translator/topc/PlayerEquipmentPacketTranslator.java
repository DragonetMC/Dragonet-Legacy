/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.net.translator.topc;

import com.flowpowered.networking.Message;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.PlayerEquipmentPacket;
import org.dragonet.net.translator.PEPacketTranslatorToPC;
import org.dragonet.net.translator.Translator_v0_11;

public class PlayerEquipmentPacketTranslator extends PEPacketTranslatorToPC<Translator_v0_11, PlayerEquipmentPacket> {

    public PlayerEquipmentPacketTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public Message[] handleSpecific(PlayerEquipmentPacket packet) {
        PlayerEquipmentPacket pkEquipment = (PlayerEquipmentPacket) packet;
        if (pkEquipment.slot == 0x28 || pkEquipment.slot == 0 || pkEquipment.slot == 255) {
            if (this.getSession().getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                this.getSession().getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR));
                return null;
            } else {
                if (this.getSession().getPlayer().getInventory().firstEmpty() == -1) {
                    this.getSession().sendInventory();
                    this.getSession().getPlayer().sendMessage("Your inventory is full, you can't hold nothing. ");
                } else {
                    this.getSession().getPlayer().getInventory().setHeldItemSlot(this.getSession().getPlayer().getInventory().firstEmpty());
                }
            }
            return null;
        }
        if (this.getSession().getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            this.getSession().getPlayer().getInventory().setItemInHand(new ItemStack(pkEquipment.item, 1, pkEquipment.meta, (byte) 0));
            return null;
        }
        int slot = pkEquipment.slot - 9;
        if (slot < 27) { //Normal Inventory
            //Swap
            ItemStack item = this.getSession().getPlayer().getInventory().getItem(slot + 9);
            this.getSession().getPlayer().getInventory().setItem(slot + 9, this.getSession().getPlayer().getInventory().getItem(0));
            this.getSession().getPlayer().getInventory().setItem(0, item);
            if (item.getAmount() <= 0) {
                this.getSession().getPlayer().getInventory().setItem(slot + 9, null);
                this.getSession().sendInventory();
                return null;
            }
            if (item.getTypeId() == pkEquipment.item) {
                this.getSession().getPlayer().getInventory().setHeldItemSlot(slot + 9);
            }
            this.getSession().sendInventory();
        } else if (slot >= 27) { //Hotbar
            ItemStack item = this.getSession().getPlayer().getInventory().getItem(slot - 27);
            if (item.getAmount() <= 0) {
                this.getSession().getPlayer().getInventory().setItem(slot - 27, null);
                this.getSession().sendInventory();
                return null;
            }
            if (item.getTypeId() == pkEquipment.item) {
                this.getSession().getPlayer().getInventory().setHeldItemSlot(slot - 27);
            } else {
                this.getSession().sendInventory();
            }
        }
        return null;
    }

}
