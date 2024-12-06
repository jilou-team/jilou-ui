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

    public void setWidth(int width) {
        if(bindSizeToScene && hasParent()) {
            width = parent.getWidth();
        }
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        if(bindSizeToScene && hasParent()) {
            height = parent.getHeight();
        }
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public boolean isBindSizeToScene() {
        return bindSizeToScene;
    }

    public void setBindSizeToScene(boolean state) {
        this.bindSizeToScene = state;
    }

    public void setParent(Scene parent) {
        this.parent = parent;
        this.setWidth(getWidth());
        this.setHeight(getHeight());
    }

    public Scene getParent() {
        return parent;
    }

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
