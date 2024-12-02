package com.jilou.ui;

import com.jilou.ui.logic.Renderer;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Factory class for generating application-specific utilities and resources.
 * <p>
 * The {@code ApplicationFactory} class provides utility methods for generating
 * unique identifiers for rendering tasks and other purposes within the application.
 * </p>
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
public final class ApplicationFactory {

    /**
     * Generates the next unique identifier for a {@link Renderer}.
     * <p>
     * This method returns a random {@link Long} value between 1,000,000,000 and 9,999,999,999,
     * which is used as a unique ID for the next renderer. The range ensures that the ID is large
     * enough to avoid collisions within typical application usage.
     * </p>
     *
     * @return a {@link Long} value representing the next unique renderer ID
     */
    public static Long giveNextRenderID() {
        return ThreadLocalRandom.current().nextLong(1_000_000_000L, 9_999_999_999L);
    }

}
