package com.runesource.network.buffer;

public enum PacketHeader {

    /**
     * Represents a non-game packet of data.
     */
    RAW,

    /**
     * Represents a fixed length game packet.
     */
    FIXED,

    /**
     * Represents a variable byte length game packet.
     */
    VARIABLE,

    /**
     * Represents a variable short length game packet.
     */
    VARIABLE_SHORT
}
