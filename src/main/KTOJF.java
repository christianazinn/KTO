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
 * @version 0.1.7
 * @since 0.0.1
 */
public class KTOJF extends JFrame implements ActionListener, DocumentListener, MouseListener, ComponentListener {

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
    private boolean isTopLevel, autosaveOn, originalAutosave, canListen;
    
    // TEMP
    private String defaultFilename, defaultDirectory, lookAndFeel;
    private Component mostRecent;




    public KTOJF() {
        // Create JFrame and title it
        super("KTO ver 0.1.7 alpha");

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
        // ...but this isn't double size?
        setMinimumSize(new Dimension(Constants.GraphicsConstants.SCREENWIDTH, Constants.GraphicsConstants.SCREENHEIGHT));

        // Set component listener to handle screen things
        addComponentListener(this);

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

            // Get autosave
            String as = r.readLine();
            as = as.substring(as.indexOf("=") + 1);
            if(as.equals("true")) autosaveOn = true;
            else autosaveOn = false;
            originalAutosave = autosaveOn;

            r.close();
        } catch(Exception e) {
            // Exit
            JOptionPane.showMessageDialog(this, "Fatal error encountered when reading settings!", "Error", JOptionPane.ERROR_MESSAGE); 
            System.exit(-1);
        }
    }

    
    /**
     * Initializes all components.
     */
    private void initComponents() {
        mmBar = new MainMenuBar(this, autosaveOn);
        csv = new CSVManager(defaultDirectory);
        if(!csv.open(defaultFilename)) {
            while(!csv.open(defaultFilename)) defaultFilename = JOptionPane.showInputDialog(this, "Default file does not exist! Please enter a valid filename.", "Error", JOptionPane.ERROR_MESSAGE); 
            def(false);
        }
        branch = csv.getTopLevelBranch(); // this has to go here so the sbPane constructor doesnt scream at me
        locBar = new LocationBar(defaultFilename, this);
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
        insets = new Insets(0,0,0,0);

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
     * Handles all-the-way-up redirects.
     */
    private void redirTop() {
        // reset textbox
        ptPane.setText("");

        // update location bar
        while(true) if(locBar.directoryUp().equals("@")) break;
        branch = csv.getTopLevelBranch();
        
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
    private void def(boolean ask) {
        try {
            String sv;
            if(ask) {
                Object[] options = {"Directory", "Filename", "L&F", "Autosave", "All"};
                sv = (String) JOptionPane.showInputDialog(this, "Save default:", "Save Default Settings", JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if(sv == null) return;
            } else sv = "";

            PrintWriter pw = new PrintWriter(new FileWriter("util/KTOJF.ini"));
            pw.println("[Options]");

            if(sv.equals("Directory") || sv.equals("All")) pw.println("defaultDirectory=" + csv.getDirectory());
            else pw.println("defaultDirectory=" + defaultDirectory);

            if(sv.equals("Filename") || sv.equals("All")) pw.println("defaultFilename=" + csv.getF());
            else pw.println("defaultFilename=" + defaultFilename);

            pw.println("lookAndFeel=" + lookAndFeel); // there's no option to change L&F yet

            if(sv.equals("Autosave") || sv.equals("All")) pw.println("autosaveOn=" + autosaveOn);
            else pw.println("autosaveOn=" + originalAutosave);

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
        String newLine = input("key", "New Branch");
        // immediately exits if the user pressed exit on the dialog
        if(newLine == null) return;
        // display error if the branch name is invalid
        if(newLine.equals("") || newLine.equals("\n") || newLine.charAt(newLine.length() - 1) == '\\') {
            error("key");
            return;
        } else if(sbPane.getButtonText().contains(newLine)) {
            JOptionPane.showMessageDialog(this, "Duplicate key!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // create a new branch in the CSVManager and add the line
        if(isTopLevel && newLine.charAt(0) == '@') csv.newBranch(newLine.substring(1));
        // formatting technicality
        branch.add(newLine + "[]");
        
        // sort arraylists
        Collections.sort(sbPane.getButtonText());
        Collections.sort(branch);
        
        // create new SidebarPane and update SidebarScrollPane
        if(isTopLevel) sbPane = new SidebarPane(csv.getTopLevelBranch(), this, this, true);
        else sbPane = new SidebarPane(branch, this, this, false);
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
        // get user input
        String newName = input("new name", "Rename");
        if(newName == null) return;
        // check for duplicate
        else if(sbPane.getButtonText().contains(newName)) {
            JOptionPane.showMessageDialog(this, "Duplicate key!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        // ensure key validity
        } else if(newName.charAt(newName.length() - 1) == '\\') {
            error("key");
            return;
        }

        // really convoluted way of getting the source button using polymorphic typecasting
        SidebarButton target = (SidebarButton) mostRecent;
        String branchTarget = target.getText().replaceAll("> ", "@");

        // changing type from subbranch to redirect is not supported
        if((newName.charAt(0) == '@' && target.getText().charAt(0) != '>') || (newName.charAt(0) != '@' && target.getText().charAt(0) == '>')) {
            error("branch type");
            return;
        } 

        // update button name
        target.update(newName);

        // update button text in SidebarPane ArrayList
        int idx = sbPane.getButtonText().indexOf(branchTarget);
        sbPane.getButtonText().set(idx, newName);

        // change target key
        csv.changeKey(branchTarget.substring(1), newName.substring(1));
        
        // change all references to target key
        branchTarget = branchTarget.replaceAll("\\\\", "\\\\\\\\");
        newName = newName.replaceAll("\\\\", "\\\\\\\\");
        csv.changeAllRefs(branchTarget, newName);

        // update key in active branch
        String tbr = branch.get(idx);
        int fnb = CSVManager.findNotBackslashed(tbr, "[");
        if(fnb != -1) branch.set(idx, newName + tbr.substring(fnb));
        else branch.set(idx, newName);

        // sort arraylist
        Collections.sort(branch);

        // create new SidebarPane and update SidebarScrollPane
        sbPane = new SidebarPane(branch, this, this, isTopLevel);
        ssPane.setViewportView(sbPane);

        // save
        if(autosaveOn) save();
        else csv.setSaved(false);
    }


    /**
     * Copies a subbranch.
     */
    private void copy() {
        // get user input
        String newName = input("new name", "Copy");
        if(newName == null) return;
        // check for duplicate
        else if(sbPane.getButtonText().contains(newName)) {
            JOptionPane.showMessageDialog(this, "Duplicate key!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        // ensure key validity
        } else if(newName.charAt(newName.length() - 1) == '\\') {
            error("key");
            return;
        }

        // really convoluted way of getting the source button using polymorphic typecasting
        SidebarButton target = (SidebarButton) mostRecent;
        String branchTarget = target.getText().replaceAll("> ", "@");

        // do the actual copying
        String copy = branch.get(sbPane.getButtonText().indexOf(branchTarget));
        branch.add(newName + copy.substring(CSVManager.findNotBackslashed(copy, "[")));

        // sort arraylist
        Collections.sort(branch);

        // create new SidebarPane and update SidebarScrollPane
        sbPane = new SidebarPane(branch, this, this, isTopLevel);
        ssPane.setViewportView(sbPane);

        // save
        if(autosaveOn) save();
        else csv.setSaved(false);
    }


    /**
     * Deletes a subbranch.
     */
    private void delete() {
        // confirm deletion
        if(JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this branch?", "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) return;

        // really convoluted way of getting the source button using polymorphic typecasting
        SidebarButton target = (SidebarButton) mostRecent;
        String branchTarget = target.getText().replaceAll("> ", "@");

        // do the actual removal
        branch.remove(sbPane.getButtonText().indexOf(branchTarget));
        
        // create new SidebarPane and update SidebarScrollPane
        sbPane = new SidebarPane(branch, this, this, isTopLevel);
        ssPane.setViewportView(sbPane);

        // save
        if(autosaveOn) save();
        else csv.setSaved(false);
    }


    /**
     * Debug method.
     */
    private void debug() {
        // do whatever
    }


    // TODOLT - BOTTOM BAR CONTAINING OTHER INFO
    // TODOLT - FAVORITING
    // TODOLT - FAILSAFES FOR BAD INFO (improperly formatted files, improper settings, etc)

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
                                def(true);
                                break; 
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
            case '$': // redirect back to top option
                redirTop();
                break;
            case '*': // right click
                switch(command.substring(1)) {
                    case "Copy":
                        copy();
                        break;
                    case "Rnme":
                        rename();
                        break;
                    case "Delt":
                        delete();
                        break;
                    default:
                        error("right click option");
                        break;
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



    // MouseListener things
    /**
     * Handles MouseEvents.
     * @param e a {@link MouseEvent} sent by a right click event - {@link SidebarButton} most likely
     */
    public void mouseReleased(MouseEvent e) { if(e.isPopupTrigger()) {
        SidebarButton source = (SidebarButton) e.getSource();
        SidebarRightClickMenu sbrcm = new SidebarRightClickMenu(this, source.getText().charAt(0) == '>');
        sbrcm.show(e.getComponent(), e.getX(), e.getY());
        mostRecent = e.getComponent();
    }}
    public void mousePressed(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}




    // ComponentListener things
    /**
     * Handles ComponentEvents.
     * @param e a {@link ComponentEvent} sent by a resize call
     */
    public void componentResized(ComponentEvent e) {
        Dimension newSize = e.getComponent().getBounds().getSize();
        locBar.setSize(new Dimension((int) newSize.getWidth(), Constants.GraphicsConstants.LOCBARHEIGHT));
        locBar.setLabelSize();
        ssPane.setSize(new Dimension(Constants.GraphicsConstants.SBWIDTH, (int) newSize.getHeight() - (Constants.GraphicsConstants.LOCBARHEIGHT + Constants.GraphicsConstants.MENUBARHEIGHT) * 2 + 7));
        psPane.setSize(new Dimension((int) newSize.getWidth() - Constants.GraphicsConstants.SBWIDTH - 12, (int) newSize.getHeight() - Constants.GraphicsConstants.PSPVOFFSET * 4));
        ptPane.setSize(new Dimension((int) newSize.getWidth() - Constants.GraphicsConstants.SBWIDTH - 12, (int) newSize.getHeight() - Constants.GraphicsConstants.PSPVOFFSET * 4));
    }
    public void componentHidden(ComponentEvent e) {}
    public void componentMoved(ComponentEvent e) {}
    public void componentShown(ComponentEvent e) {}




    /**
     * The method that's invoked when KTOJF is run. 
     * Does nothing but instantiate a KTOJF object and hand control to it.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        new KTOJF();
    }
}
