package components.menu;

import javax.swing.*;
import java.awt.event.*;

/**
 * {@code FileMenu} is a class to create a {@code JMenu} containing file-related options for the {@link MainMenuBar} to use.
 * 
 * @author Christian Azinn
 * @version 0.8
 * @since 0.0.2
 */
public class FileMenu extends MenuBase {
    public FileMenu(ActionListener a, boolean autosaveOn) {
        // Create a JMenu with name "File" and set its AccessibleDescription
        super("File", "#FILE", a);
        getAccessibleContext().setAccessibleDescription("Menu for file interactions.");

        // Toggle autosave
        JMenuItem autosaveItem = newRadioButton("Autosave", autosaveOn);
        format(autosaveItem, "Toggles autosave.", "Auto");
        add(autosaveItem);

        // Save file
        JMenuItem saveItem = newButton("Save...", "img/save.png");
        format(saveItem, "Saves the current file.", "Save");
        add(saveItem);
        
        // Save as file
        JMenuItem saveAsItem = newButton("Save As...", "img/saveAs.png");
        format(saveAsItem, "Saves the current file under another name.", "Svas");
        add(saveAsItem);

        // Open file
        JMenuItem openItem = newButton("Open...", "img/folder.png");
        format(openItem, "Opens a new file.", "Open");
        add(openItem);

        // New file
        JMenuItem newItem = newButton("New...", "img/new.png");
        format(newItem, "Creates a new file.", "New");
        add(newItem);

        // Save defaults
        JMenuItem defaultItem = newButton("Save Defaults...", "img/saveDefaults.png");
        format(defaultItem, "Saves active settings to default", "Def");
        add(defaultItem);

        // Debug
        JMenuItem testItem = newButton("Debug...", "img/bug.png");
        format(testItem, "Does some debugging things.", "Dbug");
        add(testItem);
    }
}
