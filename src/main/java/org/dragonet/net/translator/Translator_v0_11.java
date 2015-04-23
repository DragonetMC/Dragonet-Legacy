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
package org.dragonet.net.translator;

import com.flowpowered.networking.Message;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.message.KickMessage;
import net.glowstone.net.message.play.entity.AnimateEntityMessage;
import net.glowstone.net.message.play.entity.CollectItemMessage;
import net.glowstone.net.message.play.entity.DestroyEntitiesMessage;
import net.glowstone.net.message.play.entity.EntityEquipmentMessage;
import net.glowstone.net.message.play.entity.EntityMetadataMessage;
import net.glowstone.net.message.play.entity.EntityVelocityMessage;
import net.glowstone.net.message.play.entity.RelativeEntityPositionMessage;
import net.glowstone.net.message.play.entity.SpawnObjectMessage;
import net.glowstone.net.message.play.entity.SpawnPlayerMessage;
import net.glowstone.net.message.play.game.BlockChangeMessage;
import net.glowstone.net.message.play.game.ChatMessage;
import net.glowstone.net.message.play.game.MultiBlockChangeMessage;
import net.glowstone.net.message.play.game.PositionRotationMessage;
import net.glowstone.net.message.play.game.StateChangeMessage;
import net.glowstone.net.message.play.game.TimeMessage;
import net.glowstone.net.message.play.inv.CloseWindowMessage;
import net.glowstone.net.message.play.inv.OpenWindowMessage;
import net.glowstone.net.message.play.inv.SetWindowContentsMessage;
import net.glowstone.net.message.play.inv.SetWindowSlotMessage;
import net.glowstone.net.message.play.player.PlayerPositionLookMessage;
import net.glowstone.net.message.play.player.PlayerPositionMessage;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.dragonet.entity.metadata.EntityMetaData;
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.inventory.InventoryType;
import org.dragonet.inventory.ItemList;
import org.dragonet.inventory.PEWindowConstantID;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.*;
import org.dragonet.net.translator.topc.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Translator_v0_11 extends BaseTranslator {

    private static ConcurrentMap<Class<? extends Message>, MessageTranslatorToPE<Translator_v0_11, ? extends Message>> mapToPE;
    private static ConcurrentMap<Class<? extends PEPacket>, PEPacketTranslatorToPC<Translator_v0_11, ? extends PEPacket>> mapToPC;

    /**
     * Cached Window Types for Window Item Translating If the value equals
     * Integer.MAX_VALUE then the window doesn't exist
     */
    public int[] cachedWindowType;

    /**
     * Cached Entity Spawn Messages in order to wait for the meta data
     */
    public ConcurrentHashMap<Integer, SpawnObjectMessage> cachedSpawnObjects;

    /**
     * Spawned Objects, for traking which entity creation message was sent
     */
    public ArrayList<Integer> cachedEntityIDs;

    /**
     * Client's special behavior to players, can not be treated like a normal
     * entity
     */
    public ArrayList<Integer> cachedPlayerEntities;

    public ItemTranslator itemTranslator;

    public Translator_v0_11(DragonetSession session) {
        super(session);
        this.cachedWindowType = new int[256];
        this.cachedWindowType[0] = 0;
        for (int i = 1; i < 256; i++) {
            this.cachedWindowType[i] = -1;
        }
        this.cachedSpawnObjects = new ConcurrentHashMap<>();
        this.cachedEntityIDs = new ArrayList<>();
        this.cachedPlayerEntities = new ArrayList<>();
        this.itemTranslator = new ItemTranslator_v0_11();

        mapToPE = new ConcurrentHashMap<>();
        mapToPC = new ConcurrentHashMap<>();

        // [PC => PE]
        // [PE => PC]
        mapToPC.put(AnimatePacket.class, new AnimatePacketTranslator(this, this.getSession()));
        mapToPC.put(ChatPacket.class, new ChatPacketTranslator(this, this.getSession()));
        mapToPC.put(MovePlayerPacket.class, new MovePlayerPacketTranslator(this, this.getSession()));
        mapToPC.put(PlayerEquipmentPacket.class, new PlayerEquipmentPacketTranslator(this, this.getSession()));
        mapToPC.put(RemoveBlockPacket.class, new RemoveBlockPacketTranslator(this, this.getSession()) );
        mapToPC.put(UseItemPacket.class, new UseItemPacketTranslator(this, this.getSession()));
        mapToPC.put(WindowSetSlotPacket.class, new WindowSetSlotPacketTranslator(this, this.getSession()));
    }

    /* ===== TO PC ===== */
    @Override
    public Message[] translateToPC(PEPacket packet) {
        if(mapToPC.containsKey(packet.getClass())){
            return mapToPC.get(packet.getClass()).handle(packet);
        }else{
            return null;
        }
    }

    static BlockFace convertFace(int direction) {
        if (direction >= 0 && direction < faces.length) {
            return faces[direction];
        } else {
            return BlockFace.SELF;
        }
    }

    private static final BlockFace[] faces = {
        BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST
    };

    /* ===== TO PE ===== */
    @Override
    public PEPacket[] translateToPE(Message message) {
        /*
         if (!message.getClass().getSimpleName().contains("Time") && !message.getClass().getSimpleName().contains("Chunk")
         && !message.getClass().getSimpleName().contains("Move")) {
         System.out.print("Trnaslating to PE: " + message.getClass().getSimpleName() + "\nDetail: " + message.toString());
         }
         /*

         /* ==================================================================================== */
        /**
         * Kick Message
         */
        if (message instanceof KickMessage) {
            String msg = "";
            try {
                //String msg = ((ChatMessage) message).text.asPlaintext();
                Object json = new JSONParser().parse(((KickMessage) message).text.encode());
                if (json instanceof JSONObject) {
                    msg = this.translateChatMessage((JSONObject) json);
                } else {
                    msg = ((KickMessage) message).text.asPlaintext();
                }
            } catch (ParseException ex) {
                return null;
            }
            return new PEPacket[]{new DisconnectPacket(msg)};
        }

        /**
         * Chat Message
         */
        if (message instanceof ChatMessage) {
            String msg = "";
            try {
                //String msg = ((ChatMessage) message).text.asPlaintext();
                Object json = new JSONParser().parse(((ChatMessage) message).text.encode());
                if (json instanceof JSONObject) {
                    msg = this.translateChatMessage((JSONObject) json);
                } else {
                    msg = ((ChatMessage) message).text.asPlaintext();
                }
            } catch (ParseException ex) {
                return null;
            }
            //if(json)
            ChatPacket pkMessage = new ChatPacket();
            pkMessage.source = "";
            pkMessage.type = ChatPacket.TextType.RAW;
            pkMessage.message = msg;
            return new PEPacket[]{pkMessage};
        }

        /**
         * Position Update
         */
        if (message instanceof RelativeEntityPositionMessage) {
            RelativeEntityPositionMessage msgRelativeEntityPosition = ((RelativeEntityPositionMessage) message);
            Entity entity = this.getSession().getPlayer().getWorld().getEntityManager().getEntity(msgRelativeEntityPosition.id);
            if (entity == null) {
                return null;
            }
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
         * Force Update Client Position & Rotation TODO: WHY THE FUCK DOESN'T IT
         * WORK!
         */
        if (message instanceof PositionRotationMessage) {
            if (this.getSession().getPlayer() == null) {
                return null;
            }
            PositionRotationMessage msgPosRot = (PositionRotationMessage) message;
            //Hack: Yaw and pitch set to 0.0f
            //MovePlayerPacket pkMovePlayer = new MovePlayerPacket(this.getSession().getPlayer().getEntityId(), (float) msgPosRot.x, (float) msgPosRot.y, (float) msgPosRot.z, 0.0f, 0.0f, 0.0f, true);
            MoveEntitiesPacket.MoveEntityData d = new MoveEntitiesPacket.MoveEntityData();
            d.x = (float) msgPosRot.x;
            d.y = (float) msgPosRot.y;
            d.z = (float) msgPosRot.z;
            d.yaw = 0.0f;
            d.pitch = 0.0f;
            MoveEntitiesPacket pkMoveEntity = new MoveEntitiesPacket(new MoveEntitiesPacket.MoveEntityData[]{d});
            return new PEPacket[]{pkMoveEntity};
        }

        /**
         * Repositioning Player
         */
        if (message instanceof PlayerPositionMessage) {
            PlayerPositionMessage msgPlayerPos = (PlayerPositionMessage) message;
            MovePlayerPacket pkMovePlayer = new MovePlayerPacket();
            pkMovePlayer.eid = this.getSession().getPlayer().getEntityId();
            pkMovePlayer.x = (float) msgPlayerPos.x;
            pkMovePlayer.y = (float) msgPlayerPos.y;
            pkMovePlayer.z = (float) msgPlayerPos.z;
            pkMovePlayer.yaw = this.getSession().getPlayer().getLocation().getYaw();
            pkMovePlayer.pitch = this.getSession().getPlayer().getLocation().getPitch();
            pkMovePlayer.teleport = true;
            return new PEPacket[]{pkMovePlayer};
        }
        if (message instanceof PlayerPositionLookMessage) {
            PlayerPositionLookMessage msgPlayerLookPos = (PlayerPositionLookMessage) message;
            MovePlayerPacket pkMovePlayer = new MovePlayerPacket();
            pkMovePlayer.eid = this.getSession().getPlayer().getEntityId();
            pkMovePlayer.x = (float) msgPlayerLookPos.x;
            pkMovePlayer.y = (float) msgPlayerLookPos.y;
            pkMovePlayer.z = (float) msgPlayerLookPos.z;
            pkMovePlayer.yaw = msgPlayerLookPos.yaw;
            pkMovePlayer.pitch = msgPlayerLookPos.pitch;
            pkMovePlayer.teleport = true;
            return new PEPacket[]{pkMovePlayer};
        }

        /**
         * Spawn Player
         */
        if (message instanceof SpawnPlayerMessage) {
            SpawnPlayerMessage msgSpawnPlayer = (SpawnPlayerMessage) message;
            if (!this.cachedEntityIDs.contains(msgSpawnPlayer.id)) {
                this.cachedEntityIDs.add(msgSpawnPlayer.id); //Add to the spawned entity list
            }
            this.cachedPlayerEntities.add(msgSpawnPlayer.id); //Register this id as a player
            AddPlayerPacket pkAddPlayer = new AddPlayerPacket();
            pkAddPlayer.clientID = 0;
            pkAddPlayer.eid = msgSpawnPlayer.getId();
            pkAddPlayer.username = this.getSession().getServer().getPlayer(msgSpawnPlayer.getUuid()).getDisplayName();
            pkAddPlayer.x = (float) msgSpawnPlayer.getX();
            pkAddPlayer.y = (float) msgSpawnPlayer.getY();
            pkAddPlayer.z = (float) msgSpawnPlayer.getZ();
            pkAddPlayer.speedX = 0.0f;
            pkAddPlayer.speedY = 0.0f;
            pkAddPlayer.speedZ = 0.0f;
            pkAddPlayer.yaw = msgSpawnPlayer.getRotation();
            pkAddPlayer.pitch = msgSpawnPlayer.getPitch();
            pkAddPlayer.skin = String.format("http://s3.amazonaws.com/MinecraftSkins/%s.png", pkAddPlayer.username);
            pkAddPlayer.slim = true;
            pkAddPlayer.metadata = EntityMetaData.getMetaDataFromPlayer((GlowPlayer) this.getSession().getPlayer().getWorld().getEntityManager().getEntity(msgSpawnPlayer.getId()));
            return new PEPacket[]{pkAddPlayer};
        }

        /**
         * Motion Change
         */
        if (message instanceof EntityVelocityMessage) {
            EntityVelocityMessage msgVelocity = (EntityVelocityMessage) message;
            SetEntityMotionPacket pkMotion = new SetEntityMotionPacket();
            SetEntityMotionPacket.EntityMotionData data = new SetEntityMotionPacket.EntityMotionData();
            data.eid = msgVelocity.id;
            data.motionX = msgVelocity.velocityX;
            data.motionY = msgVelocity.velocityY;
            data.motionZ = msgVelocity.velocityZ;
            pkMotion.motions = new SetEntityMotionPacket.EntityMotionData[]{data};
            return new PEPacket[]{pkMotion};
        }

        /**
         * Gamemode Change
         */
        if (message instanceof StateChangeMessage) {
            if (((StateChangeMessage) message).reason == StateChangeMessage.Reason.GAMEMODE.ordinal()) {
                if (this.getSession().getPlayer() == null) {
                    return null;
                }
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
                pkInventory.hotbar = new int[9];
                for (int i = 36; i <= 44; i++) {
                    pkInventory.hotbar[i - 36] = i - 9;
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
            //NOT WORKING YET
            /*
             //System.out.println("Updating slot: WID=" + msgSetSlot.id + ", ITEM=" + msgSetSlot.item + ", SLOTID=" + msgSetSlot.slot);
             //byte typePE = (byte) (this.cachedWindowType[msgSetSlot.id & 0xFF] & 0xFF);
             int targetSlot = 0;
             if (msgSetSlot.id == 0) {
             if (msgSetSlot.slot >= 9 && msgSetSlot.slot <= 35) {
             targetSlot = (short) (msgSetSlot.slot - 9);
             } else if (msgSetSlot.slot >= 36 && msgSetSlot.slot <= 44) {
             targetSlot = (short) (msgSetSlot.slot - 36);
             } else {
             targetSlot = (short) (msgSetSlot.slot & 0xFFFF);
             }
             } else {
             targetSlot = (short) (msgSetSlot.slot & 0xFFFF);
             }
             WindowSetSlotPacket pkSetSlot = new WindowSetSlotPacket();
             pkSetSlot.windowID = (byte) (msgSetSlot.id & 0xFF);
             pkSetSlot.slot = (short) (targetSlot & 0xFFFF);
             pkSetSlot.item = new PEInventorySlot((short) (msgSetSlot.item.getTypeId() & 0xFFFF), (byte) (msgSetSlot.item.getAmount() & 0xFF), msgSetSlot.item.getDurability());
             return new PEPacket[]{pkSetSlot};
             */
            if (msgSetSlot.id == 0) {
                //Player Inventory
                this.getSession().sendInventory();
                return null;
            } else {
                //TODO
                //this.getSession().getPlayer().getOpenInventory().getTopInventory()
            }
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

        /**
         * Set time
         */
        if (message instanceof TimeMessage) {
            SetTimePacket pkTime = new SetTimePacket(0, (this.getSession().getSentAndReceivedChunks() == -1)); //Because of the hack, we use 0 here. 
            return new PEPacket[]{pkTime};
        }

        /**
         * Block Change
         */
        if (message instanceof BlockChangeMessage) {
            BlockChangeMessage msgBC = (BlockChangeMessage) message;
            UpdateBlockPacket pkBC = new UpdateBlockPacket();
            UpdateBlockPacket.UpdateBlockRecord rec = new UpdateBlockPacket.UpdateBlockRecord();
            rec.x = msgBC.x;
            rec.z = msgBC.z;
            rec.y = (byte) (msgBC.y & 0xFF);
            rec.block = (byte) (this.itemTranslator.translateToPE(msgBC.type >> 4) & 0xFF);
            rec.meta = (byte) (msgBC.type & 0xFF);
            pkBC.records = new UpdateBlockPacket.UpdateBlockRecord[]{rec};
            return new PEPacket[]{pkBC};
        }

        /**
         * Multi Block Change
         */
        if (message instanceof MultiBlockChangeMessage) {
            MultiBlockChangeMessage msgMBC = (MultiBlockChangeMessage) message;
            UpdateBlockPacket pkBC = new UpdateBlockPacket();
            pkBC.records = new UpdateBlockPacket.UpdateBlockRecord[msgMBC.records.size()];
            //PEPacket[] packets = new PEPacket[msgMBC.records.size()];
            int i = 0;
            for (BlockChangeMessage msgBC : msgMBC.records) {
                //packets[i] = this.translateToPE(msgBC)[0];
                pkBC.records[i] = new UpdateBlockPacket.UpdateBlockRecord();
                pkBC.records[i].x = msgBC.x;
                pkBC.records[i].z = msgBC.z;
                pkBC.records[i].y = (byte) (msgBC.y & 0xFF);
                pkBC.records[i].block = (byte) (this.itemTranslator.translateToPE(msgBC.type >> 4) & 0xFF);
                pkBC.records[i].meta = (byte) (msgBC.type & 0xFF);
                i++;
            }
            return new PEPacket[]{pkBC};
        }

        /**
         * Spawn Object
         */
        if (message instanceof SpawnObjectMessage) {
            SpawnObjectMessage msgObj = (SpawnObjectMessage) message;
            this.cachedSpawnObjects.put(msgObj.id, msgObj); //Cache it first :P 
        }

        /**
         * Entity Metadata
         */
        if (message instanceof EntityMetadataMessage) {
            EntityMetadataMessage msgEntityMeta = (EntityMetadataMessage) message;
            if (!this.cachedEntityIDs.contains(msgEntityMeta.id)) { //Not spawned yet, let's create them
                if (this.cachedSpawnObjects.containsKey(msgEntityMeta.id)) {
                    //Spawn it :) 
                    SpawnObjectMessage msgObj = this.cachedSpawnObjects.get(msgEntityMeta.id);
                    this.cachedSpawnObjects.remove(msgEntityMeta.id); //Remove it
                    switch (msgObj.type) {
                        case 2: //Dropped Item
                            AddItemEntityPacket pkAddItemEntity = new AddItemEntityPacket();
                            pkAddItemEntity.eid = msgObj.id;
                            pkAddItemEntity.item = new PEInventorySlot((short) (((ItemStack) msgEntityMeta.entries.get(0).value).getTypeId() & 0xFFFF), (byte) (((ItemStack) msgEntityMeta.entries.get(0).value).getAmount() & 0xFF), (short) (((ItemStack) msgEntityMeta.entries.get(0).value).getDurability() & 0xFFFF));
                            pkAddItemEntity.x = (float) (msgObj.x / 32);
                            pkAddItemEntity.y = (float) (msgObj.y / 32);
                            pkAddItemEntity.z = (float) (msgObj.z / 32);
                            return new PEPacket[]{pkAddItemEntity};
                    }
                } else {
                    return null;
                }
            }
        }

        /**
         * Remove Entities
         */
        if (message instanceof DestroyEntitiesMessage) {
            DestroyEntitiesMessage pkDestroy = (DestroyEntitiesMessage) message;
            this.cachedEntityIDs.removeAll(pkDestroy.ids); //Remove from the list
            int[] ids = ArrayUtils.toPrimitive(pkDestroy.ids.toArray(new Integer[0]));
            PEPacket[] pkRemoveEntity = new PEPacket[ids.length];
            for (int i = 0; i < ids.length; i++) {
                if (!this.cachedPlayerEntities.contains(ids[i])) {
                    pkRemoveEntity[i] = new RemoveEntityPacket();
                    ((RemoveEntityPacket) pkRemoveEntity[i]).eid = ids[i];
                } else {
                    pkRemoveEntity[i] = new RemovePlayerPacket();
                    ((RemovePlayerPacket) pkRemoveEntity[i]).clientID = 0;
                    ((RemovePlayerPacket) pkRemoveEntity[i]).eid = ids[i];
                    this.cachedPlayerEntities.remove(new Integer(ids[i]));
                }
            }
            return pkRemoveEntity;
        }

        /**
         * Collect Item
         */
        if (message instanceof CollectItemMessage) {
            CollectItemMessage msgCollectItem = (CollectItemMessage) message;
            PickUpItemPacket pkPickUp = new PickUpItemPacket();
            pkPickUp.target = msgCollectItem.collector;
            pkPickUp.eid = msgCollectItem.id;
            return new PEPacket[]{pkPickUp};
        }

        /**
         * Animate Packet
         */
        if (message instanceof AnimateEntityMessage) {
            AnimateEntityMessage msgAnimate = (AnimateEntityMessage) message;
            AnimatePacket pkAnimate = new AnimatePacket();
            pkAnimate.eid = msgAnimate.id;
            pkAnimate.action = (byte) 0x01; //(msgAnimate.animation & 0xFF);
            return new PEPacket[]{pkAnimate};
        }

        /**
         * Entity Equipment
         */
        if (message instanceof EntityEquipmentMessage) {
            EntityEquipmentMessage msgEquipment = (EntityEquipmentMessage) message;
            if (!(this.getSession().getPlayer().getWorld().getEntityManager().getEntity(msgEquipment.id) instanceof Player)) {
                return null;
            }
            switch (msgEquipment.slot) {
                case 0: //Held Item
                    PlayerEquipmentPacket pkEquipment = new PlayerEquipmentPacket();
                    pkEquipment.eid = msgEquipment.id;
                    if (msgEquipment.stack != null) {
                        pkEquipment.item = (short) (msgEquipment.stack.getTypeId() & 0xFFFF);
                        pkEquipment.meta = (short) (msgEquipment.stack.getDurability() & 0xFFFF);
                    } else {
                        pkEquipment.item = 0;
                        pkEquipment.meta = 0;
                    }
                    pkEquipment.slot = (byte) 0;
                    return new PEPacket[]{pkEquipment};
            }
        }

        /* ==================================================================================== */
        return null;
    }

    @Override
    public ItemTranslator getItemTranslator() {
        return this.itemTranslator;
    }

    public String translateChatMessage(JSONObject jsonObj) {
        StringBuilder sbuilder = new StringBuilder(jsonObj.containsKey("text") ? ((String) jsonObj.get("text")) : "");
        if (jsonObj.containsKey("extra")) {
            if (jsonObj.get("extra") instanceof LinkedList) {
                LinkedList<JSONObject> jsonList = (LinkedList<JSONObject>) jsonObj.get("extra");
                for (JSONObject obj : jsonList) {
                    if (obj.containsKey("text")) {
                        sbuilder.append((String) obj.get("text"));
                    }
                }
            } else if (jsonObj.get("extra") instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) jsonObj.get("extra");
                if (jsonArray.size() > 0) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        if (((JSONObject) jsonArray.get(i)).containsKey("text")) {
                            sbuilder.append((String) ((JSONObject) jsonArray.get(i)).get("text"));
                        }
                    }
                }
            }
        }
        return sbuilder.toString();
    }

    public void processCrafting(WindowSetSlotPacket packet) {
        if (!(this.getSession().getPlayer() instanceof Player)) {
            return;
        }
        int realSlot = 0;
        if (packet.slot < 27) {
            realSlot = packet.slot + 9;
        } else if (packet.slot >= 27) {
            realSlot = packet.slot - 27;
        }
        ItemStack item = this.getSession().getPlayer().getInventory().getItem(realSlot);
        if (item == null) {
            item = new ItemStack(Material.AIR);
        } else if (item.getAmount() <= 0) {
            this.getSession().getPlayer().getInventory().setItem(realSlot, null);
            return;
        }
        System.out.println("FROM " + item.toString() + "to (ITEM=" + packet.item.id + ",CNT=" + packet.item.count + ")");
        if (packet.item.count < 0) {
            this.getSession().sendInventory();
            return;
        }
        if (item.getTypeId() == 0 && packet.item.id == 0) {
            this.getSession().sendInventory();
            return; //No changes
        }
        if (item.getTypeId() == packet.item.id && item.getAmount() == packet.item.count && item.getDurability() == packet.item.meta) {
            this.getSession().sendInventory();
            return; //No changes
        }
        if ((item.getTypeId() != 0 && packet.item.id == 0) || (item.getTypeId() != 0 && (item.getTypeId() != packet.item.id)) || (item.getAmount() > (packet.item.count & 0xFF))) {
            this.getSession().sendInventory();
            return; //Decreasing item, ignore
        }
        int amount = packet.item.count - (item.getTypeId() == 0 ? 0 : item.getAmount());
        ItemStack result = new ItemStack(packet.item.id, amount, packet.item.meta);
        List<Recipe> recipes = this.getSession().getServer().getCraftingManager().getRecipesFor(result);
        if (recipes.size() <= 0) {
            return;
        }
        //System.out.println("CRAFTING FOR: " + result.toString() + ", recipes count: " + recipes.size());
        if (packet.windowID == PEWindowConstantID.PLAYER_INVENTORY && recipes.size() > 4) {
            //Can not craft more than 4 recipes in a player inventory
            this.getSession().sendInventory();
            return;
        }
        ItemList items = new ItemList(this.getSession().getPlayer().getInventory());
        //List all ways to craft
        for (Recipe recipe : recipes) {
            if (recipe instanceof ShapedRecipe) {
                ShapedRecipe shaped = (ShapedRecipe) recipe;
                boolean faild = false;
                for (String itemChar : shaped.getShape()) {
                    ItemStack ingredient = shaped.getIngredientMap().get(new Character(itemChar.charAt(0)));
                    if (ingredient == null) {
                        continue;
                    }
                    if (!items.tryToRemove(ingredient)) {
                        faild = true;
                        break;
                    }
                }
                if (!faild) {
                    //Apply changes
                    for (String itemChar : shaped.getShape()) {
                        ItemStack ingredient = shaped.getIngredientMap().get(new Character(itemChar.charAt(0)));
                        if (ingredient == null) {
                            continue;
                        }
                        this.getSession().getPlayer().getInventory().remove(ingredient);
                    }
                    //System.out.println("CRAFT SUCCESS! ");
                } else {
                    continue;
                }
                this.getSession().getPlayer().getInventory().addItem(result);
                this.getSession().sendInventory();
                return;
            }
        }
        //System.out.println("FAILD TO CRAFT! ");
        this.getSession().sendInventory();
    }
}
