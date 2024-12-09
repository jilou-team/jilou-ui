package com.jilou.ui.logic.callbacks.trigger;

import com.jilou.ui.widget.AbstractWidget;

/**
 * Functional interface representing a callback for handling hover events on widgets.
 * <p>
 * Implement this interface to define custom behavior when a widget's hover state changes.
 * The {@link #onHover(AbstractWidget, boolean)} method will be invoked whenever a widget
 * is hovered or the hover state is exited.
 * </p>
 *
 * @since 0.1.0
 * @see AbstractWidget
 * @author Daniel Ramke
 */
@FunctionalInterface
public interface HoverCallbackI {

    /**
     * Invoked when a widget's hover state changes.
     *
     * @param widget the widget whose hover state changed
     * @param hover  {@code true} if the widget is now hovered, {@code false} if the hover state was exited
     */
    void onHover(AbstractWidget widget, boolean hover);

}

