package com.jilou.ui.widget;

import com.jilou.ui.logic.Renderer;
import com.jilou.ui.logic.graphics.WidgetBackgroundRenderer;
import com.jilou.ui.styles.StyleSheet;
import com.jilou.ui.utils.AlignmentUtils;
import com.jilou.ui.utils.format.Alignment;
import lombok.AccessLevel;
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
     * The widget parent, default null.
     */
    @Setter(AccessLevel.PROTECTED)
    private AbstractWidget parent;

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
     * The X-coordinate position inner parent x position and his width.
     * Only worked if the parent used the {@link Alignment#NOTHING}
     */
    @Setter
    private double innerParentX;

    /**
     * The Y-coordinate position inner parent y position and his height.
     * Only worked if the parent used the {@link Alignment#NOTHING}
     */
    @Setter
    private double innerParentY;

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
        this.parent = null;
        this.name = getClass().getSimpleName();
        this.width = 0;
        this.height = 0;
        this.positionX = 0;
        this.positionY = 0;
        this.innerParentX = 0;
        this.innerParentY = 0;
        this.setStyle(null);
    }

    /**
     * Function for updating this {@code AbstractWidget}. This is needed for update all
     * children of this object.
     */
    public void update() {
        if(hasChildren()) {
            AlignmentUtils.updateAlignment(this, children);
            for (AbstractWidget child : children) {
                child.update();
            }
        }
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
     * Called function {@link #setPosition(double, double)} and placed {@code pos} to the required params.
     * @param pos the x and y position
     */
    public void setPosition(double pos) {
        this.setPosition(pos, pos);
    }

    /**
     * Set the current real time position of this object at the {@link Renderer}.
     * This is absolute needed to display ui widgets. Note if you ar using parents with {@link Alignment#NOTHING},
     * use {@link #setInnerParentX(double)} and {@link #setInnerParentY(double)} instant of this method!
     * @param x to render x coords
     * @param y to render y coords
     */
    public void setPosition(double x, double y) {
        this.setPositionX(x);
        this.setPositionY(y);
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
        if (child == null) return;
        if (hasChild(child)) return;

        if(child.hasParent()) {
            child.getParent().removeChild(child);
        }
        child.setParent(this);
        children.add(child);
    }

    /**
     * Removes a child widget from this widget's list of children.
     *
     * @param child the child widget to remove
     */
    public void removeChild(AbstractWidget child) {
        if (child == null) return;
        if (!hasChild(child)) return;

        child.setParent(null);
        children.remove(child);
    }

    /**
     * Returned true if there was a parent found.
     * @return {@code boolean}
     */
    public boolean hasParent() {
        return parent != null;
    }

    /**
     * Determines if the current widget has any child widgets.
     *
     * @return {@code true} if the widget has children, {@code false} otherwise.
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    /**
     * Checks if a specific child widget exists within the current widget's children.
     *
     * @param child the child widget to check for; must not be {@code null}.
     * @return {@code true} if the child exists, {@code false} otherwise.
     */
    public boolean hasChild(AbstractWidget child) {
        return getChild(child) != null;
    }

    /**
     * Checks if a child widget with the specified localized name exists within the current widget's children.
     *
     * @param localizedName the localized name of the child widget to check for; must not be {@code null}.
     * @return {@code true} if a child with the specified localized name exists, {@code false} otherwise.
     */
    public boolean hasChild(String localizedName) {
        return getChild(localizedName) != null;
    }

    /**
     * Retrieves a child widget that matches the specified widget.
     * This method internally uses the localized name of the provided widget for comparison.
     *
     * @param child the child widget to retrieve; must not be {@code null}.
     * @return the matching child widget, or {@code null} if no match is found.
     */
    public AbstractWidget getChild(AbstractWidget child) {
        if (child == null) return null;
        return getChild(child.getLocalizedName());
    }

    /**
     * Retrieves a child widget with the specified localized name.
     *
     * @param localizedName the localized name of the child widget to retrieve; must not be {@code null}.
     * @return the matching child widget, or {@code null} if no match is found.
     */
    public AbstractWidget getChild(String localizedName) {
        AbstractWidget child = null;
        for (AbstractWidget c : children) {
            if (c.getLocalizedName().equals(localizedName)) {
                child = c;
            }
        }
        return child;
    }

}

