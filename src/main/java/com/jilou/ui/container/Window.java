package com.jilou.ui.container;

import org.lwjgl.opengl.GL11;

public class Window extends AbstractWindow {

    public Window(String localizedName) {
        super(localizedName);
    }

    @Override
    protected void setup() {
        /**/
    }

    @Override
    protected void update(float delta) {
        /**/
    }

    @Override
    protected void render() {
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glColor3f(1.0f, 0.0f, 0.0f);
        GL11.glVertex2f(-0.5f, -0.5f);
        GL11.glColor3f(0.0f, 1.0f, 0.0f);
        GL11.glVertex2f(0.5f, -0.5f);
        GL11.glColor3f(0.0f, 0.0f, 1.0f);
        GL11.glVertex2f(0.0f, 0.5f);
        GL11.glEnd();
    }

    @Override
    protected void destroy() {
        /**/
    }
}
