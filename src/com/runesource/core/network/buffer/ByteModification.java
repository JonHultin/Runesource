package com.runesource.core.network.buffer;

public enum ByteModification {

    /**
     * Do nothing to the value.
     */
    NORMAL,

    /**
     * Add {@code 128} to the value.
     */
    ADDITION,

    /**
     * Invert the sign of the value.
     */
    NEGATED,

    /**
     * Subtract {@code 128} from the value.
     */
    S
}
