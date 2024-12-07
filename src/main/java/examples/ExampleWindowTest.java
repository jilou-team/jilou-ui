package examples;

import com.jilou.ui.JilouUI;
import com.jilou.ui.container.Scene;
import com.jilou.ui.container.Window;
import com.jilou.ui.enums.Backend;
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
        rectangle.setHeight(350.0);
        rectangle.setWidth(400.0);
        rectangle.setPositionX(100.0);
        rectangle.setPositionY(100.0);

        scene.add(rectangle);
    }

}