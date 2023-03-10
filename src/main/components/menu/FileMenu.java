package components.menu;

import util.Constants;

import javax.swing.*;
import java.awt.event.*;

/**
 * {@code FileMenu} is a class to create a {@code JMenu} containing File-related options for the {@link MainMenuBar} to use.
 * 
 * @author Christian Azinn
 * @version 0.5
 * @since 0.0.2
 */
public class FileMenu extends JMenu {
    public FileMenu(ActionListener a, boolean autosaveOn) {
        // Create a JMenu with name "File" and set its AccessibleDescription
        super("File");
        String prefix = "#FILE";
        getAccessibleContext().setAccessibleDescription("Menu for file interactions.");

        // Toggle autosave
        JMenuItem autosaveItem = new JRadioButtonMenuItem("Autosave");
        autosaveItem.setSelected(autosaveOn);
        autosaveItem.getAccessibleContext().setAccessibleDescription("Toggles autosave.");
        autosaveItem.setFont(Constants.FontConstants.FFONT);
        autosaveItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
        add(autosaveItem);
        autosaveItem.setActionCommand(prefix + "Auto");
        autosaveItem.addActionListener(a);

        // Save file
        JMenuItem saveItem = new JMenuItem("Save...", new ImageIcon(new ImageIcon("img/save.png").getImage().getScaledInstance(Constants.GraphicsConstants.ICONSIZE, Constants.GraphicsConstants.ICONSIZE, java.awt.Image.SCALE_SMOOTH)));
        saveItem.getAccessibleContext().setAccessibleDescription("Saves the current file.");
        saveItem.setFont(Constants.FontConstants.FFONT);
        saveItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
        add(saveItem);
        saveItem.setActionCommand(prefix + "Save");
        saveItem.addActionListener(a);

        // Open file
        JMenuItem openItem = new JMenuItem("Open...", new ImageIcon(new ImageIcon("img/folder.png").getImage().getScaledInstance(Constants.GraphicsConstants.ICONSIZE, Constants.GraphicsConstants.ICONSIZE, java.awt.Image.SCALE_SMOOTH)));
        openItem.getAccessibleContext().setAccessibleDescription("Opens a new file.");
        openItem.setFont(Constants.FontConstants.FFONT);
        openItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
        add(openItem);
        openItem.setActionCommand(prefix + "Open");
        openItem.addActionListener(a);

        // New file
        JMenuItem newItem = new JMenuItem("New...", new ImageIcon(new ImageIcon("img/new.png").getImage().getScaledInstance(Constants.GraphicsConstants.ICONSIZE, Constants.GraphicsConstants.ICONSIZE, java.awt.Image.SCALE_SMOOTH)));
        newItem.getAccessibleContext().setAccessibleDescription("Creates a new file.");
        newItem.setFont(Constants.FontConstants.FFONT);
        newItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
        add(newItem);
        newItem.setActionCommand(prefix + "New");
        newItem.addActionListener(a);

        // Change directory
        JMenuItem dirItem = new JMenuItem("Directory...", new ImageIcon(new ImageIcon("img/directory.png").getImage().getScaledInstance(Constants.GraphicsConstants.ICONSIZE, Constants.GraphicsConstants.ICONSIZE, java.awt.Image.SCALE_SMOOTH)));
        dirItem.getAccessibleContext().setAccessibleDescription("Changes the search directory.");
        dirItem.setFont(Constants.FontConstants.FFONT);
        dirItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
        add(dirItem);
        dirItem.setActionCommand(prefix + "Dir");
        dirItem.addActionListener(a);

        // Save defaults
        JMenuItem defaultItem = new JMenuItem("Save Defaults...", new ImageIcon(new ImageIcon("img/empty.png").getImage().getScaledInstance(Constants.GraphicsConstants.ICONSIZE, Constants.GraphicsConstants.ICONSIZE, java.awt.Image.SCALE_SMOOTH)));
        defaultItem.getAccessibleContext().setAccessibleDescription("Saves active settings to default.");
        defaultItem.setFont(Constants.FontConstants.FFONT);
        defaultItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
        add(defaultItem);
        defaultItem.setActionCommand(prefix + "Def");
        defaultItem.addActionListener(a);

        // Debug
        JMenuItem testItem = new JMenuItem("Debug...", new ImageIcon(new ImageIcon("img/empty.png").getImage().getScaledInstance(Constants.GraphicsConstants.ICONSIZE, Constants.GraphicsConstants.ICONSIZE, java.awt.Image.SCALE_SMOOTH)));
        testItem.getAccessibleContext().setAccessibleDescription("Does some debugging things.");
        testItem.setFont(Constants.FontConstants.FFONT);
        testItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
        add(testItem);
        testItem.setActionCommand(prefix + "Dbug");
        testItem.addActionListener(a);
    }
}
