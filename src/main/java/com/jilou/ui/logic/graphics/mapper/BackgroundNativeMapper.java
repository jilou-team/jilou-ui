package com.jilou.ui.logic.graphics.mapper;

import com.jilou.ui.styles.StyleSheet;
import com.jilou.ui.styles.types.Background;
import com.jilou.ui.styles.types.Radius;
import com.jilou.ui.styles.types.DropShadow;
import com.jilou.ui.utils.Color;
import com.jilou.ui.widget.AbstractWidget;
import org.lwjgl.opengl.GL11;

/**
 * The {@code BackgroundNativeMapper} class is responsible for rendering background elements for widgets,
 * including drawing rounded rectangles with optional shadows. It uses OpenGL to render graphical elements
 * such as backgrounds and shadows with rounded corners, applying the style defined in the given {@link StyleSheet}.
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
public class BackgroundNativeMapper {

    /**
     * Renders the background of the provided widget.
     * This method will call another renderBackground method with the widget's position, dimensions, and style.
     *
     * @param widget The widget whose background should be rendered.
     */
    public void renderBackground(AbstractWidget widget) {
        if (widget == null) return;

        renderBackground((float) widget.getPositionX(), (float) widget.getPositionY(),
                (float) widget.getWidth(), (float) widget.getHeight(),
                widget.getStyle());
    }

    /**
     * Renders a background with the specified position, dimensions, and style.
     * If the background color is not specified in the style, a random color is generated.
     *
     * @param x      The x-coordinate of the background's position.
     * @param y      The y-coordinate of the background's position.
     * @param width  The width of the background.
     * @param height The height of the background.
     * @param sheet  The style sheet that contains the background and other style properties.
     */
    public void renderBackground(float x, float y, float width, float height, StyleSheet sheet) {
        Background background = sheet.getBackground();
        if (background == null || background.getColor() == null) {
            background = Background.fromColor(Color.randomRGB());
        }
        renderBackground(x, y, width, height, background.getColor(), sheet);
    }

    /**
     * Renders a background with a specified color and style.
     * It also renders a shadow if defined in the style.
     *
     * @param x      The x-coordinate of the background's position.
     * @param y      The y-coordinate of the background's position.
     * @param width  The width of the background.
     * @param height The height of the background.
     * @param color  The color of the background.
     * @param sheet  The style sheet that contains the style properties.
     */
    public void renderBackground(float x, float y, float width, float height, Color color, StyleSheet sheet) {
        if (color == null) {
            color = Color.randomRGB();
        }
        renderShadow(x, y, width, height, sheet);

        GL11.glColor4f(color.getRedPercent(), color.getGreenPercent(), color.getBluePercent(), color.getAlphaPercent());
        drawRoundedRectangle(x, y, width, height, sheet, sheet.getBorderRadius());
    }

    /**
     * Renders a shadow behind the background if a drop shadow is defined in the style.
     * The shadow is rendered with multiple layers, fading out the further from the object the shadow is.
     *
     * @param x      The x-coordinate of the background's position.
     * @param y      The y-coordinate of the background's position.
     * @param width  The width of the background.
     * @param height The height of the background.
     * @param sheet  The style sheet that contains the drop shadow properties.
     */
    public void renderShadow(float x, float y, float width, float height, StyleSheet sheet) {
        DropShadow dropShadow = sheet.getDropShadow();
        if (dropShadow == null) {
            return;
        }

        Color color = dropShadow.getColor();
        if (color == null) {
            color = Color.BLACK;
        }

        int layers = dropShadow.getLayer();
        if (layers == 0) {
            return;
        }
        float alphaStep = color.getAlphaPercent() * dropShadow.getStrength() / layers;
        for (int i = 0; i < layers; i++) {
            float layerAlpha = layers == 1 ? color.getAlphaPercent() : color.getAlphaPercent() * dropShadow.getStrength() - (i * alphaStep);
            float offsetX = dropShadow.getOffsetX() + (i * 1.0f * Math.signum(dropShadow.getOffsetX()));
            float offsetY = dropShadow.getOffsetY() + (i * 1.0f * Math.signum(dropShadow.getOffsetY()));

            float offsetW = dropShadow.getOffsetW();
            float offsetH = dropShadow.getOffsetH();

            if (offsetX == 0 && offsetY == 0) {
                offsetW = dropShadow.getOffsetW() + (i * 1.0f * Math.signum(dropShadow.getOffsetW()));
                offsetH = dropShadow.getOffsetH() + (i * 1.0f * Math.signum(dropShadow.getOffsetH()));
            }

            GL11.glColor4f(color.getRedPercent(), color.getGreenPercent(), color.getBluePercent(), layerAlpha);
            Radius radius = sheet.getDropShadow().getRadius() == null ? sheet.getBorderRadius() : sheet.getDropShadow().getRadius();

            drawRoundedRectangle((x - (offsetW / 2)) + offsetX, (y - (offsetH / 2)) - offsetY,
                    width + offsetW, height + offsetH, sheet, radius);
        }
    }

    /**
     * Draws a rounded rectangle at the specified position with the given dimensions and corner radius.
     *
     * @param x        The x-coordinate of the rectangle's position.
     * @param y        The y-coordinate of the rectangle's position.
     * @param width    The width of the rectangle.
     * @param height   The height of the rectangle.
     * @param sheet    The style sheet containing the border radius and corner segmentation.
     * @param radius   The radius of the rectangle's corners.
     */
    private void drawRoundedRectangle(float x, float y, float width, float height, StyleSheet sheet, Radius radius) {
        int segments = sheet.getCornerSegmentation(); // Number of segments for smooth corners
        float theta = (float) (2 * Math.PI / segments);
        float cos = (float) Math.cos(theta);
        float sin = (float) Math.sin(theta);

        // Casting the radius to float values
        float topLeft = (float) radius.getTopLeft();
        float topRight = (float) radius.getTopRight();
        float bottomLeft = (float) radius.getBottomLeft();
        float bottomRight = (float) radius.getBottomRight();

        // Draw the rectangle body (excluding corners)
        GL11.glBegin(GL11.GL_QUADS);

        // Center
        GL11.glVertex2f(x + topLeft, y);
        GL11.glVertex2f(x + width - topRight, y);
        GL11.glVertex2f(x + width - bottomRight, y + height);
        GL11.glVertex2f(x + bottomLeft, y + height);

        // Left
        GL11.glVertex2f(x, y + topLeft);
        GL11.glVertex2f(x + topLeft, y + topLeft);
        GL11.glVertex2f(x + topLeft, y + height - bottomLeft);
        GL11.glVertex2f(x, y + height - bottomLeft);

        // Right
        GL11.glVertex2f(x + width - topRight, y + topRight);
        GL11.glVertex2f(x + width, y + topRight);
        GL11.glVertex2f(x + width, y + height - bottomRight);
        GL11.glVertex2f(x + width - bottomRight, y + height - bottomRight);

        GL11.glEnd();

        // Bottom-left corner
        if (bottomLeft > 0) {
            drawCorner(x + bottomLeft, y + bottomLeft, cos, sin, segments, bottomLeft, true, true);
        }

        // Bottom-right corner
        if (bottomRight > 0) {
            drawCorner(x + width - bottomRight, y + bottomRight, cos, sin, segments, bottomRight, false, true);
        }

        // Top-right corner
        if (topRight > 0) {
            drawCorner(x + width - topRight, y + height - topRight, cos, sin, segments, topRight, false, false);
        }

        // Top-left corner
        if (topLeft > 0) {
            drawCorner(x + topLeft, y + height - topLeft, cos, sin, segments, topLeft, true, false);
        }
    }

    /**
     * Draws a single corner of the rounded rectangle.
     *
     * @param cx      The x-coordinate of the corner's center.
     * @param cy      The y-coordinate of the corner's center.
     * @param cos     The cosine value for corner angle calculations.
     * @param sin     The sine value for corner angle calculations.
     * @param segments The number of segments for smoothness of the corner.
     * @param radius  The radius of the corner.
     * @param flipX   Whether to flip the corner horizontally.
     * @param flipY   Whether to flip the corner vertically.
     */
    @SuppressWarnings("java:S107")
    private void drawCorner(float cx, float cy, float cos, float sin, int segments, float radius, boolean flipX, boolean flipY) {
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2f(cx, cy); // Center of the corner
        float px = radius;
        float py = 0;
        for (int i = 0; i <= segments / 4; i++) {
            GL11.glVertex2f(
                    cx + (flipX ? -px : px),
                    cy + (flipY ? -py : py)
            );
            float temp = px;
            px = cos * px - sin * py;
            py = sin * temp + cos * py;
        }
        GL11.glEnd();
    }
}

