package com.jilou.ui.logic.graphics;

import com.jilou.ui.container.LWJGLWindow;
import com.jilou.ui.logic.graphics.mapper.BorderNativeMapper;
import com.jilou.ui.widget.AbstractWidget;

import java.util.List;

/**
 * A renderer class responsible for rendering borders for a list of widgets.
 * Extends the {@code AbstractWidgetRenderer} to integrate with the rendering pipeline.
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
public class WidgetBorderRenderer extends AbstractWidgetRenderer {

    /**
     * Mapper responsible for rendering borders using OpenGL.
     */
    private BorderNativeMapper borderMapper;

    /**
     * Default constructor for {@code WidgetBorderRenderer}.
     * Initializes the parent renderer with no associated style or theme.
     */
    public WidgetBorderRenderer() {
        super(null);
    }

    /**
     * Renders borders for all widgets in the provided list.
     *
     * @param widgets A list of {@code AbstractWidget} instances to be rendered.
     */
    @Override
    public void render(List<AbstractWidget> widgets) {
        for (AbstractWidget widget : widgets) {
            borderMapper.renderBorder(widget);
        }
    }

    /**
     * Pre-loads the renderer by initializing resources, including the {@code BorderNativeMapper}.
     *
     * @param nativeWindow The LWJGL native window associated with the renderer.
     */
    @Override
    public void preLoad(LWJGLWindow nativeWindow) {
        this.borderMapper = new BorderNativeMapper();
    }

    /**
     * Disposes of any resources or widgets associated with this renderer.
     * Currently, this method is a placeholder for cleanup logic.
     */
    @Override
    public void dispose() {
        /* dispose widgets */
    }
}

