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

package org.dragonet.utilities;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MD5Encrypt {
    public static byte[] encrypt(byte[] data){
        MessageDigest md; 
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(data);
        return md.digest();
        } catch (NoSuchAlgorithmException ex) {
        }
        return null;
    }
    
    public static byte[] encryptString(String str){
        try {
            return encrypt(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }
}
