package com.jilou.ui.styles;

import com.jilou.ui.styles.types.Background;
import com.jilou.ui.styles.types.Border;
import com.jilou.ui.styles.types.Radius;
import com.jilou.ui.styles.types.DropShadow;
import com.jilou.ui.utils.Color;
import com.jilou.ui.utils.format.Alignment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@code StyleSheet} class represents a collection of style properties for rendering graphical elements,
 * such as background, shadows, border radius, and corner segmentation. It allows easy configuration
 * and customization of visual elements through a builder pattern.
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
@Getter
@Setter
@Builder
public class StyleSheet {

    /**
     * The z-index determines the stacking order of the element. Higher values are drawn on top of lower values.
     * Default is 0.
     */
    @Builder.Default
    private int zIndex = 0;

    /**
     * The background of the element. This includes the color or other background properties.
     * Default is a white background.
     */
    @Builder.Default
    private Background background = Background.fromColor(Color.ORANGE);

    /**
     * The border of the element. This build the default widget border by calling {@code Border.builder().build()}.
     */
    @Builder.Default
    private Border border = Border.builder().build();

    /**
     * The drop shadow properties for the element. It includes the shadow color, offsets, layers, and strength.
     * Default values are set for a semi-transparent black shadow with 10px offsets and 8 layers.
     */
    @Builder.Default
    private DropShadow dropShadow = DropShadow.builder()
            .color(Color.rgba(0, 0, 0, 0.3))
            .offsetW(10).offsetH(10).offsetX(0).offsetY(0).layer(0).radius(null).strength(0.1f).build();

    /**
     * The border radius of the element. This defines the roundness of the corners.
     * Default radius is set to 5.0 pixels.
     */
    @Builder.Default
    private Radius borderRadius = new Radius(0);

    /**
     * The alignment content or justify content from css value.
     * Default it is {@link Alignment#NOTHING} which means set the child anywhere in the parent container.
     */
    @Builder.Default
    private Alignment alignment = Alignment.NOTHING;

    /**
     * The number of segments used to render the corners. This controls the smoothness of the rounded corners.
     * Default is set to 128 segments.
     */
    @Builder.Default
    private int cornerSegmentation = 128;
}

