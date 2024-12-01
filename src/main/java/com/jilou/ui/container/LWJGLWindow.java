package com.jilou.ui.container;

import com.jilou.ui.enums.Backend;
import com.jilou.ui.enums.WindowStates;
import com.jilou.ui.logic.Renderer;
import com.jilou.ui.logic.graphics.WidgetBackgroundRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
    private int width;
    private int height;



    protected LWJGLWindow(String localizedName) {
        this.localizedName = generateLocalizedName(localizedName == null ? getClass().getSimpleName() : localizedName);
        this.windowHandle = 0L;
        this.title = getClass().getSimpleName();
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
        this.backend = DEFAULT_BACKEND;
        this.windowStates = WindowStates.DECLARING;
    }

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

    protected abstract void initialize();

    protected abstract void nativeUpdate();

    protected abstract void destroy();

    /* ############################################################################################
     *
     *                                       Thread Management
     *
     * ############################################################################################ */

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

    public void alwaysOnTop(boolean alwaysOnTop) {
        awaitHandle(() -> GLFW.glfwSetWindowAttrib(getWindowHandle(), GLFW.GLFW_FLOATING, alwaysOnTop ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE));
    }

    public void alwaysOnTop() {
        this.alwaysOnTop(!isAlwaysOnTop());
    }

    public void close() {
        awaitHandle(() -> GLFW.glfwSetWindowShouldClose(getWindowHandle(), true));
    }

    public void focus(boolean focus) {
        if(focus) {
            awaitHandle(() -> GLFW.glfwFocusWindow(getWindowHandle()));
            return;
        }
        this.restore();
    }

    public void focus() {
        this.focus(!isFocused());
    }

    public void hide() {
        if(isVisible()) {
            windowStates = WindowStates.INACTIVE;
            awaitHandle(() -> GLFW.glfwHideWindow(getWindowHandle()));
        }
    }

    public void restore() {
        awaitHandle(() -> GLFW.glfwRestoreWindow(getWindowHandle()));
    }

    public void show() {
        if(!isVisible()) {
            windowStates = WindowStates.ACTIVE;
            awaitHandle(() -> GLFW.glfwShowWindow(getWindowHandle()));
        }
    }

    public void maximize(boolean maximize) {
        if(maximize) {
            awaitHandle(() -> GLFW.glfwMaximizeWindow(getWindowHandle()));
            return;
        }
        this.restore();
    }

    public void maximize() {
        this.maximize(!isMaximized());
    }

    public void minimize(boolean minimize) {
        if(minimize) {
            awaitHandle(() -> GLFW.glfwIconifyWindow(getWindowHandle()));
            return;
        }
        this.restore();
    }

    public void minimize() {
        this.minimize(!isMinimized());
    }

    public void setTitle(String title) {
        if(title == null) {
            title = getType();
        }
        this.title = title;
        String finalTitle = title;
        awaitHandle(() -> GLFW.glfwSetWindowTitle(getWindowHandle(), finalTitle));
    }

    public void setWidth(int width) {
        if(width < 0) {
            width = 0;
        }
        this.width = width;
        int finalWidth = width;
        awaitHandle(() -> GLFW.glfwSetWindowSize(getWindowHandle(), finalWidth, getHeight()));
    }

    public void setHeight(int height) {
        if(height < 0) {
            height = 0;
        }
        this.height = height;
        int finalHeight = height;
        awaitHandle(() -> GLFW.glfwSetWindowSize(getWindowHandle(), getWidth(), finalHeight));
    }

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

    public void addRenderer(Renderer renderer) {
        if(hasRenderer(renderer)) {
            return;
        }
        renderers.add(renderer);
        LOGGER.info("Renderer [ {} ] added to [ {} ]", renderer.getName(), getWindowHandle());
    }

    public void removeRenderer(Renderer renderer) {

    }

    public void removeRenderer(String name) {

    }

    public void removeAllRenderers() {

    }

    public boolean hasRenderer(Renderer renderer) {
        return false;
    }

    public boolean hasRenderer(String name) {
        return false;
    }

    public Renderer getRenderer(String name) {
        return null;
    }

    public List<Renderer> getRenderers() {
        return renderers;
    }

    /* ############################################################################################
     *
     *                                       Getter / Setter
     *
     * ############################################################################################ */

    public boolean isAlwaysOnTop() {
        long handle = blockingHandle();
        return GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_FLOATING) == GLFW.GLFW_TRUE;
    }

    public CompletableFuture<Boolean> isAlwaysOnTopAsync() {
        return awaitHandle().thenApply(handle ->
            GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_FLOATING) == GLFW.GLFW_TRUE
        );
    }

    public boolean isClosing() {
        long handle = blockingHandle();
        return GLFW.glfwWindowShouldClose(handle);
    }

    public CompletableFuture<Boolean> isClosingAsync() {
        return awaitHandle().thenApply(GLFW::glfwWindowShouldClose);
    }

    public boolean isFocused() {
        long handle = blockingHandle();
        return GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_FOCUSED) == GLFW.GLFW_TRUE;
    }

    public CompletableFuture<Boolean> isFocusedAsync() {
        return awaitHandle().thenApply(handle ->
                GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_FOCUSED) == GLFW.GLFW_TRUE
        );
    }

    public boolean isMaximized() {
        long handle = blockingHandle();
        return GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_MAXIMIZED) == GLFW.GLFW_TRUE;
    }

    public CompletableFuture<Boolean> isMaximizedAsync() {
        return awaitHandle().thenApply(handle ->
                GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_MAXIMIZED) == GLFW.GLFW_TRUE
        );
    }

    public boolean isMinimized() {
        long handle = blockingHandle();
        return GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_ICONIFIED) == GLFW.GLFW_TRUE;
    }

    public CompletableFuture<Boolean> isMinimizedAsync() {
        return awaitHandle().thenApply(handle ->
                GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_ICONIFIED) == GLFW.GLFW_TRUE
        );
    }

    public boolean isVisible() {
        long handle = blockingHandle();
        return GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_VISIBLE) == GLFW.GLFW_TRUE;
    }

    public CompletableFuture<Boolean> isVisibleAsync() {
        return awaitHandle().thenApply(handle ->
                GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_VISIBLE) == GLFW.GLFW_TRUE
        );
    }

    public String getType() {
        return getClass().getSimpleName();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public Thread getWindowThread() {
        return windowThread;
    }

    public WindowStates getWindowStates() {
        return windowStates;
    }

    public Backend getBackend() {
        return backend;
    }

    public Context getContext() {
        return context;
    }

    public List<LWJGLWindow> getNativeWindows() {
        return nativeWindows;
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    /* ############################################################################################
     *
     *                                  Internal Functions (Package)
     *
     * ############################################################################################ */

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
        registerDefaultRenderers();

        LOGGER.info("Native window created successfully with backend [ {} ]", backend);
        windowStates = WindowStates.INACTIVE;
        nativeWindows.add(this);
    }

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

    private void registerDefaultRenderers() {
        addRenderer(new WidgetBackgroundRenderer());
    }

    /* ############################################################################################
     *
     *                                  Internal Functions (Class)
     *
     * ############################################################################################ */

    private void initVulkan() {
        LOGGER.info("Vulkan not supported!");
    }

    private void renderVulkan(Runnable func) {
        renderOpenGL(func);
    }

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

    private void renderOpenGL(Runnable func) {
        GL11.glClearColor(0.2f, 0.3f, 0.4f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        if (func != null) {
            func.run();
        }

        GLFW.glfwSwapBuffers(windowHandle);
        GLFW.glfwPollEvents();
    }

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
