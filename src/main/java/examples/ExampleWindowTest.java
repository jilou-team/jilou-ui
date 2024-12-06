package examples;

import com.jilou.ui.JilouUI;
import com.jilou.ui.container.Scene;
import com.jilou.ui.container.Window;
import com.jilou.ui.enums.Backend;
import com.jilou.ui.widget.shapes.Rectangle;

import java.util.concurrent.TimeUnit;

public class ExampleWindowTest {

    public static void main(String[] args) throws InterruptedException {
        JilouUI.load(args);

        Window window = new Window();
        window.setBackend(Backend.VULKAN);
        window.start();
        window.show();
        window.setTitle("Example Window");

        Scene scene = window.getActiveScene();
        Rectangle rectangle = new Rectangle("Test");
        rectangle.setHeight(50.0);
        rectangle.setWidth(250.0);
        rectangle.setPositionX(20.0);
        rectangle.setPositionY(20.0);

        scene.add(rectangle);
    }

}