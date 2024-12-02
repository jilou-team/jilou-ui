package com.jilou.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

/**
 * The core class of the JilouUI framework.
 * <p>
 * JilouUI is a UI framework built natively on {@link GLFW}, utilizing the C programming language as its foundation.
 * It is not a standalone program but rather a library designed to be integrated into other projects.
 * </p>
 * <p>
 * For more information, visit our GitHub repository:
 * <a href="https://github.com/your-repo-link">https://github.com/jilou-team/jilou-ui</a>
 * </p>
 * <p>
 * This class serves as the central component of the library, orchestrating the key functionalities of JilouUI.
 * </p>
 * @since 0.1.0
 * @author Daniel Ramke
 */
public final class JilouUI {

    private static final Logger LOGGER = LogManager.getLogger(JilouUI.class);

    /**
     * Private Constructor for block wrong initialisation
     * @throws IllegalStateException if you try to initialize this class.
     */
    private JilouUI() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Function for load the jilou-ui library in your project. This call is needed at your main function or
     * a function which is called in the main thread.
     * @param args an array of {@link String}'s which store program arguments.
     */
    public static void load(String[] args) {
        LOGGER.info("Start loading jilou-ui...");
        if(args.length != 0) {
            LOGGER.info("Found arguments [ {} ], try to convert the arguments...", args.length);
        } else {
            LOGGER.info("No arguments was found. Skipp checking...");
        }

        generateCLibraryNatives();
    }

    /**
     * Function trys to generate headers from the native LWJGL C library.
     * If there wasn't successfully it will crash the {@link JilouUI} application.
     * @throws IllegalStateException if the check was failed.
     */
    private static void generateCLibraryNatives() {
        if (!(GLFW.glfwInit())) {
            LOGGER.fatal("Failed to initialize GLFW");
            GLFW.glfwSetErrorCallback((error, description) -> LOGGER.error("GLFW error: {}, {}", error, MemoryUtil.memUTF8(description)));
            throw new IllegalStateException("Failed to initialize GLFW");
        }

        LOGGER.info("GLFW initialized");
    }

}
