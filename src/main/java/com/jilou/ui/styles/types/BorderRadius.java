package com.jilou.ui.styles.types;

import com.jilou.ui.math.Maths;
import lombok.Getter;

@Getter
public class BorderRadius {

    private double topLeft;
    private double topRight;
    private double bottomRight;
    private double bottomLeft;

    public BorderRadius() {
        this(0.0);
    }

    public BorderRadius(double radius) {
        this(radius, radius);
    }

    public BorderRadius(double top, double bottom) {
        this(top, top, bottom, bottom);
    }

    public BorderRadius(double topLeft, double topRight, double bottomLeft, double bottomRight) {
        this.set(topLeft, topRight, bottomLeft, bottomRight);
    }

    public void set(double radius) {
        this.set(radius, radius);
    }

    public void set(double top, double bottom) {
        this.set(top, top, bottom, bottom);
    }

    public void set(double topLeft, double topRight, double bottomLeft, double bottomRight) {
        this.topLeft = Maths.changeNegative(topLeft);
        this.topRight = Maths.changeNegative(topRight);
        this.bottomLeft = Maths.changeNegative(bottomLeft);
        this.bottomRight = Maths.changeNegative(bottomRight);
    }
}
