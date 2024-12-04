package com.jilou.ui.logic.graphics;

import com.jilou.ui.container.LWJGLWindow;
import com.jilou.ui.widget.AbstractWidget;
import com.jilou.ui.widget.shapes.Rectangle;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * A specialized renderer for rendering widget backgrounds.
 * <p>
 * The {@code WidgetBackgroundRenderer} class extends {@link AbstractWidgetRenderer}
 * to provide functionality for rendering the background of widgets. This class is designed
 * to handle background-specific rendering tasks while integrating seamlessly with
 * the JilouUI rendering pipeline.
 * </p>
 * <p>
 * The class also provides methods to manage resource preloading and disposal, making it
 * suitable for both OpenGL and Vulkan contexts.
 * </p>
 *
 * @since 0.1.0
 * @see AbstractWidgetRenderer
 * @author Daniel Ramke
 */
public class WidgetBackgroundRenderer extends AbstractWidgetRenderer {

    /**
     * Constructs a new {@code WidgetBackgroundRenderer}.
     * <p>
     * This constructor initializes the renderer with a default name of {@code null}.
     * The renderer will be assigned a unique ID during its initialization phase.
     * </p>
     */
    public WidgetBackgroundRenderer() {
        super(null);
    }

    /**
     * Renders the given list of widgets.
     * <p>
     * This method processes the provided list of widgets and performs the necessary
     * rendering operations to display their backgrounds. The implementation is left
     * open for customization based on the specific requirements of the widget rendering.
     * </p>
     *
     * @param widgets the list of widgets to render
     */
    @Override
    public void render(List<AbstractWidget> widgets) {
        /* render the given list */
        for(AbstractWidget widget : widgets) {
            GL11.glColor3f(0.2f, 5.0f, 0.0f);

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f((float) widget.getPositionX(), (float) widget.getPositionY());
            GL11.glVertex2f((float) (widget.getPositionX() + widget.getWidth()), (float) widget.getPositionY());
            GL11.glVertex2f((float) (widget.getPositionX() + widget.getWidth()), (float) (widget.getPositionY() + widget.getHeight()));
            GL11.glVertex2f((float) widget.getPositionX(), (float) (widget.getPositionY() + widget.getHeight()));
            GL11.glEnd();
        }
    }

    /**
     * Preloads resources for rendering in the given window context.
     * <p>
     * This method is used to prepare OpenGL or Vulkan resources before rendering begins.
     * It is invoked during the initialization phase and allows for setting up any
     * required rendering states or loading assets.
     * </p>
     *
     * @param nativeWindow the window context for which resources are being preloaded
     */
    @Override
    public void preLoad(LWJGLWindow nativeWindow) {
        /* preload GL/Vulkan resources */
    }

    /**
     * Disposes resources associated with the renderer.
     * <p>
     * This method is responsible for cleaning up resources such as textures, buffers,
     * or other assets allocated during the rendering process. It ensures that no memory
     * leaks occur when the renderer is no longer needed.
     * </p>
     */
    @Override
    public void dispose() {
        /* dispose widgets */
    }
}

