package org.dragonet.raknet.server;

import org.dragonet.raknet.protocol.EncapsulatedPacket;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public interface ServerInstance {

    void openSession(Session rakSession);

    void closeSession(Session rakSession, String reason);

    void handleEncapsulated(Session rakSession, EncapsulatedPacket packet, int flags);

    void handleRaw(String address, int port, byte[] payload);

    void notifyACK(String identifier, int identifierACK);

    void handleOption(String option, String value);

	String getServerName();

}
