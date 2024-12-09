package com.jilou.ui.logic.callbacks;

import com.jilou.ui.logic.callbacks.trigger.HoverCallbackI;
import com.jilou.ui.widget.AbstractWidget;

/**
 * Utility class that defines and manages native widget callbacks.
 * <p>
 * This class is a container for various widget callback implementations, allowing
 * centralized handling of widget-specific events such as hover interactions.
 * </p>
 * <p>
 * The {@link NativeWidgetCallbacks.HoverCallback} class provides functionality to handle
 * hover state changes for widgets and to notify all registered listeners about these changes.
 * </p>
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
public final class NativeWidgetCallbacks {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * Throws an {@link IllegalStateException} if instantiation is attempted.
     */
    private NativeWidgetCallbacks() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Handles hover events for widgets by invoking all registered {@link HoverCallbackI} listeners.
     * <p>
     * This callback class manages a list of {@link HoverCallbackI} listeners and notifies them
     * when the hover state of a widget changes. The listeners are invoked in the order they were added.
     * </p>
     */
    public static class HoverCallback extends WidgetCallback<HoverCallbackI> implements HoverCallbackI {

        /**
         * Invoked when the hover state of a widget changes.
         * <p>
         * This method iterates through all registered {@link HoverCallbackI} listeners and
         * invokes their {@link HoverCallbackI#onHover(AbstractWidget, boolean)} method with
         * the provided widget and hover state.
         * </p>
         *
         * @param widget the widget whose hover state has changed
         * @param hover  {@code true} if the widget is now hovered, {@code false} if it is no longer hovered
         */
        @Override
        public void onHover(AbstractWidget widget, boolean hover) {
            for (HoverCallbackI listener : listeners) {
                listener.onHover(widget, hover);
            }
        }
    }
}


