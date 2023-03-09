package components.menu;

import javax.swing.*;
//import java.awt.*;
import java.awt.event.*;

/**
 * {@code FileMenu} is a class to create a {@code JMenu} containing File-related options for the {@link MainMenuBar} to use.
 * 
 * @author Christian Azinn
 * @version 0.2
 * @since 0.0.2
 */
public class FileMenu extends JMenu {
    public FileMenu(ActionListener a) {
        // Create a JMenu with name "File" and set its AccessibleDescription
        super("File");
        String prefix = "#FILE";
        getAccessibleContext().setAccessibleDescription("Menu for file interactions.");

        JMenuItem saveItem = new JMenuItem("Save...");
        saveItem.getAccessibleContext().setAccessibleDescription("Saves the current file.");
        add(saveItem);
        saveItem.setActionCommand(prefix + "Save");
        saveItem.addActionListener(a);

        // TODO - implement these
        JMenuItem openItem = new JMenuItem("Open...");
        openItem.getAccessibleContext().setAccessibleDescription("Opens a new file.");
        add(openItem);
        openItem.setActionCommand(prefix + "Open");
        openItem.addActionListener(a);

        JMenuItem newItem = new JMenuItem("New...");
        newItem.getAccessibleContext().setAccessibleDescription("Creates a new file.");
        add(newItem);
        newItem.setActionCommand(prefix + "New");
        newItem.addActionListener(a);
    }
}
