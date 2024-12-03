package com.jilou.ui.container;

/**
 * An abstract representation of a window built on top of the {@link LWJGLWindow}.
 * <p>
 * The {@link AbstractWindow} class provides a high-level framework for creating and managing
 * windows in the JilouUI library. It includes lifecycle methods for setting up, updating,
 * and rendering the window, while handling the native GLFW window operations internally.
 * </p>
 * <p>
 * Subclasses must implement the following abstract methods:
 * <ul>
 *   <li>{@link #setup()}: Initialize resources and configure the window.</li>
 *   <li>{@link #update(float)}: Handle per-frame updates (e.g., logic updates).</li>
 *   <li>{@link #render()}: Define the rendering logic for each frame.</li>
 * </ul>
 * </p>
 * <p>
 * The {@link AbstractWindow} also provides an option to configure whether rendering
 * should continue when the window is minimized via the {@link #setRenderAtMinimized(boolean)} method.
 * </p>
 *
 * @since 0.1.0
 * @see LWJGLWindow
 * @author Daniel Ramke
 */
public abstract class AbstractWindow extends LWJGLWindow {

    private boolean renderAtMinimized;
    private boolean useDefaultCallbacks;

    /**
     * Constructs an {@link AbstractWindow} with the specified localized name.
     *
     * @param localizedName the localized name of the window, used for identification
     */
    protected AbstractWindow(String localizedName) {
        super(localizedName);
        this.renderAtMinimized = false;
        this.useDefaultCallbacks = true;
    }

    /**
     * Called during initialization to set up resources and configure the window.
     * Subclasses must implement this to define their setup logic.
     */
    protected abstract void setup();

    /**
     * Called each frame to handle update logic.
     *
     * @param delta the time elapsed since the last frame in seconds
     */
    protected abstract void update(float delta);

    /**
     * Called each frame to perform rendering logic.
     */
    protected abstract void render();

    /**
     * Initializes the native window and calls the {@link #setup()} method for custom setup logic.
     */
    @Override
    protected void initialize() {
        generateNativeWindow();
        loadDefaultCallbacks();
        setup();
    }

    /**
     * Handles the main update and rendering loop for the window.
     * The loop continues until the window is marked as closing.
     */
    @Override
    protected void nativeUpdate() {
        while (!isClosing()) {
            renderNative(this::render);
        }
    }

    /**
     * Sets whether the window should continue rendering while minimized.
     *
     * @param renderAtMinimized {@code true} if rendering should continue while minimized;
     *                          {@code false} otherwise
     */
    public void setRenderAtMinimized(boolean renderAtMinimized) {
        this.renderAtMinimized = renderAtMinimized;
    }

    /**
     * Returns whether the window is configured to render while minimized.
     *
     * @return {@code true} if rendering continues while minimized; {@code false} otherwise
     */
    public boolean isRenderAtMinimized() {
        return renderAtMinimized;
    }

    public void setUseDefaultCallbacks(boolean useDefaultCallbacks) {
        this.useDefaultCallbacks = useDefaultCallbacks;
    }

    public boolean isUseDefaultCallbacks() {
        return useDefaultCallbacks;
    }

    private void loadDefaultCallbacks() {
        if(useDefaultCallbacks) {
            addPositionCallback((handle, posX, posY) -> {

            });

            addFrameBufferSizeCallback((handle, width, height) -> {
                setWidth(width);
                setHeight(height);
            });
        }
    }
}