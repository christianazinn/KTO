import javax.swing.*;
import java.awt.*;

// fuck it abspos
// remember to use multiple content panes to separate different parts of the ui
// each content pane can be individually moved, resized, etc

/**
 * {@code KTOJF} is the main file of the KTO JFrame-based application.
 * 
 * @author Christian Azinn
 * @version 0.1
 * @since 0.0.1
 */
public class KTOJF extends JFrame {
    public KTOJF() {
        super("KTO ver 0.0.1 pre-alpha");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setJMenuBar(new MainMenuBar());

        // for whatever reason this shows up double size on my screen
        setMinimumSize(new Dimension(960,540));
        setLocationRelativeTo(null);

        setVisible(true);

    }

    public static void main(String[] args) {
        new KTOJF();
    }
}
