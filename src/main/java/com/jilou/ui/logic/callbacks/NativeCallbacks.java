package com.jilou.ui.logic.callbacks;

import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.glfw.GLFWCharModsCallbackI;
import org.lwjgl.glfw.GLFWCursorEnterCallbackI;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import org.lwjgl.glfw.GLFWWindowFocusCallbackI;
import org.lwjgl.glfw.GLFWWindowIconifyCallbackI;
import org.lwjgl.glfw.GLFWWindowMaximizeCallbackI;
import org.lwjgl.glfw.GLFWWindowPosCallbackI;
import org.lwjgl.glfw.GLFWWindowRefreshCallbackI;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;

/**
 * A collection of native GLFW callback wrappers that support multiple listeners for each event type.
 * <p>
 * Each nested class implements a specific GLFW callback interface and allows registering multiple listeners
 * that will be invoked in the order of their registration.
 * </p>
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
public final class NativeCallbacks {

    /**
     * Handles window close events by invoking all registered {@link GLFWWindowCloseCallbackI} listeners.
     */
    public static class NativeCloseCallback extends Callback<GLFWWindowCloseCallbackI> implements GLFWWindowCloseCallbackI {
        @Override
        public void invoke(long nativeHandle) {
            for (GLFWWindowCloseCallbackI callback : callbacks) {
                callback.invoke(nativeHandle);
            }
        }
    }

    /**
     * Handles window resize events by invoking all registered {@link GLFWWindowSizeCallbackI} listeners.
     */
    public static class NativeSizeCallback extends Callback<GLFWWindowSizeCallbackI> implements GLFWWindowSizeCallbackI {
        @Override
        public void invoke(long nativeHandle, int width, int height) {
            for (GLFWWindowSizeCallbackI callback : callbacks) {
                callback.invoke(nativeHandle, width, height);
            }
        }
    }

    /**
     * Handles window position events by invoking all registered {@link GLFWWindowPosCallbackI} listeners.
     */
    public static class NativePositionCallback extends Callback<GLFWWindowPosCallbackI> implements GLFWWindowPosCallbackI {
        @Override
        public void invoke(long nativeHandle, int positionX, int positionY) {
            for (GLFWWindowPosCallbackI callback : callbacks) {
                callback.invoke(nativeHandle, positionX, positionY);
            }
        }
    }

    /**
     * Handles window focus events by invoking all registered {@link GLFWWindowFocusCallbackI} listeners.
     */
    public static class NativeFocusCallback extends Callback<GLFWWindowFocusCallbackI> implements GLFWWindowFocusCallbackI {
        @Override
        public void invoke(long nativeHandle, boolean focused) {
            for (GLFWWindowFocusCallbackI callback : callbacks) {
                callback.invoke(nativeHandle, focused);
            }
        }
    }

    /**
     * Handles window maximization events by invoking all registered {@link GLFWWindowMaximizeCallbackI} listeners.
     */
    public static class NativeMaximizedCallback extends Callback<GLFWWindowMaximizeCallbackI> implements GLFWWindowMaximizeCallbackI {
        @Override
        public void invoke(long nativeHandle, boolean maximized) {
            for (GLFWWindowMaximizeCallbackI callback : callbacks) {
                callback.invoke(nativeHandle, maximized);
            }
        }
    }

    /**
     * Handles window minimization events by invoking all registered {@link GLFWWindowIconifyCallbackI} listeners.
     */
    public static class NativeMinimizedCallback extends Callback<GLFWWindowIconifyCallbackI> implements GLFWWindowIconifyCallbackI {
        @Override
        public void invoke(long nativeHandle, boolean minimized) {
            for (GLFWWindowIconifyCallbackI callback : callbacks) {
                callback.invoke(nativeHandle, minimized);
            }
        }
    }

    /**
     * Handles window refresh events by invoking all registered {@link GLFWWindowRefreshCallbackI} listeners.
     */
    public static class NativeRefreshCallback extends Callback<GLFWWindowRefreshCallbackI> implements GLFWWindowRefreshCallbackI {
        @Override
        public void invoke(long nativeHandle) {
            for (GLFWWindowRefreshCallbackI callback : callbacks) {
                callback.invoke(nativeHandle);
            }
        }
    }

    /**
     * Handles framebuffer resize events by invoking all registered {@link GLFWFramebufferSizeCallbackI} listeners.
     */
    public static class NativeFrameBufferSizeCallback extends Callback<GLFWFramebufferSizeCallbackI> implements GLFWFramebufferSizeCallbackI {
        @Override
        public void invoke(long nativeHandle, int width, int height) {
            for (GLFWFramebufferSizeCallbackI callback : callbacks) {
                callback.invoke(nativeHandle, width, height);
            }
        }
    }

    /**
     * Handles key input events by invoking all registered {@link GLFWKeyCallbackI} listeners.
     */
    public static class NativeKeyCallback extends Callback<GLFWKeyCallbackI> implements GLFWKeyCallbackI {
        @Override
        public void invoke(long nativeHandle, int key, int scancode, int action, int mods) {
            for (GLFWKeyCallbackI callback : callbacks) {
                callback.invoke(nativeHandle, key, scancode, action, mods);
            }
        }
    }

    /**
     * Handles character input events by invoking all registered {@link GLFWCharCallbackI} listeners.
     */
    public static class NativeCharCallback extends Callback<GLFWCharCallbackI> implements GLFWCharCallbackI {
        @Override
        public void invoke(long nativeHandle, int codepoint) {
            for (GLFWCharCallbackI callback : callbacks) {
                callback.invoke(nativeHandle, codepoint);
            }
        }
    }

    /**
     * Handles character input with modifiers by invoking all registered {@link GLFWCharModsCallbackI} listeners.
     */
    public static class NativeCharModsCallback extends Callback<GLFWCharModsCallbackI> implements GLFWCharModsCallbackI {
        @Override
        public void invoke(long nativeHandle, int codepoint, int mods) {
            for (GLFWCharModsCallbackI callback : callbacks) {
                callback.invoke(nativeHandle, codepoint, mods);
            }
        }
    }

    /**
     * Handles mouse button input events by invoking all registered {@link GLFWMouseButtonCallbackI} listeners.
     */
    public static class NativeMouseButtonCallback extends Callback<GLFWMouseButtonCallbackI> implements GLFWMouseButtonCallbackI {
        @Override
        public void invoke(long nativeHandle, int button, int action, int mods) {
            for (GLFWMouseButtonCallbackI callback : callbacks) {
                callback.invoke(nativeHandle, button, action, mods);
            }
        }
    }

    /**
     * Handles mouse position events by invoking all registered {@link GLFWCursorPosCallbackI} listeners.
     */
    public static class NativeMousePositionCallback extends Callback<GLFWCursorPosCallbackI> implements GLFWCursorPosCallbackI {
        @Override
        public void invoke(long nativeHandle, double positionX, double positionY) {
            for (GLFWCursorPosCallbackI callback : callbacks) {
                callback.invoke(nativeHandle, positionX, positionY);
            }
        }
    }

    /**
     * Handles mouse enter and leave events by invoking all registered {@link GLFWCursorEnterCallbackI} listeners.
     */
    public static class NativeMouseEnteredCallback extends Callback<GLFWCursorEnterCallbackI> implements GLFWCursorEnterCallbackI {
        @Override
        public void invoke(long nativeHandle, boolean entered) {
            for (GLFWCursorEnterCallbackI callback : callbacks) {
                callback.invoke(nativeHandle, entered);
            }
        }
    }

    /**
     * Handles mouse scroll events by invoking all registered {@link GLFWScrollCallbackI} listeners.
     */
    public static class NativeScrollCallback extends Callback<GLFWScrollCallbackI> implements GLFWScrollCallbackI {
        @Override
        public void invoke(long nativeHandle, double xOffset, double yOffset) {
            for (GLFWScrollCallbackI callback : callbacks) {
                callback.invoke(nativeHandle, xOffset, yOffset);
            }
        }
    }
}

