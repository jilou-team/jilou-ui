package com.jilou.ui.widget;

import com.jilou.ui.logic.graphics.WidgetBackgroundRenderer;
import com.jilou.ui.styles.StyleSheet;
import lombok.Getter;
import lombok.Setter;

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
@Getter
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
    @Setter
    private double positionX;

    /**
     * The Y-coordinate position of the widget.
     */
    @Setter
    private double positionY;

    /**
     * The {@link StyleSheet} of the widget.
     */
    private StyleSheet style;

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
        this.setStyle(null);
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
     * Sets the width of the widget. The width cannot be negative.
     *
     * @param width the new width of the widget in pixels
     */
    public void setWidth(double width) {
        this.width = Math.max(0, width);
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
     * Set the current {@link StyleSheet} for this widget. It has impact to the {@link WidgetBackgroundRenderer}.
     * @param style new style can be null but is replaced than by {@code StyleSheet.builder().build()}
     */
    public void setStyle(StyleSheet style) {
        if(style == null) {
            style = StyleSheet.builder().build();
        }
        this.style = style;
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

}

