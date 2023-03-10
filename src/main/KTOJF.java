import components.location.*;
import components.menu.*;
import components.primary.*;
import components.sidebar.*;
import util.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;

/**
 * {@code KTOJF} is the main file of the KTO JFrame-based application. 
 * 
 * @author Christian Azinn
 * @version 0.1.2
 * @since 0.0.1
 */
public class KTOJF extends JFrame implements ActionListener, DocumentListener {

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
    // Instance variable for the active branch
    private ArrayList<String> branch;
    private String activeSubbranch;
    private boolean isTopLevel;
    private boolean autosaveOn;
    private boolean canListen;
    
    // TEMP
    private String defaultFilename = "todo.csv";

    public KTOJF() {

        // Create JFrame and title it
        super("KTO ver 0.1.2 alpha");

        // Set UI style
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e) {} // fail silently

        // Set to exit program on window close, absolute positioning layout, and icon
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setIconImage(new ImageIcon("kto.png").getImage());

        // Instantiate MainMenuBar and set as menu bar
        mmBar = new MainMenuBar(this);
        setJMenuBar(mmBar);

        // Instantiate CSVManager
        csv = new CSVManager("C:/Users/chris/OneDrive/Documents/kto/src/csvs/");
        try { csv.open(defaultFilename); } catch(Exception e) {} // TEMP
        branch = csv.getTopLevelBranch();
        activeSubbranch = "";
        isTopLevel = true;
        autosaveOn = true;
        canListen = true;

        // Get insets
        insets = getInsets();

        // Create LocationBar, position properly, and add
        locBar = new LocationBar(defaultFilename);
        Dimension size = locBar.getPreferredSize();
        locBar.setBounds(insets.left, insets.top, size.width, size.height);
        add(locBar);

        // Test code for SidebarScrollPane, TBR
        sbPane = new SidebarPane(branch, this, true);
        ssPane = new SidebarScrollPane(sbPane);
        size = ssPane.getPreferredSize();
        ssPane.setBounds(insets.left, insets.top + Constants.GraphicsConstants.LOCBARHEIGHT, size.width, size.height);
        add(ssPane);

        // Test code for PrimaryScrollPane, TBR
        ptPane = new PrimaryTextPane("", this);
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
     * Saves the current {@link PrimaryTextPane} to the active {@code branch}.
     */
    public void save() {
        try {
            String tbText = ptPane.getDocument().getText(0, ptPane.getDocument().getLength()).replaceAll("\n", "\\\\n").replaceAll(",", "\\\\,").replaceAll("\\[", "\\\\[")
                                            .replaceAll("\\]", "\\\\]");
            String finStr = activeSubbranch + "[" + tbText + "]";
            branch.set(sbPane.getButtonText().indexOf(activeSubbranch), finStr);
        } catch(Exception e) {} // fail silently
        finally { csv.save(); }
    }

    // TODOST - deal with top-level textboxes, non-top-level keys/redirects (update getBranch call to newBranch!!!)
    // TODOST - copying/renaming keys (right click menus!)
    // TODOST - separate actionPerformed into sub-methods?
    // TODOMT - save options (eg directory, default file) to file
    // TODOLT - keybinds?
    // TODOST - comments lmao
    // TODOLT - BOTTOM BAR CONTAINING OTHER INFO

