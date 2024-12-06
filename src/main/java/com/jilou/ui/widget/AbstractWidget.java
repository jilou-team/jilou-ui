package com.jilou.ui.widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an abstract base class for widgets in the application.
 * <p>
 * Each widget has properties such as dimensions, position, and a localized name.
 * It also supports managing child widgets for hierarchical relationships.
 * </p>
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
public abstract class AbstractWidget {

    /**
     * The list of child widgets associated with this widget.
     */
    private final List<AbstractWidget> children = new ArrayList<>();

    /**
     * The unique localized name of the widget.
     */
    private final String localizedName;

    /**
     * The display name of the widget.
     */
    private String name;

    /**
     * The width of the widget in pixels.
     */
    private double width;

    /**
     * The height of the widget in pixels.
     */
    private double height;

    /**
     * The X-coordinate position of the widget.
     */
    private double positionX;

    /**
     * The Y-coordinate position of the widget.
     */
    private double positionY;

    /**
     * Constructs a new {@code AbstractWidget} with the specified localized name.
     *
     * @param localizedName the unique localized name of the widget
     */
    protected AbstractWidget(String localizedName) {
        this.localizedName = localizedName;
        this.name = getClass().getSimpleName();
        this.width = 0;
        this.height = 0;
        this.positionX = 0;
        this.positionY = 0;
    }

    /**
     * Handles input events for the widget. This method must be implemented by subclasses.
     */
    public abstract void input();

    /**
     * Cleans up resources associated with the widget. This method must be implemented by subclasses.
     */
    public abstract void destroy();

    /**
     * Retrieves the name of the widget, which is the simple name of its class.
     *
     * @return the name of the widget
     */
    public String getWidgetName() {
        return getClass().getSimpleName();
    }

    /**
     * Retrieves the localized name of the widget.
     *
     * @return the localized name
     */
    public String getLocalizedName() {
        return localizedName;
    }

    /**
     * Sets the display name of the widget. If the provided name is null or blank,
     * the widget's class name is used as the default.
     *
     * @param name the new display name of the widget
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            name = getWidgetName();
        }
        this.name = name;
    }

    /**
     * Retrieves the display name of the widget.
     *
     * @return the display name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the width of the widget. The width cannot be negative.
     *
     * @param width the new width of the widget in pixels
     */
    public void setWidth(double width) {
        this.width = Math.max(0, width);
    }

    /**
     * Retrieves the width of the widget.
     *
     * @return the width in pixels
     */
    public double getWidth() {
        return width;
    }

    /**
     * Sets the height of the widget. The height cannot be negative.
     *
     * @param height the new height of the widget in pixels
     */
    public void setHeight(double height) {
        this.height = Math.max(0, height);
    }

    /**
     * Retrieves the height of the widget.
     *
     * @return the height in pixels
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets the X-coordinate position of the widget.
     *
     * @param positionX the new X-coordinate
     */
    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    /**
     * Retrieves the X-coordinate position of the widget.
     *
     * @return the X-coordinate
     */
    public double getPositionX() {
        return positionX;
    }

    /**
     * Sets the Y-coordinate position of the widget.
     *
     * @param positionY the new Y-coordinate
     */
    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    /**
     * Retrieves the Y-coordinate position of the widget.
     *
     * @return the Y-coordinate
     */
    public double getPositionY() {
        return positionY;
    }

    /**
     * Adds a child widget to this widget's list of children.
     *
     * @param child the child widget to add
     */
    public void addChild(AbstractWidget child) {
        children.add(child);
    }

    /**
     * Removes a child widget from this widget's list of children.
     *
     * @param child the child widget to remove
     */
    public void removeChild(AbstractWidget child) {
        children.remove(child);
    }

    /**
     * Retrieves the list of child widgets associated with this widget.
     *
     * @return the list of child widgets
     */
    public List<AbstractWidget> getChildren() {
        return children;
    }
}

