package com.jilou.ui.logic.graphics.font;

import com.jilou.ui.utils.Files;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;

@Getter
public class FontFaces {

    @Getter(AccessLevel.PRIVATE)
    private final Logger logger = LogManager.getLogger(FontFaces.class);

    private String path;
    private String faceName;

    private ByteBuffer buffer;

    public FontFaces(String path, String faceName) {
        if (path == null || faceName == null) {
            logger.error("Path or faceName is null!");
            return;
        }
        this.path = path;
        this.faceName = faceName;
        this.buildBuffer();
    }

    private void buildBuffer() {
        if(path.isBlank() || faceName.isBlank()) {
            logger.error("Path and faceName is blank!");
            return;
        }

        String source = path + "/" + faceName + ".ttf";
        buffer = Files.resourceToByteBuffer(source);
        if(buffer == null) {
            logger.error("Can't load face '{}' from file '{}'!", faceName, source);
        }
    }

}
