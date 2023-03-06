import javax.swing.*;
import java.awt.*;

import menu.MainMenuBar;
import locbar.LocationBar;

// fuck it abspos
// remember to use multiple content panes to separate different parts of the ui
// each content pane can be individually moved, resized, etc

/**
 * {@code KTOJF} is the main file of the KTO JFrame-based application.
 * 
 * @author Christian Azinn
 * @version 0.2
 * @since 0.0.1
 */
public class KTOJF extends JFrame {

    private Insets insets;

    public KTOJF() {
        super("KTO ver 0.0.2 pre-alpha");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setJMenuBar(new MainMenuBar());
        insets = getInsets();

        // for whatever reason this shows up double size on my screen

        LocationBar locbar = new LocationBar("test");
        Dimension size = locbar.getPreferredSize();
        locbar.setBounds(insets.left, insets.top, size.width, size.height);
        add(locbar);

        setPreferredSize(new Dimension(960, 540));
        pack();
        setLocationRelativeTo(null);

        setVisible(true);

    }

    public static void main(String[] args) {
        new KTOJF();
    }
}
