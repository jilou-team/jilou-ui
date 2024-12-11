package examples;

import com.jilou.ui.JilouUI;
import com.jilou.ui.container.Scene;
import com.jilou.ui.container.Window;
import com.jilou.ui.widget.control.Label;

public final class ExampleLabelTest {

    public static void main(String[] args) {
        JilouUI.load(args);

        Window window = new Window();
        window.start();
        window.show();
        window.setTitle("Example Label");

        Scene scene = window.getActiveScene();
        Label label = new Label("Test!");
        label.setPosition(20);

        scene.add(label);
    }

}
