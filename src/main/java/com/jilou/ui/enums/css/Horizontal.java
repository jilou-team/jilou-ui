package com.jilou.ui.enums.css;

import lombok.Getter;

/**
 * Represents horizontal alignment options with corresponding index values.
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
@Getter
public enum Horizontal {

    /**
     * Represents no specific horizontal alignment.
     */
    NOTHING(-1),

    /**
     * Represents alignment to the left.
     */
    LEFT(0),

    /**
     * Represents alignment to the center.
     */
    CENTER(1),

    /**
     * Represents alignment to the right.
     */
    RIGHT(2);

    /**
     * The index associated with the horizontal alignment option.
     */
    private final int index;

    /**
     * Constructs a {@code Horizontal} instance with the specified index value.
     *
     * @param index the index value representing the horizontal alignment.
     */
    Horizontal(int index) {
        this.index = index;
    }
}


