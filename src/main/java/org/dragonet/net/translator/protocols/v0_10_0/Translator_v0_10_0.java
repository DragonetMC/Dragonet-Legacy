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
import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.message.play.entity.RelativeEntityPositionMessage;
import net.glowstone.net.message.play.entity.SpawnPlayerMessage;
import net.glowstone.net.message.play.game.ChatMessage;
import net.glowstone.net.message.play.game.IncomingChatMessage;
import net.glowstone.net.message.play.game.StateChangeMessage;
import net.glowstone.net.message.play.inv.CloseWindowMessage;
import net.glowstone.net.message.play.inv.OpenWindowMessage;
import net.glowstone.net.message.play.inv.SetWindowContentsMessage;
import net.glowstone.net.message.play.inv.SetWindowSlotMessage;
import net.glowstone.net.message.play.player.PlayerPositionLookMessage;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.InventoryView;
import org.dragonet.entity.DragonetPlayer;
import org.dragonet.entity.metadata.EntityMetaData;
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.inventory.InventoryType;
import org.dragonet.inventory.PEWindowConstantID;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.AddPlayerPacket;
import org.dragonet.net.packet.minecraft.MessagePacket;
import org.dragonet.net.packet.minecraft.MovePlayerPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.PEPacketIDs;
import org.dragonet.net.packet.minecraft.StartGamePacket;
import org.dragonet.net.packet.minecraft.WindowClosePacket;
import org.dragonet.net.packet.minecraft.WindowItemsPacket;
import org.dragonet.net.packet.minecraft.WindowOpenPacket;
import org.dragonet.net.packet.minecraft.WindowSetSlotPacket;
import org.dragonet.net.translator.BaseTranslator;
import org.dragonet.net.translator.ItemTranslator;

public class Translator_v0_10_0 extends BaseTranslator {

    /**
     * Cached Window Types for Window Item Translating If the value equals
     * Integer.MAX_VALUE then the window doesn't exist
     */
    private int[] cachedWindowType;

    private ItemTranslator itemTranslator;

