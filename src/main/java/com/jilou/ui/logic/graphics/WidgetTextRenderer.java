package com.jilou.ui.logic.graphics;

import com.jilou.ui.container.LWJGLWindow;
import com.jilou.ui.logic.graphics.font.Font;
import com.jilou.ui.logic.graphics.mapper.TextNativeMapper;
import com.jilou.ui.widget.AbstractWidget;
import com.jilou.ui.widget.control.Text;

import java.util.List;

public class WidgetTextRenderer extends AbstractWidgetRenderer {

    private TextNativeMapper textNativeMapper;

    public WidgetTextRenderer() {
        super(null);
    }

    @Override
    public void render(List<AbstractWidget> widgets) {
        for (AbstractWidget widget : widgets) {
            if(widget instanceof Text text) {
                Font font = text.getFont();
                if(!font.isTest()) {
                    font.nativeLoadBuffers();
                }
                textNativeMapper.renderText(text);
            }
        }
    }

    @Override
    public void preLoad(LWJGLWindow nativeWindow) {
        this.textNativeMapper = new TextNativeMapper();
    }

    @Override
    public void dispose() {

    }
}
