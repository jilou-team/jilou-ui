package com.jilou.ui.enums;

/**
 * Represents the various states a window can be in during its lifecycle.
 * <p>
 * The {@link WindowStates} enum defines distinct phases or conditions
 * that a window may transition through in the JilouUI library.
 * These states can be used to track or manage the window's current behavior.
 * </p>
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
public enum WindowStates {

    /**
     * Indicates that the window is currently active and operational.
     * This state is typically when the window is fully functional and rendering.
     */
    ACTIVE,

    /**
     * Indicates that the window is currently inactive.
     * This could mean the window is paused or minimized, but not destroyed.
     */
    INACTIVE,

    /**
     * Indicates that the window is in the process of being built.
     * This state is used during the initialization or setup phase before the window becomes active.
     */
    BUILDING,

    /**
     * Indicates that the window is being declared or registered.
     * This state is usually before the actual building process, where configurations are being set.
     */
    DECLARING,
}
