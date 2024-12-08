package examples;

import com.jilou.ui.JilouUI;
import com.jilou.ui.container.Scene;
import com.jilou.ui.container.Window;
import com.jilou.ui.enums.Backend;
import com.jilou.ui.styles.types.Background;
import com.jilou.ui.styles.types.DropShadow;
import com.jilou.ui.styles.types.Radius;
import com.jilou.ui.utils.Color;
import com.jilou.ui.utils.format.Alignment;
import com.jilou.ui.widget.shapes.Rectangle;

public class ExampleWindowTest {

    public static void main(String[] args) {
        JilouUI.load(args);

        Window window = new Window();
        window.setBackend(Backend.VULKAN);
        window.start();
        window.show();
        window.setTitle("Example Window");

        Scene scene = window.getActiveScene();
        Rectangle rectangle = new Rectangle("Test");
        rectangle.setHeight(400);
        rectangle.setWidth(500);
        rectangle.setPositionX(100.0);
        rectangle.setPositionY(100.0);
        rectangle.getStyle().setBorderRadius(new Radius(10));
        rectangle.getStyle().setBackground(Background.fromColor(Color.WHITE));

        Rectangle rectangle2 = new Rectangle("Test2");
        rectangle2.setHeight(200);
        rectangle2.setWidth(200);
        rectangle2.setInnerParentX(200.0);
        rectangle2.setInnerParentY(120.0);

        rectangle2.getStyle().setBackground(Background.fromColor(Color.WHITE));
        rectangle2.getStyle().setAlignment(Alignment.CENTER);
        rectangle2.getStyle().setDropShadow(DropShadow.builder().layer(8).color(Color.rgba(0, 0, 0, 0.5)).strength(0.4f).offsetX(5).offsetY(-5).build());

        Rectangle rectangle3 = new Rectangle("Test3");
        rectangle3.setHeight(50);
        rectangle3.setWidth(50);

        rectangle3.getStyle().setBackground(Background.fromColor(Color.WHITE));

        rectangle2.addChild(rectangle3);
        rectangle.addChild(rectangle2);
        scene.add(rectangle);
    }

}