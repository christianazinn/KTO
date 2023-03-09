import components.location.*;
import components.menu.*;
import components.primary.*;
import components.sidebar.*;
import util.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

/**
 * {@code KTOJF} is the main file of the KTO JFrame-based application. 
 * 
 * @author Christian Azinn
 * @version 0.1.1a
 * @since 0.0.1
 */
public class KTOJF extends JFrame implements ActionListener {

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
    
    // TEMP
    private String defaultFilename = "todo.csv";

    public KTOJF() {

        // Create JFrame and title it
        super("KTO ver 0.1.1a pre-alpha");

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
        ptPane = new PrimaryTextPane("");
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
            String tbText = ptPane.getText().replaceAll("\n", "\\\\n").replaceAll(",", "\\\\,").replaceAll("\\[", "\\\\[")
                                            .replaceAll("\\]", "\\\\]");
            String finStr = activeSubbranch + "[" + tbText + "]";
            branch.set(sbPane.getButtonText().indexOf(activeSubbranch), finStr);
        } catch(Exception e) {} // fail silently
        finally { csv.save(); }
    }

    // TODO - deal with top-level textboxes, non-top-level keys/redirects (update getBranch call to newBranch!!!)
    // TODO - copying/renaming keys (right click menus!)
    // TODO - have the user change directory
    // TODO - separate actionPerformed into sub-methods?
    // TODO - keybinds?
    // TODO - comments lmao
    // TODO - BOTTOM BAR CONTAINING OTHER INFO

    // FIXME stop naming all your input windows "Input" lmao

    /**
     * Manages all button {@link ActionEvent}s.
     * @param a an {@link ActionEvent} sent by some sort of Button
     */
    public void actionPerformed(ActionEvent a) {
        String command = a.getActionCommand();
        char tag = command.charAt(0);
        switch(tag) {
            case '@': // redirect downward
                save();
                ptPane.setText("");
                locBar.directoryDown(command.substring(1));
                branch = csv.getBranch(command.substring(1));
                sbPane = new SidebarPane(branch, this, false);
                ssPane.updateView(sbPane);
                isTopLevel = false;
                break;
            case '#': // menu option
                switch(command.substring(1,5)) { // get menu
                    case "FILE":
                        switch(command.substring(5)) {
                            case "Save": // save case
                                save();
                                break;
                            case "Open": // open case
                                String file = JOptionPane.showInputDialog("Please input the filename:");
                                if(file == null) break;
                                if(!csv.open(file)) JOptionPane.showMessageDialog(this, "Invalid filename!", "Error", JOptionPane.ERROR_MESSAGE);
                                else {
                                    save();
                                    ptPane.setText("");
                                    locBar.reset(file);
                                    branch = csv.getTopLevelBranch();
                                    sbPane = new SidebarPane(branch, this, true);
                                    ssPane.updateView(sbPane);
                                    isTopLevel = true;
                                }
                                break;
                            case "New":  // create case
                                String newFile = JOptionPane.showInputDialog("Please input the filename:");
                                if(newFile == null) break;
                                if(!csv.create(newFile)) JOptionPane.showMessageDialog(this, "Invalid filename!", "Error", JOptionPane.ERROR_MESSAGE);
                                else {
                                    csv.open(newFile);
                                    save();
                                    ptPane.setText("");
                                    locBar.reset(newFile);
                                    branch = csv.getTopLevelBranch();
                                    sbPane = new SidebarPane(branch, this, true);
                                    ssPane.updateView(sbPane);
                                    isTopLevel = true;
                                }
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
                save();
                ptPane.setText("");
                String redirect = locBar.directoryUp();
                if(redirect.equals("@")) {
                    branch = csv.getTopLevelBranch();
                    isTopLevel = true;
                } else {
                    branch = csv.getBranch(redirect);
                    isTopLevel = false;
                }
                sbPane = new SidebarPane(branch, this, isTopLevel);
                ssPane.updateView(sbPane);
                break;
            case '”': // new option
                String newLine = JOptionPane.showInputDialog("Please input the key:");
                if(newLine == null) break;
                if(newLine.equals("") || newLine.equals("\n")) JOptionPane.showMessageDialog(this, "Invalid key!", "Error", JOptionPane.ERROR_MESSAGE);
                else if(isTopLevel) {
                    newLine += "[]";
                    csv.newBranch(newLine);
                    branch.add(newLine);
                    sbPane = new SidebarPane(branch, this, true);
                    ssPane.updateView(sbPane);
                } else {
                    newLine += "[]";
                    branch.add(newLine);
                    sbPane = new SidebarPane(branch, this, false);
                    ssPane.updateView(sbPane);
                }
                break;
            default:  // display text
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
                break;
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
