package com.jilou.ui.logic.graphics.mapper;

import com.jilou.ui.styles.StyleSheet;
import com.jilou.ui.styles.types.Border;
import com.jilou.ui.styles.types.Radius;
import com.jilou.ui.utils.Color;
import com.jilou.ui.widget.AbstractWidget;
import org.lwjgl.opengl.GL11;

/**
 * A class responsible for rendering borders with rounded corners for UI widgets using OpenGL.
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
public class BorderNativeMapper {

    /**
     * Renders the border of a given widget based on its style and properties.
     *
     * @param widget The widget whose border is to be rendered. If the widget is null, no action is taken.
     */
    public void renderBorder(AbstractWidget widget) {
        if (widget == null) return;

        renderBorder((float) widget.getPositionX(), (float) widget.getPositionY(),
                (float) widget.getWidth(), (float) widget.getHeight(), widget.getStyle());
    }

    /**
     * Renders the border of a rectangular area with support for rounded corners.
     *
     * @param x       The x-coordinate of the rectangle's position.
     * @param y       The y-coordinate of the rectangle's position.
     * @param width   The width of the rectangle.
     * @param height  The height of the rectangle.
     * @param sheet   The style sheet containing the border properties and radius information.
     */
    @SuppressWarnings("java:S1192")
    public void renderBorder(float x, float y, float width, float height, StyleSheet sheet) {
        Border border = sheet.getBorder();
        if (border == null || border.getThickness() <= 0) return;

        float thickness = (float) border.getThickness();
        Color color = border.getColor();
        Radius radius = sheet.getBorderRadius();

        float topLeft = (float) radius.getTopLeft();
        float topRight = (float) radius.getTopRight();
        float bottomLeft = (float) radius.getBottomLeft();
        float bottomRight = (float) radius.getBottomRight();

        GL11.glColor4f(color.getRedPercent(), color.getGreenPercent(), color.getBluePercent(), color.getAlphaPercent());

        GL11.glBegin(GL11.GL_QUADS);

        // Top Border
        GL11.glVertex2f(x + topLeft, y);
        GL11.glVertex2f(x + width - topRight, y);
        GL11.glVertex2f(x + width - topRight, y + thickness);
        GL11.glVertex2f(x + topLeft, y + thickness);

        // Right Border
        GL11.glVertex2f(x + width - thickness, y + topRight);
        GL11.glVertex2f(x + width, y + topRight);
        GL11.glVertex2f(x + width, y + height - bottomRight);
        GL11.glVertex2f(x + width - thickness, y + height - bottomRight);

        // Bottom Border
        GL11.glVertex2f(x + bottomLeft, y + height - thickness);
        GL11.glVertex2f(x + width - bottomRight, y + height - thickness);
        GL11.glVertex2f(x + width - bottomRight, y + height);
        GL11.glVertex2f(x + bottomLeft, y + height);

        // Left Border
        GL11.glVertex2f(x, y + topLeft);
        GL11.glVertex2f(x + thickness, y + topLeft);
        GL11.glVertex2f(x + thickness, y + height - bottomLeft);
        GL11.glVertex2f(x, y + height - bottomLeft);

        GL11.glEnd();

        // Draw rounded corners
        drawRoundedCorner(x + topLeft, y + topLeft, topLeft, thickness, 180, 270); // Top-left corner
        drawRoundedCorner(x + width - topRight, y + topRight, topRight, thickness, 270, 360); // Top-right corner
        drawRoundedCorner(x + bottomLeft, y + height - bottomLeft, bottomLeft, thickness, 90, 180); // Bottom-left corner
        drawRoundedCorner(x + width - bottomRight, y + height - bottomRight, bottomRight, thickness, 0, 90); // Bottom-right corner
    }

    /**
     * Draws a rounded corner of the border.
     *
     * @param cx         The x-coordinate of the corner's center.
     * @param cy         The y-coordinate of the corner's center.
     * @param radius     The radius of the corner.
     * @param thickness  The thickness of the border.
     * @param startAngle The starting angle of the corner in degrees.
     * @param endAngle   The ending angle of the corner in degrees.
     */
    private void drawRoundedCorner(float cx, float cy, float radius, float thickness, float startAngle, float endAngle) {
        int segments = 20; // Number of segments for smoothness
        float angleStep = (float) Math.toRadians((endAngle - startAngle) / segments);

        GL11.glBegin(GL11.GL_QUAD_STRIP);
        for (int i = 0; i <= segments; i++) {
            float angle = (float) Math.toRadians(startAngle) + i * angleStep;
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);

            GL11.glVertex2f(cx + cos * radius, cy + sin * radius); // Outer edge
            GL11.glVertex2f(cx + cos * (radius - thickness), cy + sin * (radius - thickness)); // Inner edge
        }
        GL11.glEnd();
    }
}

