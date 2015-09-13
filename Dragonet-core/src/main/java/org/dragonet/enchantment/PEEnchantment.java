<<<<<<< HEAD
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
package org.dragonet.enchantment;

 public class PEEnchantment {
     public final static int TYPE_ARMOR_PROTECTION = 0;
     public final static int TYPE_ARMOR_FIRE_PROTECTION = 1;
     public final static int TYPE_ARMOR_FALL_PROTECTION = 2;
     public final static int TYPE_ARMOR_EXPLOSION_PROTECTION = 3;
     public final static int TYPE_ARMOR_PROJECTILE_PROTECTION = 4;
     public final static int TYPE_ARMOR_THORNS = 5;
     public final static int TYPE_WATER_BREATHING = 6;
     public final static int TYPE_WATER_SPEED = 7;
     public final static int TYPE_WATER_AFFINITY = 8;
     public final static int TYPE_WEAPON_SHARPNESS = 9;
     public final static int TYPE_WEAPON_SMITE = 10;
     public final static int TYPE_WEAPON_ARTHROPODS = 11;
     public final static int TYPE_WEAPON_KNOCKBACK = 12;
     public final static int TYPE_WEAPON_FIRE_ASPECT = 13;
     public final static int TYPE_WEAPON_LOOTING = 14;
     public final static int TYPE_MINING_EFFICIENCY = 15;
     public final static int TYPE_MINING_SILK_TOUCH = 16;
     public final static int TYPE_MINING_DURABILITY = 17;
     public final static int TYPE_MINING_FORTUNE = 18;
     public final static int TYPE_BOW_POWER = 19;
     public final static int TYPE_BOW_KNOCKBACK = 20;
     public final static int TYPE_BOW_FLAME = 21;
     public final static int TYPE_BOW_INFINITY = 22;
     public final static int TYPE_FISHING_FORTUNE = 23;
     public final static int TYPE_FISHING_LURE = 24;

     public final static int RARITY_COMMON = 0;
     public final static int RARITY_UNCOMMON = 1;
     public final static int RARITY_RARE = 2;
     public final static int RARITY_MYTHIC = 3;

     public final static int ACTIVATION_EQUIP = 0;
     public final static int ACTIVATION_HELD = 1;
     public final static int ACTIVATION_SELF = 2;

     public final static int SLOT_NONE = 0;
     public final static int SLOT_ALL = 0b11111111111111;
     public final static int SLOT_ARMOR = 0b1111;
     public final static int SLOT_HEAD = 0b1;
     public final static int SLOT_TORSO = 0b10;
     public final static int SLOT_LEGS = 0b100;
     public final static int SLOT_FEET = 0b1000;
     public final static int SLOT_SWORD = 0b10000;
     public final static int SLOT_BOW = 0b100000;
     public final static int SLOT_TOOL = 0b111000000;
     public final static int SLOT_HOE = 0b1000000;
     public final static int SLOT_SHEARS = 0b10000000;
     public final static int SLOT_FLINT_AND_STEEL = 0b10000000;
     public final static int SLOT_DIG = 0b111000000000;
     public final static int SLOT_AXE = 0b1000000000;
     public final static int SLOT_PICKAXE = 0b10000000000;
     public final static int SLOT_SHOVEL = 0b10000000000;
     public final static int SLOT_FISHING_ROD = 0b100000000000;
     public final static int SLOT_CARROT_STICK = 0b1000000000000;
     
     private int id;
     private int level = 1;
     private String name;
	 
	 public PEEnchantment(int id, String name){
		 this.id = id;
		 this.name = name;
	 }
	 
	 public int getId(){
	 	return id;
	 }
	 
	 public String getName(){
	 	return name;
	 }
	 
	 public int getLevel(){
	 	return level;
	 }
	 
	 public void setLevel(int value){
	 	this.level = level;
	 }
	 
=======
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
package org.dragonet.enchantment;

 public class PEEnchantment {
     public final static int TYPE_ARMOR_PROTECTION = 0;
     public final static int TYPE_ARMOR_FIRE_PROTECTION = 1;
     public final static int TYPE_ARMOR_FALL_PROTECTION = 2;
     public final static int TYPE_ARMOR_EXPLOSION_PROTECTION = 3;
     public final static int TYPE_ARMOR_PROJECTILE_PROTECTION = 4;
     public final static int TYPE_ARMOR_THORNS = 5;
     public final static int TYPE_WATER_BREATHING = 6;
     public final static int TYPE_WATER_SPEED = 7;
     public final static int TYPE_WATER_AFFINITY = 8;
     public final static int TYPE_WEAPON_SHARPNESS = 9;
     public final static int TYPE_WEAPON_SMITE = 10;
     public final static int TYPE_WEAPON_ARTHROPODS = 11;
     public final static int TYPE_WEAPON_KNOCKBACK = 12;
     public final static int TYPE_WEAPON_FIRE_ASPECT = 13;
     public final static int TYPE_WEAPON_LOOTING = 14;
     public final static int TYPE_MINING_EFFICIENCY = 15;
     public final static int TYPE_MINING_SILK_TOUCH = 16;
     public final static int TYPE_MINING_DURABILITY = 17;
     public final static int TYPE_MINING_FORTUNE = 18;
     public final static int TYPE_BOW_POWER = 19;
     public final static int TYPE_BOW_KNOCKBACK = 20;
     public final static int TYPE_BOW_FLAME = 21;
     public final static int TYPE_BOW_INFINITY = 22;
     public final static int TYPE_FISHING_FORTUNE = 23;
     public final static int TYPE_FISHING_LURE = 24;

     public final static int RARITY_COMMON = 0;
     public final static int RARITY_UNCOMMON = 1;
     public final static int RARITY_RARE = 2;
     public final static int RARITY_MYTHIC = 3;

     public final static int ACTIVATION_EQUIP = 0;
     public final static int ACTIVATION_HELD = 1;
     public final static int ACTIVATION_SELF = 2;

     public final static int SLOT_NONE = 0;
     public final static int SLOT_ALL = 0b11111111111111;
     public final static int SLOT_ARMOR = 0b1111;
     public final static int SLOT_HEAD = 0b1;
     public final static int SLOT_TORSO = 0b10;
     public final static int SLOT_LEGS = 0b100;
     public final static int SLOT_FEET = 0b1000;
     public final static int SLOT_SWORD = 0b10000;
     public final static int SLOT_BOW = 0b100000;
     public final static int SLOT_TOOL = 0b111000000;
     public final static int SLOT_HOE = 0b1000000;
     public final static int SLOT_SHEARS = 0b10000000;
     public final static int SLOT_FLINT_AND_STEEL = 0b10000000;
     public final static int SLOT_DIG = 0b111000000000;
     public final static int SLOT_AXE = 0b1000000000;
     public final static int SLOT_PICKAXE = 0b10000000000;
     public final static int SLOT_SHOVEL = 0b10000000000;
     public final static int SLOT_FISHING_ROD = 0b100000000000;
     public final static int SLOT_CARROT_STICK = 0b1000000000000;
	 
	 public PEEnchantment(){
		 //TODO
	 }
	 
>>>>>>> parent of 44cedc5... Some enchantment API work
}