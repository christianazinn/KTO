import components.location.*;
import components.menu.*;
import components.primary.*;
import components.sidebar.*;
import util.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

// remember to use multiple content panes to separate different parts of the ui
// each content pane can be individually moved, resized, etc

/**
 * {@code KTOJF} is the main file of the KTO JFrame-based application. 
 * Also handles all button interactions because it already has a handle on everything.
 * 
 * @author Christian Azinn
 * @version 0.0.4
 * @since 0.0.1
 */
public class KTOJF extends JFrame implements ActionListener {

    // Instance variable for the insets
    private Insets insets;
    // More instance variables for every component created
    private MainMenuBar mmBar;
    private LocationBar locBar;
    private SidebarPane sbPane;
    private SidebarScrollPane ssPane;
    private PrimaryTextPane ptPane;
    private PrimaryScrollPane psPane;
    private CSVManager csv;

    public KTOJF() {

        // Create JFrame and title it
        super("KTO ver 0.0.4 pre-alpha");

        // Set to exit program on window close, absolute positioning layout, and icon
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setIconImage(new ImageIcon("kto.png").getImage());

        // Instantiate MainMenuBar and set as menu bar
        mmBar = new MainMenuBar(this);
        setJMenuBar(mmBar);

        // Instantiate CSVManager
        csv = new CSVManager();
        try { csv.open("todo"); } catch(Exception e) {} // TEMP

        // Get insets
        insets = getInsets();

        // Create LocationBar, position properly, and add
        locBar = new LocationBar("test");
        Dimension size = locBar.getPreferredSize();
        locBar.setBounds(insets.left, insets.top, size.width, size.height);
        add(locBar);

        // Test code for SidebarScrollPane, TBR
        ArrayList<String> e = new ArrayList<String>();
        e.add("@hi");
        e.add("hello[test lmao]");
        e.add("@test2[mris]");
        e.add("@tzpn");
        e.add("@npqw");
        sbPane = new SidebarPane(e, this);
        ssPane = new SidebarScrollPane(sbPane);
        size = ssPane.getPreferredSize();
        ssPane.setBounds(insets.left, insets.top + Constants.GraphicsConstants.LOCBARHEIGHT, size.width, size.height);
        add(ssPane);

        // Test code for PrimaryScrollPane, TBR
        ptPane = new PrimaryTextPane("This is a test PrimaryTextPane!");
        psPane = new PrimaryScrollPane(ptPane);
        size = psPane.getPreferredSize();
        psPane.setBounds(insets.left + Constants.GraphicsConstants.SBWIDTH, 
                    insets.top + Constants.GraphicsConstants.PSPVOFFSET, size.width, size.height);
        add(psPane);

        // For whatever reason this shows up double size on my screen
        // Set size
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SCREENWIDTH, Constants.GraphicsConstants.SCREENHEIGHT));

        // Ensure all elements are shown on screen, center window on screen, and set visible
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }


    /**
     * Manages all button {@link ActionEvent}s.
     * @param a an {@link ActionEvent} sent by some sort of Button
     */
    public void actionPerformed(ActionEvent a) {
        String e = a.getActionCommand();
        char tag = e.charAt(0);
        switch(tag) {
            case '@': // redirect downward
                locBar.directoryDown(e.substring(1)); // TODO: implement check to ensure directory exists
                // TODO: something to actually redirect downward
                break;
            case '#': // menu option
                System.out.println("Menu option placeholder!");
                break;
            case '“': // back option
                locBar.directoryUp(1);
                break;
            case '”': // new option
                break;
            default:  // display text
                break;
        }
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
