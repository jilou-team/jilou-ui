package com.jilou.ui.logic;

import com.jilou.ui.container.LWJGLWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public abstract class AbstractRenderer implements Renderer {

    private static final Logger LOGGER = LogManager.getLogger(AbstractRenderer.class);

    private final String id;
    private final String name;

    private RenderPriority priority;
    private LWJGLWindow nativeWindow;

    private boolean initialized;
    private boolean upToDate;

    /**
     * Constructor for create a new {@link Renderer}. This is needed for pre-set up
     * the new {@link AbstractRenderer}. It will create a new ID entry and name.
     * @param name {@link Renderer} name
     */
    protected AbstractRenderer(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name == null ? getClass().getSimpleName() : name;
        this.initialized = false;
        this.upToDate = false;
    }

    /**
     * Function for load your content which needs one time initialisation. This is perfect for handle
     * new instances or create widgets.
     * @param nativeWindow the window which is stored this {@link Renderer}
     */
    public abstract void preLoad(LWJGLWindow nativeWindow);

    /**
     * Internal Function which is called at {@link #render(LWJGLWindow)}. It will include your code
     * to the active render pipeline. This is used for render Widgets or other graphical things.
     * @param nativeWindow the window which is stored this {@link Renderer}
     */
    protected abstract void func(LWJGLWindow nativeWindow);

    @Override
    public void initialize() {
        if(initialized) {
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
