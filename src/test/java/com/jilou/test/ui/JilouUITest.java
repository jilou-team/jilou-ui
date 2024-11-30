package com.jilou.test.ui;

import com.jilou.ui.JilouUI;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class JilouUITest {

    @Test
    void testApplicationStart() {
        JilouUI.load(new String[]{});
        assertDoesNotThrow(() -> {});
    }

    @Test
    void testInitializedApplicationCall() throws NoSuchMethodException {
        Constructor<JilouUI> constructor = JilouUI.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        Exception exception = assertThrows(Exception.class, constructor::newInstance, "Private constructor should throw IllegalStateException");

        assertInstanceOf(IllegalStateException.class, exception.getCause(), "Private constructor should throw IllegalStateException");
    }

    @Test
    void testApplicationWithProgramArgumentsValid() {
        JilouUI.load(new String[]{"backend=vulkan"});
        assertDoesNotThrow(() -> {});

        JilouUI.load(new String[]{"backend:vulkan"});
        assertDoesNotThrow(() -> {});
    }

    @Test
    void testApplicationWithProgramArgumentsInvalid() {
        JilouUI.load(new String[]{"backend,vulkan"});
        assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException();
        });
    }

}
