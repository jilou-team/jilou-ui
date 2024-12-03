package com.jilou.ui.logic.callbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic callback handler that manages a list of callbacks of type {@code T}.
 * <p>
 * This abstract class provides methods to add and remove callbacks, allowing subclasses
 * to define specific behavior for handling or invoking these callbacks.
 *
 * @since 0.1.0
 * @author Daniel Ramke
 *
 * @param <T> the type of the callbacks managed by this class
 */
public abstract class Callback<T> {

    /**
     * A list containing all registered callbacks.
     */
    protected final List<T> callbacks = new ArrayList<>();

    /**
     * Adds a callback to the list of managed callbacks.
     * <p>
     * If the provided callback is {@code null}, it will not be added.
     *
     * @param callback the callback to be added, must not be {@code null}
     */
    public void add(T callback) {
        if (callback != null) {
            callbacks.add(callback);
        }
    }

    /**
     * Removes a callback from the list of managed callbacks.
     * <p>
     * If the provided callback is {@code null}, the method does nothing.
     *
     * @param callback the callback to be removed, must not be {@code null}
     */
    public void remove(T callback) {
        if (callback != null) {
            callbacks.remove(callback);
        }
    }
}
