package com.jilou.ui.utils;

import com.jilou.ui.styles.StyleSheet;
import com.jilou.ui.widget.AbstractWidget;

import java.util.List;

/**
 * Utility class for aligning child widgets relative to a parent widget.
 * <p>
 * This class provides a method to update the alignment of child widgets based on the alignment
 * settings defined in the parent's {@link StyleSheet}. Alignment options include various positions
 * such as top-left, center, bottom-right, etc.
 * </p>
 *
 * <p>
 * This class cannot be instantiated.
 * </p>
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
public final class AlignmentUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * @throws IllegalStateException if an attempt is made to instantiate the class
     */
    private AlignmentUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Updates the alignment of a list of child widgets relative to the specified parent widget.
     * <p>
     * This method adjusts the position of each child widget based on the alignment specified
     * in the parent's {@link StyleSheet}. Supported alignments include top-left, top-right,
     * center, bottom-left, and other common layouts. If the alignment is set to {@code NOTHING},
     * the child's current position is bounded within the parent's area.
     * </p>
     *
     * <p>
     * The method ensures that child widgets do not overlap the parent's boundaries unless explicitly
     * allowed by the alignment type.
     * </p>
     *
     * @param parent   the parent widget that defines the alignment context
     * @param children the list of child widgets to align relative to the parent
     *
     * @throws NullPointerException if either the {@code parent} or {@code children} list is null
     */
    public static void updateAlignment(AbstractWidget parent, List<AbstractWidget> children) {
        if (parent == null || children == null) return;

        StyleSheet parentSheet = parent.getStyle();

        for (AbstractWidget child : children) {
            if (child.getLocalizedName().equals(parent.getLocalizedName())) {
                continue;
            }

            double childMinX = parent.getPositionX();
            double childMinY = parent.getPositionY();
            double childMaxX = ((childMinX + parent.getWidth()) - (child.getWidth()));
            double childMaxY = ((childMinY + parent.getHeight()) - (child.getHeight()));

            double innerStartX = childMinX + child.getInnerParentX();
            double innerStartY = childMinY + child.getInnerParentY();

            innerStartX = Math.max(innerStartX, childMinX);
            innerStartY = Math.max(innerStartY, childMinY);
            innerStartX = Math.min(innerStartX, childMaxX);
            innerStartY = Math.min(innerStartY, childMaxY);

            switch (parentSheet.getAlignment()) {
                case TOP_LEFT -> child.setPosition(childMinX, childMinY);
                case TOP_RIGHT -> child.setPosition(childMaxX, childMinY);
                case TOP_CENTER -> {
                    double center = childMinX + (parent.getWidth() / 2) - (child.getWidth() / 2);
                    child.setPosition(center, childMinY);
                }
                case BOTTOM_LEFT -> child.setPosition(childMinX, childMaxY);
                case BOTTOM_RIGHT -> child.setPosition(childMaxX, childMaxY);
                case BOTTOM_CENTER -> {
                    double center = childMinX + (parent.getWidth() / 2) - (child.getWidth() / 2);
                    child.setPosition(center, childMaxY);
                }
                case CENTER_LEFT -> {
                    double centerY = childMinY + (parent.getHeight() / 2) - (child.getHeight() / 2);
                    child.setPosition(childMinX, centerY);
                }
                case CENTER_RIGHT -> {
                    double centerY = childMinY + (parent.getHeight() / 2) - (child.getHeight() / 2);
                    child.setPosition(childMaxX, centerY);
                }
                case CENTER -> {
                    double centerX = childMinX + (parent.getWidth() / 2) - (child.getWidth() / 2);
                    double centerY = childMinY + (parent.getHeight() / 2) - (child.getHeight() / 2);
                    child.setPosition(centerX, centerY);
                }
                case NOTHING -> child.setPosition(innerStartX, innerStartY);
                default -> throw new IllegalStateException("Unexpected value: " + parentSheet.getAlignment());
            }
        }
    }
}

