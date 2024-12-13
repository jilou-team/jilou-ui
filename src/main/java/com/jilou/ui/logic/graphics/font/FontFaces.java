package com.jilou.ui.logic.graphics.font;

import com.jilou.ui.utils.Files;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;

import java.io.File;
import java.nio.ByteBuffer;

@Getter
public class FontFaces {

    @Getter(AccessLevel.PRIVATE)
    private final Logger logger = LogManager.getLogger(FontFaces.class);

    private String path;
    private String faceName;

    private ByteBuffer buffer;
    private STBTTBakedChar.Buffer chars;
    private int nativeID;

    private int ascent;
    private int descent;
    private int lineGap;

    public FontFaces(String path, String faceName) {
        if (path == null || faceName == null) {
            logger.error("Path or faceName is null!");
            return;
        }
        this.path = path;
        this.faceName = faceName;
    }

    protected void buildBuffer() {
        if(path.isBlank() || faceName.isBlank()) {
            logger.error("Path and faceName is blank!");
            return;
        }
        logger.debug("Try to generate native context in stb for {}", faceName);

        String source = path + "/" + faceName + ".ttf";
        File fontFile = new File(source);
        if(!fontFile.exists()) {
            logger.error("Font face file [{}] wasn't found!", faceName);
            return;
        }
        buffer = Files.resourceToByteBuffer(source);

        STBTTFontinfo fontInfo = STBTTFontinfo.create();
        if (!STBTruetype.stbtt_InitFont(fontInfo, buffer)) {
            fontInfo.free();
            logger.fatal("Failed to initialize STB font! [{}]", faceName);
            return;
        }

        int bitmapWidth = 512;
        int bitmapHeight = 1024;
        ByteBuffer bitmap = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight);
        STBTTBakedChar.Buffer charData = STBTTBakedChar.malloc(96);

        int result = STBTruetype.stbtt_BakeFontBitmap(
                buffer,
                16.0f, // Variable
                bitmap,
                bitmapWidth,
                bitmapHeight,
                32,
                charData
        );

        if(result <= 0) {
            logger.error("Can't create font chars for [{}]", faceName);
            return;
        }

        nativeID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, nativeID);

        GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D,
                0,
                GL11.GL_ALPHA,
                bitmapWidth,
                bitmapHeight,
                0,
                GL11.GL_ALPHA,
                GL11.GL_UNSIGNED_BYTE,
                bitmap
        );

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

        this.chars = charData;
            fontInfo.free();
    }

}
