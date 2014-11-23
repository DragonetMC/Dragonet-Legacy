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

import org.dragonet.net.translator.protocols.v0_10_0.Translator_v0_10_0;

public class TranslatorProvider {
    public Translator getByPEProtocolID(int protocolID){
        switch(protocolID){
            case 20:
                return new Translator_v0_10_0();
            default:
                return null;
        }
    }
}
