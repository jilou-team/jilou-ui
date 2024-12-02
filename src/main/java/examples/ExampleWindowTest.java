package examples;

import com.jilou.ui.JilouUI;
import com.jilou.ui.container.Window;
import com.jilou.ui.enums.Backend;

public class ExampleWindowTest {

    public static void main(String[] args) {
        JilouUI.load(args);

        Window window = new Window();
        window.setBackend(Backend.VULKAN);
        window.start();
        window.show();
        window.setTitle("Example Window");
    }

}