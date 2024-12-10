package com.jilou.ui.logic.graphics.font;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Font {

    private static final Logger logger = LogManager.getLogger(Font.class);

    private String path;
    private String fontName;

    private boolean created;
    private boolean updated;

    private String errorReason;

    private final List<FontFaces> fontFaces = new ArrayList<>();


}
