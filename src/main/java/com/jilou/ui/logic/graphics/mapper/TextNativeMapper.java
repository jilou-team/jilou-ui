package com.jilou.ui.logic.graphics.mapper;

import com.jilou.ui.logic.graphics.font.Font;
import com.jilou.ui.widget.control.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;

import java.nio.FloatBuffer;

public class TextNativeMapper {

    private final Logger logger = LogManager.getLogger(TextNativeMapper.class);

    public void renderText(Text text) {
        Font font = text.getFont();
        double x = text.getPositionX();
        double y = text.getPositionY();
        String toRenderText = text.getMessage();

        STBTTBakedChar.Buffer charData = font.getFace("Regular").getChars();
        if(charData == null) {
            return;
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getFace("Regular").getNativeID());

        FloatBuffer xPos = BufferUtils.createFloatBuffer(1);
        xPos.put((float) x).flip();
        FloatBuffer yPos = BufferUtils.createFloatBuffer(1);
        yPos.put((float) y).flip();

        STBTTAlignedQuad quad = STBTTAlignedQuad.malloc();

        GL11.glBegin(GL11.GL_QUADS);
        for(char c : toRenderText.toCharArray()) {
            if(c < 32 || c >= 32 + charData.capacity()) {
                continue;
            }

            STBTruetype.stbtt_GetBakedQuad(
                    charData,
                    512,
                    1024,
                    c - 32,
                    xPos,
                    yPos,
                    quad,
                    true
            );

            GL11.glTexCoord2f(quad.s0(), quad.t0());
            GL11.glVertex2f(quad.x0(), quad.y0());

            GL11.glTexCoord2f(quad.s1(), quad.t0());
            GL11.glVertex2f(quad.x1(), quad.y0());

            GL11.glTexCoord2f(quad.s1(), quad.t1());
            GL11.glVertex2f(quad.x1(), quad.y1());

            GL11.glTexCoord2f(quad.s0(), quad.t1());
            GL11.glVertex2f(quad.x0(), quad.y1());
        }

        GL11.glEnd();

        quad.free();
    }

}