    /**
     * Manages all button {@link ActionEvent}s.
     * @param a an {@link ActionEvent} sent by some sort of Button
     */
    public void actionPerformed(ActionEvent a) {
        String command = a.getActionCommand();
        char tag = command.charAt(0);
        switch(tag) {
            case '@': // redirect downward
                if(!csv.getSaved()) {
                    int result = JOptionPane.showConfirmDialog(this, "Changes have not been saved! Continue?", "Confirm Continue", JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.NO_OPTION) return;
                }
                canListen = false;
                ptPane.setText("");
                locBar.directoryDown(command.substring(1));
                branch = csv.getBranch(command.substring(1));
                sbPane = new SidebarPane(branch, this, false);
                ssPane.updateView(sbPane);
                isTopLevel = false;
                canListen = true;
                csv.setSaved(true);
                break;
            case '#': // menu option
                switch(command.substring(1,5)) { // get menu
                    case "FILE":
                        switch(command.substring(5)) {
                            case "Save": // save case
                                save();
                                break;
                            case "Open": // open case
                                if(!csv.getSaved()) {
                                    int result = JOptionPane.showConfirmDialog(this, "Changes have not been saved! Continue?", "Confirm Continue", JOptionPane.YES_NO_OPTION);
                                    if(result == JOptionPane.NO_OPTION) return;
                                }
                                String file = JOptionPane.showInputDialog(this, "Please input the filename:", "Open File", JOptionPane.QUESTION_MESSAGE);
                                if(file == null) break;
                                if(!csv.open(file)) JOptionPane.showMessageDialog(this, "Invalid filename!", "Error", JOptionPane.ERROR_MESSAGE);
                                else {
                                    canListen = false;
                                    ptPane.setText("");
                                    locBar.reset(file);
                                    branch = csv.getTopLevelBranch();
                                    sbPane = new SidebarPane(branch, this, true);
                                    ssPane.updateView(sbPane);
                                    isTopLevel = true;
                                    canListen = true;
                                }
                                break;
                            case "New":  // create case
                                if(!csv.getSaved()) {
                                    int result = JOptionPane.showConfirmDialog(this, "Changes have not been saved! Continue?", "Confirm Continue", JOptionPane.YES_NO_OPTION);
                                    if(result == JOptionPane.NO_OPTION) return;
                                }
                                String newFile = JOptionPane.showInputDialog(this, "Please input the filename:", "New File", JOptionPane.QUESTION_MESSAGE);
                                if(newFile == null) break;
                                if(!csv.create(newFile)) JOptionPane.showMessageDialog(this, "Invalid filename!", "Error", JOptionPane.ERROR_MESSAGE);
                                else {
                                    canListen = false;
                                    csv.open(newFile);
                                    ptPane.setText("");
                                    locBar.reset(newFile);
                                    branch = csv.getTopLevelBranch();
                                    sbPane = new SidebarPane(branch, this, true);
                                    ssPane.updateView(sbPane);
                                    isTopLevel = true;
                                    canListen = true;
                                }
                                break;
                            case "Dir": // directory change case
                                if(!csv.getSaved()) {
                                    int result = JOptionPane.showConfirmDialog(this, "Changes have not been saved! Continue?", "Confirm Continue", JOptionPane.YES_NO_OPTION);
                                    if(result == JOptionPane.NO_OPTION) return;
                                }
                                String newDir = JOptionPane.showInputDialog(this, "Please input the directory name:", "Change Directory", JOptionPane.QUESTION_MESSAGE);
                                if(newDir == null) break;
                                File dirCheck = new File(newDir);
                                if(!dirCheck.exists()) JOptionPane.showMessageDialog(this, "Invalid directory!", "Error", JOptionPane.ERROR_MESSAGE);
                                else {
                                    newDir = newDir.replaceAll("\\\\", "/");
                                    if(newDir.charAt(newDir.length() - 1) != '/') newDir += '/';
                                    csv.setDirectory(newDir);
                                }
                                break;
                            case "Auto": // autosave toggle
                                autosaveOn = !autosaveOn;
                                break;
                            case "Dbug": // debug (test code goes here)
                                System.out.println(csv.getDirectory());
                                break;
                            default:
                                JOptionPane.showMessageDialog(this, "An invalid menu option was received!", "Error", JOptionPane.ERROR_MESSAGE);
                                break;
                        }
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "An invalid menu option was received!", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                }
                break;
            case '“': // redirect upward (back option)
                if(!csv.getSaved()) {
                    int result = JOptionPane.showConfirmDialog(this, "Changes have not been saved! Continue?", "Confirm Continue", JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.NO_OPTION) return;
                }
                canListen = false;
                ptPane.setText("");
                String redirect = locBar.directoryUp();
                if(redirect.equals("@")) {
                    branch = csv.getTopLevelBranch();
                    isTopLevel = true;
                } else {
                    branch = csv.getBranch(redirect);
                    isTopLevel = false;
                }
                canListen = true;
                sbPane = new SidebarPane(branch, this, isTopLevel);
                ssPane.updateView(sbPane);
                csv.setSaved(true);
                break;
            case '”': // new option
                if(!csv.getSaved()) {
                    int result = JOptionPane.showConfirmDialog(this, "Changes have not been saved! Continue?", "Confirm Continue", JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.NO_OPTION) return;
                }
                String newLine = JOptionPane.showInputDialog(this, "Please input the key:", "New Branch", JOptionPane.QUESTION_MESSAGE);
                if(newLine == null) break;
                if(newLine.equals("") || newLine.equals("\n")) {
                    JOptionPane.showMessageDialog(this, "Invalid key!", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                newLine += "[]";
                if(isTopLevel) csv.newBranch(newLine); // TODOMT - fix this to work with top-level non-redirects
                branch.add(newLine);
                sbPane = new SidebarPane(branch, this, true);
                ssPane.updateView(sbPane);
                csv.setSaved(false);
                break;
            default:  // display text
                if(!csv.getSaved()) {
                    int result = JOptionPane.showConfirmDialog(this, "Changes have not been saved! Continue?", "Confirm Continue", JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.NO_OPTION) return;
                }
                canListen = false;
                activeSubbranch = command;
                String fullText = branch.get(sbPane.getButtonText().indexOf(command));

                // I can't believe all this code is needed just to backslash escape...
                ptPane.setText("");

                int bracketIdx = CSVManager.findNotBackslashed(fullText, "[");
                if(bracketIdx == -1) break; // no bracket - empty (this isn't a display error it's just improperly formatted)
                fullText = fullText.substring(bracketIdx + 1);

                bracketIdx = CSVManager.findNotBackslashed(fullText, "]");
                if(bracketIdx == -1) { // no bracket - corrupted
                    JOptionPane.showMessageDialog(this, "An invalid display input was received!", "Error", JOptionPane.ERROR_MESSAGE);
                    break; 
                }
                fullText = fullText.substring(0, bracketIdx);

                // regex my behated
                // any backslashes not escaped or newlines
                fullText = fullText.replaceAll("(?<!\\\\)(\\\\)(?!n)", "");

                // java is really dumb about this and apparently newline has to be a string literal?! oh my god
                int last = 0;
                for(int i = 0; i < fullText.length() - 1; i++) if(fullText.substring(i, i+2).equals("\\n")) {
                    ptPane.append(fullText.substring(last, i));
                    ptPane.append("\n");
                    last = i+2;
                }

                ptPane.append(fullText.substring(last));
                canListen = true;
                break;
        }
    }


    /**
     * Handles DocumentEvents.
     * @param e a {@link DocumentEvent} sent by a {@link PrimaryTextPane}
     */
    public void insertUpdate(DocumentEvent e) {
        if(canListen) {
            if(autosaveOn) save();
            else csv.setSaved(false);
        }
    }
    /**
     * Handles DocumentEvents.
     * @param e a {@link DocumentEvent} sent by a {@link PrimaryTextPane}
     */
    public void removeUpdate(DocumentEvent e) {
        if(canListen) {
            if(autosaveOn) save();
            else csv.setSaved(false);
        }
    }
    /**
     * Handles DocumentEvents.
     * @param e a {@link DocumentEvent} sent by a {@link PrimaryTextPane}
     */
    public void changedUpdate(DocumentEvent e) {
        if(canListen) {
            if(autosaveOn) save();
            else csv.setSaved(false);
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
