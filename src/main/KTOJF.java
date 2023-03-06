import components.location.LocationBar;
import components.menu.MainMenuBar;
import components.sidebar.SidebarPane;
import components.sidebar.SidebarScrollPane;
import util.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.*;

// fuck it abspos
// remember to use multiple content panes to separate different parts of the ui
// each content pane can be individually moved, resized, etc

/**
 * {@code KTOJF} is the main file of the KTO JFrame-based application.
 * 
 * @author Christian Azinn
 * @version 0.0.3
 * @since 0.0.1
 */
public class KTOJF extends JFrame {

    private Insets insets;

    public KTOJF() {
        super("KTO ver 0.0.3 pre-alpha");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setJMenuBar(new MainMenuBar());
        insets = getInsets();

        LocationBar locbar = new LocationBar("test");
        Dimension size = locbar.getPreferredSize();
        locbar.setBounds(insets.left, insets.top, size.width, size.height);
        add(locbar);

        ArrayList<String> e = new ArrayList<String>();
        e.add("hi");
        e.add("hello[test lmao]");
        SidebarPane sidebar = new SidebarPane(e);
        SidebarScrollPane ssp = new SidebarScrollPane(sidebar);
        size = ssp.getPreferredSize();
        ssp.setBounds(insets.left, insets.top + Constants.GraphicsConstants.LOCBARHEIGHT, size.width, size.height);
        add(ssp);

        // for whatever reason this shows up double size on my screen
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SCREENWIDTH, Constants.GraphicsConstants.SCREENHEIGHT));
        pack();
        setLocationRelativeTo(null);

        setVisible(true);

    }

    public static void main(String[] args) {
        new KTOJF();
    }
}
