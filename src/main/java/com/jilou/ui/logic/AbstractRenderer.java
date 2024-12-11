package com.jilou.ui.logic;

import com.jilou.ui.ApplicationFactory;
import com.jilou.ui.container.LWJGLWindow;
import com.jilou.ui.enums.RenderPriority;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An abstract base class for creating renderers in the JilouUI framework.
 * <p>
 * The {@code AbstractRenderer} class provides a foundation for implementing custom renderers
 * that manage the rendering pipeline for graphical elements. It includes mechanisms for
 * initialization, rendering, priority management, and state tracking.
 * </p>
 *
 * <p>
 * Subclasses must implement the {@link #preLoad(LWJGLWindow)} and {@link #func(LWJGLWindow)} methods
 * to define specific behavior during the preload phase and the rendering process, respectively.
 * </p>
 *
 * @since 0.1.0
 * @see Renderer
 * @author Daniel Ramke
 */
public abstract class AbstractRenderer implements Renderer {

    private static final Logger LOGGER = LogManager.getLogger(AbstractRenderer.class);

    private final Long id;
    private final String name;

    private RenderPriority priority;
    private LWJGLWindow nativeWindow;

    private boolean initialized;
    private boolean upToDate;

    /**
     * Constructs a new {@code AbstractRenderer} with the specified name.
     * <p>
     * This constructor generates a unique ID for the renderer and assigns the specified name.
     * If the name is {@code null}, the simple name of the class is used instead.
     * </p>
     *
     * @param name the name of the renderer, or {@code null} for a default name
     */
    protected AbstractRenderer(String name) {
        this.name = name == null ? getClass().getSimpleName() : name;
        this.id = ApplicationFactory.giveNextRenderID();
        this.initialized = false;
        this.upToDate = false;
    }

    /**
     * Prepares the renderer for use by performing one-time initialization tasks.
     * <p>
     * Subclasses should use this method to load content, create resources, or set up
     * any dependencies required for rendering. It is invoked the first time the renderer
     * is used with a window.
     * </p>
     *
     * @param nativeWindow the window context in which the renderer operates
     */
    public abstract void preLoad(LWJGLWindow nativeWindow);

    /**
     * Defines the main rendering logic to be executed during the rendering phase.
     * <p>
     * Subclasses must implement this method to include their rendering code in the active
     * render pipeline. This method is called internally by {@link #render(LWJGLWindow)}.
     * </p>
     *
     * @param nativeWindow the window context in which the rendering occurs
     */
    protected abstract void func(LWJGLWindow nativeWindow);

    @Override
    public void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        LOGGER.info("Initializing Renderer [ {} ]", id);
    }

    @Override
    public void render(LWJGLWindow nativeWindow) {
        if (nativeWindow == null) {
            return;
        }

        if (this.nativeWindow == null) {
            this.nativeWindow = nativeWindow;
            preLoad(nativeWindow);
        }

        func(nativeWindow);
    }

    @Override
    public String getName() {
        return name + "-" + id;
    }

    /**
     * Returns the unique ID of this renderer.
     * <p>
     * The ID is assigned during construction and remains constant throughout the lifetime
     * of the renderer.
     * </p>
     *
     * @return the unique identifier for this renderer
     */
    public Long getId() {
        return id;
    }

    @Override
    public void setPriority(RenderPriority priority) {
        this.priority = priority;
        update();
    }

    @Override
    public RenderPriority getPriority() {
        return priority;
    }

    @Override
    public void update() {
        upToDate = !upToDate;
    }

    @Override
    public boolean upToDate() {
        return upToDate;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }
}

