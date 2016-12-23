package com.runesource.network.buffer;

public enum ByteOrder {

    /**
     * Least significant byte is stored first and the most significant byte is stored last.
     */
    LITTLE_ENDIAN,

    /**
     * Most significant byte is stored first and the least significant byte is stored last.
     */
    BIG,

    /**
     * Neither big endian nor little endian, the v1 order.
     */
    MIDDLE,

    /**
     * Neither big endian nor little endian, the v2 order.
     */
    INVERSE_MIDDLE_ENDIAN
}
