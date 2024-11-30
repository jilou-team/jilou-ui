package com.jilou.test.ui.container;

import com.jilou.ui.JilouUI;
import com.jilou.ui.container.Window;
import com.jilou.ui.enums.Backend;
import com.jilou.ui.enums.WindowStates;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class LWJGLWindowTest {

    private Window window;

    @BeforeEach
    void setUp() {
        JilouUI.load(new String[]{});
        window = new Window(UUID.randomUUID().toString());
    }

    @SuppressWarnings("java:S5961")
    @Test
    void testWindowFunctionality() {
        window.setBackend(Backend.VULKAN);
        window.start();

        window.isMaximizedAsync().thenAccept(Assertions::assertFalse);
        window.maximize();
        window.isMaximizedAsync().thenAccept(Assertions::assertTrue);

        window.maximize();
        window.isMaximizedAsync().thenAccept(Assertions::assertFalse);

        window.isFocusedAsync().thenAccept(Assertions::assertFalse);
        window.focus();
        window.isFocusedAsync().thenAccept(Assertions::assertTrue);

        window.isMinimizedAsync().thenAccept(Assertions::assertFalse);
        window.minimize();
        window.isMinimizedAsync().thenAccept(Assertions::assertTrue);

        window.isVisibleAsync().thenAccept(Assertions::assertFalse);
        window.show();
        window.isVisibleAsync().thenAccept(Assertions::assertTrue);
        window.hide();
        window.isVisibleAsync().thenAccept(Assertions::assertFalse);

        window.isAlwaysOnTopAsync().thenAccept(Assertions::assertFalse);
        window.alwaysOnTop();
        window.isAlwaysOnTopAsync().thenAccept(Assertions::assertTrue);

        assertEquals(Backend.OPENGL, window.getBackend());

        window.setTitle("Test Window");
        assertEquals("Test Window", window.getTitle());

        window.setWidth(300);
        window.setHeight(500);
        assertEquals(300, window.getWidth());
        assertEquals(500, window.getHeight());

        window.setWidth(-100);
        window.setHeight(-100);

        assertEquals(0, window.getWidth());
        assertEquals(0, window.getHeight());

        window.restore();
        window.start();

        assertNotNull(window.getWindowThread());
        assertNotNull(window.getLocalizedName());
        assertNotEquals(0L, window.getWindowHandle());

        window.setBackend(Backend.VULKAN);
        assertEquals(WindowStates.INACTIVE, window.getWindowStates());
        assertEquals(Backend.OPENGL, window.getBackend());

        window.setTitle(null);
        assertNotNull(window.getTitle());

        int size = window.getNativeWindows().size();
        assertEquals(0, size);

        window.stop();

        window.isClosingAsync().thenAccept(Assertions::assertFalse);
        window.isAlwaysOnTopAsync().thenAccept(Assertions::assertTrue);
        assertFalse(window.getWindowThread().isAlive());
    }


}
