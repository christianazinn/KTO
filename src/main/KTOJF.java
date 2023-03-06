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
 * @version 0.0.3a
 * @since 0.0.1
 */
public class KTOJF extends JFrame {

    // Instance variable for the insets because why not, I guess
    private Insets insets;

    public KTOJF() {

        // Create JFrame and title it
        super("KTO ver 0.0.3a pre-alpha");

        // Set to exit program on window close and absolute positioning layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Instantiate MainMenuBar and set as menu bar
        setJMenuBar(new MainMenuBar());

        // Get insets
        insets = getInsets();

        // Create LocationBar, position properly, and add
        LocationBar locbar = new LocationBar("test");
        Dimension size = locbar.getPreferredSize();
        locbar.setBounds(insets.left, insets.top, size.width, size.height);
        add(locbar);

        // Test code for SidebarScrollPane, TBR
        ArrayList<String> e = new ArrayList<String>();
        e.add("hi");
        e.add("hello[test lmao]");
        SidebarPane sidebar = new SidebarPane(e);
        SidebarScrollPane ssp = new SidebarScrollPane(sidebar);
        size = ssp.getPreferredSize();
        ssp.setBounds(insets.left, insets.top + Constants.GraphicsConstants.LOCBARHEIGHT, size.width, size.height);
        add(ssp);

        // For whatever reason this shows up double size on my screen
        // Set size
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SCREENWIDTH, Constants.GraphicsConstants.SCREENHEIGHT));

        // Ensure all elements are shown on screen, center window on screen, and set visible
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    /**
     * The method that's invoked when KTOJF is run. 
     * Does nothing but instantiate a KTOJF object and hand control to it.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        new KTOJF();
    }
}
