package com.jilou.ui.utils.format;

import lombok.Getter;

/**
 * Represents combined vertical and horizontal alignment options.
 * <p>
 * Each alignment option specifies a combination of a {@link Vertical} and a {@link Horizontal} alignment.
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
@Getter
public enum Alignment {

    /**
     * Represents no specific alignment.
     */
    NOTHING(Vertical.NOTHING, Horizontal.NOTHING),

    /**
     * Represents alignment to the top-left.
     */
    TOP_LEFT(Vertical.TOP, Horizontal.LEFT),

    /**
     * Represents alignment to the top-center.
     */
    TOP_CENTER(Vertical.TOP, Horizontal.CENTER),

    /**
     * Represents alignment to the top-right.
     */
    TOP_RIGHT(Vertical.TOP, Horizontal.RIGHT),

    /**
     * Represents alignment to the center-left.
     */
    CENTER_LEFT(Vertical.CENTER, Horizontal.LEFT),

    /**
     * Represents alignment to the center.
     */
    CENTER(Vertical.CENTER, Horizontal.CENTER),

    /**
     * Represents alignment to the center-right.
     */
    CENTER_RIGHT(Vertical.CENTER, Horizontal.RIGHT),

    /**
     * Represents alignment to the bottom-left.
     */
    BOTTOM_LEFT(Vertical.BOTTOM, Horizontal.LEFT),

    /**
     * Represents alignment to the bottom-center.
     */
    BOTTOM_CENTER(Vertical.BOTTOM, Horizontal.CENTER),

    /**
     * Represents alignment to the bottom-right.
     */
    BOTTOM_RIGHT(Vertical.BOTTOM, Horizontal.RIGHT);

    /**
     * The vertical alignment component of this alignment.
     */
    private final Vertical vertical;

    /**
     * The horizontal alignment component of this alignment.
     */
    private final Horizontal horizontal;

    /**
     * Constructs an {@code Alignment} instance with the specified vertical and horizontal alignments.
     *
     * @param vertical   the vertical alignment component.
     * @param horizontal the horizontal alignment component.
     */
    Alignment(Vertical vertical, Horizontal horizontal) {
        this.vertical = vertical;
        this.horizontal = horizontal;
    }
}


