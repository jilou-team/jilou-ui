package com.jilou.ui.container;

import com.jilou.ui.ApplicationFactory;
import com.jilou.ui.JilouUI;
import com.jilou.ui.enums.Backend;
import com.jilou.ui.enums.WindowStates;
import com.jilou.ui.logic.AbstractRenderer;
import com.jilou.ui.logic.Renderer;
import com.jilou.ui.logic.callbacks.NativeCallbacks.*;
import com.jilou.ui.logic.graphics.WidgetBackgroundRenderer;
import com.jilou.ui.logic.graphics.tools.GLCalculate;
import com.jilou.ui.logic.input.KeyBoard;
import com.jilou.ui.logic.input.Mouse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWCharModsCallbackI;
import org.lwjgl.glfw.GLFWCursorEnterCallbackI;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import org.lwjgl.glfw.GLFWWindowFocusCallbackI;
import org.lwjgl.glfw.GLFWWindowIconifyCallbackI;
import org.lwjgl.glfw.GLFWWindowMaximizeCallbackI;
import org.lwjgl.glfw.GLFWWindowPosCallbackI;
import org.lwjgl.glfw.GLFWWindowRefreshCallbackI;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Represents a native GLFW window for creating custom application windows.
 * <p>
 * The {@link LWJGLWindow} class directly interfaces with GLFW to provide
 * low-level windowing functionality. It is designed for advanced users who
 * need fine-grained control over the window behavior.
 * </p>
 * <p>
 * This class is primarily used by the {@link Window} class, which inherits
 * from the {@link AbstractWindow} class. The {@link AbstractWindow} class,
 * in turn, extends {@link LWJGLWindow}. For most use cases, it is highly
 * recommended to use the {@link AbstractWindow} class instead of
 * {@link LWJGLWindow} directly, as it provides all necessary features and
 * simplifies the window management process.
 * </p>
 *
 * @since 0.1.0
 * @see AbstractWindow
 * @see Window
 * @author Daniel Ramke
 */
public abstract class LWJGLWindow {

    private static final Logger LOGGER = LogManager.getLogger(LWJGLWindow.class);

    private static final String ERROR_TEXT = "Interrupted while waiting for window handle";
    private static final Backend DEFAULT_BACKEND = Backend.OPENGL;

    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;

    private final List<Renderer> renderers = new ArrayList<>();

    private final List<LWJGLWindow> nativeWindows = new ArrayList<>();

    private Thread windowThread;

    private long windowHandle;
    private final String localizedName;

    private Context context;

    private Backend backend;
    private WindowStates windowStates;

    private String title;
    protected int width;
    protected int height;

    private NativeSizeCallback sizeCallback;
    private NativePositionCallback positionCallback;
    private NativeFrameBufferSizeCallback frameBufferSizeCallback;

    private NativeScrollCallback scrollCallback;
    private NativeMouseButtonCallback mouseButtonCallback;
    private NativeMouseEnteredCallback mouseEnteredCallback;
    private NativeMousePositionCallback mousePositionCallback;

    private NativeKeyCallback keyCallback;
    private NativeCharCallback charCallback;
    private NativeCharModsCallback charModsCallback;

    private NativeCloseCallback closeCallback;
    private NativeFocusCallback focusCallback;
    private NativeMaximizedCallback maximizedCallback;
    private NativeMinimizedCallback minimizedCallback;
    private NativeRefreshCallback refreshCallback;

    /**
     * Constructor create a new native Window which used {@link GLFW} raw. It is recommended to use
     * {@link AbstractWindow} if you wish to create your own one which is compatible with {@link JilouUI} itself.
     * This class can be used of you write your own project which not need our Features.
     * @param localizedName the identifier as {@link String}
     */
    protected LWJGLWindow(String localizedName) {
        this.localizedName = generateLocalizedName(localizedName == null ? getClass().getSimpleName() : localizedName);
        this.windowHandle = 0L;
        this.title = getClass().getSimpleName();
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
        this.backend = DEFAULT_BACKEND;
        this.windowStates = WindowStates.DECLARING;
    }

    /**
     * Function to is called at {@link #start()}. This will be order the correct workflow
     * of the {@link LWJGLWindow}. This is needed for waiting at VULKAN or OPENGL have load
     * his context!
     */
    protected void run() {
        initialize();
        nativeUpdate();
        destroy();
    }

    /* ############################################################################################
     *
     *                                   Implemented Functions
     *
     * ############################################################################################ */

    /**
     * Function for initialize classes or other things. This is called one time after using the {@link #start()} command.
     * It is called at {@link #run()}.
     */
    protected abstract void initialize();

