package com.jilou.ui.styles;

import com.jilou.ui.styles.types.Background;
import com.jilou.ui.styles.types.BorderRadius;
import com.jilou.ui.styles.types.DropShadow;
import com.jilou.ui.utils.Color;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StyleSheet {

    @Builder.Default
    private int zIndex = 0;

    @Builder.Default
    private Background background = Background.fromColor(Color.ORANGE);

    @Builder.Default
    private DropShadow dropShadow = new DropShadow(0.3f, 5.0f, -5.0f, 5, Color.rgba(0, 0, 0, 1.0f));

    @Builder.Default
    private BorderRadius borderRadius = new BorderRadius(20.0);

}
