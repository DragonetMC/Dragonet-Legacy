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

            byte[] buffer = new byte[4096];
            int count;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            defaultSkin = out.toByteArray();
            out.close();
            in.close();
        } catch (IOException e) {
        }
    }
}
