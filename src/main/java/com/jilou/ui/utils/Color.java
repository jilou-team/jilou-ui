package com.jilou.ui.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

/**
 * The Color class provides utilities to manage and manipulate colors in various formats such as RGB, HSB, hexadecimal, and more.
 * It includes methods for converting between color models and blending or modifying colors.
 */
@SuppressWarnings("unused")
public class Color {

    private static final Logger logger = LogManager.getLogger(Color.class);
    private static final Random random = new Random();

    private int red;
    private int green;
    private int blue;
    private int alpha;

    private int primitive;
    private float[] hsb;
    private String hexadecimal;

    /**
     * Creates a Color instance from a 32-bit integer representation.
     * @param primitive The 32-bit integer color value.
     */
    public Color(int primitive) {
        set(primitive);
    }

    /**
     * Creates a Color instance from a hexadecimal string.
     * @param hexadecimal The hexadecimal color string (e.g., "#RRGGBBAA").
     */
    public Color(String hexadecimal) {
        setHexadecimal(hexadecimal);
    }

    /**
     * Creates a Color instance from HSB values.
     * @param hue The hue component (0.0 to 1.0).
     * @param saturation The saturation component (0.0 to 1.0).
     * @param brightness The brightness component (0.0 to 1.0).
     */
    public Color(float hue, float saturation, float brightness) {
        convertHsbToRgb(hue, saturation, brightness);
    }

    /**
     * Creates a Color instance from RGB values as doubles.
     * @param red The red component (0.0 to 1.0).
     * @param green The green component (0.0 to 1.0).
     * @param blue The blue component (0.0 to 1.0).
     */
    public Color(double red, double green, double blue) {
        this(red, green, blue, 1.0);
    }

    /**
     * Creates a Color instance from RGBA values as doubles.
     * @param red The red component (0.0 to 1.0).
     * @param green The green component (0.0 to 1.0).
     * @param blue The blue component (0.0 to 1.0).
     * @param alpha The alpha component (0.0 to 1.0).
     */
    public Color(double red, double green, double blue, double alpha) {
        this((int) (red * 255), (int) (green * 255), (int) (blue * 255), (int) (alpha * 255));
    }

    /**
     * Creates a Color instance from RGB values as integers.
     * @param red The red component (0 to 255).
     * @param green The green component (0 to 255).
     * @param blue The blue component (0 to 255).
     */
    public Color(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    /**
     * Creates a Color instance from RGBA values as integers.
     * @param red The red component (0 to 255).
     * @param green The green component (0 to 255).
     * @param blue The blue component (0 to 255).
     * @param alpha The alpha component (0 to 255).
     */
    public Color(int red, int green, int blue, int alpha) {
        set(red, green, blue, alpha);
    }

    /**
     * Creates a copy of the current Color instance.
     * @return A new Color object with the same values.
     */
    public Color copy() {
        return new Color(this.primitive);
    }

    /**
     * Sets the color using RGBA values.
     * @param red The red component (0 to 255).
     * @param green The green component (0 to 255).
     * @param blue The blue component (0 to 255).
     * @param alpha The alpha component (0 to 255).
     */
    public void set(int red, int green, int blue, int alpha) {
        this.red = clamp(red);
        this.green = clamp(green);
        this.blue = clamp(blue);
        this.alpha = clamp(alpha);
        generatePrimitive();
        updateHexadecimal();
        updateHsb();
    }

    /**
     * Sets the color using a 32-bit integer representation.
     * @param primitive The 32-bit integer color value.
     */
    public void set(int primitive) {
        this.primitive = primitive;
        this.red = (primitive >> 16) & 0xFF;
        this.green = (primitive >> 8) & 0xFF;
        this.blue = primitive & 0xFF;
        this.alpha = (primitive >> 24) & 0xFF;
        updateHexadecimal();
        updateHsb();
    }

    /**
     * Sets the color using a hexadecimal string.
     * @param hexadecimal The hexadecimal color string (e.g., "#RRGGBBAA" or "#RRGGBB").
     */
    public void setHexadecimal(String hexadecimal) {
        if (!hexadecimal.startsWith("#")) {
            hexadecimal = "#" + hexadecimal;
        }

        try {
            if (hexadecimal.length() == 9) {
                set(
                        Integer.parseInt(hexadecimal.substring(1, 3), 16),
                        Integer.parseInt(hexadecimal.substring(3, 5), 16),
                        Integer.parseInt(hexadecimal.substring(5, 7), 16),
                        Integer.parseInt(hexadecimal.substring(7, 9), 16)
                );
            } else if (hexadecimal.length() == 7) {
                set(
                        Integer.parseInt(hexadecimal.substring(1, 3), 16),
                        Integer.parseInt(hexadecimal.substring(3, 5), 16),
                        Integer.parseInt(hexadecimal.substring(5, 7), 16),
                        255
                );
            } else {
                logger.error("Invalid hexadecimal format: [ {} ]", hexadecimal);
                set(0, 0, 0, 255);
            }
        } catch (NumberFormatException e) {
            logger.error("Error parsing hexadecimal: [ {} ]", hexadecimal, e);
            set(0, 0, 0, 255);
        }
    }

    /**
     * Converts the current RGB color to its hexadecimal representation with alpha included.
     */
    private void updateHexadecimal() {
        this.hexadecimal = String.format("#%02X%02X%02X%02X", red, green, blue, alpha);
    }

    /**
     * Converts the current RGB color to its hexadecimal representation without alpha.
     * @return The hexadecimal string representing the color without alpha.
     */
    public String getHexadecimalWithoutAlpha() {
        return String.format("#%02X%02X%02X", red, green, blue);
    }

    /**
     * Converts the current RGB color to HSB values and updates the internal HSB array.
     */
    private void updateHsb() {
        float r = red / 255.0f;
        float g = green / 255.0f;
        float b = blue / 255.0f;

        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));

