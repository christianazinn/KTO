package components.menu;

import javax.swing.*;
//import java.awt.*;
import java.awt.event.*;

/**
 * {@code FileMenu} is a class to create a {@code JMenu} containing File-related options for the {@link MainMenuBar} to use.
 * 
 * @author Christian Azinn
 * @version 0.1
 * @since 0.0.2
 */
public class FileMenu extends JMenu {
    public FileMenu(ActionListener parent) {
        // Create a JMenu with name "File" and set its AccessibleDescription
        super("File");
        getAccessibleContext().setAccessibleDescription("Menu for file interactions.");

        // Placeholder menu item
        JMenuItem menuItem = new JMenuItem("A text-only menu item");
        menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        add(menuItem);
        menuItem.setActionCommand("item1");
        menuItem.addActionListener(parent);
    }
}