    /**
     * Function for continually update the current {@link LWJGLWindow}. This is absolute needed for render
     * Objects to your window. Please note that this function is working with the context directly.
     * Changes on the base system have damage impact to your PC!
     */
    protected abstract void nativeUpdate();

    /**
     * Function is called at the last position in the {@link #run()} function. It will clean up you memory.
     * If you want to clear your own created stuff after closing the {@link LWJGLWindow} use the function!
     */
    protected abstract void destroy();

    /* ############################################################################################
     *
     *                                       Thread Management
     *
     * ############################################################################################ */

    /**
     * Function which starts the {@link LWJGLWindow} thread. In task order it will call the {@link #run()}
     * function for handle the {@link Thread} life cycle. After reaching the {@link #nativeUpdate()} function
     * this will loop all time. To stop this {@link Thread} it is recommended to use {@link #stop()}!
     */
    public void start() {
        if (windowThread != null && windowThread.isAlive()) {
            LOGGER.warn("Window [ {} ] thread already started", localizedName);
            return;
        }

        windowThread = new Thread(Thread.currentThread().getThreadGroup(), () -> {
            try {
                run();
            } catch (Exception exception) {
                LOGGER.error("There was an unexpected error: ", exception);
            }
        }, localizedName);
        windowThread.start();
    }

    /**
     * Function for stopping the current {@link Thread}. This will close the {@link LWJGLWindow}.
     * The {@link #close()} function is called at this function. This way is the recommend for closing an existing
     * {@link LWJGLWindow}.
     */
    public void stop() {
        if (windowThread != null && windowThread.isAlive()) {
            close();
            try {
                windowThread.join();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                LOGGER.error("Interrupted while stopping window thread", exception);
            }
        } else {
            LOGGER.warn("Window [ {} ] thread already stopped", localizedName);
        }
    }


    /* ############################################################################################
     *
     *                                       Functions
     *
     * ############################################################################################ */

    /**
     * Function to set the {@link LWJGLWindow} always on top of all other windows.
     * @param alwaysOnTop the state
     */
    public void alwaysOnTop(boolean alwaysOnTop) {
        awaitHandle(() -> GLFW.glfwSetWindowAttrib(getWindowHandle(), GLFW.GLFW_FLOATING, alwaysOnTop ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE));
    }

    /**
     * Function to set the {@link LWJGLWindow} always on top of all other windows.
     * This switch the state by calling {@link #alwaysOnTop(boolean)}.
     */
    public void alwaysOnTop() {
        this.alwaysOnTop(!isAlwaysOnTop());
    }

    /**
     * Function to trigger the {@link GLFW#glfwSetWindowShouldClose(long, boolean)} function. This is better because
     * we handle it async!
     */
    public void close() {
        awaitHandle(() -> GLFW.glfwSetWindowShouldClose(getWindowHandle(), true));
    }

    /**
     * Function to change the current focus to our {@link LWJGLWindow}.
     * @param focus the state
     */
    public void focus(boolean focus) {
        if(focus) {
            awaitHandle(() -> GLFW.glfwFocusWindow(getWindowHandle()));
            return;
        }
        this.restore();
    }

    /**
     * Function to change the current focus to our {@link LWJGLWindow}.
     * This switch the state by calling {@link #focus(boolean)}.
     */
    public void focus() {
        this.focus(!isFocused());
    }

    /**
     * Function to hide the {@link LWJGLWindow}. Can be shown after calling
     * {@link #show()}.
     */
    public void hide() {
        if(isVisible()) {
            windowStates = WindowStates.INACTIVE;
            awaitHandle(() -> GLFW.glfwHideWindow(getWindowHandle()));
        }
    }

    /**
     * Function to restore the complete {@link LWJGLWindow}. This is called at many
     * internal functions.
     */
    public void restore() {
        awaitHandle(() -> GLFW.glfwRestoreWindow(getWindowHandle()));
    }

    /**
     * Function to show the {@link LWJGLWindow}. Can be hide after calling
     * {@link #hide()}.
     */
    public void show() {
        if(!isVisible()) {
            windowStates = WindowStates.ACTIVE;
            awaitHandle(() -> GLFW.glfwShowWindow(getWindowHandle()));
        }
    }

    /**
     * Function maximized the {@link LWJGLWindow} to the fit of virtual desktop.
     * Note this can make problems if there is no virtual desktop.
     * @param maximize the state
     */
    public void maximize(boolean maximize) {
        if(maximize) {
            awaitHandle(() -> GLFW.glfwMaximizeWindow(getWindowHandle()));
            return;
        }
        this.restore();
    }

    /**
     * Function maximized the {@link LWJGLWindow} to the fit of virtual desktop.
     * Note this can make problems if there is no virtual desktop.
     * This switch the state by calling {@link #maximize(boolean)}.
     */
    public void maximize() {
        this.maximize(!isMaximized());
    }

