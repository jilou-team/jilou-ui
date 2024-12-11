package com.jilou.ui.logic.graphics.font;

import com.jilou.ui.utils.Files;
import com.jilou.ui.utils.Paths;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Font {

    private final Logger logger = LogManager.getLogger(Font.class);

    private String path;
    private String fontName;

    private boolean created;
    private boolean updated;

    private String errorReason;

    private final List<FontFaces> fontFaces = new ArrayList<>();

    public Font(String path, String fontName) {
        this(path + "/" + fontName);
    }

    public Font(String absolutePath) {
        if(absolutePath == null || absolutePath.isEmpty()) {
            logger.warn("Font path is null or empty.");
            return;
        }

        this.fontName = Files.getFileNameFromPath(absolutePath);
        this.path = absolutePath.substring(0, (absolutePath.length() - fontName.length()));
        this.fetchFaces();
        if(created) {
            logger.debug("Font [{}] has been created", fontName);
            return;
        }
        logger.error("Font [{}] has not been created! Reason: {}", fontName, errorReason);
    }

    public void update() {
        updated = false;
        this.fetchFaces();
        logger.debug("Font [{}] has been updated", fontName);
    }

    public String getAbsolutePath() {
        return getPath() + getFontName();
    }

    private void fetchFaces() {
        if (created && updated) {
            return;
        }

        File folder = new File(getAbsolutePath());
        if (!folder.exists() || !folder.isDirectory()) {
            errorReason = String.format("Font [%s] is not a folder!", getAbsolutePath());
            return;
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            errorReason = String.format("Font [%s] needs at least 1 font file to create a face but found [%s]", fontName, 0);
            return;
        }

        int valid = 0;
        int invalid = 0;
        int duplicate = 0;

        for (File file : files) {
            String name = file.getName();

            // Validate file name structure and extension
            if (!isValidFontFile(name)) {
                invalid++;
                continue;
            }

            // Extract font name and face name
            String[] nameParts = extractFontNameAndFace(name);
            if (nameParts == null) {
                invalid++;
                continue;
            }

            String fileFontName = nameParts[0];
            String faceName = nameParts[1];

            if (!getFontName().equalsIgnoreCase(fileFontName)) {
                invalid++;
                continue;
            }

            String packedName = fileFontName + "-" + faceName;

            if (getFace(packedName) != null) {
                if (!updated) {
                    continue;
                }
                duplicate++;
                continue;
            }

            fontFaces.add(new FontFaces(getAbsolutePath(), packedName));
            valid++;
        }

        logger.info("Font [{}] faces have been updated! Valid [{}], Invalid [{}], Duplicated [{}]", fontName, valid, invalid, duplicate);
        this.created = true;
        this.updated = true;
    }

    public FontFaces getFace(String name) {
        if(fontFaces.isEmpty()) return null;
        String formatted = name;
        if(!(name.contains("-"))) {
            formatted = getFontName() + "-" + name;
        }
        FontFaces face = null;
        for(FontFaces faces : fontFaces) {
            if(faces.getFaceName().equals(formatted)) {
                face = faces;
                break;
            }
        }
        return face;
    }

    private boolean isValidFontFile(String name) {
        return name.endsWith(".ttf") && name.contains("-") && name.indexOf('-') > 0 && name.lastIndexOf('.') > name.indexOf('-');
    }

    private String[] extractFontNameAndFace(String name) {
        int dashIndex = name.lastIndexOf('-');
        if (dashIndex == -1 || dashIndex == 0 || dashIndex == name.length() - 4) { // Check bounds for "-" and ".ttf"
            return null;
        }
        String fontName = name.substring(0, dashIndex);
        String faceName = name.substring(dashIndex + 1, name.length() - 4); // Remove ".ttf"
        return new String[]{fontName, faceName};
    }

    public static Font FALLBACK = new Font(Paths.internal("/assets/fonts/"), "Default");
}
