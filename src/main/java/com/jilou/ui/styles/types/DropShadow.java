package com.jilou.ui.styles.types;

import com.jilou.ui.utils.Color;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DropShadow {

    private static final float MIN_LAYER = 1;
    private static final float MAX_LAYER = 20;

    private float strength;
    private float offsetX;
    private float offsetY;
    private int layer;
    private Color color;

    public DropShadow(float strength, float offsetX, float offsetY, int layer, Color color) {
        this.strength = strength;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.setLayer(layer);
        this.color = color;
    }

    public void setLayer(int layer) {
        if(layer < MIN_LAYER || layer > MAX_LAYER) {
            layer = 2;
        }
        this.layer = layer;
    }
}