    /**
     * Function minimized the {@link LWJGLWindow} to the task bar.
     * It is similar to {@link GLFW#glfwIconifyWindow(long)} function.
     * @param minimize the state
     */
    public void minimize(boolean minimize) {
        if(minimize) {
            awaitHandle(() -> GLFW.glfwIconifyWindow(getWindowHandle()));
            return;
        }
        this.restore();
    }

    /**
     * Function minimized the {@link LWJGLWindow} to the task bar.
     * It is similar to {@link GLFW#glfwIconifyWindow(long)} function.
     * This switch the state by calling {@link #minimize(boolean)}.
     */
    public void minimize() {
        this.minimize(!isMinimized());
    }

    /**
     * Function to set the current {@link LWJGLWindow} title. If the title
     * null it will set to {@link #getType()}.
     * @param title your title message
     */
    public void setTitle(String title) {
        if(title == null) {
            title = getType();
        }
        this.title = title;
        String finalTitle = title;
        awaitHandle(() -> GLFW.glfwSetWindowTitle(getWindowHandle(), finalTitle));
    }

    /**
     * Function to set the current {@link LWJGLWindow} width in the virtual desktop.
     * @param width the new window width.
     */
    public void setWidth(int width) {
        if(width < 0) {
            width = 0;
        }
        this.width = width;
        int finalWidth = width;
        awaitHandle(() -> GLFW.glfwSetWindowSize(getWindowHandle(), finalWidth, getHeight()));
    }

    /**
     * Function to set the current {@link LWJGLWindow} height in the virtual desktop.
     * @param height the new window height.
     */
    public void setHeight(int height) {
        if(height < 0) {
            height = 0;
        }
        this.height = height;
        int finalHeight = height;
        awaitHandle(() -> GLFW.glfwSetWindowSize(getWindowHandle(), getWidth(), finalHeight));
    }

    /**
     * Function to set the active render backend which is by default {@link Backend#OPENGL}.
     * This can't be change if the window has pass {@link WindowStates#ACTIVE} or {@link WindowStates#INACTIVE}.
     * @param backend switch opengl or vulkan.
     */
    public void setBackend(Backend backend) {
        if(windowStates.equals(WindowStates.INACTIVE) || windowStates.equals(WindowStates.ACTIVE)) {
            LOGGER.warn("Not allowed to swap the backend in this state [ {} ]", windowStates.name());
            return;
        }
        if(backend == null) {
            backend = DEFAULT_BACKEND;
        }
        this.backend = backend;
    }

    /* ############################################################################################
     *
     *                                    Renderer Function
     *
     * ############################################################################################ */

    /**
     * Function add a new {@link Renderer}. It is recommended to use a {@link AbstractRenderer} for handle
     * rendering in {@link JilouUI}. You can register own renderer to if you use our {@link Renderer} interface.
     * @param renderer the {@link Renderer} object.
     */
    public void addRenderer(Renderer renderer) {
        if(hasRenderer(renderer)) {
            return;
        }
        renderers.add(renderer);
        LOGGER.info("Renderer [ {} ] added to [ {} ]", renderer.getName(), getWindowHandle());
    }

    /**
     * Function removes a {@link Renderer} from this window. Note that this
     * {@link Renderer} is removed by {@link #removeRenderer(String)} and used his name.
     * @param renderer to remove
     */
    public void removeRenderer(Renderer renderer) {
        if(!(hasRenderer(renderer))) {
            return;
        }
        removeRenderer(renderer.getName());
    }

    /**
     * Function removes an existing {@link Renderer} by the given name. Is the name null
     * this function will be returned without a warning! {@link Renderer#dispose()} is called
     * at this function.
     * @param name {@link Renderer#getName()} as example.
     */
    public void removeRenderer(String name) {
        if(name == null) {
            return;
        }
        Renderer renderer = getRenderer(name);
        if(!(hasRenderer(renderer))) {
            return;
        }
        renderer.dispose();
        renderers.remove(renderer);
        LOGGER.info("Renderer [ {} ] removed from [ {} ]", renderer.getName(), getWindowHandle());
    }

    /**
     * Function removes an existing {@link AbstractRenderer} by the given id. Is the {@link Renderer} object
     * not an instance of {@link AbstractRenderer} it will return with a warning.
     * Is all correct it will call {@link #removeRenderer(String)} for clean up.
     * @param id {@link AbstractRenderer#getId()} as example.
     */
    public void removeRenderer(long id) {
        AbstractRenderer renderer = null;
        for(Renderer entry : renderers) {
            if(entry instanceof AbstractRenderer abstractRenderer && abstractRenderer.getId() == id) {
                renderer = abstractRenderer;
                break;
            }
        }

        if(renderer == null) {
            LOGGER.warn("No renderer was found by id [ {} ], make sure it is implements AbstractRenderer!", id);
            return;
        }
        removeRenderer(renderer.getName());
    }

