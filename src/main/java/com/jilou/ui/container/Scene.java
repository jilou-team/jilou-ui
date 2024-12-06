package com.jilou.ui.container;

import com.jilou.ui.ApplicationFactory;
import com.jilou.ui.container.layout.Layout;
import com.jilou.ui.widget.AbstractWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a scene in the application, which contains a collection of widgets and a layout.
 * <p>
 * Each scene is identified by a unique ID and can optionally be associated with a container window and a root layout.
 * The scene manages widgets, including adding, removing, and updating them, and serves as the basis for user interface rendering.
 * </p>
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
public class Scene {

    /**
     * The unique identifier for the {@code Scene}.
     */
    private final int id;

    /**
     * The localized name of the {@code Scene}, derived from its class name and ID.
     */
    private final String localizedName;

    /**
     * The list of {@link AbstractWidget} contained in the scene.
     */
    private List<AbstractWidget> widgetList;

    /**
     * The container window associated with the scene.
     */
    private AbstractWindow container;

    /**
     * The root {@link Layout} of the scene.
     */
    private Layout root;

    /**
     * The {@code Scene} width.
     */
    private int width;

    /**
     * The {@code Scene} height.
     */
    private int height;

    /**
     * Constructs a scene with no root layout and no container.
     */
    public Scene() {
        this(null, null);
    }

    /**
     * Constructs a scene with the specified root layout and no container.
     *
     * @param root the root layout of the scene
     */
    public Scene(Layout root) {
        this(root, null);
    }

    /**
     * Constructs a scene with the specified root layout and container.
     *
     * @param root the root layout of the scene
     * @param container the container window associated with the scene
     */
    public Scene(Layout root, AbstractWindow container) {
        this.id = ApplicationFactory.giveNextSceneID();
        this.localizedName = getClass().getSimpleName() + "-" + id;
        if (root == null) {
            this.widgetList = new ArrayList<>();
        } else {
            this.widgetList = root.getWidgetList();
        }
        this.setContainer(container);
        this.setRoot(root);
    }

    /**
     * Updates the scene by updating its root layout if it exists.
     */
    protected void update() {
        if (root != null) {
            root.update();
        }
    }

    /**
     * Sets the root layout of the scene.
     * <p>
     * If the scene already has a root layout, it is cleaned up before setting the new root.
     * </p>
     *
     * @param root the new root layout for the scene
     */
    public void setRoot(Layout root) {
        if (getRoot() != null) {
            root.cleanUp();
        }
        this.root = root;
        if (root != null) {
            root.setParent(this);
            this.widgetList = root.getWidgetList();
        }
    }

    /**
     * Adds a widget to the scene if it does not already exist.
     *
     * @param widget the widget to add
     */
    public void add(AbstractWidget widget) {
        if (hasWidget(widget)) {
            return;
        }

        widgetList.add(widget);
    }

    /**
     * Removes a widget from the scene.
     *
     * @param widget the widget to remove
     */
    public void remove(AbstractWidget widget) {
        if (widget != null) {
            remove(widget.getLocalizedName());
        }
    }

    /**
     * Removes a widget from the scene by its localized name.
     *
     * @param localizedName the localized name of the widget to remove
     */
    public void remove(String localizedName) {
        if (!hasWidget(localizedName)) {
            return;
        }
        AbstractWidget widget = getWidget(localizedName);
        widget.destroy();
        widgetList.remove(widget);
    }

    /**
     * Checks if a widget exists in the scene.
     *
     * @param widget the widget to check
     * @return true if the widget exists, false otherwise
     */
    public boolean hasWidget(AbstractWidget widget) {
        return widgetList.contains(widget);
    }

    /**
     * Checks if a widget exists in the scene by its localized name.
     *
     * @param localizedName the localized name of the widget to check
     * @return true if the widget exists, false otherwise
     */
    public boolean hasWidget(String localizedName) {
        return getWidget(localizedName) != null;
    }

    /**
     * Retrieves a widget from the scene by its localized name.
     *
     * @param localizedName the localized name of the widget to retrieve
     * @return the widget with the specified localized name, or null if not found
     */
    public AbstractWidget getWidget(String localizedName) {
        AbstractWidget widget = null;
        for (AbstractWidget widgets : widgetList) {
            if (widgets.getLocalizedName().equals(localizedName)) {
                widget = widgets;
            }
        }
        return widget;
    }

    /**
     * Sets the container window associated with the scene.
     *
     * @param container the container window to associate with the scene
     */
    protected void setContainer(AbstractWindow container) {
        this.container = container;
        if(container != null) {
            this.setWidth(container.getWidth());
            this.setHeight(container.getHeight());
        } else {
            this.setWidth(800);
            this.setHeight(600);
        }
    }

    /**
     * Retrieves the container window associated with the scene.
     *
     * @return the container window
     */
    public AbstractWindow getContainer() {
        return container;
    }

    /**
     * Retrieves the root layout of the scene.
     *
     * @return the root layout
     */
    public Layout getRoot() {
        return root;
    }

    /**
     * Retrieves the list of widgets in the scene.
     *
     * @return the list of widgets
     */
    public List<AbstractWidget> getWidgetList() {
        return widgetList;
    }

    /**
     * Sets the width of the component.
     * <p>
     * If a root is present, its width is also updated to match its current width.
     * </p>
     *
     * @param width the new width to set
     */
    public void setWidth(int width) {
        this.width = width;
        if (root != null) {
            root.setWidth(root.getWidth());
        }
    }

    /**
     * Retrieves the current width of the component.
     *
     * @return the current width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the height of the component.
     * <p>
     * If a root is present, its height is also updated to match its current height.
     * </p>
     *
     * @param height the new height to set
     */
    public void setHeight(int height) {
        this.height = height;
        if (root != null) {
            root.setHeight(root.getHeight());
        }
    }

    /**
     * Retrieves the current height of the component.
     *
     * @return the current height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Retrieves the localized name of the scene.
     *
     * @return the localized name
     */
    public String getLocalizedName() {
        return localizedName;
    }

    /**
     * Retrieves the unique identifier of the scene.
     *
     * @return the unique identifier
     */
    public int getId() {
        return id;
    }
}

