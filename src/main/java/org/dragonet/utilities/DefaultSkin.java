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

import com.caucho.util.Hex;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class DefaultSkin {

    protected static byte[] defaultSkin;

    static {
        loadSkin();
    }
    
    public static byte[] getDefaultSkin() {
        return defaultSkin;
    }

    private static void loadSkin() {
        try {
            DataInputStream in = new DataInputStream(DefaultSkin.class.getResourceAsStream("/defaults/SKIN.bin"));
            ByteArrayOutputStream out = new ByteArrayOutputStream();

	        String hex = in.readLine();
	        defaultSkin = hexStringToByteArray(hex);
	        Hex a;
	        out.close();
	        in.close();

        } catch (IOException e) {
        }
    }

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i+1), 16));
		}
		return data;
	}
}
