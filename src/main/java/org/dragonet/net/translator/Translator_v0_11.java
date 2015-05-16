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
import net.glowstone.net.message.KickMessage;
import net.glowstone.net.message.play.entity.AnimateEntityMessage;
import net.glowstone.net.message.play.entity.CollectItemMessage;
import net.glowstone.net.message.play.entity.DestroyEntitiesMessage;
import net.glowstone.net.message.play.entity.EntityEffectMessage;
import net.glowstone.net.message.play.entity.EntityEquipmentMessage;
import net.glowstone.net.message.play.entity.EntityMetadataMessage;
import net.glowstone.net.message.play.entity.EntityVelocityMessage;
import net.glowstone.net.message.play.entity.RelativeEntityPositionMessage;
import net.glowstone.net.message.play.entity.RelativeEntityPositionRotationMessage;
import net.glowstone.net.message.play.entity.SpawnObjectMessage;
import net.glowstone.net.message.play.entity.SpawnPlayerMessage;
import net.glowstone.net.message.play.game.BlockChangeMessage;
import net.glowstone.net.message.play.game.ChatMessage;
import net.glowstone.net.message.play.game.HealthMessage;
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
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.dragonet.inventory.ItemList;
import org.dragonet.inventory.PEWindowConstantID;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.*;
import org.dragonet.net.translator.topc.*;
import org.dragonet.net.translator.tope.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
        mapToPE.put(AnimateEntityMessage.class, new AnimateEntityMessageTranslator(this, this.getSession()));
        mapToPE.put(BlockChangeMessage.class, new BlockChangeMessageTranslator(this, this.getSession()));
        mapToPE.put(ChatMessage.class, new ChatMessageTranslator(this, this.getSession()));
        mapToPE.put(CloseWindowMessage.class, new CloseWindowMessageTranslator(this, this.getSession()));
        mapToPE.put(CollectItemMessage.class, new CollectItemMessageTranslator(this, this.getSession()));
        mapToPE.put(DestroyEntitiesMessage.class, new DestroyEntitiesMessageTranslator(this, this.getSession()));
        mapToPE.put(EntityEffectMessage.class, new EntityEffectMessageTranslator(this, this.getSession()));
        mapToPE.put(EntityEquipmentMessage.class, new EntityEquipmentMessageTranslator(this, this.getSession()));
        mapToPE.put(EntityMetadataMessage.class, new EntityMetadataMessageTranslator(this, this.getSession()));
        mapToPE.put(EntityVelocityMessage.class, new EntityVelocityMessageTranslator(this, this.getSession()));
        mapToPE.put(KickMessage.class, new KickMessageTranslator(this, this.getSession()));
        mapToPE.put(MultiBlockChangeMessage.class, new MultiBlockChangeMessageTranslator(this, this.getSession()));
        mapToPE.put(OpenWindowMessage.class, new OpenWindowMessageTranslator(this, this.getSession()));
        mapToPE.put(PlayerPositionLookMessage.class, new PlayerPositionLookMessageTranslator(this, this.getSession()));
        mapToPE.put(PlayerPositionMessage.class, new PlayerPositionMessageTranslator(this, this.getSession()));
        mapToPE.put(PositionRotationMessage.class, new PositionRotationMessageTranslator(this, this.getSession()));
        mapToPE.put(RelativeEntityPositionMessage.class, new RelativeEntityPositionMessageTranslator(this, this.getSession()));
        mapToPE.put(RelativeEntityPositionRotationMessage.class, new RelativeEntityPositionRotationMessageTranslator(this, this.getSession()));
        mapToPE.put(SetWindowContentsMessage.class, new SetWindowContentsMessageTranslator(this, this.getSession()));
        mapToPE.put(SetWindowSlotMessage.class, new SetWindowSlotMessageTranslator(this, this.getSession()));
        mapToPE.put(SpawnObjectMessage.class, new SpawnObjectMessageTranslator(this, this.getSession()));
        mapToPE.put(SpawnPlayerMessage.class, new SpawnPlayerMessageTranslator(this, this.getSession()));
        mapToPE.put(StateChangeMessage.class, new StateChangeMessageTranslator(this, this.getSession()));
        mapToPE.put(TimeMessage.class, new TimeMessageTranslator(this, this.getSession()));
        mapToPE.put(HealthMessage.class, new HealthMessageTranslator(this, this.getSession()));

        // [PE => PC]
        mapToPC.put(AnimatePacket.class, new AnimatePacketTranslator(this, this.getSession()));
        mapToPC.put(ChatPacket.class, new ChatPacketTranslator(this, this.getSession()));
        mapToPC.put(MovePlayerPacket.class, new MovePlayerPacketTranslator(this, this.getSession()));
        mapToPC.put(PlayerEquipmentPacket.class, new PlayerEquipmentPacketTranslator(this, this.getSession()));
        mapToPC.put(RemoveBlockPacket.class, new RemoveBlockPacketTranslator(this, this.getSession()));
        mapToPC.put(UseItemPacket.class, new UseItemPacketTranslator(this, this.getSession()));
        mapToPC.put(WindowSetSlotPacket.class, new WindowSetSlotPacketTranslator(this, this.getSession()));
        mapToPC.put(WindowItemsPacket.class, new WindowItemsPacketTranslator(this, this.getSession()));
    }

    /* ===== TO PC ===== */
    @Override
    public Message[] translateToPC(PEPacket packet) {
        if (mapToPC.containsKey(packet.getClass())) {
            return mapToPC.get(packet.getClass()).handle(packet);
        } else {
            //System.out.println("FAILD TO TRANSLATE TO PC MESSAGE: " + packet.getClass().getSimpleName());
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
         */

        if (mapToPE.containsKey(message.getClass())) {
            return mapToPE.get(message.getClass()).handle(message);
        } else {
            //System.out.println("FAILD TO TRANSLATE TO PE PACKET: " + message.getClass().getSimpleName());
            return null;
        }
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
