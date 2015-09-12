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

package org.dragonet.entity;

import lombok.Getter;

public class PEEntityType {
    public static enum BuiltInType{
        WOLF (14),
        VILLAGER (15),
        SQUID (17),
        ZOMBIE (32),
		CREEPER (33),
        PRIMED_TNT (65),
        FALLING_BLOCK (66),
        ARROW (80),
        SNOW_BALL (81);
        
        private int type;
        
        private BuiltInType(int type){
            this.type = type;
        }
        
        public PEEntityType getType(){
            return new PEEntityType(type);
        }
    }
    
    @Getter
    private int type;
    
    public PEEntityType(int type){
        this.type = type;
    }
    
    public static PEEntityType match(String type){
        try{
            return BuiltInType.valueOf(type.toUpperCase()).getType();
        }catch(IllegalArgumentException e){
            return null;
        }
    }
}
