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

import org.dragonet.net.DragonetSession;
import org.dragonet.utilities.DragonetVersioning;

public final class TranslatorProvider {

    public static BaseTranslator getByPEProtocolID(int protocolID, int maxProtocol) {
        if (maxProtocol < protocolID) {
            return null;
        }
        if (maxProtocol - protocolID > 2000) {
            return null;
        }
        for (int p = maxProtocol; p >= protocolID; p--) {
            switch (p) {
                case DragonetVersioning.MINECRAFT_PE_PROTOCOL:
                    return new Translator_v0_11();
            }
        }
        return null;
    }
}
