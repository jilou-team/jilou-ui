package com.jilou.ui.styles.types;

import com.jilou.ui.math.Maths;
import lombok.Getter;

/**
 * Represents a customizable radius configuration for an object's corners.
 * Allows defining individual corner radii or using uniform values for multiple corners.
 * This class provides flexibility in setting radii while ensuring all values are non-negative.
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
@Getter
public class Radius {

    /**
     * Radius of the top-left corner.
     */
    private double topLeft;

    /**
     * Radius of the top-right corner.
     */
    private double topRight;

    /**
     * Radius of the bottom-right corner.
     */
    private double bottomRight;

    /**
     * Radius of the bottom-left corner.
     */
    private double bottomLeft;

    /**
     * Default constructor, initializing all corner radii to {@code 0.0}.
     */
    public Radius() {
        this(0.0);
    }

    /**
     * Constructor to set a uniform radius for all corners.
     *
     * @param radius the radius value for all corners.
     */
    public Radius(double radius) {
        this(radius, radius);
    }

    /**
     * Constructor to set the radius for top and bottom corners uniformly.
     *
     * @param top    the radius for the top-left and top-right corners.
     * @param bottom the radius for the bottom-left and bottom-right corners.
     */
    public Radius(double top, double bottom) {
        this(top, top, bottom, bottom);
    }

    /**
     * Constructor to set individual radii for all corners.
     *
     * @param topLeft     the radius for the top-left corner.
     * @param topRight    the radius for the top-right corner.
     * @param bottomLeft  the radius for the bottom-left corner.
     * @param bottomRight the radius for the bottom-right corner.
     */
    public Radius(double topLeft, double topRight, double bottomLeft, double bottomRight) {
        this.set(topLeft, topRight, bottomLeft, bottomRight);
    }

    /**
     * Sets a uniform radius for all corners.
     *
     * @param radius the radius value for all corners.
     */
    public void set(double radius) {
        this.set(radius, radius);
    }

    /**
     * Sets the radius for the top and bottom corners uniformly.
     *
     * @param top    the radius for the top-left and top-right corners.
     * @param bottom the radius for the bottom-left and bottom-right corners.
     */
    public void set(double top, double bottom) {
        this.set(top, top, bottom, bottom);
    }

    /**
     * Sets individual radii for all corners, ensuring no negative values are assigned.
     * If a negative value is provided, it is converted to a non-negative value using
     * {@link Maths#changeNegative(double)}.
     *
     * @param topLeft     the radius for the top-left corner.
     * @param topRight    the radius for the top-right corner.
     * @param bottomLeft  the radius for the bottom-left corner.
     * @param bottomRight the radius for the bottom-right corner.
     */
    public void set(double topLeft, double topRight, double bottomLeft, double bottomRight) {
        this.topLeft = Maths.changeNegative(topLeft);
        this.topRight = Maths.changeNegative(topRight);
        this.bottomLeft = Maths.changeNegative(bottomLeft);
        this.bottomRight = Maths.changeNegative(bottomRight);
    }
}