    /**
     * Function removes all {@link Renderer}'s which included in {@link LWJGLWindow}.
     * This is perfect for clean up functions!
     */
    public void removeAllRenderers() {
        for(Renderer renderer : renderers) {
            renderer.dispose();
        }
        renderers.clear();
        LOGGER.info("Removed all renderers!");
    }

    /**
     * Function checks if a {@link Renderer} was found.
     * This is handled by {@link #hasRenderer(String)} function.
     * @param renderer renderer object which need to be searched.
     * @return {@link Boolean}- true if there was a renderer found.
     */
    public boolean hasRenderer(Renderer renderer) {
        return hasRenderer(renderer.getName());
    }

    /**
     * Function checks if a {@link Renderer} was found.
     * This called the {@link #getRenderer(String)} by the given name.
     * If it gets a null it will return false.
     * @param name {@link Renderer#getName()} as example
     * @return {@link Boolean}- true if there was a renderer found.
     */
    public boolean hasRenderer(String name) {
        return getRenderer(name) != null;
    }

    /**
     * Function for get one {@link Renderer} from the {@link List} of renderers.
     * This fetch the {@link Renderer} by his name.
     * @param name {@link Renderer#getName()} as example
     * @return {@link Renderer}- null if there was no {@link Renderer} found.
     */
    public Renderer getRenderer(String name) {
        Renderer found = null;
        for(Renderer renderer : renderers) {
            if(renderer.getName().equals(name)) {
                found = renderer;
                break;
            }
        }
        return found;
    }

    /**
     * Function returned all {@link Renderer}'s which current used by {@link LWJGLWindow}.
     * @return {@link List}<{@link Renderer}> - all {@link Renderer}'s which used by this window.
     */
    public List<Renderer> getRenderers() {
        return renderers;
    }

    /* ############################################################################################
     *
     *                                       Getter / Setter
     *
     * ############################################################################################ */

