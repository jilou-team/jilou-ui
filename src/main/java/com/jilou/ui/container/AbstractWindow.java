package com.jilou.ui.container;


public abstract class AbstractWindow extends LWJGLWindow {

    private boolean renderAtMinimized;

    protected AbstractWindow(String localizedName) {
        super(localizedName);
        this.renderAtMinimized = false;
    }

    protected abstract void setup();

    protected abstract void update(float delta);

    protected abstract void render();

    @Override
    protected void initialize() {
        generateNativeWindow();
        setup();
    }

    @Override
    protected void nativeUpdate() {
        while (!(isClosing())) {
            renderNative(this::render);
        }
    }

    public void setRenderAtMinimized(boolean renderAtMinimized) {
        this.renderAtMinimized = renderAtMinimized;
    }

    public boolean isRenderAtMinimized() {
        return renderAtMinimized;
    }
}