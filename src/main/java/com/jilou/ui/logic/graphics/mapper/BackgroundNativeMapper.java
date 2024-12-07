package com.jilou.ui.logic.graphics.mapper;

import com.jilou.ui.styles.types.Background;
import com.jilou.ui.styles.types.BorderRadius;
import com.jilou.ui.styles.types.DropShadow;
import com.jilou.ui.utils.Color;
import org.lwjgl.opengl.GL11;

public class BackgroundNativeMapper {


    public void renderBackground(float x, float y, float width, float height, Background background, DropShadow dropShadow, BorderRadius borderRadius) {
        if (background == null || background.getColor() == null) {
            background = Background.fromColor(Color.randomRGB());
        }
        renderBackground(x, y, width, height, background.getColor(), dropShadow, borderRadius);
    }

    public void renderBackground(float x, float y, float width, float height, Color color, DropShadow dropShadow, BorderRadius borderRadius) {
        if (color == null) {
            color = Color.randomRGB();
        }
        renderShadow(x, y, width, height, dropShadow, borderRadius);

        GL11.glColor4f(color.getRedPercent(), color.getGreenPercent(), color.getBluePercent(), color.getAlphaPercent());
        drawRoundedRectangle(x, y, width, height, borderRadius);
/*        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();*/
    }

    public void renderShadow(float x, float y, float width, float height, DropShadow dropShadow, BorderRadius borderRadius) {
        if (dropShadow == null) {
            return;
        }

        Color color = dropShadow.getColor();
        if(color == null) {
            color = Color.BLACK;
        }

        int layers = dropShadow.getLayer();
        float alphaStep = color.getAlphaPercent() * dropShadow.getStrength() / layers;
        for (int i = 0; i < layers; i++) {
            float layerAlpha = layers == 1 ? color.getAlphaPercent() :  color.getAlphaPercent() * dropShadow.getStrength() - (i * alphaStep);
            float offsetX = dropShadow.getOffsetX() + (i * 1.0f * Math.signum(dropShadow.getOffsetX()));
            float offsetY = dropShadow.getOffsetY() + (i * 1.0f * Math.signum(dropShadow.getOffsetY()));

            GL11.glColor4f(color.getRedPercent(), color.getGreenPercent(), color.getBluePercent(), layerAlpha);
            drawRoundedRectangle(x + offsetX, y - offsetY, width, height, borderRadius);
/*            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(x + offsetX, y - offsetY);
            GL11.glVertex2f(x + width + offsetX, y - offsetY);
            GL11.glVertex2f(x + width + offsetX, y + height - offsetY);
            GL11.glVertex2f(x + offsetX, y + height - offsetY);
            GL11.glEnd();*/
        }
    }

    private void drawRoundedRectangle(float x, float y, float width, float height, BorderRadius radius) {
        int segments = 32; // Number of segments for smooth corners
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

        // Top side (excluding left and right corners)
        GL11.glVertex2f(x + topLeft, y);
        GL11.glVertex2f(x + width - topRight, y);
        GL11.glVertex2f(x + width - topRight, y + topRight);
        GL11.glVertex2f(x + topLeft, y + topLeft);

        // Bottom side (excluding left and right corners)
        GL11.glVertex2f(x + bottomLeft, y + height - bottomLeft);
        GL11.glVertex2f(x + width - bottomRight, y + height - bottomRight);
        GL11.glVertex2f(x + width - bottomRight, y + height);
        GL11.glVertex2f(x + bottomLeft, y + height);

        // Left side (excluding top and bottom corners)
        GL11.glVertex2f(x, y + topLeft);
        GL11.glVertex2f(x + bottomLeft, y + height - bottomLeft);
        GL11.glVertex2f(x + bottomLeft, y + height);
        GL11.glVertex2f(x, y + topLeft);

        // Right side (excluding top and bottom corners)
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
