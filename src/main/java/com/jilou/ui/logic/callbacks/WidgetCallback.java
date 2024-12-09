package com.jilou.ui.logic.callbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for managing callback listeners associated with widgets.
 * <p>
 * This class provides a foundation for implementing specific callback types,
 * such as hover or click callbacks, by maintaining a list of registered listeners
 * and offering methods to add or remove them.
 * </p>
 *
 * @since 0.1.0
 * @author Daniel Ramke
 * @param <T> the type of the listener managed by this callback
 */
public abstract class WidgetCallback<T> {

    /**
     * A list of registered listeners for this callback.
     * <p>
     * Each listener in this list will be notified of the relevant event
     * when the callback is triggered.
     * </p>
     */
    protected final List<T> listeners = new ArrayList<>();

    /**
     * Adds a listener to this callback.
     * <p>
     * The provided listener is added to the internal list of listeners
     * if it is not {@code null}. Duplicate listeners are allowed.
     * </p>
     *
     * @param listener the listener to add; must implement the callback interface
     */
    public void add(T listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /**
     * Removes a listener from this callback.
     * <p>
     * If the provided listener is present in the internal list, it is removed.
     * If the listener is {@code null} or not present in the list, no action is taken.
     * </p>
     *
     * @param listener the listener to remove
     */
    public void remove(T listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }
}

