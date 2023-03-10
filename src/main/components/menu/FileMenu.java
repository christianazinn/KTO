package components.menu;

import util.Constants;

import javax.swing.*;
import java.awt.event.*;

/**
 * {@code FileMenu} is a class to create a {@code JMenu} containing File-related options for the {@link MainMenuBar} to use.
 * 
 * @author Christian Azinn
 * @version 0.4
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
        saveItem.setFont(Constants.FontConstants.FFONT);
        saveItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
        add(saveItem);
        saveItem.setActionCommand(prefix + "Save");
        saveItem.addActionListener(a);

        JMenuItem openItem = new JMenuItem("Open...");
        openItem.getAccessibleContext().setAccessibleDescription("Opens a new file.");
        openItem.setFont(Constants.FontConstants.FFONT);
        openItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
        add(openItem);
        openItem.setActionCommand(prefix + "Open");
        openItem.addActionListener(a);

        JMenuItem newItem = new JMenuItem("New...");
        newItem.getAccessibleContext().setAccessibleDescription("Creates a new file.");
        newItem.setFont(Constants.FontConstants.FFONT);
        newItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
        add(newItem);
        newItem.setActionCommand(prefix + "New");
        newItem.addActionListener(a);

        JMenuItem dirItem = new JMenuItem("Directory...");
        dirItem.getAccessibleContext().setAccessibleDescription("Changes the search directory.");
        dirItem.setFont(Constants.FontConstants.FFONT);
        dirItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
        add(dirItem);
        dirItem.setActionCommand(prefix + "Dir");
        dirItem.addActionListener(a);

        JMenuItem autosaveItem = new JRadioButtonMenuItem("Autosave");
        autosaveItem.setSelected(true);
        autosaveItem.getAccessibleContext().setAccessibleDescription("Toggles autosave.");
        autosaveItem.setFont(Constants.FontConstants.FFONT);
        autosaveItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
        add(autosaveItem);
        autosaveItem.setActionCommand(prefix + "Auto");
        autosaveItem.addActionListener(a);

        JMenuItem testItem = new JMenuItem("Debug...");
        testItem.getAccessibleContext().setAccessibleDescription("Does some debugging things.");
        testItem.setFont(Constants.FontConstants.FFONT);
        testItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
        add(testItem);
        testItem.setActionCommand(prefix + "Dbug");
        testItem.addActionListener(a);
    }
}
