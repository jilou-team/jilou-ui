package com.jilou.ui.logic.graphics.tools;

import org.lwjgl.opengl.GL11;

public final class GLCalculate {

    private GLCalculate() {
        throw new IllegalStateException("Utility class");
    }

    public static void updateProjectionMatrix(int width, int height) {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
    }

}
