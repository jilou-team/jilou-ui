package com.jilou.ui.logic;

import com.jilou.ui.container.LWJGLWindow;

/**
 * Represents a rendering system for graphical elements in the JilouUI framework.
 * <p>
 * The {@code Renderer} interface defines the essential methods for managing rendering
 * tasks, including initialization, rendering, priority handling, and updating the
 * renderer's state. Implementing classes must provide specific implementations for
 * handling graphical content within a window.
 * </p>
 * <p>
 * Renderers can be associated with a {@link LWJGLWindow} and rendered in a prioritized
 * sequence based on their {@link RenderPriority}.
 * </p>
 *
 * @since 0.1.0
 * @see LWJGLWindow
 * @see RenderPriority
 * @author Daniel Ramke
 */
public interface Renderer {

    /**
     * Returns the name of the renderer. This name identifies the renderer in the system.
     * <p>
     * By default, it returns the simple name of the implementing class. Duplicated names
     * are allowed; if unique identification is needed, an ID system should be implemented
     * in the subclass.
     * </p>
     *
     * @return the name of the renderer
     */
    default String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Returns the current rendering priority of the renderer.
     * <p>
     * By default, the priority is set to {@link RenderPriority#MEDIUM}. The priority
     * can be adjusted at any time using {@link #setPriority(RenderPriority)}.
     * </p>
     *
     * @return the current render priority
     */
    default RenderPriority getPriority() {
        return RenderPriority.MEDIUM;
    }

    /**
     * Sets the render priority for the renderer.
     * <p>
     * The priority can be changed at any time, allowing dynamic adjustments to the
     * render order of the renderer.
     * </p>
     *
     * @param priority the new priority for the renderer
     */
    void setPriority(RenderPriority priority);

    /**
     * Returns the update status of the renderer.
     * <p>
     * This method checks whether the renderer is up to date, returning {@code true}
     * if no changes were detected (e.g., no widgets were modified) and {@code false}
     * if changes have occurred.
     * </p>
     *
     * @return {@code true} if the renderer is up to date, {@code false} otherwise
     */
    boolean upToDate();

    /**
     * Updates the renderer's state.
     * <p>
     * This method is called to trigger a re-render if changes are detected,
     * such as modifications to widgets or other graphical elements.
     * </p>
     */
    void update();

    /**
     * Checks if the renderer has been successfully initialized.
     * <p>
     * This method returns {@code true} if the renderer has completed its
     * initialization phase, ensuring that resources and settings are ready for rendering.
     * </p>
     *
     * @return {@code true} if the renderer is initialized, {@code false} otherwise
     */
    boolean isInitialized();

    /**
     * Initializes the renderer.
     * <p>
     * This method is called once during the initialization phase of the renderer.
     * It should include any setup code that needs to be executed only once, such
     * as creating resources or preparing graphical settings.
     * </p>
     */
    void initialize();

    /**
     * Renders content to the specified {@link LWJGLWindow}.
     * <p>
     * This method is called at regular system ticks and serves as the main rendering
     * method for the renderer. It is responsible for displaying graphical content
     * within the window, based on the renderer's current state.
     * </p>
     *
     * @param nativeWindow the window context in which to render the content
     */
    void render(LWJGLWindow nativeWindow);

    /**
     * Disposes of any resources used by the renderer.
     * <p>
     * This method is called when the renderer is unregistered and can be used for
     * cleanup tasks such as releasing resources, clearing caches, or other shutdown
     * operations.
     * </p>
     */
    void dispose();
}

