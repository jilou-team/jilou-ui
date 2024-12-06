package com.jilou.ui.container.layout;

import com.jilou.ui.container.Scene;
import com.jilou.ui.widget.AbstractWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a layout that manages a collection of widgets in the application.
 * <p>
 * Subclasses are responsible for defining how the layout is updated and managing the widgets it contains.
 * The layout provides cleanup functionality to destroy and clear all widgets.
 * </p>
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
public abstract class Layout {

    /**
     * The list of widgets managed by this layout.
     */
    private final List<AbstractWidget> widgetList;

    /**
     * The layout width count.
     */
    private int width;

    /**
     * The layout height count.
     */
    private int height;

    /**
     * Bind the current layout size to fit the {@link Scene} size.
     */
    private boolean bindSizeToScene;

    /**
     * Parent must be a {@link Scene}.
     */
    private Scene parent;

    /**
     * Constructs a new {@code Layout} instance with an empty widget list.
     */
    protected Layout() {
        widgetList = new ArrayList<>();
        this.parent = null;
        this.bindSizeToScene = true;
        this.setWidth(0);
        this.setHeight(0);
    }

    /**
     * Updates the layout.
     * <p>
     * Subclasses must implement this method to define specific update behavior for the layout.
     * </p>
     */
    public abstract void update();

    /**
     * Cleans up the layout by destroying and clearing all widgets in the widget list.
     * <p>
     * If the widget list is empty, this method does nothing.
     * </p>
     */
    public void cleanUp() {
        if (widgetList.isEmpty()) {
            return;
        }

        for (AbstractWidget widget : widgetList) {
            widget.destroy();
        }

        widgetList.clear();
    }

    /**
     * Sets the width of the graphical object.
     * If {@code bindSizeToScene} is {@code true} and the object has a parent,
     * the width is set to the width of the parent scene instead.
     *
     * @param width the desired width of the graphical object.
     */
    public void setWidth(int width) {
        if (bindSizeToScene && hasParent()) {
            width = parent.getWidth();
        }
        this.width = width;
    }

    /**
     * Returns the width of the graphical object.
     *
     * @return the width of the graphical object.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the height of the graphical object.
     * If {@code bindSizeToScene} is {@code true} and the object has a parent,
     * the height is set to the height of the parent scene instead.
     *
     * @param height the desired height of the graphical object.
     */
    public void setHeight(int height) {
        if (bindSizeToScene && hasParent()) {
            height = parent.getHeight();
        }
        this.height = height;
    }

    /**
     * Returns the height of the graphical object.
     *
     * @return the height of the graphical object.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns whether the size of the graphical object is bound to its parent scene.
     *
     * @return {@code true} if the size is bound to the parent scene, {@code false} otherwise.
     */
    public boolean isBindSizeToScene() {
        return bindSizeToScene;
    }

    /**
     * Sets whether the size of the graphical object should be bound to its parent scene.
     *
     * @param state {@code true} to bind the size to the parent scene, {@code false} otherwise.
     */
    public void setBindSizeToScene(boolean state) {
        this.bindSizeToScene = state;
    }

    /**
     * Sets the parent scene of the graphical object.
     * When a parent is set, the width and height are updated based on the current settings.
     *
     * @param parent the parent scene to set.
     */
    public void setParent(Scene parent) {
        this.parent = parent;
        this.setWidth(getWidth());
        this.setHeight(getHeight());
    }

    /**
     * Returns the parent scene of the graphical object.
     *
     * @return the parent scene, or {@code null} if no parent is set.
     */
    public Scene getParent() {
        return parent;
    }

    /**
     * Checks whether the graphical object has a parent scene.
     *
     * @return {@code true} if the object has a parent scene, {@code false} otherwise.
     */
    public boolean hasParent() {
        return parent != null;
    }

    /**
     * Retrieves the list of widgets managed by this layout.
     *
     * @return the list of widgets
     */
    public List<AbstractWidget> getWidgetList() {
        return widgetList;
    }
}
