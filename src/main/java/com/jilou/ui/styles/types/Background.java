package com.jilou.ui.styles.types;

import com.jilou.ui.utils.Color;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Background {

    private Color color;

    public Background(Color color) {
        this.color = color;
    }

    public static Background fromColor(Color color) {
        return new Background(color);
    }

}
