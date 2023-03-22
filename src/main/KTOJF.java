import components.bars.*;
import components.menu.*;
import components.primary.*;
import components.sidebar.*;
import listeners.*;
import util.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;

/**
 * {@code KTOJF} is the main file of the KTO JFrame-based application. 
 * 
 * @author Christian Azinn
 * @version 0.2.4
 * @since 0.0.1
 */
public class KTOJF extends JFrame implements ActionListener {

    // Component container + listeners
    private ComponentContainer cc;
    private ComponentL cl;
    private CaretL al;
    private DocumentL dl;
    private MouseL ml;
    // Instance variables for other things
    private ArrayList<String> branch;
    private String activeSubbranch;
    private boolean isTopLevel, autosaveOn, originalAutosave;
    
    // TEMP
    private String defaultFilename, defaultDirectory, lookAndFeel;
    private static final String version = "0.2.4";
    private static final String releaseVer = "beta";




    public KTOJF() {
        // Create JFrame and title it
        super("KTO " + version);

        // Set to exit program on window close, absolute positioning layout, and icon
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setIconImage(new ImageIcon(ClassLoader.getSystemResource("img/kto.png")).getImage());

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

        // For whatever reason this shows up double size on my screen
        // Set size
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SCREENWIDTH, Constants.GraphicsConstants.SCREENHEIGHT));
        // ...but this isn't double size?
        setMinimumSize(new Dimension(Constants.GraphicsConstants.SCREENWIDTH, Constants.GraphicsConstants.SCREENHEIGHT));

        // Set component listener to handle screen things
        addComponentListener(cl);

        // Set autosave to refresh automatically
        EventQueue.invokeLater(new Runnable() { public void run() {
            Timer timer = new Timer(Constants.GeneralConstants.AUTOSAVEREFRESH, new ActionListener() { 
                public void actionPerformed(ActionEvent e) { if(dl.getSave()) save(); }
            });
            timer.start();
        }});

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
            BufferedReader r = new BufferedReader(new FileReader("KTOJF.ini"));
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
        CSVManager csv = new CSVManager(defaultDirectory);
        if(!csv.open(defaultFilename)) {
            while(!csv.open(defaultFilename)) defaultFilename = JOptionPane.showInputDialog(this, "Default file does not exist! Please enter a valid filename.", "Error", JOptionPane.ERROR_MESSAGE); 
            def(false);
        }
        branch = csv.getTopLevelBranch(); // this has to go here so the sbPane constructor doesnt scream at me
        cc = new ComponentContainer(csv);
        cl = new ComponentL(cc);
        dl = new DocumentL(cc, autosaveOn);
        al = new CaretL(cc);
        ml = new MouseL(this);
        cc.mmBar = new MainMenuBar(this, autosaveOn);
        cc.locBar = new LocationBar(defaultFilename, this);
        cc.brBut = new BottomRedirectButton(this);
        cc.botBar = new BottomBar(cc.brBut, "Running KTO ver " + version + " " + releaseVer + ", 03/22/2023 build | Figure out what else to put here!", this);
        cc.sbPane = new SidebarPane(branch, this, ml, true);
        cc.ssPane = new SidebarScrollPane(cc.sbPane);
        cc.ptPane = new PrimaryTextPane("", dl, al);
        cc.pnPane = new PrimaryNullPane();
        cc.psPane = new PrimaryScrollPane(cc.pnPane);
    }


    /**
     * Adds all necessary components.
     */
    private void addComponents() {
        // Add everything
        setJMenuBar(cc.mmBar);
        add(cc.locBar);
        add(cc.botBar); // bottom redirect button is added within the bottom bar
        add(cc.ssPane);
        add(cc.psPane);
    }


    /**
     * Sets bounds of all necessary components.
     */
    private void setBounds() {
        // Position location bar
        Dimension size = cc.locBar.getPreferredSize();
        cc.locBar.setBounds(0, 0, size.width, size.height);

        // Position bottom bar
        size = cc.botBar.getPreferredSize();
        cc.botBar.setBounds(0, Constants.GraphicsConstants.SCREENHEIGHT - 76, size.width, size.height);
        
        // Position sidebar scroll pane
        size = cc.ssPane.getPreferredSize();
        cc.ssPane.setBounds(0, Constants.GraphicsConstants.BARHEIGHT + 2, size.width, size.height - Constants.GraphicsConstants.BARHEIGHT - 2);
        
        // Position primary scroll pane
        size = cc.psPane.getPreferredSize();
        cc.psPane.setBounds(Constants.GraphicsConstants.SBWIDTH, Constants.GraphicsConstants.PSPVOFFSET, size.width, size.height - Constants.GraphicsConstants.BARHEIGHT);
    }




    // TODOST - MAKE @LINKS IN TEXT CREATE BUTTONS ON BOTTOM BAR TO REDIRECT
    // TODOMT - FAVORITING
    // TODOLT - FAILSAFES FOR BAD INFO (improperly formatted files, improper settings, etc)

    /**
     * Manages all button {@link ActionEvent}s.
     * @param a an {@link ActionEvent} sent by some sort of Button
     */
    public void actionPerformed(ActionEvent a) {
        String command = a.getActionCommand();
        if(command.equals("")) return; // empty button
        char tag = command.charAt(0);
        dl.setListen(false);
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
                                dl.setAutosave(autosaveOn);
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
                    case "Dacv":
                        activate(false);
                        break;
                    case "Actv":
                        activate(true);
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
        dl.setListen(true);
    }


    // ActionListener things


    /**
     * Saves the current {@link PrimaryTextPane} to the active {@code branch}.
     */
    private void save() {
        try {
            // process regexes in one line because why not
            String tbText = cc.ptPane.getDocument().getText(0, cc.ptPane.getDocument().getLength()).replaceAll("\n", "\\\\n")
                                        .replaceAll(",", "\\\\,").replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]");
            // format and set to arraylist
            String finStr = activeSubbranch + "[" + tbText + "]";
            branch.set(cc.sbPane.getButtonText().indexOf(activeSubbranch), finStr);
        } catch(Exception e) {} // fail silently
        finally { cc.csv.save(); } // actually save regardless of whether the formatting was successful
    }
    

    /**
     * Handles the display of save alerts.
     * @return whether to break or not
     */
    private boolean saveAlert() {
        // check whether a dialog needs to be shown at all
        if(!cc.csv.getSaved()) {
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
        cc.ptPane.setText("");

        // update location bar
        cc.locBar.directoryDown(command.substring(1));
        // update active branch
        branch = cc.csv.getBranch(command.substring(1));

        // update top-level indicator
        isTopLevel = false;
        // create new SidebarPane and update SidebarScrollPane
        cc.sbPane = new SidebarPane(branch, this, ml, isTopLevel);
        cc.ssPane.setViewportView(cc.sbPane);

        // there's nothing to write to so blank out textbox
        cc.psPane.setViewportView(cc.pnPane);

        // mark as saved
        cc.csv.setSaved(true);
    }


    /**
     * Handles up redirects.
     */
    private void redirUp() {
        // reset textbox
        cc.ptPane.setText("");

        // update location bar
        String redirect = cc.locBar.directoryUp();
        // check whether the destination is top-level and update active branch accordingly
        isTopLevel = redirect.equals("@");
        if(isTopLevel) branch = cc.csv.getTopLevelBranch();
        else branch = cc.csv.getBranch(redirect);

        // create new SidebarPane and update SidebarScrollPane
        cc.sbPane = new SidebarPane(branch, this, ml, isTopLevel);
        cc.ssPane.setViewportView(cc.sbPane);
        
        // there's nothing to write to so blank out textbox
        cc.psPane.setViewportView(cc.pnPane);

        // mark as saved
        cc.csv.setSaved(true);
    }

    
    /**
     * Handles all-the-way-up redirects.
     */
    private void redirTop() {
        // reset textbox
        cc.ptPane.setText("");

        // update location bar
        while(true) if(cc.locBar.directoryUp().equals("@")) break;
        branch = cc.csv.getTopLevelBranch();
        
        // create new SidebarPane and update SidebarScrollPane
        cc.sbPane = new SidebarPane(branch, this, ml, isTopLevel);
        cc.ssPane.setViewportView(cc.sbPane);

        // there's nothing to write to so blank out textbox
        cc.psPane.setViewportView(cc.pnPane);
    
        // mark as saved
        cc.csv.setSaved(true);
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
        if(!cc.csv.open(file)) {
            error("filename");
            return;
        }

        // reset textbox, location bar, branch location, and top-level indicator
        cc.ptPane.setText("");
        cc.locBar.reset(file);
        branch = cc.csv.getTopLevelBranch();
        isTopLevel = true;
        
        // create new SidebarPane and update SidebarScrollPane
        cc.sbPane = new SidebarPane(branch, this, ml, isTopLevel);
        cc.ssPane.setViewportView(cc.sbPane);
        
        // there's nothing to write to so blank out textbox
        cc.psPane.setViewportView(cc.pnPane);
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
        if(!cc.csv.create(file)) {
            error("filename");
            return;
        }

        // opens the new file
        cc.csv.open(file);

        // reset textbox, location bar, branch location, and top-level indicator
        cc.ptPane.setText("");
        cc.locBar.reset(file);
        branch = cc.csv.getTopLevelBranch();
        isTopLevel = true;
        
        // create new SidebarPane and update SidebarScrollPane
        cc.sbPane = new SidebarPane(branch, this, ml, isTopLevel);
        cc.ssPane.setViewportView(cc.sbPane);
        
        // there's nothing to write to so blank out textbox
        cc.psPane.setViewportView(cc.pnPane);
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
        // set the working directory in the csvManager
        cc.csv.setDirectory(newDir);
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

            if(sv.equals("Directory") || sv.equals("All")) pw.println("defaultDirectory=" + cc.csv.getDirectory());
            else pw.println("defaultDirectory=" + defaultDirectory);

            if(sv.equals("Filename") || sv.equals("All")) pw.println("defaultFilename=" + cc.csv.getF());
            else pw.println("defaultFilename=" + defaultFilename);

            pw.println("lookAndFeel=" + lookAndFeel); // there's no option to change L&F yet

            if(sv.equals("Autosave") || sv.equals("All")) pw.println("autosaveOn=" + autosaveOn);
            else pw.println("autosaveOn=" + originalAutosave);

            pw.close();
        } catch(Exception e) { error("options"); }
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
        } else if(cc.sbPane.getButtonText().contains(newLine)) {
            JOptionPane.showMessageDialog(this, "Duplicate key!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // create a new branch in the CSVManager and add the line
        if(isTopLevel && newLine.charAt(0) == '@') cc.csv.newBranch(newLine.substring(1));
        // formatting technicality
        branch.add(newLine + "[]");
        
        // sort arraylists
        Collections.sort(cc.sbPane.getButtonText());
        Collections.sort(branch);
        
        // create new SidebarPane and update SidebarScrollPane
        if(isTopLevel) cc.sbPane = new SidebarPane(cc.csv.getTopLevelBranch(), this, ml, true);
        else cc.sbPane = new SidebarPane(branch, this, ml, false);
        cc.ssPane.setViewportView(cc.sbPane);

        // save accordingly
        if(autosaveOn) save();
        else cc.csv.setSaved(false);
    }


    /**
     * Displays the text of a selected subbranch.
     * @param command the {@link ActionEvent} command
     */
    private void display(String command) {
        // set active subbranch
        activeSubbranch = command;
        // get the text to be displayed
        String fullText = branch.get(cc.sbPane.getButtonText().indexOf(command));
        // reset textbox
        cc.ptPane.setText("");

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
        cc.psPane.setViewportView(cc.ptPane);
        cc.ptPane.setText(fullText);
    }


    /**
     * Renames a subbranch or redirect.
     */
    private void rename() {
        // get user input
        String newName = input("new name", "Rename");
        if(newName == null) return;
        // check for duplicate
        else if(cc.sbPane.getButtonText().contains(newName)) {
            JOptionPane.showMessageDialog(this, "Duplicate key!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        // ensure key validity
        } else if(newName.charAt(newName.length() - 1) == '\\') {
            error("key");
            return;
        }

        // really convoluted way of getting the source button using polymorphic typecasting
        SidebarButton target = ml.getMostRecent();
        String branchTarget = target.getText().replaceAll("> ", "@");

        // changing type from subbranch to redirect is not supported
        if((newName.charAt(0) == '@' && target.getText().charAt(0) != '>') || (newName.charAt(0) != '@' && target.getText().charAt(0) == '>')) {
            error("branch type");
            return;
        } 

        // update button name
        target.update(newName);

        // update button text in SidebarPane ArrayList
        int idx = cc.sbPane.getButtonText().indexOf(branchTarget);
        cc.sbPane.getButtonText().set(idx, newName);

        // change target key
        cc.csv.changeKey(branchTarget.substring(1), newName.substring(1));
        
        // change all references to target key
        branchTarget = branchTarget.replaceAll("\\\\", "\\\\\\\\");
        newName = newName.replaceAll("\\\\", "\\\\\\\\");
        cc.csv.changeAllRefs(branchTarget, newName);

        // update key in active branch
        String tbr = branch.get(idx);
        int fnb = CSVManager.findNotBackslashed(tbr, "[");
        if(fnb != -1) branch.set(idx, newName + tbr.substring(fnb));
        else branch.set(idx, newName);

        // sort arraylist
        Collections.sort(branch);

        // create new SidebarPane and update SidebarScrollPane
        cc.sbPane = new SidebarPane(branch, this, ml, isTopLevel);
        cc.ssPane.setViewportView(cc.sbPane);

        // save
        if(autosaveOn) save();
        else cc.csv.setSaved(false);
    }


    /**
     * Copies a subbranch.
     */
    private void copy() {
        // get user input
        String newName = input("new name", "Copy");
        if(newName == null) return;
        // check for duplicate
        else if(cc.sbPane.getButtonText().contains(newName)) {
            JOptionPane.showMessageDialog(this, "Duplicate key!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        // ensure key validity
        } else if(newName.charAt(newName.length() - 1) == '\\') {
            error("key");
            return;
        }

        // really convoluted way of getting the source button using polymorphic typecasting
        SidebarButton target = ml.getMostRecent();
        String branchTarget = target.getText().replaceAll("> ", "@");

        // do the actual copying
        String copy = branch.get(cc.sbPane.getButtonText().indexOf(branchTarget));
        branch.add(newName + copy.substring(CSVManager.findNotBackslashed(copy, "[")));

        // sort arraylist
        Collections.sort(branch);

        // create new SidebarPane and update SidebarScrollPane
        cc.sbPane = new SidebarPane(branch, this, ml, isTopLevel);
        cc.ssPane.setViewportView(cc.sbPane);

        // save
        if(autosaveOn) save();
        else cc.csv.setSaved(false);
    }


    /**
     * Deletes a subbranch.
     */
    private void delete() {
        // confirm deletion
        if(JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this branch?", "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) return;

        // really convoluted way of getting the source button using polymorphic typecasting
        SidebarButton target = ml.getMostRecent();
        String branchTarget = target.getText().replaceAll("> ", "@");

        // check if the deleted one was active, if so blank out
        if(activeSubbranch.equals(target.getText())) cc.psPane.setViewportView(cc.pnPane);
        if(!target.isEnabled()) branchTarget = "|" + branchTarget;

        // do the actual removal
        branch.remove(cc.sbPane.getButtonText().indexOf(branchTarget));
        
        // create new SidebarPane and update SidebarScrollPane
        cc.sbPane = new SidebarPane(branch, this, ml, isTopLevel);
        cc.ssPane.setViewportView(cc.sbPane);

        // save
        if(autosaveOn) save();
        else cc.csv.setSaved(false);
    }


    /**
     * Activates or deactivates a button.
     * @param targetStatus the enabled status after the method is called
     */
    private void activate(boolean targetStatus) {
        // really convoluted way of getting the source button using polymorphic typecasting
        SidebarButton target = ml.getMostRecent();
        String branchTarget = target.getText().replaceAll("> ", "@");

        // check if the deactivated one was active, if so blank out
        if(activeSubbranch.equals(target.getText())) cc.psPane.setViewportView(cc.pnPane);

        // change things accordingly to handle | operator
        String newString, info;
        if(targetStatus) {
            newString = branchTarget;
            branchTarget = branchTarget + "|";
        }
        else newString = branchTarget + "|";

        // save info
        info = branch.get(cc.sbPane.getButtonText().indexOf(branchTarget)).substring(branchTarget.length());

        // update branch
        branch.set(cc.sbPane.getButtonText().indexOf(branchTarget), newString + info);

        // create new SidebarPane and update SidebarScrollPane
        cc.sbPane = new SidebarPane(branch, this, ml, isTopLevel);
        cc.ssPane.setViewportView(cc.sbPane);

        target.setEnabled(targetStatus);

        // save
        if(autosaveOn) save();
        else cc.csv.setSaved(false);
    } 


    /**
     * Debug method.
     */
    private void debug() {
        System.out.println(cc.botBar.getBounds());
        System.out.println(cc.locBar.getBounds());
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
