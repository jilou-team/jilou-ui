package com.jilou.test.ui;

import com.jilou.ui.JilouUI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JilouUITest {

    @Test
    void testApplicationStart() {
        JilouUI.load(new String[]{});
        assertDoesNotThrow(() -> {});
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
