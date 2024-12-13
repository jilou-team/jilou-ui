package com.jilou.ui.styles.types;

import com.jilou.ui.enums.css.BorderType;
import com.jilou.ui.utils.Color;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a customizable border with properties such as color, thickness, and type.
 * This class uses the Lombok annotations {@code @Getter}, {@code @Setter}, and {@code @Builder}
 * to reduce boilerplate code for getters, setters, and builder pattern implementation.
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
@Getter
@Setter
@Builder
public class Border {

    /**
     * The color of the border.
     * Default value: {@code Color.BLACK}.
     */
    @Builder.Default
    private Color color = Color.BLACK;

    /**
     * The thickness of the border in pixels.
     * Default value: {@code 2}.
     */
    @Builder.Default
    private double thickness = 2;

    /**
     * The type of the border, which defines its rendering style.
     * Example types could include LINE, DASHED, etc.
     * Default value: {@code BorderType.LINE}.
     */
    @Builder.Default
    private BorderType type = BorderType.LINE;
}

