package com.jilou.ui.enums;

/**
 * Enum for backend handle. >ou can find all allowed backends here.
 * Note other backend ar not tested and supported by us. If you wish for
 * example to use DirectX then you need to implement the logic by u self.
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
public enum Backend {

    /**
     * Enables the Vulkan renderer.
     */
    VULKAN,

    /**
     * Enables the OpenGL renderer.
     */
    OPENGL

}
