package com.jilou.ui.utils;

import java.net.URL;

/**
 * This class is a storage for random path methods.
 * The class can be used in all others classes.
 * <p>
 * @since 0.1.0
 * @author Daniel Ramke
 */

public final class Paths {

    /**
     * This method checked the given path for exist.
     * Note that this method called the resource url of the given class.
     * @param path - the extended path for the resource.
     * @return String - the complete resource path.
     */
    public static String internal(String path) {
        URL url = Paths.class.getResource(path);
        return url == null ? "" : url.getPath();
    }


}
