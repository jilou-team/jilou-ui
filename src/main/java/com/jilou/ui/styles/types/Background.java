package com.jilou.ui.styles.types;

import com.jilou.ui.utils.Color;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@code Background} class represents the background of a graphical element.
 * It contains a {@link Color} that defines the appearance of the background.
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
@Setter
@Getter
public class Background {

    /**
     * The color of the background. It controls the visual appearance of the background in a graphical context.
     */
    private Color color;

    /**
     * Constructor for the {@code Background} class, which sets the background color.
     *
     * @param color The color of the background.
     */
    public Background(Color color) {
        this.color = color;
    }

    /**
     * Static method to create a new {@code Background} with the specified {@link Color}.
     *
     * @param color The color of the background.
     * @return A new {@code Background} object with the specified color.
     */
    public static Background fromColor(Color color) {
        return new Background(color);
    }

}

