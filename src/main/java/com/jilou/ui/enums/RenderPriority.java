package com.jilou.ui.enums;

import com.jilou.ui.logic.Renderer;

/**
 * Represents the priority levels for rendering tasks in the JilouUI framework.
 * <p>
 * The {@code RenderPriority} enum defines different priority levels for rendering operations.
 * These priorities determine the order in which rendering tasks are processed. Higher priorities
 * are processed first.
 * </p>
 * <p>
 * The default rendering priority is {@link #MEDIUM}. Renderers can use the {@link Renderer#setPriority(RenderPriority)}
 * method to adjust their priority dynamically during runtime.
 * </p>
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
public enum RenderPriority {

    /**
     * Represents the lowest rendering priority. Renderers with this priority
     * are processed last.
     */
    LOWEST(0),

    /**
     * Represents a low rendering priority. These renderers are processed after
     * medium-priority renderers but before those with the lowest priority.
     */
    LOW(1),

    /**
     * Represents the default rendering priority. Renderers with this priority
     * are processed in the middle of the render order.
     */
    MEDIUM(2),

    /**
     * Represents a high rendering priority. These renderers are processed before
     * those with medium and low priority.
     */
    HIGH(3),

    /**
     * Represents the highest rendering priority. Renderers with this priority
     * are processed first, before all other renderers.
     */
    HIGHEST(4);

    private final int rate;

    /**
     * Constructs a new {@link RenderPriority} with the specified rate value.
     * <p>
     * The rate determines the order in which tasks are processed, with higher values
     * representing higher priority.
     * </p>
     *
     * @param rate the numeric value representing the priority level
     */
    RenderPriority(int rate) {
        this.rate = rate;
    }

    /**
     * Returns the numeric rate associated with this priority level.
     * <p>
     * This value is used internally to compare priorities and determine the order of rendering.
     * </p>
     *
     * @return the numeric rate of the priority level
     */
    public int getRate() {
        return rate;
    }

}

