package examples;

import com.jilou.ui.JilouUI;
import com.jilou.ui.container.Scene;
import com.jilou.ui.container.Window;
import com.jilou.ui.widget.control.Text;

public final class ExampleLabelTest {

    public static void main(String[] args) {
        JilouUI.load(args);

        Window window = new Window();
        window.start();
        window.show();
        window.setTitle("Example Label");

        Scene scene = window.getActiveScene();
        Text label = new Text("Hallo Fabian!");
        label.setPosition(20, 200);

        scene.add(label);
    }

}