    /**
     * Function for check the state of {@link #alwaysOnTop()} synced.
     * @return {@link Boolean}- current state.
     */
    public boolean isAlwaysOnTop() {
        long handle = blockingHandle();
        return GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_FLOATING) == GLFW.GLFW_TRUE;
    }

    /**
     * Function for check the state of {@link #alwaysOnTop()} async.
     * @return {@link Boolean}- current state.
     */
    public CompletableFuture<Boolean> isAlwaysOnTopAsync() {
        return awaitHandle().thenApply(handle ->
            GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_FLOATING) == GLFW.GLFW_TRUE
        );
    }

    /**
     * Function for check the state of {@link #close()} synced.
     * @return {@link Boolean}- current state.
     */
    public boolean isClosing() {
        long handle = blockingHandle();
        return GLFW.glfwWindowShouldClose(handle);
    }

    /**
     * Function for check the state of {@link #close()} async.
     * @return {@link Boolean}- current state.
     */
    public CompletableFuture<Boolean> isClosingAsync() {
        return awaitHandle().thenApply(GLFW::glfwWindowShouldClose);
    }

    /**
     * Function for check the state of {@link #focus()} synced.
     * @return {@link Boolean}- current state.
     */
    public boolean isFocused() {
        long handle = blockingHandle();
        return GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_FOCUSED) == GLFW.GLFW_TRUE;
    }

    /**
     * Function for check the state of {@link #focus()} async.
     * @return {@link Boolean}- current state.
     */
    public CompletableFuture<Boolean> isFocusedAsync() {
        return awaitHandle().thenApply(handle ->
                GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_FOCUSED) == GLFW.GLFW_TRUE
        );
    }

    /**
     * Function for check the state of {@link #maximize()} synced.
     * @return {@link Boolean}- current state.
     */
    public boolean isMaximized() {
        long handle = blockingHandle();
        return GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_MAXIMIZED) == GLFW.GLFW_TRUE;
    }

    /**
     * Function for check the state of {@link #maximize()} async.
     * @return {@link Boolean}- current state.
     */
    public CompletableFuture<Boolean> isMaximizedAsync() {
        return awaitHandle().thenApply(handle ->
                GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_MAXIMIZED) == GLFW.GLFW_TRUE
        );
    }

    /**
     * Function for check the state of {@link #minimize()} synced.
     * @return {@link Boolean}- current state.
     */
    public boolean isMinimized() {
        long handle = blockingHandle();
        return GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_ICONIFIED) == GLFW.GLFW_TRUE;
    }

    /**
     * Function for check the state of {@link #minimize()} async.
     * @return {@link Boolean}- current state.
     */
    public CompletableFuture<Boolean> isMinimizedAsync() {
        return awaitHandle().thenApply(handle ->
                GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_ICONIFIED) == GLFW.GLFW_TRUE
        );
    }

    /**
     * Function for check the state of {@link #show()} or {@link #hide()} synced.
     * @return {@link Boolean}- current state.
     */
    public boolean isVisible() {
        long handle = blockingHandle();
        return GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_VISIBLE) == GLFW.GLFW_TRUE;
    }

    /**
     * Function for check the state of {@link #show()} or {@link #hide()} async.
     * @return {@link Boolean}- current state.
     */
    public CompletableFuture<Boolean> isVisibleAsync() {
        return awaitHandle().thenApply(handle ->
                GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_VISIBLE) == GLFW.GLFW_TRUE
        );
    }

    /**
     * Function returns simple {@link String} which is represented your class name.
     * @return {@link String}- the class name by {@link Class#getSimpleName()}.
     */
    public String getType() {
        return getClass().getSimpleName();
    }

    /**
     * Function gets the current {@link LWJGLWindow} width in the virtual desktop.
     * @return {@link Integer}- current window width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Function gets the current {@link LWJGLWindow} height in the virtual desktop.
     * @return {@link Integer}- current window height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Function returns the current window title.
     * This was set by {@link #setTitle(String)}.
     * @return {@link String}- current title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Function returns the current running {@link LWJGLWindow} Thread.
     * The thread can be start with {@link #start()}.
     * @return {@link Thread}- active window thread.
     */
    public Thread getWindowThread() {
        return windowThread;
    }

    /**
     * Function returns the current {@link LWJGLWindow} state given by {@link WindowStates}.
     * @return {@link WindowStates}- current window state.
     */
    public WindowStates getWindowStates() {
        return windowStates;
    }

    /**
     * Function returns the used {@link Backend} of the {@link LWJGLWindow}.
     * @return {@link Backend}- by default {@link Backend#OPENGL}.
     */
    public Backend getBackend() {
        return backend;
    }

    /**
     * Function returned the {@link Context} of the created {@link LWJGLWindow}.
     * Context stored the current {@link #getWindowHandle()}.
     * @return {@link Context}- current window context.
     */
    public Context getContext() {
        return context;
    }

    /**
     * Function returned the current {@link LWJGLWindow} list. This is deprecated because
     * we working on {@link ApplicationFactory} which is handle it later.
     * @return {@link List}<{@link LWJGLWindow}> - list of all windows.
     * @deprecated
     */
    @Deprecated(since = "0.1.0", forRemoval = true)
    public List<LWJGLWindow> getNativeWindows() {
        return nativeWindows;
    }

    /**
     * Function returns the generated handle which is native from {@link GLFW#glfwCreateWindow(int, int, ByteBuffer, long, long)}.
     * Note: use {@link #getContext()} instand of this!
     * @return {@link Long}- current generated native handle.
     */
    public long getWindowHandle() {
        return windowHandle;
    }

    /**
     * Function returns the generated secure name of this {@link LWJGLWindow}. This is
     * better to use as {@link #getWindowHandle()} because it is human-readable.
     * @return {@link String}- named identifier.
     */
    public String getLocalizedName() {
        return localizedName;
    }

    /* ############################################################################################
     *
     *                                       Callbacks
     *
     * ############################################################################################ */

    /**
     * Adds a size callback to handle window size changes.
     *
     * @param lambda the {@link GLFWWindowSizeCallbackI} implementation to handle size changes
     */
    public void addSizeCallback(GLFWWindowSizeCallbackI lambda) {
        this.sizeCallback.add(lambda);
    }

    /**
     * Adds a position callback to handle window position changes.
     *
     * @param lambda the {@link GLFWWindowPosCallbackI} implementation to handle position changes
     */
    public void addPositionCallback(GLFWWindowPosCallbackI lambda) {
        this.positionCallback.add(lambda);
    }

    /**
     * Adds a framebuffer size callback to handle framebuffer size changes.
     *
     * @param lambda the {@link GLFWFramebufferSizeCallbackI} implementation to handle framebuffer size changes
     */
    public void addFrameBufferSizeCallback(GLFWFramebufferSizeCallbackI lambda) {
        this.frameBufferSizeCallback.add(lambda);
    }

    /**
     * Adds a close callback to handle window close events.
     *
     * @param lambda the {@link GLFWWindowCloseCallbackI} implementation to handle close events
     */
    public void addCloseCallback(GLFWWindowCloseCallbackI lambda) {
        this.closeCallback.add(lambda);
    }

    /**
     * Adds a focus callback to handle window focus changes.
     *
     * @param lambda the {@link GLFWWindowFocusCallbackI} implementation to handle focus changes
     */
    public void addFocusCallback(GLFWWindowFocusCallbackI lambda) {
        this.focusCallback.add(lambda);
    }

    /**
     * Adds a maximize callback to handle window maximize events.
     *
     * @param lambda the {@link GLFWWindowMaximizeCallbackI} implementation to handle maximize events
     */
    public void addMaximizeCallback(GLFWWindowMaximizeCallbackI lambda) {
        this.maximizedCallback.add(lambda);
    }

    /**
     * Adds a minimize callback to handle window minimize events.
     *
     * @param lambda the {@link GLFWWindowIconifyCallbackI} implementation to handle minimize events
     */
    public void addMinimizeCallback(GLFWWindowIconifyCallbackI lambda) {
        this.minimizedCallback.add(lambda);
    }

    /**
     * Adds a refresh callback to handle window refresh events.
     *
     * @param lambda the {@link GLFWWindowRefreshCallbackI} implementation to handle refresh events
     */
    public void addRefreshCallback(GLFWWindowRefreshCallbackI lambda) {
        this.refreshCallback.add(lambda);
    }

    /**
     * Adds a key callback to handle keyboard input events.
     *
     * @param lambda the {@link GLFWKeyCallbackI} implementation to handle key events
     */
    public void addKeyCallback(GLFWKeyCallbackI lambda) {
        this.keyCallback.add(lambda);
    }

    /**
     * Adds a character callback to handle character input events.
     *
     * @param lambda the {@link GLFWCharCallbackI} implementation to handle character input
     */
    public void addCharCallback(GLFWCharCallbackI lambda) {
        this.charCallback.add(lambda);
    }

    /**
     * Adds a character mods callback to handle character input events with modifier keys.
     *
     * @param lambda the {@link GLFWCharModsCallbackI} implementation to handle character input with modifiers
     */
    public void addCharModCallback(GLFWCharModsCallbackI lambda) {
        this.charModsCallback.add(lambda);
    }

    /**
     * Adds a mouse button callback to handle mouse button events.
     *
     * @param lambda the {@link GLFWMouseButtonCallbackI} implementation to handle mouse button events
     */
    public void addMouseButtonCallback(GLFWMouseButtonCallbackI lambda) {
        this.mouseButtonCallback.add(lambda);
    }

    /**
     * Adds a mouse entered callback to handle mouse cursor entry and exit events.
     *
     * @param lambda the {@link GLFWCursorEnterCallbackI} implementation to handle mouse entry/exit events
     */
    public void addMouseEnteredCallback(GLFWCursorEnterCallbackI lambda) {
        this.mouseEnteredCallback.add(lambda);
    }

    /**
     * Adds a mouse position callback to handle mouse movement events.
     *
     * @param lambda the {@link GLFWCursorPosCallbackI} implementation to handle mouse position changes
     */
    public void addMousePositionCallback(GLFWCursorPosCallbackI lambda) {
        this.mousePositionCallback.add(lambda);
    }

    /**
     * Adds a scroll callback to handle mouse scroll wheel events.
     *
     * @param lambda the {@link GLFWScrollCallbackI} implementation to handle scroll wheel events
     */
    public void addScrollCallback(GLFWScrollCallbackI lambda) {
        this.scrollCallback.add(lambda);
    }


    /* ############################################################################################
     *
     *                                  Internal Functions (Package)
     *
     * ############################################################################################ */

    /**
     * Function which generates a native {@link GLFW#glfwCreateWindow(int, int, ByteBuffer, long, long)} and bind this
     * to {@link LWJGLWindow}. This function can be used by any from {@link LWJGLWindow} extended window. It
     * will provide the handle, name, buffers and all native stuff. If you like to create directly a
     * window by Vulkan or OpenGL you need to write by ur self. We have not planed this as a feature!
     */
    protected void generateNativeWindow() {
        this.windowStates = WindowStates.BUILDING;
        LOGGER.info("Generating native window with backend [ {} ]", backend);

        if (backend == null) {
            backend = DEFAULT_BACKEND;
        }

        if(backend.equals(Backend.VULKAN)) {
            LOGGER.info("Vulkan not supported yet! Look at GitHub for Information: [{}]", "https://github.com/jilou-team/jilou-ui");
            backend = DEFAULT_BACKEND;
        }

        switch (backend) {
            case VULKAN:
                LOGGER.info("Initializing Vulkan backend...");
                GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_NO_API);
                break;
            case OPENGL:
                LOGGER.info("Initializing OpenGL backend...");
                GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_OPENGL_API);
                GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4); // OpenGL 4.x
                GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);
                GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_COMPAT_PROFILE);
                break;

            default:
                throw new IllegalStateException("Failed to create the GLFW window with backend [ " + backend + " ]");

        }

        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        this.windowHandle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (windowHandle <= 0L) {
            throw new IllegalStateException("Failed to create the GLFW window with backend [ " + backend + " ]");
        }

        initBackend();
        bindGLFWCallbacks();
        registerDefaultRenderers();

        LOGGER.info("Native window created successfully with backend [ {} ]", backend);
        windowStates = WindowStates.INACTIVE;
        nativeWindows.add(this);
    }

    /**
     * Function to initialize the used backend. This is used at {@link #generateNativeWindow()}.
     * Note that DirectX is not supported!
     * @throws IllegalStateException - if there was no backend found!
     */
    protected void initBackend() {
        switch (backend) {
            case VULKAN:
                initVulkan();
                break;
            case OPENGL:
                initOpenGL();
                break;
            default:
                throw new IllegalStateException("Unsupported Render Backend: " + backend);
        }
    }

    /**
     * Function send the render func to the correct backend. This is needed
     * because we allow the usage of {@link Backend#OPENGL} and {@link Backend#VULKAN}.
     * Both of them have other ways to render the things.
     * @param func your lambda code which need execution.
     */
    protected void renderNative(Runnable func) {
        switch (backend) {
            case VULKAN:
                renderVulkan(func);
                break;
            case OPENGL:
                renderOpenGL(func);
                break;
            default:
                throw new IllegalStateException("Unsupported Render Backend: " + backend);
        }
    }

    /* ############################################################################################
     *
     *                                      Internal Registry
     *
     * ############################################################################################ */

    /**
     * Function registered default {@link Renderer}. For example
     * {@link WidgetBackgroundRenderer} is registered by default.
     */
    private void registerDefaultRenderers() {
        addRenderer(new WidgetBackgroundRenderer());
    }

    /* ############################################################################################
     *
     *                                  Internal Functions (Class)
     *
     * ############################################################################################ */

    /**
     * Function initialize {@link Backend#VULKAN} systems.
     * Currently, not in use!
     */
    private void initVulkan() {
        LOGGER.info("Vulkan not supported!");
    }

    /**
     * Function update {@link Backend#VULKAN} systems.
     * Currently, not in use!
     */
    private void renderVulkan(Runnable func) {
        renderOpenGL(func);
    }

    /**
     * Function initialize {@link Backend#OPENGL} systems.
     */
    private void initOpenGL() {
        GLFW.glfwMakeContextCurrent(windowHandle);
        GLFW.glfwSwapInterval(1);

        GLCapabilities capabilities = GL.createCapabilities();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        this.context = new Context(windowHandle, capabilities);
    }

    /**
     * Function update {@link Backend#OPENGL} systems.
     */
    private void renderOpenGL(Runnable func) {
        GLCalculate.updateProjectionMatrix(width, height);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glViewport(0, 0, width, height);
        GL11.glClearColor(0.2f, 0.3f, 0.4f, 1.0f);

        for(Renderer renderer : renderers) {
            renderer.render(this);
        }

        if (func != null) {
            func.run();
        }

        GLFW.glfwSwapBuffers(windowHandle);
        GLFW.glfwPollEvents();
    }

    /**
     * Binds all GLFW window callbacks to their respective native callback implementations.
     * <p>
     * This method initializes and sets up each GLFW callback for the specified window.
     * It registers the following callbacks:
     * </p>
     * <ul>
     *   <li>Window Size</li>
     *   <li>Window Position</li>
     *   <li>Framebuffer Size</li>
     *   <li>Window Close</li>
     *   <li>Window Focus</li>
     *   <li>Window Iconify (Minimize)</li>
     *   <li>Window Maximize</li>
     *   <li>Window Refresh</li>
     *   <li>Character Input</li>
     *   <li>Character Input with Modifiers</li>
     *   <li>Key Input</li>
     *   <li>Mouse Button</li>
     *   <li>Mouse Position</li>
     *   <li>Mouse Enter/Exit</li>
     *   <li>Mouse Scroll</li>
     * </ul>
     * <p>
     * Each callback is registered with GLFW and optionally supplemented with additional functionality
     * through external handler methods such as {@code KeyBoard::callback} or {@code Mouse::callback}.
     * </p>
     *
     * @see GLFW
     * @see Callbacks
     */
    private void bindGLFWCallbacks() {
        this.sizeCallback = new NativeSizeCallback();
        this.sizeCallback.add(GLFW.glfwSetWindowSizeCallback(windowHandle, sizeCallback));

        this.positionCallback = new NativePositionCallback();
        this.positionCallback.add(GLFW.glfwSetWindowPosCallback(windowHandle, positionCallback));

        this.frameBufferSizeCallback = new NativeFrameBufferSizeCallback();
        this.frameBufferSizeCallback.add(GLFW.glfwSetFramebufferSizeCallback(windowHandle, frameBufferSizeCallback));

        this.closeCallback = new NativeCloseCallback();
        this.closeCallback.add(GLFW.glfwSetWindowCloseCallback(windowHandle, closeCallback));

        this.focusCallback = new NativeFocusCallback();
        this.focusCallback.add(GLFW.glfwSetWindowFocusCallback(windowHandle, focusCallback));

        this.minimizedCallback = new NativeMinimizedCallback();
        this.minimizedCallback.add(GLFW.glfwSetWindowIconifyCallback(windowHandle, minimizedCallback));

        this.maximizedCallback = new NativeMaximizedCallback();
        this.maximizedCallback.add(GLFW.glfwSetWindowMaximizeCallback(windowHandle, maximizedCallback));

        this.refreshCallback = new NativeRefreshCallback();
        this.refreshCallback.add(GLFW.glfwSetWindowRefreshCallback(windowHandle, refreshCallback));

        this.charCallback = new NativeCharCallback();
        this.charCallback.add(GLFW.glfwSetCharCallback(windowHandle, charCallback));

        this.charModsCallback = new NativeCharModsCallback();
        this.charModsCallback.add(GLFW.glfwSetCharModsCallback(windowHandle, charModsCallback));

        this.keyCallback = new NativeKeyCallback();
        this.keyCallback.add(GLFW.glfwSetKeyCallback(windowHandle, keyCallback));
        this.keyCallback.add(KeyBoard::callback);

        this.mouseButtonCallback = new NativeMouseButtonCallback();
        this.mouseButtonCallback.add(GLFW.glfwSetMouseButtonCallback(windowHandle, mouseButtonCallback));
        this.mouseButtonCallback.add(Mouse::callback);

        this.mousePositionCallback = new NativeMousePositionCallback();
        this.mousePositionCallback.add(GLFW.glfwSetCursorPosCallback(windowHandle, mousePositionCallback));
        this.mousePositionCallback.add(Mouse::positionCallback);

        this.mouseEnteredCallback = new NativeMouseEnteredCallback();
        this.mouseEnteredCallback.add(GLFW.glfwSetCursorEnterCallback(windowHandle, mouseEnteredCallback));
        this.mouseEnteredCallback.add(Mouse::enteredCallback);

        this.scrollCallback = new NativeScrollCallback();
        this.scrollCallback.add(GLFW.glfwSetScrollCallback(windowHandle, scrollCallback));
        this.scrollCallback.add(Mouse::scrollCallback);
    }

    /**
     * Function await for your lambda code. This is needed because {@link LWJGLWindow}
     * is native and will perform little later than our runtime code.
     * @param action your lambda code which need execution.
     */
    private void awaitHandle(Runnable action) {
        if (windowHandle != 0L) {
            action.run();
        } else {
            CompletableFuture.runAsync(() -> {
                while (windowHandle == 0L) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                        throw new IllegalStateException(ERROR_TEXT, exception);
                    }
                }
                action.run();
            });
        }
    }

    /**
     * @return {@link CompletableFuture}<{@link Long}> - return await long handle.
     */
    private CompletableFuture<Long> awaitHandle() {
        return CompletableFuture.supplyAsync(() -> {
            while (windowHandle == 0L) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException(ERROR_TEXT, exception);
                }
            }
            return windowHandle;
        });
    }

    /**
     * Function to block the current {@link Thread}. After the {@link LWJGLWindow}
     * is finished with his task the {@link Thread} will work again.
     * @return {@link Long} - return sync long handle.
     */
    private long blockingHandle() {
        while (windowHandle == 0L) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(ERROR_TEXT, e);
            }
        }
        return windowHandle;
    }

    /**
     * Function which generate secure names for the {@link LWJGLWindow}. This is useful
     * if you don't wanna save your names in lists.
     * @param localizedName the placeholder name.
     * @return {@link String}- the generated saver name.
     */
    private String generateLocalizedName(String localizedName) {
        if (nativeWindows.isEmpty()) {
            return localizedName;
        }

        String buildName = localizedName;
        int foundCount = 0;
        for (LWJGLWindow window : nativeWindows) {
            if (buildName.equalsIgnoreCase(window.generateLocalizedName(localizedName))) {
                foundCount++;
            }
        }

        if (foundCount > 0) {
            buildName = localizedName + "-" + foundCount;
        }

        return buildName;
    }

}
