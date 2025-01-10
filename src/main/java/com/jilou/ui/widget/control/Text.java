package com.jilou.ui.widget.control;

import com.jilou.ui.logic.graphics.font.Font;
import com.jilou.ui.styles.types.Background;
import com.jilou.ui.styles.types.Border;
import com.jilou.ui.utils.Color;
import com.jilou.ui.widget.AbstractWidget;
import lombok.Getter;

@Getter
public class Text extends AbstractWidget {

    private String message;
    private Font font;

    public Text(String message) {
        super(null);
        this.message = message == null ? "" : message;
        this.font = Font.FALLBACK;
        this.setHeight(20.0);
        this.setWidth(80.0);

        this.getStyle().setBackground(Background.fromColor(Color.WHITE));
        this.getStyle().setBorder(Border.builder().thickness(1).build());
    }

    public Text() {
        this("Label");
    }

    public void setFont(Font font) {
        if(font == null) this.font = Font.FALLBACK;
        else this.font = font;
    }

    public void setMessage(String message) {
        if(message == null) message = "";
        this.message = message;
    }

    @Override
    public void destroy() {

    }
}
