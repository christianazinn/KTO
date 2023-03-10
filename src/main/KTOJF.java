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
 * @version 0.1.4
 * @since 0.0.1
 */
public class KTOJF extends JFrame implements ActionListener, DocumentListener, MouseListener {

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
    // Instance variables for other things
    private ArrayList<String> branch;
    private String activeSubbranch;
    private boolean isTopLevel, autosaveOn, canListen;
    
    // TEMP
    private String defaultFilename, defaultDirectory, lookAndFeel;
    private Component mostRecent;




    public KTOJF() {
        // Create JFrame and title it
        super("KTO ver 0.1.4 alpha");

        // Set to exit program on window close, absolute positioning layout, and icon
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setIconImage(new ImageIcon("img/kto.png").getImage());

        // Read settings
        readSettings();
        // Initialize all components
        initComponents();
        // Set component bounds
        setBounds();
        // Add components
        addComponents();
        
        // Set all other instance variables
        activeSubbranch = "";
        isTopLevel = true;
        canListen = true;

        // For whatever reason this shows up double size on my screen
        // Set size
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SCREENWIDTH, Constants.GraphicsConstants.SCREENHEIGHT));

        // Ensure all elements are shown on screen, center window on screen, and set visible
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    /**
     * Reads settings from a .ini file.
     */
    private void readSettings() {
        try {
            // Initialize input
            BufferedReader r = new BufferedReader(new FileReader("util/KTOJF.ini"));
            r.readLine();

            // Get default directory
            String defDir = r.readLine();
            defaultDirectory = defDir.substring(defDir.indexOf("=") + 1);

            // Get default filename
            String defFin = r.readLine();
            defaultFilename = defFin.substring(defFin.indexOf("=") + 1);

            // Set Look and Feel
            String lnf = r.readLine();
            lookAndFeel = lnf.substring(lnf.indexOf("=") + 1);
            if(lookAndFeel.equals("Metal")) try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch(Exception e) {} // fail silently
            else try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e) {} // fail silently

            String as = r.readLine();
            as = as.substring(as.indexOf("=") + 1);
            if(as.equals("true")) autosaveOn = true;
            else autosaveOn = false;

            r.close();
        } catch(Exception e) {
            // Exit
            System.out.println("Fatal error encountered when reading settings!");
            System.exit(-1);
        }
    }

    
    /**
     * Initializes all components.
     */
    private void initComponents() {
        mmBar = new MainMenuBar(this, autosaveOn);
        csv = new CSVManager(defaultDirectory);
        try { csv.open(defaultFilename); } catch(Exception e) {} // see below
        branch = csv.getTopLevelBranch(); // this has to go here so the sbPane constructor doesnt scream at me
        locBar = new LocationBar(defaultFilename);
        sbPane = new SidebarPane(branch, this, this, true);
        ssPane = new SidebarScrollPane(sbPane);
        ptPane = new PrimaryTextPane("", this);
        psPane = new PrimaryScrollPane(ptPane);
    }


    /**
     * Adds all necessary components.
     */
    private void addComponents() {
        setJMenuBar(mmBar);
        add(locBar);
        add(ssPane);
        add(psPane);
    }


    /**
     * Sets bounds of all necessary components.
     */
    private void setBounds() {
        // Get insets
        insets = getInsets();

        // Create LocationBar, position properly, and add
        Dimension size = locBar.getPreferredSize();
        locBar.setBounds(insets.left, insets.top, size.width, size.height);
        
        // Test code for SidebarScrollPane, TBR
        size = ssPane.getPreferredSize();
        ssPane.setBounds(insets.left, insets.top + Constants.GraphicsConstants.LOCBARHEIGHT, size.width, size.height);
        
        // Test code for PrimaryScrollPane, TBR
        size = psPane.getPreferredSize();
        psPane.setBounds(insets.left + Constants.GraphicsConstants.SBWIDTH, 
                    insets.top + Constants.GraphicsConstants.PSPVOFFSET, size.width, size.height);
    }




    // ActionListener things
    
    /**
     * Saves the current {@link PrimaryTextPane} to the active {@code branch}.
     */
    private void save() {
        try {
            // process regexes in one line because why not
            String tbText = ptPane.getDocument().getText(0, ptPane.getDocument().getLength()).replaceAll("\n", "\\\\n").replaceAll(",", "\\\\,").replaceAll("\\[", "\\\\[")
                                            .replaceAll("\\]", "\\\\]");
            // format and set to arraylist
            String finStr = activeSubbranch + "[" + tbText + "]";
            branch.set(sbPane.getButtonText().indexOf(activeSubbranch), finStr);
        } catch(Exception e) {} // fail silently
        finally { csv.save(); } // actually save regardless of whether the formatting was successful
    }
    

    /**
     * Handles the display of save alerts.
     * @return whether to break or not
     */
    private boolean saveAlert() {
        // check whether a dialog needs to be shown at all
        if(!csv.getSaved()) {
            int result = JOptionPane.showConfirmDialog(this, "Changes have not been saved! Continue?", "Confirm Continue", JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.NO_OPTION) return true; // returns true if the user doesn't want to continue
            return false; // returns false if the user wants to continue
        }
        return false; // returns false if it didn't need to be called
    }


    /**
     * Handles the display of error messages.
     * @param message the error message
     */
    private void error(String message) {
        JOptionPane.showMessageDialog(this, "Invalid " + message + "!", "Error", JOptionPane.ERROR_MESSAGE);
    }


    /**
     * Handles the display of input prompts.
     * @param message the input message
     * @param title the window title
     * @return the user input
     */
    private String input(String message, String title) {
        return JOptionPane.showInputDialog(this, "Please input the " + message + ":", title, JOptionPane.QUESTION_MESSAGE);
    }


    /**
     * Handles down redirects.
     * @param command the {@link ActionEvent} command
     */
    private void redirDown(String command) {
        // reset textbox
        ptPane.setText("");

        // update location bar
        locBar.directoryDown(command.substring(1));
        // update active branch
        branch = csv.getBranch(command.substring(1));

        // update top-level indicator
        isTopLevel = false;
        // create new SidebarPane and update SidebarScrollPane
        sbPane = new SidebarPane(branch, this, this, isTopLevel);
        ssPane.setViewportView(sbPane);

        // mark as saved
        csv.setSaved(true);
    }


    /**
     * Handles up redirects.
     */
    private void redirUp() {
        // reset textbox
        ptPane.setText("");

        // update location bar
        String redirect = locBar.directoryUp();
        // check whether the destination is top-level and update active branch accordingly
        isTopLevel = redirect.equals("@");
        if(isTopLevel) branch = csv.getTopLevelBranch();
        else branch = csv.getBranch(redirect);

        // create new SidebarPane and update SidebarScrollPane
        sbPane = new SidebarPane(branch, this, this, isTopLevel);
        ssPane.setViewportView(sbPane);

        // mark as saved
        csv.setSaved(true);
    }


    /**
     * Opens a new file and sets it up.
     */
    private void open() {
        // gets the filename
        String file = input("filename", "Open File");
        // immediately exits if user pressed exit on the dialog
        if(file == null) return;
        // display error message if the file could not be opened
        if(!csv.open(file)) {
            error("filename");
            return;
        }

        // reset textbox, location bar, branch location, and top-level indicator
        ptPane.setText("");
        locBar.reset(file);
        branch = csv.getTopLevelBranch();
        isTopLevel = true;
        
        // create new SidebarPane and update SidebarScrollPane
        sbPane = new SidebarPane(branch, this, this, isTopLevel);
        ssPane.setViewportView(sbPane);
    }


    /**
     * Creates a new file and sets it up.
     */
    private void fnew() {
        // gets the filename
        String file = input("filename", "Open File");
        // immediately exits if user pressed exit on the dialog
        if(file == null) return;
        // display error message if the file could not be created
        if(!csv.create(file)) {
            error("filename");
            return;
        }

        // opens the new file
        csv.open(file);

        // reset textbox, location bar, branch location, and top-level indicator
        ptPane.setText("");
        locBar.reset(file);
        branch = csv.getTopLevelBranch();
        isTopLevel = true;
        
        // create new SidebarPane and update SidebarScrollPane
        sbPane = new SidebarPane(branch, this, this, isTopLevel);
        ssPane.setViewportView(sbPane);
    }


    /**
     * Changes the active working directory.
     */
    private void dir() {
        // gets the directory name
        String newDir = input("directory name", "Change Directory");
        // immediately exits if user pressed exit on the dialog
        if(newDir == null) return;
        // display error message if the directory does not exist
        if(!(new File(newDir).exists())) {
            error("directory");
            return;
        }
        
        // replace backslashes with forward slashes for Java String formatting
        newDir = newDir.replaceAll("\\\\", "/");
        // ensure the path ends in a forward slash
        if(newDir.charAt(newDir.length() - 1) != '/') newDir += '/';
        // set the working directory in the CSVManager
        csv.setDirectory(newDir);
    }


    /**
     * Saves current options to default.
     */
    private void def() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("util/KTOJF.ini"));
            pw.println("defaultDirectory=" + csv.getDirectory());
            pw.println("defaultFilename=" + csv.getF());
            pw.println("lookAndFeel=" + lookAndFeel);
            pw.println("autosaveOn=" + autosaveOn);
            pw.close();
        } catch(Exception e) {
            error("options");
        }
    }


    /**
     * Creates a new branch in the current file.
     */
    private void onew() {
        // gets the branch name
        System.out.println(branch);
        String newLine = input("key", "New Branch");
        // immediately exits if the user pressed exit on the dialog
        if(newLine == null) return;
        // display error if the branch name is invalid
        if(newLine.equals("") || newLine.equals("\n")) {
            error("key");
            return;
        } else if(sbPane.getButtonText().contains(newLine)) {
            JOptionPane.showMessageDialog(this, "Duplicate key!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // formatting technicality
        newLine += "[]";
        // create a new branch in the CSVManager and add the line
        if(isTopLevel) csv.newBranch(newLine); // TODOMT - fix this to work with top-level non-redirects
        branch.add(newLine);
        
        // create new SidebarPane and update SidebarScrollPane
        sbPane = new SidebarPane(branch, this, this, isTopLevel);
        ssPane.setViewportView(sbPane);

        // save accordingly
        if(autosaveOn) save();
        else csv.setSaved(false);
    }


    /**
     * Displays the text of a selected subbranch.
     * @param command the {@link ActionEvent} command
     */
    private void display(String command) {
        // set active subbranch
        activeSubbranch = command;
        // get the text to be displayed
        String fullText = branch.get(sbPane.getButtonText().indexOf(command));
        // reset textbox
        ptPane.setText("");

        // search for the open bracket indicating start of text area and truncate before it
        int bracketIdx = CSVManager.findNotBackslashed(fullText, "[");
        if(bracketIdx == -1) return; // no bracket - empty (this isn't a display error it's just improperly formatted)
        fullText = fullText.substring(bracketIdx + 1);

        // search for the close bracket indicating end of text area and truncate after it
        bracketIdx = CSVManager.findNotBackslashed(fullText, "]");
        if(bracketIdx == -1) { // no bracket - corrupted
            error("display input");
            return; 
        }
        fullText = fullText.substring(0, bracketIdx);

        // regex my behated
        // change any backslashes not escaped or newlines, and any newlines
        fullText = fullText.replaceAll("(?<!\\\\)(\\\\)(?!n)", "").replaceAll("(?<!\\\\)(\\\\n)","\n");

        // set the textbox to the formatted text
        ptPane.setText(fullText);
    }


    /**
     * Renames a subbranch or redirect.
     */
    private void rename() {
        // TODOST - IMPLEMENT
    }


    /**
     * Copies a subbranch.
     */
    private void copy() {
        // TODOST - IMPLEMENT
    }


    /**
     * Debug method.
     */
    private void debug() {
        // do whatever
    }


    // TODOST - deal with top-level textboxes, non-top-level keys/redirects (update getBranch call to newBranch!!!)
    // TODOMT - manual save options (eg directory, default file) to file
    // TODOMT - resize elements with window
    // TODOLT - BOTTOM BAR CONTAINING OTHER INFO

    /**
     * Manages all button {@link ActionEvent}s.
     * @param a an {@link ActionEvent} sent by some sort of Button
     */
    public void actionPerformed(ActionEvent a) {
        String command = a.getActionCommand();
        char tag = command.charAt(0);
        canListen = false;
        switch(tag) {
            case '@': // redirect downward
                if(saveAlert()) break;
                redirDown(command);
                break;
            case '#': // menu option
                switch(command.substring(1,5)) { // get menu
                    case "FILE":
                        switch(command.substring(5)) {
                            case "Save": // save case
                                save();
                                break;
                            case "Open": // open case
                                if(saveAlert()) break;
                                open();
                                break;
                            case "New":  // create case
                                if(saveAlert()) break;
                                fnew();
                                break;
                            case "Dir": // directory change case
                                if(saveAlert()) break;
                                dir();
                                break;
                            case "Def": // default save case 
                                def();
                                break; // TODOMT - pop up a menu to individually change
                            case "Auto": // autosave toggle
                                autosaveOn = !autosaveOn;
                                break;
                            case "Dbug": // debug
                                debug();
                                break;
                            default:
                                error("menu option");
                                break;
                        }
                        break;
                    default:
                        error("menu option");
                        break;
                }
                break;
            case '“': // redirect upward (back option)
                if(saveAlert()) break;
                redirUp();
                break;
            case '”': // new option
                if(saveAlert()) break;
                onew();
                break;
            case '*': // right click
                switch(command.substring(1)) {
                    case "Copy":
                        copy();
                        break;
                    case "Rnme":
                        rename();
                        break;
                    default:
                        error("right click option");
                }
                break;
            default:  // display text
                if(saveAlert()) break;
                display(command);
                break;
        }
        canListen = true;
    }




    // DocumentListener things

    /**
     * Handles DocumentEvents.
     * @param e a {@link DocumentEvent} sent by a {@link PrimaryTextPane}
     */
    public void insertUpdate(DocumentEvent e) { if(canListen) {
        if(autosaveOn) save();
        else csv.setSaved(false);
    }}


    /**
     * Handles DocumentEvents.
     * @param e a {@link DocumentEvent} sent by a {@link PrimaryTextPane}
     */
    public void removeUpdate(DocumentEvent e) { if(canListen) {
        if(autosaveOn) save();
        else csv.setSaved(false);
    }}


    /**
     * Handles DocumentEvents.
     * @param e a {@link DocumentEvent} sent by a {@link PrimaryTextPane}
     */
    public void changedUpdate(DocumentEvent e) { if(canListen) {
        if(autosaveOn) save();
        else csv.setSaved(false);
    }}



    // mouseListener things
    

    /**
     * 
     */
    private void rightClickSidebar(MouseEvent e) {
        SidebarRightClickMenu sbrcm = new SidebarRightClickMenu(this);
        sbrcm.show(e.getComponent(), e.getX(), e.getY());
        mostRecent = e.getComponent();
    } // TODOST - IMPLEMENT


    /**
     * Handles MouseEvents.
     * @param e a {@link MouseEvent} sent by a right click event - {@link SidebarButton} most likely
     */
    public void mousePressed(MouseEvent e) {
        // nothing - implemented because interface goes brr
    }


    /**
     * Handles MouseEvents.
     * @param e a {@link MouseEvent} sent by a right click event - {@link SidebarButton} most likely
     */
    public void mouseReleased(MouseEvent e) {
        if(e.isPopupTrigger()) {
            rightClickSidebar(e);
        }
    }


    /**
     * Handles MouseEvents.
     * @param e a {@link MouseEvent} sent by a right click event - {@link SidebarButton} most likely
     */
    public void mouseClicked(MouseEvent e) {
        // nothing - implemented because interface goes brr
    }


    /**
     * Handles MouseEvents.
     * @param e a {@link MouseEvent} sent by a right click event - {@link SidebarButton} most likely
     */
    public void mouseEntered(MouseEvent e) {
        // nothing - implemented because interface goes brr
    }


    /**
     * Handles MouseEvents.
     * @param e a {@link MouseEvent} sent by a right click event - {@link SidebarButton} most likely
     */
    public void mouseExited(MouseEvent e) {
        // nothing - implemented because interface goes brr
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
