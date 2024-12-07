package com.jilou.ui.styles.types;

import com.jilou.ui.utils.Color;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@code DropShadow} class represents the shadow effect applied to graphical elements. It includes properties
 * such as strength, offset, color, layer depth, and radius to customize how the shadow appears around an element.
 * This class uses a builder pattern for easy instantiation and customization.
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
@Getter
@Setter
@Builder
public class DropShadow {

    /**
     * The minimum allowed layer value for the shadow. This constant is used to enforce constraints on the layer.
     */
    private static final float MIN_LAYER = 0;

    /**
     * The maximum allowed layer value for the shadow. This constant is used to enforce constraints on the layer.
     */
    private static final float MAX_LAYER = 20;

    /**
     * The strength of the shadow, which controls how dark or prominent the shadow is.
     * Default value is 0.5.
     */
    @Builder.Default
    private float strength = 0.5f;

    /**
     * The horizontal offset of the shadow, which controls the horizontal displacement of the shadow.
     * Default value is 0.0.
     */
    @Builder.Default
    private float offsetX = 0.0f;

    /**
     * The vertical offset of the shadow, which controls the vertical displacement of the shadow.
     * Default value is 0.0.
     */
    @Builder.Default
    private float offsetY = 0.0f;

    /**
     * The width of the shadow's spread. This controls how far the shadow extends from the element.
     * Default value is 2.0.
     */
    @Builder.Default
    private float offsetW = 2.0f;

    /**
     * The height of the shadow's spread. This controls how far the shadow extends from the element.
     * Default value is 2.0.
     */
    @Builder.Default
    private float offsetH = 2.0f;

    /**
     * The number of layers for the shadow, which controls the number of shadow iterations.
     * The layer depth controls the complexity and appearance of the shadow.
     * A valid value is between {@link #MIN_LAYER} and {@link #MAX_LAYER}.
     */
    private int layer;

    /**
     * The color of the shadow. This controls the shadow's appearance, including opacity and color.
     * Default color is semi-transparent black (rgba(0, 0, 0, 0.75)).
     */
    @Builder.Default
    private Color color = Color.rgba(0, 0, 0, 0.75);

    /**
     * The radius of the shadow corners, which controls how round the shadow's edges appear.
     * A {@code null} value indicates no rounded edges.
     */
    private Radius radius;

    /**
     * Sets the layer value with constraints. If the provided layer value is outside the allowed range,
     * it is clamped to a default value of 2.
     *
     * @param layer The layer value to be set. It must be between {@link #MIN_LAYER} and {@link #MAX_LAYER}.
     */
    public void setLayer(int layer) {
        if(layer < MIN_LAYER || layer > MAX_LAYER) {
            layer = 2;
        }
        this.layer = layer;
    }

    /**
     * The builder class for {@code DropShadow}. It provides a custom method for setting the {@code layer} field with constraints.
     *
     * @since 0.1.0
     * @author Daniel Ramke
     */
    public static class DropShadowBuilder {

        /**
         * Sets the layer value for the {@code DropShadow}. It clamps the value between {@link #MIN_LAYER} and {@link #MAX_LAYER}.
         *
         * @param layer The layer value to be set.
         * @return The current {@code DropShadowBuilder} instance for method chaining.
         */
        public DropShadowBuilder layer(int layer) {
            if(layer < MIN_LAYER || layer > MAX_LAYER) {
                layer = 2;
            }
            this.layer = layer;
            return this;
        }

    }
}

