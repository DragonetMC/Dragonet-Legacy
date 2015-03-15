/*
 * (c) 2015 The Dragonet Team
 * All rights reserved.
 */
package org.dragonet.rhino.api;

import org.dragonet.rhino.Script;

/**
 * @author Ash (QuarkTheAwesome)
 */
public class onChatSent
{
    public static void onChatSent(String playerName, String message)
    {
        for(Script s : org.dragonet.DragonetServer.instance().getRhino().Scripts)
        {
            s.runFunction("onChatSent", new Object[] {playerName, message});
        }
    }
}
