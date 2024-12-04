package com.jilou.ui.container.layout;

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
     * Constructs a new {@code Layout} instance with an empty widget list.
     */
    protected Layout() {
        widgetList = new ArrayList<>();
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
     * Retrieves the list of widgets managed by this layout.
     *
     * @return the list of widgets
     */
    public List<AbstractWidget> getWidgetList() {
        return widgetList;
    }
}