        float delta = max - min;

        float hue = 0;
        if (delta > 0) {
            if (max == r) {
                hue = (g - b) / delta + (g < b ? 6 : 0);
            } else if (max == g) {
                hue = (b - r) / delta + 2;
            } else {
                hue = (r - g) / delta + 4;
            }
            hue /= 6;
        }

        float saturation = max == 0 ? 0 : delta / max;
        this.hsb = new float[]{hue, saturation, max};
    }

    /**
     * Converts HSB (Hue, Saturation, Brightness) values to RGB (Red, Green, Blue) and updates the color.
     *
     * @param hue        The hue component (0.0 to 1.0), representing the color type (e.g., red, green, blue).
     * @param saturation The saturation component (0.0 to 1.0), representing the intensity of the color.
     * @param brightness The brightness component (0.0 to 1.0), representing the lightness of the color.
     */
    public void convertHsbToRgb(float hue, float saturation, float brightness) {
        int redBit;
        int greenBit;
        int blueBit;

        if (saturation == 0) {
            redBit = greenBit = blueBit = (int) (brightness * 255.0f + 0.5f);
        } else {
            float tempH = (hue - (float) Math.floor(hue)) * 6.0f;
            float tempF = tempH - (float) Math.floor(tempH);
            float tempP = brightness * (1.0f - saturation);
            float tempQ = brightness * (1.0f - saturation * tempF);
            float tempT = brightness * (1.0f - (saturation * (1.0f - tempF)));

            switch ((int) tempH) {
                case 0 -> {
                    redBit = (int) (brightness * 255.0f + 0.5f);
                    greenBit = (int) (tempT * 255.0f + 0.5f);
                    blueBit = (int) (tempP * 255.0f + 0.5f);
                }
                case 1 -> {
                    redBit = (int) (tempQ * 255.0f + 0.5f);
                    greenBit = (int) (brightness * 255.0f + 0.5f);
                    blueBit = (int) (tempP * 255.0f + 0.5f);
                }
                case 2 -> {
                    redBit = (int) (tempP * 255.0f + 0.5f);
                    greenBit = (int) (brightness * 255.0f + 0.5f);
                    blueBit = (int) (tempT * 255.0f + 0.5f);
                }
                case 3 -> {
                    redBit = (int) (tempP * 255.0f + 0.5f);
                    greenBit = (int) (tempQ * 255.0f + 0.5f);
                    blueBit = (int) (brightness * 255.0f + 0.5f);
                }
                case 4 -> {
                    redBit = (int) (tempT * 255.0f + 0.5f);
                    greenBit = (int) (tempP * 255.0f + 0.5f);
                    blueBit = (int) (brightness * 255.0f + 0.5f);
                }
                case 5 -> {
                    redBit = (int) (brightness * 255.0f + 0.5f);
                    greenBit = (int) (tempP * 255.0f + 0.5f);
                    blueBit = (int) (tempQ * 255.0f + 0.5f);
                }
                default -> {
                    redBit = 0;
                    greenBit = 0;
                    blueBit = 0;
                }
            }
        }
        this.set((redBit << 16), (greenBit << 8), (blueBit), 0xff000000);
    }


    /**
     * Generates the 32-bit integer representation of the current RGBA color.
     */
    private void generatePrimitive() {
        this.primitive = ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF);
    }

    /**
     * Clamps an integer value between a minimum and maximum range.
     *
     * @param value The value to clamp.
     * @return The clamped value.
     */
    private static int clamp(int value) {
        return Math.clamp(value, 0, 255);
    }

    /**
     * Clamps a float value between a minimum and maximum range.
     *
     * @param value The value to clamp.
     * @return The clamped value.
     */
    private static float clamp(float value) {
        return Math.clamp(value, 0.0f, 1.0f);
    }

    /**
     * Blends two colors together based on a ratio.
     * @param c1 The first color.
     * @param c2 The second color.
     * @param ratio The blend ratio (0.0 to 1.0).
     * @return The blended color.
     */
    public static Color blend(Color c1, Color c2, float ratio) {
        ratio = clamp(ratio);
        int r = (int) (c1.red * (1 - ratio) + c2.red * ratio);
        int g = (int) (c1.green * (1 - ratio) + c2.green * ratio);
        int b = (int) (c1.blue * (1 - ratio) + c2.blue * ratio);
        int a = (int) (c1.alpha * (1 - ratio) + c2.alpha * ratio);
        return new Color(r, g, b, a);
    }

    /**
     * Converts the color to grayscale.
     * @return A new Color instance in grayscale.
     */
    public Color toGrayscale() {
        int avg = (red + green + blue) / 3;
        return new Color(avg, avg, avg, alpha);
    }

    /**
     * Returns the red component as a percentage (0.0 to 1.0).
     * @return The red percentage.
     */
    public float getRedPercent() {
        return red / 255.0f;
    }

    /**
     * Returns the red component as (0 to 255).
     * @return The red amount.
     */
    public int getRed() {
        return red;
    }

    /**
     * Returns the green component as a percentage (0.0 to 1.0).
     * @return The green percentage.
     */
    public float getGreenPercent() {
        return green / 255.0f;
    }

    /**
     * Returns the green component as (0 to 255).
     * @return The green amount.
     */
    public int getGreen() {
        return green;
    }

    /**
     * Returns the blue component as a percentage (0.0 to 1.0).
     * @return The blue percentage.
     */
    public float getBluePercent() {
        return blue / 255.0f;
    }

    /**
     * Returns the blue component as (0 to 255).
     * @return The blue amount.
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Returns the alpha component as a percentage (0.0 to 1.0).
     * @return The alpha percentage.
     */
    public float getAlphaPercent() {
        return alpha / 255.0f;
    }

    /**
     * Returns the alpha component as (0 to 255).
     * @return The alpha amount.
     */
    public int getAlpha() {
        return alpha;
    }


    /**
     * Retrieves the primitive RGB value of the color as an integer.
     *
     * @return The primitive RGB value, represented as an integer. The value is a combination of
     *         red, green, and blue components, typically packed into a single integer (e.g.,
     *         RGB value 0xRRGGBB).
     */
    public int getPrimitive() {
        return primitive;
    }

    /**
     * Retrieves the hexadecimal representation of the color as a string.
     *
     * @return The color in hexadecimal format, e.g., "#RRGGBB", where RR, GG, and BB
     *         represent the red, green, and blue components in hexadecimal notation.
     */
    public String getHexadecimal() {
        return hexadecimal;
    }

    /**
     * Retrieves the HSB (Hue, Saturation, Brightness) values of the color.
     *
     * @return An array of floats representing the HSB components:
     *         [0] = Hue (0 to 360),
     *         [1] = Saturation (0 to 1),
     *         [2] = Brightness (0 to 1).
     */
    public float[] getHsb() {
        return hsb;
    }

    /* ############################################################################################
     *
     *                                       Base Colors
     *
     * ############################################################################################ */

    /**
     * Black - The absence of color, represented by RGB(0, 0, 0).
     */
    public static final Color BLACK = new Color(0, 0, 0);

    /**
     * White - The combination of all colors, represented by RGB(255, 255, 255).
     */
    public static final Color WHITE = new Color(255, 255, 255);

    /**
     * Red - A bright, pure red color, represented by RGB(255, 0, 0).
     */
    public static final Color RED = new Color(255, 0, 0);

    /**
     * Green - A bright, pure green color, represented by RGB(0, 255, 0).
     */
    public static final Color GREEN = new Color(0, 255, 0);

    /**
     * Blue - A bright, pure blue color, represented by RGB(0, 0, 255).
     */
    public static final Color BLUE = new Color(0, 0, 255);

    /**
     * Yellow - A bright yellow color, represented by RGB(255, 255, 0).
     */
    public static final Color YELLOW = new Color(255, 255, 0);

    /**
     * Cyan - A bright cyan color, represented by RGB(0, 255, 255).
     */
    public static final Color CYAN = new Color(0, 255, 255);

    /**
     * Magenta - A bright magenta color, represented by RGB(255, 0, 255).
     */
    public static final Color MAGENTA = new Color(255, 0, 255);

    /**
     * Orange - A vibrant orange color, represented by RGB(255, 165, 0).
     */
    public static final Color ORANGE = new Color(255, 165, 0);

    /**
     * Purple - A deep purple color, represented by RGB(128, 0, 128).
     */
    public static final Color PURPLE = new Color(128, 0, 128);

    /**
     * Pink - A soft pink color, represented by RGB(255, 192, 203).
     */
    public static final Color PINK = new Color(255, 192, 203);

    /**
     * Grey - A neutral gray color, represented by RGB(169, 169, 169).
     */
    public static final Color GREY = new Color(169, 169, 169);

    /**
     * Brown - A deep brown color, represented by RGB(139, 69, 19).
     */
    public static final Color BROWN = new Color(139, 69, 19);

    /* ############################################################################################
     *
     *                                       Optional Colors
     *
     * ############################################################################################ */

    /**
     * Alice Blue - A very light blue shade with a hint of blue.
     */
    public static final Color ALICE_BLUE = new Color(240, 248, 255);

    /**
     * Antique White - A soft, warm beige color with a hint of pink.
     */
    public static final Color ANTIQUE_WHITE = new Color(250, 235, 215);

    /**
     * Aqua - A bright cyan color.
     */
    public static final Color AQUA = new Color(0, 255, 255);

    /**
     * Aquamarine - A pale greenish-blue shade.
     */
    public static final Color AQUAMARINE = new Color(127, 255, 212);

    /**
     * Azure - A light blue-white shade.
     */
    public static final Color AZURE = new Color(240, 255, 255);

    /**
     * Beige - A warm, light brownish color.
     */
    public static final Color BEIGE = new Color(245, 245, 220);

    /**
     * Bisque - A light, brownish color.
     */
    public static final Color BISQUE = new Color(255, 228, 196);

    /**
     * Blanched Almond - A pale, slightly yellowish color.
     */
    public static final Color BLANCHED_ALMOND = new Color(255, 235, 205);

    /**
     * Blue Violet - A vivid blue-violet shade.
     */
    public static final Color BLUE_VIOLET = new Color(138, 43, 226);

    /**
     * Burnt Orange - A deep, brownish-orange shade.
     */
    public static final Color BURNT_ORANGE = new Color(204, 85, 0);

    /**
     * Burnt Sienna - A rich, reddish-brown shade.
     */
    public static final Color BURNT_SIENNA = new Color(233, 116, 81);

    /**
     * Cadet Blue - A soft, bluish-gray color.
     */
    public static final Color CADET_BLUE = new Color(95, 158, 160);

    /**
     * Chartreuse - A bright, yellow-green color.
     */
    public static final Color CHARTREUSE = new Color(127, 255, 0);

    /**
     * Chocolate - A rich, dark brown color.
     */
    public static final Color CHOCOLATE = new Color(210, 105, 30);

    /**
     * Coral - A bright, reddish-orange color.
     */
    public static final Color CORAL = new Color(255, 127, 80);

    /**
     * Cornflower Blue - A medium, slightly purplish blue.
     */
    public static final Color CORNFLOWER_BLUE = new Color(100, 149, 237);

    /**
     * Crimson - A strong, red color with a slight blue undertone.
     */
    public static final Color CRIMSON = new Color(220, 20, 60);

    /**
     * Dark Blue - A deep, dark blue.
     */
    public static final Color DARK_BLUE = new Color(0, 0, 139);

    /**
     * Dark Cyan - A deep cyan with a hint of green.
     */
    public static final Color DARK_CYAN = new Color(0, 139, 139);

    /**
     * Dark Goldenrod - A dark, yellowish-brown color.
     */
    public static final Color DARK_GOLDENROD = new Color(184, 134, 11);

    /**
     * Dark Gray - A deep gray color.
     */
    public static final Color DARK_GRAY = new Color(169, 169, 169);

    /**
     * Dark Green - A deep, forest green.
     */
    public static final Color DARK_GREEN = new Color(0, 100, 0);

    /**
     * Dark Khaki - A dark yellow-brown color.
     */
    public static final Color DARK_KHAKI = new Color(189, 183, 107);

    /**
     * Dark Magenta - A deep purple-magenta color.
     */
    public static final Color DARK_MAGENTA = new Color(139, 0, 139);

    /**
     * Dark Olive Green - A dark olive green color.
     */
    public static final Color DARK_OLIVE_GREEN = new Color(85, 107, 47);

    /**
     * Dark Orange - A deep, reddish-orange color.
     */
    public static final Color DARK_ORANGE = new Color(255, 140, 0);

    /**
     * Dark Orchid - A dark purple color.
     */
    public static final Color DARK_ORCHID = new Color(153, 50, 204);

    /**
     * Dark Red - A deep, dark red color.
     */
    public static final Color DARK_RED = new Color(139, 0, 0);

    /**
     * Dark Salmon - A darker, salmon-colored shade.
     */
    public static final Color DARK_SALMON = new Color(233, 150, 122);

    /**
     * Dark Sea Green - A dark green with a blueish hue.
     */
    public static final Color DARK_SEA_GREEN = new Color(143, 188, 143);

    /**
     * Dark Slate Blue - A dark, muted blue.
     */
    public static final Color DARK_SLATE_BLUE = new Color(72, 61, 139);

    /**
     * Dark Slate Gray - A dark grayish-blue.
     */
    public static final Color DARK_SLATE_GRAY = new Color(47, 79, 79);

    /**
     * Dark Turquoise - A deep, bright turquoise color.
     */
    public static final Color DARK_TURQUOISE = new Color(0, 206, 209);

    /**
     * Dark Violet - A deep, vibrant violet.
     */
    public static final Color DARK_VIOLET = new Color(148, 0, 211);

    /**
     * Deep Pink - A vibrant, deep pink color.
     */
    public static final Color DEEP_PINK = new Color(255, 20, 147);

    /**
     * Deep Sky Blue - A bright, sky-blue color.
     */
    public static final Color DEEP_SKY_BLUE = new Color(0, 191, 255);

    /**
     * Dim Gray - A muted, dark gray color.
     */
    public static final Color DIM_GRAY = new Color(105, 105, 105);

    /**
     * Firebrick - A dark red-brown color.
     */
    public static final Color FIREBRICK = new Color(178, 34, 34);

    /**
     * Floral White - A very light, creamy white with a hint of yellow.
     */
    public static final Color FLORAL_WHITE = new Color(255, 250, 240);

    /**
     * Forest Green - A deep, rich green.
     */
    public static final Color FOREST_GREEN = new Color(34, 139, 34);

    /**
     * Fuchsia - A vivid purple-pink color.
     */
    public static final Color FUCHSIA = new Color(255, 0, 255);

    /**
     * Gainsboro - A light, soft gray.
     */
    public static final Color GAINSBORO = new Color(220, 220, 220);

    /**
     * Ghost White - A very pale, slightly bluish white.
     */
    public static final Color GHOST_WHITE = new Color(248, 248, 255);

    /**
     * Gold - A rich, bright yellow color.
     */
    public static final Color GOLD = new Color(255, 215, 0);

    /**
     * Goldenrod - A warm, yellow-brown color.
     */
    public static final Color GOLDENROD = new Color(218, 165, 32);

    /**
     * Gray - A neutral gray color.
     */
    public static final Color GRAY = new Color(128, 128, 128);

    /**
     * Green Yellow - A bright yellow-green color.
     */
    public static final Color GREEN_YELLOW = new Color(173, 255, 47);

    /**
     * Honeydew - A pale, soft green color.
     */
    public static final Color HONEYDEW = new Color(240, 255, 240);

    /**
     * Hot Pink - A vibrant, hot pink color.
     */
    public static final Color HOT_PINK = new Color(255, 105, 180);

    /**
     * Transparent - A fully transparent color (Alpha = 0).
     */
    public static final Color transparent = new Color(0, 0, 0, 0);

    /**
     * Create a new color from the given int values. The color range for rgb (0 - 255).
     * If the value outside the range, the result color will be different to the wish color!
     * The alpha component is by default 255 (MAX)
     *
     * @param red   the red component
     * @param green the green component
     * @param blue  the blue component
     * @return the finished color from the given components.
     */
    public static Color rgb(int red, int green, int blue) {
        return rgba(red, green, blue, 255);
    }

    /**
     * Create a new color from the given int values. The color range for rgba (0 - 255).
     * If the value outside the range, the result color will be different to the wish color!
     *
     * @param red   the red component
     * @param green the green component
     * @param blue  the blue component
     * @param alpha the alpha component
     * @return the finished color from the given components.
     */
    public static Color rgba(int red, int green, int blue, int alpha) {
        return new Color(red, green, blue, alpha);
    }

    /**
     * Create a new color from the given int values. The color range for rgb (0.0 - 1.0).
     * If the value outside the range, the result color will be different to the wish color!
     * The alpha component is by default 1.0 (MAX)
     *
     * @param red   the red component
     * @param green the green component
     * @param blue  the blue component
     * @return the finished color from the given components.
     */
    public static Color rgb(double red, double green, double blue) {
        return rgba(red, green, blue, 1.0);
    }

    /**
     * Create a new color from the given int values. The color range for rgba (0.0 - 1.0).
     * If the value outside the range, the result color will be different to the wish color!
     *
     * @param red   the red component
     * @param green the green component
     * @param blue  the blue component
     * @param alpha the alpha component
     * @return the finished color from the given components.
     */
    public static Color rgba(double red, double green, double blue, double alpha) {
        return new Color(red, green, blue, alpha);
    }

    /**
     * Creates a color from the given hsb values.
     * Note this method is raw and not all user use this, The reason is we have rgba support.
     *
     * @param hue        the color temperature
     * @param saturation the color saturation
     * @param brightness the color lightning
     * @return the finished color from the given hsb codes.
     */
    public static Color hsb(float hue, float saturation, float brightness) {
        return new Color(hue, saturation, brightness);
    }

    /**
     * Creates a color from the given hsb values.
     * Note this method is raw and not all user use this, The reason is we have rgba support.
     *
     * @param hsb an array with length 3;
     * @return the finished color from the given hsb codes.
     */
    public static Color hsb(float[] hsb) {
        if (hsb.length < 3) {
            return Color.BLACK;
        }
        return hsb(hsb[0], hsb[1], hsb[2]);
    }

    /**
     * Creates a color from the given hexadecimal string value.
     *
     * @param hexadecimal e.g #FFFFFF for white color.
     * @return the finished color from the given hex code.
     */
    public static Color hexadecimal(String hexadecimal) {
        return new Color(hexadecimal);
    }

    /**
     * Create a new color form the primitive int value. This means you can create some color from all components.
     * The components (red green blue, alpha).
     *
     * @param primitive the primitive number like -100225667
     * @return the finished color from the given components.
     */
    public static Color primitive(int primitive) {
        return new Color(primitive);
    }

    /**
     * Creates a new color from a java Random. The color value is created by mixing the {@link Color#rgb(int, int, int)} method.
     *
     * @return the finished color from the random.
     */
    public static Color randomRGB() {
        return rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }


}