package com.jilou.ui.logic;

import com.jilou.ui.container.LWJGLWindow;

public interface Renderer {

    /**
     * Function which returned the render name. This name
     * identifies the current {@link Renderer}. Duplicated names ar not allowed!
     * @return {@link String}- named identifier
     */
    default String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Function for get the current render {@link RenderPriority}.
     * By default, the {@link RenderPriority#MEDIUM} is set. The {@link RenderPriority}
     * can be changed with {@link #setPriority(RenderPriority)} all time.
     * @return {@link RenderPriority}- current render order.
     */
    default RenderPriority getPriority() {
        return RenderPriority.MEDIUM;
    }

    /**
     * Function for set the new {@link RenderPriority} for the active {@link Renderer}.
     * This can be changed all time.
     * @param priority the new {@link RenderPriority} for the {@link Renderer}
     */
    void setPriority(RenderPriority priority);

    /**
     * Function returns a {@link Boolean} for check the update status of the {@link Renderer}.
     * It will return false if a widget was changed or something.
     * @return {@link Boolean}- current update state
     */
    boolean upToDate();

    /**
     * Function is called the {@link Renderer} that he needed to check the current status.
     * If there was change detected it will be triggered a new render task.
     */
    void update();

    /**
     * Function returned true if the {@link Renderer} was successfully initialized.
     * @return {@link Boolean}- the state of initialization.
     */
    boolean isInitialized();

    /**
     * Function which is called one time by the initialisation process or the {@link Renderer}.
     * Include your code in there if it needed one time initialisation.
     * Don't create a loop in there!
     */
    void initialize();

    /**
     * Function for display things to the {@link LWJGLWindow}. This is the main function
     * of a {@link Renderer} and is called at system ticks. You can start render here.
     * @param nativeWindow window which represent the current {@link Renderer}
     */
    void render(LWJGLWindow nativeWindow);

    /**
     * Function which is called if the {@link Renderer} unregistered. This can
     * be used for clean up tasks or handle other things.
     */
    void dispose();

}
