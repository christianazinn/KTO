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
 * @version 0.3.4
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
    private String lookAndFeel;
    private File defaultFile;
    private static final String version = "0.3.4";
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
                public void actionPerformed(ActionEvent e) { if(dl.getSave()) save(false); }
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
            String defFil = r.readLine();
            defaultFile = new File(defFil.substring(defFil.indexOf("=") + 1));

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
        CSVManager csv = new CSVManager(defaultFile);
        cc = new ComponentContainer(csv);
        cc.fc = new JFileChooser(defaultFile.toString().substring(0, defaultFile.toString().lastIndexOf("\\")));
        if(!defaultFile.exists()) {
            while(!csv.open(defaultFile)) {  
                // gets the filename
                int ret = cc.fc.showOpenDialog(this);
                if(ret == JFileChooser.CANCEL_OPTION) System.exit(-1);
                defaultFile = cc.fc.getSelectedFile();
            }
            def(false);
        }
        csv.open(defaultFile);
        cc.fc.setCurrentDirectory(new File(defaultFile.toString().substring(0, defaultFile.toString().lastIndexOf("\\")))); // reset base directory
        branch = csv.getTopLevelBranch(); // this has to go here so the sbPane constructor doesnt scream at me
        cl = new ComponentL(cc);
        dl = new DocumentL(cc, autosaveOn);
        al = new CaretL(cc);
        ml = new MouseL(this);
        cc.mmBar = new MainMenuBar(this, autosaveOn);
        cc.locBar = new LocationBar(defaultFile.toString().substring(defaultFile.toString().lastIndexOf("\\") + 1), this);
        cc.brBut = new BottomRedirectButton(this);
        cc.botBar = new BottomBar(cc.brBut, "Running KTO ver " + version + " " + releaseVer + ", 03/30/2023 build | Figure out what else to put here!", this);
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




    // TODOMT - MIGRATE TO USING JSPLITPANE FOR THE SIDEBAR/PRIMARY WINDOW?
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
                                save(false);
                                break;
                            case "Svas": // save as case
                                save(true);
                                break;
                            case "Open": // open case
                                if(saveAlert()) break;
                                open();
                                break;
                            case "New":  // create case
                                if(saveAlert()) break;
                                fnew();
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
                    case "BRCH":
                        switch(command.substring(5)) {
                            case "Aval":
                                activateAll(true);
                                break;
                            case "Dval":
                                activateAll(false);
                                break;
                            case "Ufal":
                                unfavoriteAll();
                                break;
                            case "Rsal":
                                resetBranch();
                                break;
                        } // TODOST - implement things like activate/deactivate all, unfavorite all, reset entire branch
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
                    case "Fvrt":
                        favorite();
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
    private void save(boolean saveAs) {
        try {
            // process regexes in one line because why not
            String tbText = cc.ptPane.getDocument().getText(0, cc.ptPane.getDocument().getLength()).replaceAll("\n", "\\\\n")
                                        .replaceAll(",", "\\\\,").replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]");
            // format and set to arraylist
            String finStr = activeSubbranch + "[" + tbText + "]";
            branch.set(cc.sbPane.getButtonText().indexOf(activeSubbranch), finStr);
        } catch(Exception e) {} // fail silently
        finally { 
            if(!saveAs) cc.csv.save(cc.csv.getF()); 
            else {
                // gets the filename
                int ret = cc.fc.showSaveDialog(this);
                if(ret == JFileChooser.CANCEL_OPTION) return;
                File file = cc.fc.getSelectedFile();
                // immediately exits if user pressed exit on the dialog
                if(file == null) return;
                // display warning when overwriting
                if(file.exists()) {
                    int result = JOptionPane.showConfirmDialog(this, "You are overwriting a file! Are you sure you want to continue?", "Overwrite Warning", JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.NO_OPTION) return;
                } else cc.csv.create(file);
                cc.csv.save(file);
                cc.fc = new JFileChooser(file.toString().substring(0, defaultFile.toString().lastIndexOf("\\"))); 
                cc.locBar.reset(file.toString().substring(file.toString().lastIndexOf("\\") + 1));
            }
        } // actually save regardless of whether the formatting was successful
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
        isTopLevel = true;
        
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
        int ret = cc.fc.showOpenDialog(this);
        if(ret == JFileChooser.CANCEL_OPTION) return;
        File file = cc.fc.getSelectedFile();
        // immediately exits if user pressed exit on the dialog
        if(file == null) return;
        // display error message if the file could not be opened
        if(!cc.csv.open(file)) {
            error("filename");
            return;
        }
        cc.fc = new JFileChooser(file.toString().substring(0, defaultFile.toString().lastIndexOf("\\")));

        // reset textbox, location bar, branch location, and top-level indicator
        cc.ptPane.setText("");
        cc.locBar.reset(file.toString().substring(file.toString().lastIndexOf("\\") + 1));
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
        int ret = cc.fc.showOpenDialog(this);
        if(ret == JFileChooser.CANCEL_OPTION) return;
        File file = cc.fc.getSelectedFile();
        // immediately exits if user pressed exit on the dialog
        if(file == null) return;
        // display error message if the file could not be created
        if(!cc.csv.create(file)) {
            error("filename");
            return;
        }
        cc.fc = new JFileChooser(file.toString().substring(0, defaultFile.toString().lastIndexOf("\\")));

        // opens the new file
        cc.csv.open(file);

        // reset textbox, location bar, branch location, and top-level indicator
        cc.ptPane.setText("");
        cc.locBar.reset(file.toString().substring(file.toString().lastIndexOf("\\") + 1));
        branch = cc.csv.getTopLevelBranch();
        isTopLevel = true;
        
        // create new SidebarPane and update SidebarScrollPane
        cc.sbPane = new SidebarPane(branch, this, ml, isTopLevel);
        cc.ssPane.setViewportView(cc.sbPane);
        
        // there's nothing to write to so blank out textbox
        cc.psPane.setViewportView(cc.pnPane);
    }


    /**
     * Saves current options to default.
     */
    private void def(boolean ask) {
        try {
            String sv;
            if(ask) {
                Object[] options = {"File", "L&F", "Autosave", "All"};
                sv = (String) JOptionPane.showInputDialog(this, "Save default:", "Save Default Settings", JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if(sv == null) return;
            } else sv = "";

            PrintWriter pw = new PrintWriter(new FileWriter("KTOJF.ini"));
            pw.println("[Options]");

            if(sv.equals("File") || sv.equals("All")) pw.println("defaultFilePath=" + cc.csv.getF());
            else pw.println("defaultFilePath=" + defaultFile);

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
        // check for duplicate
        else if(cc.sbPane.getButtonText().contains(newLine) || cc.sbPane.getButtonText().contains("#" + newLine)) {
            JOptionPane.showMessageDialog(this, "Duplicate key!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        // ensure key validity
        } else if(newLine.equals("") || newLine.equals("\n") || newLine.charAt(newLine.length() - 1) == '\\' || newLine.charAt(newLine.length() - 1) == '|' || newLine.charAt(0) == '#') {
            error("key");
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
        if(autosaveOn) save(false);
        else cc.csv.setSaved(false);
    }


    /**
     * Displays the text of a selected subbranch.
     * @param command the {@link ActionEvent} command
     */
    private void display(String command) {
        // set active subbranch
        activeSubbranch = command;
        cc.locBar.updateSubbranch(activeSubbranch);
        // handle favorites
        if(cc.sbPane.getButtonText().indexOf(activeSubbranch) == -1) activeSubbranch = "#" + activeSubbranch;
        // get the text to be displayed
        String fullText = branch.get(cc.sbPane.getButtonText().indexOf(activeSubbranch));
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
        else if(cc.sbPane.getButtonText().contains(newName) || cc.sbPane.getButtonText().contains("#" + newName)) {
            JOptionPane.showMessageDialog(this, "Duplicate key!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        // ensure key validity
        } else if(newName.equals("") || newName.equals("\n") || newName.charAt(newName.length() - 1) == '\\' || newName.charAt(newName.length() - 1) == '|' || newName.charAt(0) == '#') {
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

        // handle favorites
        boolean wasFavorited = cc.sbPane.getButtonText().indexOf(branchTarget) == -1;
        if(wasFavorited) {
            branchTarget = "#" + branchTarget;
            newName = "#" + newName;
        }
        
        // change target key
        int overwrite = JOptionPane.YES_OPTION;
        if(cc.csv.exists(newName.replaceAll("@", ""))) overwrite = JOptionPane.showConfirmDialog(this, "You are overwriting an existing redirect! Continue?\n(No will still rename the redirect. Cancel will stop entirely.)", "Confirm Overwrite", JOptionPane.YES_NO_CANCEL_OPTION);
        if(overwrite == JOptionPane.CANCEL_OPTION) return;
        else if(overwrite == JOptionPane.YES_OPTION) {
            if(wasFavorited) cc.csv.changeKey(branchTarget.substring(2), newName.substring(2));
            else cc.csv.changeKey(branchTarget.substring(1), newName.substring(1));
        }

        // update button name
        target.update(newName, false);
        
        // change all references to target key
        branchTarget = branchTarget.replaceAll("\\\\", "\\\\\\\\");
        newName = newName.replaceAll("\\\\", "\\\\\\\\");
        if(overwrite == JOptionPane.YES_OPTION) cc.csv.changeAllRefs(branchTarget, newName);

        // update key in active branch
        int idx = cc.sbPane.getButtonText().indexOf(branchTarget);
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
        if(autosaveOn) save(false);
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
        else if(cc.sbPane.getButtonText().contains(newName) || cc.sbPane.getButtonText().contains("#" + newName)) {
            JOptionPane.showMessageDialog(this, "Duplicate key!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        // ensure key validity
        } else if(newName.equals("") || newName.equals("\n") || newName.charAt(newName.length() - 1) == '\\' || newName.charAt(newName.length() - 1) == '|' || newName.charAt(0) == '#') {
            error("key");
            return;
        }

        // really convoluted way of getting the source button using polymorphic typecasting
        SidebarButton target = ml.getMostRecent();
        String branchTarget = target.getText().replaceAll("> ", "@");

        // handle favorites
        if(cc.sbPane.getButtonText().indexOf(branchTarget) == -1) branchTarget = "#" + branchTarget;

        // do the actual copying
        String copy = branch.get(cc.sbPane.getButtonText().indexOf(branchTarget));
        branch.add(newName + copy.substring(CSVManager.findNotBackslashed(copy, "[")));

        // sort arraylist
        Collections.sort(branch);

        // create new SidebarPane and update SidebarScrollPane
        cc.sbPane = new SidebarPane(branch, this, ml, isTopLevel);
        cc.ssPane.setViewportView(cc.sbPane);

        // save
        if(autosaveOn) save(false);
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

        // handle favorites
        if(cc.sbPane.getButtonText().indexOf(branchTarget) == -1) branchTarget = "#" + branchTarget;

        // check if the deleted one was active, if so blank out
        if(activeSubbranch.equals(target.getText())) cc.psPane.setViewportView(cc.pnPane);
        if(!target.isEnabled()) branchTarget = "|" + branchTarget;

        // do the actual removal
        branch.remove(cc.sbPane.getButtonText().indexOf(branchTarget));
        
        // create new SidebarPane and update SidebarScrollPane
        cc.sbPane = new SidebarPane(branch, this, ml, isTopLevel);
        cc.ssPane.setViewportView(cc.sbPane);

        // save
        if(autosaveOn) save(false);
        else cc.csv.setSaved(false);
    }


    /**
     * Activates or deactivates a subbranch or redirect.
     * @param targetStatus the enabled status after the method is called
     */
    private void activate(boolean targetStatus) {
        // really convoluted way of getting the source button using polymorphic typecasting
        SidebarButton target = ml.getMostRecent();
        String newString, info, branchTarget = target.getText().replaceAll("> ", "@");

        // change things accordingly to handle | operator
        if(targetStatus) {
            newString = branchTarget;
            branchTarget = branchTarget + "|";
        }
        else newString = branchTarget + "|";
        
        // handle favorites
        if(cc.sbPane.getButtonText().indexOf(branchTarget) == -1) {
            branchTarget = "#" + branchTarget;
            newString = "#" + newString;
        }

        // check if the deactivated one was active, if so blank out
        if(activeSubbranch.equals(target.getText())) cc.psPane.setViewportView(cc.pnPane);

        // save info
        info = branch.get(cc.sbPane.getButtonText().indexOf(branchTarget)).substring(branchTarget.length());

        // update branch
        branch.set(cc.sbPane.getButtonText().indexOf(branchTarget), newString + info);

        // create new SidebarPane and update SidebarScrollPane
        cc.sbPane = new SidebarPane(branch, this, ml, isTopLevel);
        cc.ssPane.setViewportView(cc.sbPane);

        target.setEnabled(targetStatus);

        // save
        if(autosaveOn) save(false);
        else cc.csv.setSaved(false);
    } 


    /**
     * Favorites a subbranch or redirect.
     */
    private void favorite() {
        // really convoluted way of getting the source button using polymorphic typecasting
        SidebarButton target = ml.getMostRecent();
        String newString, info, branchTarget = target.getText().replaceAll("> ", "@");

        if(cc.sbPane.getButtonText().indexOf(branchTarget) != -1) newString = "#" + branchTarget;
        else {
            newString = branchTarget;
            branchTarget = "#" + branchTarget;
        }

        // save info
        info = branch.get(cc.sbPane.getButtonText().indexOf(branchTarget)).substring(branchTarget.length());
        
        // update branch
        branch.set(cc.sbPane.getButtonText().indexOf(branchTarget), newString + info);
        Collections.sort(branch);

        // create new SidebarPane and update SidebarScrollPane
        cc.sbPane = new SidebarPane(branch, this, ml, isTopLevel);
        cc.ssPane.setViewportView(cc.sbPane);

        // save
        if(autosaveOn) save(false);
        else cc.csv.setSaved(false);
    }


    /**
     * Activates or deactivates all buttons in the current branch.
     */
    private void activateAll(boolean targetStatus) {
        for(int i = 0; i < branch.size(); i++) {
            String str = branch.get(i), info;
            int idx = CSVManager.findNotBackslashed(str, "[");
            info = str.substring(idx);
            str = str.substring(0, idx);
            if(str.charAt(str.length() - 1) == '|' && targetStatus) str = str.substring(0, str.length() - 1);
            else if(str.charAt(str.length() - 1) != '|' && !targetStatus) str += '|';
            branch.set(i, str + info);
        }

        // create new SidebarPane and update SidebarScrollPane
        cc.sbPane = new SidebarPane(branch, this, ml, isTopLevel);
        cc.ssPane.setViewportView(cc.sbPane);

        // save
        if(autosaveOn) save(false);
        else cc.csv.setSaved(false);
    }

    
    /**
     * Unfavorites all favorited buttons in the current branch.
     */
    private void unfavoriteAll() {
        for(int i = 0; i < branch.size(); i++) {
            String str = branch.get(i);
            if(str.length() == 0 || str.charAt(0) != '#') break;
            branch.set(i, str.substring(1));
        }
        Collections.sort(branch);

        // create new SidebarPane and update SidebarScrollPane
        cc.sbPane = new SidebarPane(branch, this, ml, isTopLevel);
        cc.ssPane.setViewportView(cc.sbPane);

        // save
        if(autosaveOn) save(false);
        else cc.csv.setSaved(false);
    }


    /**
     * Deletes all buttons/subbranches in the current branch.
     */
    private void resetBranch() {
        branch.clear();

        // create new SidebarPane and update SidebarScrollPane
        cc.sbPane = new SidebarPane(branch, this, ml, isTopLevel);
        cc.ssPane.setViewportView(cc.sbPane);

        // save
        if(autosaveOn) save(false);
        else cc.csv.setSaved(false);
    }

    /**
     * Debug method.
     */
    private void debug() {
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(this);
        System.out.println(fc.getSelectedFile());
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
