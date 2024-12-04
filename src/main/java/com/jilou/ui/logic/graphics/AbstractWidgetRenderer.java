package com.jilou.ui.logic.graphics;

import com.jilou.ui.container.AbstractWindow;
import com.jilou.ui.container.LWJGLWindow;
import com.jilou.ui.container.Scene;
import com.jilou.ui.logic.AbstractRenderer;
import com.jilou.ui.logic.Renderer;
import com.jilou.ui.widget.AbstractWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract base class for rendering widgets within a windowing context.
 * <p>
 * The {@code AbstractWidgetRenderer} class extends the functionality of {@link AbstractRenderer}
 * by introducing support for rendering widget elements. Subclasses must implement the
 * {@link #render(List)} method to define how a collection of widgets should be rendered.
 * </p>
 *
 * @since 0.1.0
 * @see AbstractRenderer
 * @author Daniel Ramke
 */
public abstract class AbstractWidgetRenderer extends AbstractRenderer {

    /**
     * Constructs a new {@code AbstractWidgetRenderer} with the specified name.
     * <p>
     * This constructor initializes the renderer with a unique ID and assigns the
     * specified name for identification purposes.
     * </p>
     *
     * @param name the name of the renderer
     */
    protected AbstractWidgetRenderer(String name) {
        super(name);
    }

    /**
     * Renders a collection of widgets.
     * <p>
     * This method is abstract and must be implemented by subclasses to define
     * the specific rendering logic for the provided widget list.
     * </p>
     *
     * @param widgets a list of widgets to render
     */
    public abstract void render(List<AbstractWidget> widgets);

    /**
     * Executes rendering logic for a given native window.
     * <p>
     * This method overrides {@link AbstractRenderer#func(LWJGLWindow)} and provides
     * a specific implementation for windows that are instances of {@link AbstractWindow}.
     * When such a window is detected, it invokes the {@link #render(List)} method
     * with an empty list of widgets as a placeholder.
     * </p>
     *
     * @param nativeWindow the native window being rendered
     */
    @Override
    protected void func(LWJGLWindow nativeWindow) {
        if (nativeWindow instanceof AbstractWindow window) {
            Scene scene = window.getActiveScene();
            if(scene != null) {
                render(scene.getWidgetList());
            }
        }
    }
}