    public Translator_v0_10_0(DragonetSession session) {
        super(session);
        this.cachedWindowType = new int[256];
        for (int i = 1; i < 256; i++) {
            this.cachedWindowType[i] = -1;
        }
        this.itemTranslator = new ItemTranslator_v0_10_0();
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
                //Hack ;P
                ((DragonetPlayer) this.getSession().getPlayer()).setLocation(new Location(((DragonetPlayer) this.getSession().getPlayer()).getWorld(), pkMovePlayer.x, pkMovePlayer.y, pkMovePlayer.z, pkMovePlayer.yaw, pkMovePlayer.pitch));
                return new Message[]{new PlayerPositionLookMessage(false, (double) pkMovePlayer.x, (double) pkMovePlayer.y, (double) pkMovePlayer.z, pkMovePlayer.yaw, pkMovePlayer.pitch)};
        }
        return null;
    }

    /* ===== TO PE ===== */
    @Override
    public PEPacket[] translateToPE(Message message) {
        if (message.getClass().getSimpleName().contains("Player") || message.getClass().getSimpleName().contains("Window")
                || message.getClass().getSimpleName().contains("Chunk")) {
            System.out.print("Trnaslating to PE: " + message.getClass().getSimpleName());
        }

        /* ==================================================================================== */
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
        if (message instanceof RelativeEntityPositionMessage) {
            RelativeEntityPositionMessage msgRelativeEntityPosition = ((RelativeEntityPositionMessage) message);
            Entity entity = this.getSession().getPlayer().getWorld().getEntityManager().getEntity(msgRelativeEntityPosition.id);
            if (entity instanceof GlowPlayer) {
                boolean isTeleport = Math.sqrt(msgRelativeEntityPosition.deltaX ^ 2 + msgRelativeEntityPosition.deltaY ^ 2 + msgRelativeEntityPosition.deltaZ ^ 2) > 2;
                MovePlayerPacket pkMovePlayer = new MovePlayerPacket(msgRelativeEntityPosition.id, (float) entity.getLocation().getX(), (float) entity.getLocation().getY(), (float) entity.getLocation().getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch(), entity.getLocation().getYaw(), isTeleport);
                return new PEPacket[]{pkMovePlayer};
            } else {
                //TODO: Handle other entities
                return null;
            }
        }

        /**
         * Spawn Player
         */
        if (message instanceof SpawnPlayerMessage) {
            SpawnPlayerMessage msgSpawnPlayer = (SpawnPlayerMessage) message;
            AddPlayerPacket pkAddPlayer = new AddPlayerPacket();
            pkAddPlayer.clientID = 0;
            pkAddPlayer.eid = msgSpawnPlayer.getId();
            pkAddPlayer.username = this.getSession().getServer().getPlayer(msgSpawnPlayer.getUuid()).getDisplayName();
            pkAddPlayer.x = (float) msgSpawnPlayer.getX();
            pkAddPlayer.y = (float) msgSpawnPlayer.getY();
            pkAddPlayer.z = (float) msgSpawnPlayer.getZ();
            pkAddPlayer.yaw = (msgSpawnPlayer.getRotation() % 360 + 360) % 360;
            pkAddPlayer.pitch = msgSpawnPlayer.getPitch();
            pkAddPlayer.unknown1 = 0;
            pkAddPlayer.unknown2 = 0;
            pkAddPlayer.metadata = EntityMetaData.getMetaDataFromPlayer((GlowPlayer) this.getSession().getPlayer().getWorld().getEntityManager().getEntity(msgSpawnPlayer.getId()));
            return new PEPacket[]{pkAddPlayer};
        }

        /**
         * Gamemode Change
         */
        if (message instanceof StateChangeMessage) {
            if (((StateChangeMessage) message).reason == StateChangeMessage.Reason.GAMEMODE.ordinal()) {
                StartGamePacket pkStartGame = new StartGamePacket();
                pkStartGame.eid = this.getSession().getPlayer().getEntityId();
                pkStartGame.gamemode = ((int) ((StateChangeMessage) message).value) & 0x1;
                pkStartGame.seed = 0;
                pkStartGame.generator = 1;
                pkStartGame.spawnX = this.getSession().getPlayer().getWorld().getSpawnLocation().getBlockX();
                pkStartGame.spawnY = this.getSession().getPlayer().getWorld().getSpawnLocation().getBlockY();
                pkStartGame.spawnZ = this.getSession().getPlayer().getWorld().getSpawnLocation().getBlockZ();
                pkStartGame.x = (float) this.getSession().getPlayer().getLocation().getX();
                pkStartGame.y = (float) this.getSession().getPlayer().getLocation().getY();
                pkStartGame.z = (float) this.getSession().getPlayer().getLocation().getZ();
                return new PEPacket[]{pkStartGame};
            }
        }

        /**
         * Open Window
         */
        if (message instanceof OpenWindowMessage) {
            OpenWindowMessage msgOpenWindow = (OpenWindowMessage) message;
            byte typePE = InventoryType.PEInventory.toPEInventory(InventoryType.PCInventory.fromString(msgOpenWindow.type), msgOpenWindow.slots);
            if (typePE == (byte) 0xFF) {
                //Not supported, close it
                CloseWindowMessage msgCloseWindow = new CloseWindowMessage(msgOpenWindow.id);
                this.getSession().messageReceived(msgCloseWindow);
                return null;
            }
            WindowOpenPacket pkOpenWindow = new WindowOpenPacket();
            pkOpenWindow.windowID = (byte) (msgOpenWindow.id & 0xFF);
            pkOpenWindow.type = typePE;
            pkOpenWindow.slots = (byte) (msgOpenWindow.slots & 0xFFFF);
            pkOpenWindow.x = this.getSession().getPlayer().getLocation().getBlockX();
            pkOpenWindow.y = this.getSession().getPlayer().getLocation().getBlockY();
            pkOpenWindow.z = this.getSession().getPlayer().getLocation().getBlockZ();
            this.cachedWindowType[msgOpenWindow.id & 0xFF] = typePE;
            return new PEPacket[]{pkOpenWindow};
        }

        /**
         * Set Window Items
         */
        if (message instanceof SetWindowContentsMessage) {
            SetWindowContentsMessage msgWindowContents = (SetWindowContentsMessage) message;
            if (msgWindowContents.id == 0) {
                //Inventory Items(Included hotbar)
                WindowItemsPacket pkInventory = new WindowItemsPacket();
                pkInventory.windowID = PEWindowConstantID.PLAYER_INVENTORY;
                pkInventory.slots = new PEInventorySlot[InventoryType.SlotSize.PLAYER];
                for (int i = 9; i <= 44; i++) {
                    if (msgWindowContents.items[i] != null) {
                        pkInventory.slots[i - 9] = new PEInventorySlot((short) (msgWindowContents.items[i].getTypeId() & 0xFFFF), (byte) (msgWindowContents.items[i].getAmount() & 0xFF), msgWindowContents.items[i].getDurability());
                    } else {
                        pkInventory.slots[i - 9] = new PEInventorySlot();
                    }
                }
                pkInventory.hotbar = new PEInventorySlot[9];
                for (int i = 36; i <= 44; i++) {
                    if (msgWindowContents.items[i] != null) {
                        pkInventory.hotbar[i - 36] = new PEInventorySlot((short) (msgWindowContents.items[i].getTypeId() & 0xFFFF), (byte) (msgWindowContents.items[i].getAmount() & 0xFF), msgWindowContents.items[i].getDurability());
                    } else {
                        pkInventory.hotbar[i - 36] = new PEInventorySlot();
                    }
                }
                //Armor
                WindowItemsPacket pkArmorInv = new WindowItemsPacket();
                pkArmorInv.windowID = PEWindowConstantID.PLAYER_ARMOR;
                pkArmorInv.slots = new PEInventorySlot[4];
                for (int i = 5; i <= 8; i++) {
                    if (msgWindowContents.items[i] != null) {
                        pkArmorInv.slots[i - 5] = new PEInventorySlot((short) (msgWindowContents.items[i].getTypeId() & 0xFFFF), (byte) (msgWindowContents.items[i].getAmount() & 0xFF), msgWindowContents.items[i].getDurability());
                    } else {
                        pkArmorInv.slots[i - 5] = new PEInventorySlot();
                    }
                }
                if (this.getSession().getSentAndReceivedChunks() != -1) {
                    //Not fully loaded
                    this.getSession().getQueueAfterChunkSent().add(pkInventory);
                    this.getSession().getQueueAfterChunkSent().add(pkArmorInv);
                } else {
                    return new PEPacket[]{pkInventory, pkArmorInv};
                }
                return null;
            }
            //TODO: Implement other types of inventory
            //switch(this.getSession().getPlayer().)
            System.out.println("Updating window content for " + msgWindowContents.id + ", which has " + msgWindowContents.items.length + " slots. ");
        }

        /**
         * Set Window Slot
         */
        if (message instanceof SetWindowSlotMessage) {
            SetWindowSlotMessage msgSetSlot = (SetWindowSlotMessage) message;
            if (this.cachedWindowType[msgSetSlot.id & 0xFF] == -1) {
                return null;
            }
            //byte typePE = (byte) (this.cachedWindowType[msgSetSlot.id & 0xFF] & 0xFF);
            int targetSlot = msgSetSlot.slot; //For now the slot ids are same so we use this directly. 
            WindowSetSlotPacket pkSetSlot = new WindowSetSlotPacket();
            pkSetSlot.windowID = (byte) (msgSetSlot.id & 0xFF);
            pkSetSlot.slot = (short) (targetSlot & 0xFFFF);
            pkSetSlot.item = new PEInventorySlot((short) (msgSetSlot.item.getTypeId() & 0xFFFF), (byte) (msgSetSlot.item.getAmount() & 0xFF), msgSetSlot.item.getDurability());
            return new PEPacket[]{pkSetSlot};
        }

        /**
         * Close Window Slot
         */
        if (message instanceof CloseWindowMessage) {
            CloseWindowMessage msgCloseWindow = (CloseWindowMessage) message;
            if (msgCloseWindow.id != 0) {
                this.cachedWindowType[msgCloseWindow.id & 0xFF] = -1;
            }
            WindowClosePacket pkCloseWindow = new WindowClosePacket();
            pkCloseWindow.windowID = (byte) (msgCloseWindow.id & 0xFF);
            return new PEPacket[]{pkCloseWindow};
        }

        /* ==================================================================================== */
        return null;
    }

    @Override
    public ItemTranslator getItemTranslator() {
        return this.itemTranslator;
    }

}
