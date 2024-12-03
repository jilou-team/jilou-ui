package com.jilou.ui.widget;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 0.1.0
 * @author Daniel Ramke
 */
public abstract class AbstractWidget {

    private final List<AbstractWidget> children = new ArrayList<>();

    private final String localizedName;

    private String name;

    private double width;
    private double height;

    private double positionX;
    private double positionY;

    protected AbstractWidget(String localizedName) {
        this.localizedName = localizedName;
    }

    public abstract void input();

    /**
     * @return {@link String}- {@link Class#getSimpleName()} is the name of widget.
     */
    public String getWidgetName() {
        return getClass().getSimpleName();
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public void setName(String name) {
        if(name == null || name.isBlank()) {
            name = getWidgetName();
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setWidth(double width) {
        this.width = Math.max(0, width);
    }

    public double getWidth() {
        return width;
    }

    public void setHeight(double height) {
        this.height = Math.max(0, height);
    }

    public double getHeight() {
        return height;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public double getPositionX() {
        return positionX;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public double getPositionY() {
        return positionY;
    }

    public void addChild(AbstractWidget child) {
        children.add(child);
    }

    public void removeChild(AbstractWidget child) {
        children.remove(child);
    }

    public List<AbstractWidget> getChildren() {
        return children;
    }
}
