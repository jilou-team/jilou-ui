package com.jilou.ui.enums.font;

/**
 * This enum have stored know font face types.
 * The enum can be used instance of hard string usage.
 *
 * @since 0.1.0
 * @author Daniel Ramke
 */
public enum Faces {

    REGULAR("Regular", 0),
    BOLD("Bold", 1),
    BOLD_ITALIC("BoldItalic", 2),
    ITALIC("Italic", 3),
    LIGHT("Light", 4),
    LIGHT_ITALIC("LightItalic", 5),
    THIN("Thin", 6),
    THIN_ITALIC("ThinItalic", 7),
    MEDIUM("Medium", 8),
    MEDIUM_ITALIC("MediumItalic", 9),
    BLACK("Black", 10),
    BLACK_ITALIC("BlackItalic", 11);

    private final String name;
    private final int index;

    /**
     * This is the default enum constructor.
     * @param name the face name.
     * @param index the index to sort the faces.
     */
    Faces(String name, int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * @return the face name in correct format.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the index number.
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * @param name face name to check for enum const.
     * @return the enum object by found the name, if it not found this returns REGULAR.
     */
    public static Faces get(String name) {
        Faces face = REGULAR;
        for(Faces faces : values()) {
            if(faces.getName().equals(name)) {
                face = faces;
            }
        }
        return face;
    }

    /**
     * @param index face index to check for enum const.
     * @return the enum object by found the index, if it not found this returns REGULAR.
     */
    public static Faces get(int index) {
        Faces face = REGULAR;
        for(Faces faces : values()) {
            if(faces.getIndex() == index) {
                face = faces;
            }
        }
        return face;
    }

    /**
     * @return the face name as lower case, this is not the correct name of a face!
     * for the correct name use the method getName();
     */
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}

