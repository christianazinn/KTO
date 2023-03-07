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
    public FileMenu(ActionListener a) {
        // Create a JMenu with name "File" and set its AccessibleDescription
        super("File");
        String prefix = "#FILE";
        getAccessibleContext().setAccessibleDescription("Menu for file interactions.");

        // Placeholder menu item
        JMenuItem menuItem = new JMenuItem("Save...");
        menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        add(menuItem);
        menuItem.setActionCommand(prefix + "Save");
        menuItem.addActionListener(a);
    }
}
