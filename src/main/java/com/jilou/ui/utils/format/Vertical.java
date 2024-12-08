package com.jilou.ui.utils.format;

import lombok.Getter;

/**
 * Represents vertical alignment options with corresponding index values.
 *
 * @since 0.1.0
 * @author Daniel
 */
@Getter
public enum Vertical {

    /**
     * Represents no specific vertical alignment.
     */
    NOTHING(-1),

    /**
     * Represents alignment at the top.
     */
    TOP(0),

    /**
     * Represents alignment at the center.
     */
    CENTER(1),

    /**
     * Represents alignment at the bottom.
     */
    BOTTOM(2);

    /**
     * The index associated with the vertical alignment option.
     */
    private final int index;

    /**
     * Constructs a {@code Vertical} instance with the specified index value.
     *
     * @param index the index value representing the vertical alignment.
     */
    Vertical(int index) {
        this.index = index;
    }
}


