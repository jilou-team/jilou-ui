package com.jilou.ui.container;

import com.jilou.ui.container.layout.Page;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * An abstract representation of a window built on top of the {@link LWJGLWindow}.
 * <p>
 * The {@link AbstractWindow} class provides a high-level framework for creating and managing
 * windows in the JilouUI library. It includes lifecycle methods for setting up, updating,
 * and rendering the window, while handling the native GLFW window operations internally.
 * </p>
 * <p>
 * Subclasses must implement the following abstract methods:
 * <ul>
 *   <li>{@link #setup()}: Initialize resources and configure the window.</li>
 *   <li>{@link #update(float)}: Handle per-frame updates (e.g., logic updates).</li>
 *   <li>{@link #render()}: Define the rendering logic for each frame.</li>
 * </ul>
 * </p>
 * <p>
 * The {@link AbstractWindow} also provides an option to configure whether rendering
 * should continue when the window is minimized via the {@link #setRenderAtMinimized(boolean)} method.
 * </p>
 *
 * @since 0.1.0
 * @see LWJGLWindow
 * @author Daniel Ramke
 */
public abstract class AbstractWindow extends LWJGLWindow {

    private boolean renderAtMinimized;
    private boolean useDefaultCallbacks;

    private final List<Scene> sceneList = new ArrayList<>();
    private Scene activeScene;

    /**
     * Constructs an {@link AbstractWindow} with the specified localized name.
     *
     * @param localizedName the localized name of the window, used for identification
     */
    protected AbstractWindow(String localizedName) {
        super(localizedName);
        this.renderAtMinimized = false;
        this.useDefaultCallbacks = true;
        this.setWidth(DEFAULT_WIDTH);
        this.setHeight(DEFAULT_HEIGHT);
        this.activeScene = new Scene(new Page(), this);
        this.addScene(activeScene);
    }

    /**
     * Called during initialization to set up resources and configure the window.
     * Subclasses must implement this to define their setup logic.
     */
    protected abstract void setup();

    /**
     * Called each frame to handle update logic.
     *
     * @param delta the time elapsed since the last frame in seconds
     */
    protected abstract void update(float delta);

    /**
     * Called each frame to perform rendering logic.
     */
    protected abstract void render();

    /**
     * Initializes the native window and calls the {@link #setup()} method for custom setup logic.
     */
    @Override
    protected void initialize() {
        generateNativeWindow();
        loadDefaultCallbacks();
        setup();
    }

    /**
     * Handles the main update and rendering loop for the window.
     * The loop continues until the window is marked as closing.
     */
    @Override
    protected void nativeUpdate() {
        while (!isClosing()) {
            if(activeScene != null) {
                activeScene.update();
            }
            renderNative(this::render);
        }
    }

    /**
     * Sets the width of the graphical object.
     * If {@code bindSizeToScene} is {@code true} and the object has a parent,
     * the width is set to the width of the parent scene instead.
     * Additionally, if an active scene is set, its width is updated.
     *
     * @param width the desired width of the graphical object.
     */
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        if (activeScene != null) {
            activeScene.setWidth(width);
        }
    }

    /**
     * Sets the height of the graphical object.
     * If {@code bindSizeToScene} is {@code true} and the object has a parent,
     * the height is set to the height of the parent scene instead.
     * Additionally, if an active scene is set, its height is updated.
     *
     * @param height the desired height of the graphical object.
     */
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        if (activeScene != null) {
            activeScene.setHeight(height);
        }
    }

    /**
     * Sets whether the window should continue rendering while minimized.
     *
     * @param renderAtMinimized {@code true} if rendering should continue while minimized;
     *                          {@code false} otherwise
     */
    public void setRenderAtMinimized(boolean renderAtMinimized) {
        this.renderAtMinimized = renderAtMinimized;
    }

    /**
     * Returns whether the window is configured to render while minimized.
     *
     * @return {@code true} if rendering continues while minimized; {@code false} otherwise
     */
    public boolean isRenderAtMinimized() {
        return renderAtMinimized;
    }

    /**
     * Sets whether default callbacks should be used.
     *
     * @param useDefaultCallbacks {@code true} to enable default callbacks; {@code false} otherwise
     */
    public void setUseDefaultCallbacks(boolean useDefaultCallbacks) {
        this.useDefaultCallbacks = useDefaultCallbacks;
    }

    /**
     * Checks if default callbacks are enabled.
     *
     * @return {@code true} if default callbacks are enabled; {@code false} otherwise
     */
    public boolean isUseDefaultCallbacks() {
        return useDefaultCallbacks;
    }

    /**
     * Adds a scene to the manager.
     * <p>
     * If the scene already exists, it is not added. Scenes are sorted by their ID after addition.
     * </p>
     *
     * @param scene the scene to add
     */
    public void addScene(Scene scene) {
        if (hasScene(scene)) {
            return;
        }
        sceneList.add(scene);
        sceneList.sort(Comparator.comparingInt(Scene::getId));
    }

    /**
     * Removes a scene from the manager.
     *
     * @param scene the scene to remove
     */
    public void removeScene(Scene scene) {
        if (scene != null) {
            removeScene(scene.getLocalizedName());
        }
    }

    /**
     * Removes a scene by its localized name.
     * <p>
     * If the scene being removed is the active scene and other scenes exist, the next scene is activated.
     * </p>
     *
     * @param localizedName the localized name of the scene to remove
     */
    public void removeScene(String localizedName) {
        if (!hasScene(localizedName)) {
            return;
        }

        Scene scene = getScene(localizedName);
        if (scene.equals(activeScene) && sceneList.size() > 1) {
            nextScene();
        }
        scene.getRoot().cleanUp();
        sceneList.remove(scene);
    }

    /**
     * Retrieves a scene by its localized name.
     *
     * @param localizedName the localized name of the scene to retrieve
     * @return the scene with the specified localized name, or {@code null} if not found
     */
    public Scene getScene(String localizedName) {
        Scene scene = null;
        for (Scene scenes : sceneList) {
            if (scenes.getLocalizedName().equals(localizedName)) {
                scene = scenes;
            }
        }
        return scene;
    }

    /**
     * Retrieves a scene by its ID.
     *
     * @param id the ID of the scene to retrieve
     * @return the scene with the specified ID, or {@code null} if not found
     */
    public Scene getScene(int id) {
        Scene scene = null;
        for (Scene scenes : sceneList) {
            if (scenes.getId() == id) {
                scene = scenes;
            }
        }
        return scene;
    }

    /**
     * Checks if the manager contains the specified scene.
     *
     * @param scene the scene to check
     * @return {@code true} if the scene exists; {@code false} otherwise
     */
    public boolean hasScene(Scene scene) {
        return sceneList.contains(scene);
    }

    /**
     * Checks if the manager contains a scene with the specified localized name.
     *
     * @param localizedName the localized name to check
     * @return {@code true} if the scene exists; {@code false} otherwise
     */
    public boolean hasScene(String localizedName) {
        return getScene(localizedName) != null;
    }

    /**
     * Checks if the manager contains a scene with the specified ID.
     *
     * @param id the ID to check
     * @return {@code true} if the scene exists; {@code false} otherwise
     */
    public boolean hasScene(int id) {
        return getScene(id) != null;
    }

    /**
     * Sets the active scene.
     *
     * @param scene the scene to set as active
     */
    public void setActiveScene(Scene scene) {
        this.activeScene = scene;
    }

    /**
     * Retrieves the currently active scene.
     *
     * @return the active scene
     */
    public Scene getActiveScene() {
        return activeScene;
    }

    /**
     * Navigates to a scene by its ID.
     *
     * @param id the ID of the scene to navigate to
     */
    public void goToScene(int id) {
        if (id < 0) {
            return;
        }
        if (!hasScene(id)) {
            return;
        }
        if (activeScene != null && activeScene.getId() == id) {
            return;
        }

        setActiveScene(getScene(id));
    }

    /**
     * Navigates to the next scene in the list. If no next scene exists, wraps around to the first scene.
     */
    public void nextScene() {
        int currentIndexID = activeScene.getId();
        int futureID = currentIndexID + 1;
        System.out.println("Next: " + currentIndexID + ", " + futureID);
        if (hasScene(futureID)) {
            setActiveScene(getScene(futureID));
        } else {
            setActiveScene(sceneList.getFirst());
        }
    }

    /**
     * Navigates to the previous scene in the list. If no previous scene exists, wraps around to the last scene.
     */
    public void previousScene() {
        int currentIndexID = activeScene.getId();
        int futureID = currentIndexID - 1;
        System.out.println("Previous: " + currentIndexID + ", " + futureID);
        if (hasScene(futureID)) {
            setActiveScene(getScene(futureID));
        } else {
            setActiveScene(sceneList.getLast());
        }
    }

    /**
     * Retrieves the list of all scenes managed by this manager.
     *
     * @return the list of scenes
     */
    public List<Scene> getSceneList() {
        return sceneList;
    }

    /**
     * Loads the default callbacks if they are enabled.
     */
    private void loadDefaultCallbacks() {
        if (useDefaultCallbacks) {
            addPositionCallback((handle, posX, posY) -> {

            });

            addFrameBufferSizeCallback((handle, width, height) -> {
                this.width = width;
                this.height = height;
                if(activeScene != null) {
                    activeScene.setWidth(width);
                    activeScene.setHeight(height);
                }
            });
        }
    }
}