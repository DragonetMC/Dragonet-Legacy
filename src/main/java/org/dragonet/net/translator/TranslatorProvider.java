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

public final class TranslatorProvider {

    public static BaseTranslator getByPEProtocolID(DragonetSession session, int protocolID) {
        switch (protocolID) {
            case 26:
                return new Translator_v0_11(session);
            default:
                return null;
        }
    }
}
