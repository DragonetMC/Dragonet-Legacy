/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragonet.utilities;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class DefaultSkin {

    protected static byte[] defaultSkin;

    public static byte[] getDefaultSkin() {
        if (defaultSkin == null) {
            loadSkin();
        }
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
