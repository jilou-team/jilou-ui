package com.jilou.ui.logic.graphics;

import com.jilou.ui.container.LWJGLWindow;
import com.jilou.ui.logic.AbstractRenderer;
import com.jilou.ui.logic.Renderer;

import java.util.List;

public class WidgetBackgroundRenderer extends AbstractWidgetRenderer {

    /**
     * Constructor for create a new {@link Renderer}. This is needed for pre-set up
     * the new {@link AbstractRenderer}. It will create a new ID entry and name.
     */
    public WidgetBackgroundRenderer() {
        super(null);
    }

    @Override
    public void render(List<?> widgets) {
        /* render the give list */
    }

    @Override
    public void preLoad(LWJGLWindow nativeWindow) {
        /* preload gl / vk things */
    }

    @Override
    public void dispose() {
        /* dispose widgets */
    }
}
