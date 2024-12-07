package com.jilou.ui.math;

/**
 * Math's class is a collection class.
 * It has different formulas and ways of calculating which we can often use.
 * This class can be constantly expanded if required.
 * @since 0.1.0
 * @author Daniel Ramke
 */
public final class Maths {

    /**
     * Private constructor to prevent instantiation of the {@code Maths} utility class.
     *
     * @throws IllegalStateException If the constructor is called.
     */
    private Maths() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * The constant {@code PI_90} represents the value of 90 degrees in radians.
     * The value is equal to {@code Math.PI / 2}.
     */
    public static final float PI_90 = (float) (Math.PI / 2);

    /**
     * The constant {@code PI_270} represents the value of 270 degrees in radians.
     * The value is equal to {@code 3 * Math.PI / 2}.
     */
    public static final float PI_270 = (float) (3 * Math.PI / 2);
    /**
     * Function checks if the value negative.
     * If the value negative so change the method it to value 0.
     * @param value the value to check.
     * @return {@link Double} - the result of the value.
     */
    public static double changeNegative(double value) {
        return value < 0 ? 0 : value;
    }

}
