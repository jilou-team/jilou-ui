package com.jilou.ui.logic.graphics;

import com.jilou.ui.container.AbstractWindow;
import com.jilou.ui.container.LWJGLWindow;
import com.jilou.ui.logic.AbstractRenderer;
import com.jilou.ui.logic.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWidgetRenderer extends AbstractRenderer {


    /**
     * Constructor for create a new {@link Renderer}. This is needed for pre-set up
     * the new {@link AbstractRenderer}. It will create a new ID entry and name.
     *
     * @param name {@link Renderer} name
     */
    protected AbstractWidgetRenderer(String name) {
        super(name);
    }

    public abstract void render(List<?> widgets);

    @Override
    protected void func(LWJGLWindow nativeWindow) {
        if (nativeWindow instanceof AbstractWindow) {
            render(new ArrayList<>());
        }
    }
}
