package components.menu;

import javax.swing.*;
import java.awt.event.*;

/**
 * {@code MainMenuBar} is a class to create a {@code JMenuBar} for the main window to use.
 * 
 * @author Christian Azinn
 * @version 0.4
 * @since 0.0.1
 */
public class MainMenuBar extends JMenuBar {
    
    // Instance variables for each Menu
    private JMenu fileMenu, branchMenu;

    // TODOLT - new menus

    public MainMenuBar(ActionListener a, boolean autosaveOn) {
        // Create new [thing]Menu classes and add them here so this file stays organized
        // Also write getter method for each JIC
        fileMenu = new FileMenu(a, autosaveOn);
        add(fileMenu);

        branchMenu = new BranchMenu(a);
        add(branchMenu);
    }


    /**
     * Getter method for the {@link FileMenu} used by this object.
     * @return a reference to the active {@link FileMenu}
     */
    public JMenu getFileMenu() {
        return fileMenu;
    }


    /**
     * Getter method for the {@link BranchMenu} used by this object.
     * @return a reference to the active {@link BranchMenu}
     */
    public JMenu getBranchMenu() {
        return branchMenu;
    }
}
