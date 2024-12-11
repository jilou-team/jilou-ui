package com.jilou.ui.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class Files {

    private static final Logger LOGGER = LogManager.getLogger(Files.class);

    private Files() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * This method get the current path of the file path.
     * This means if the path ends with filename dot extension.
     * You can use this method to catch the path without the filename.
     * @param path the file path.
     * @return the path without the filename.
     */
    public static String getFileDirectoryFromPath(String path) {
        if(path == null) return "";
        path = fixPath(path);
        String directory = path;

        if(directory.contains(File.separator)) {
            directory = directory.substring(0, path.lastIndexOf(File.separatorChar));
        }

        if(directory.endsWith(File.separator)) {
            return directory;
        }
        return directory + File.separatorChar;
    }

    /**
     * This method catch the filename from a path.
     * @param path the file path.
     * @return the filename witch was found.
     */
    public static String getFileNameFromPath(String path) {
        if(path == null) return "";
        path = fixPath(path);
        return path.substring(path.lastIndexOf(File.separatorChar) + 1);
    }

    /**
     * This method returned only the filename without the extension.
     * @param file the file witch will be lost his extension.
     * @return the result filename.
     */
    public static String getOnlyFileName(String file) {
        String withoutExtension = file;

        int pos = withoutExtension.lastIndexOf(".");
        if(pos != -1) {
            withoutExtension = withoutExtension.substring(0, pos);
        }
        return withoutExtension;
    }

    /**
     * This method returned only the file extension.
     * This is useful for if statements or search for types.
     * @param pathOrFile the given path or file.
     * @return the extension of the path/file
     */
    public static String getFileExtension(String pathOrFile) {
        String fileName = getFileNameFromPath(pathOrFile);
        String converted = getOnlyFileName(fileName);
        return converted.replace(fileName, "");
    }

    /**
     * This method fixed path variables, this means the separator will be changed to readable thinks.
     * This is useful if you use multiply operating systems.
     * @param path to fix file path.
     * @return the fixed path.
     */
    public static String fixPath(String path) {
        path = path.replace("\\/", File.separator)
                .replace("\\\\", File.separator)
                .replace("\\", File.separator)
                .replace("/", File.separator);
        return path;
    }

    /**
     * This method create a byte buffer from the given resource.
     * @param resource to convert as byte buffer.
     * @param size the buffer size.
     * @return the finished buffer, buffer is flipped!
     */
    public static ByteBuffer resourceToByteBuffer(String resource, int size) {
        ByteBuffer data = null;
        try {
            InputStream stream = inputStream(resource);
            if (stream == null) {
                LOGGER.error("File [ {} ] can't be found!", resource);
                return null;
            }
            byte[] bytes = toByteArray(stream, size);
            data = ByteBuffer.allocateDirect(bytes.length).order(ByteOrder.nativeOrder()).put(bytes);
            data.flip();
        } catch (IOException exception) {
            LOGGER.error(exception);
        }
        return data;
    }

    /**
     * This method create a byte buffer from the given resource.
     * Warning the size is caught by the resource.
     * @param resource to convert as byte buffer.
     * @return the finished buffer, buffer is flipped!
     */
    public static ByteBuffer resourceToByteBuffer(String resource) {
        return resourceToByteBuffer(resource, capacity(resource));
    }

    /**
     * This method searched for the running location of the program.
     * The location is the correct path to the .exe.
     * @return the current run path.
     */
    public static File getRunningJarLocation(Class<?> c) {
        File file = null;
        try {
            file = new File(c.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException exception) {
            LOGGER.error(exception);
        }
        return file;
    }

    /**
     * @return the parent directory of the running jar file.
     */
    public static File getRunningJarParent(Class<?> c) {
        File file = null;
        try {
            File jarLocation = getRunningJarLocation(c);
            if(jarLocation != null) {
                file = new File(jarLocation.getParentFile().getPath());
            }
        } catch (NullPointerException exception) {
            LOGGER.error(exception);
        }
        return file;
    }

    /**
     * This method create an input stream from the given resource.
     * If the resource not found, the method will throw an IOException.
     * @param resource the file resource.
     * @return the resource as stream.
     * @throws IOException if the resource wasn't found.
     */
    public static InputStream inputStream(String resource) throws IOException {
        InputStream stream;
        File file = new File(resource);
        if(file.exists() && file.isFile()) {
            stream = new FileInputStream(file);
        } else {
            stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        }
        return stream;
    }

    /**
     * This method create a byte array from the given stream.
     * @param stream to convert as byte array.
     * @param size the array size.
     * @return the generated byte array.
     */
    public static byte[] toByteArray(InputStream stream, int size) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read;
        byte[] data = new byte[size];

        try {
            while ((read = stream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, read);
            }
            buffer.flush();
        } catch (IOException exception) {
            LOGGER.error(exception);
        }
        return buffer.toByteArray();
    }

    /**
     * This method returned the correct resource length as byte[].
     * @param path the resource witch give us the size.
     * @return the length value witch is stored.
     */
    public static int capacity(String path) {
        int capacity = 0;
        try(InputStream stream = new FileInputStream(path)) {
            capacity = stream.available();
        } catch (IOException exception) {
            LOGGER.error(exception);
        }
        return capacity;
    }


}
