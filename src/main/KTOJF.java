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
 * Also handles all button interactions because it already has a handle on everything.
 * 
 * @author Christian Azinn
 * @version 0.1.0
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
    
    // TEMP
    private String defaultFilename = "todo";

    public KTOJF() {

        // Create JFrame and title it
        super("KTO ver 0.0.5 pre-alpha");

        // Set to exit program on window close, absolute positioning layout, and icon
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setIconImage(new ImageIcon("kto.png").getImage());

        // Instantiate MainMenuBar and set as menu bar
        mmBar = new MainMenuBar(this);
        setJMenuBar(mmBar);

        // Instantiate CSVManager
        csv = new CSVManager();
        try { csv.open(defaultFilename); } catch(Exception e) {} // TEMP
        branch = csv.getTopLevelBranch();
        activeSubbranch = "";

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
        ptPane = new PrimaryTextPane("This is a test PrimaryTextPane!");
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

    
    // TODO - correctly present backslash escaped commas, fix interpretation of backslash escaped brackets (sidebarPane?)
    // TODO - stop replacing \, with \\, and so on (read: don't multiple escape)
    // TODO - GET THIS SHIT TO WORK
    /**
     * Saves the current {@link PrimaryTextPane} to the active {@code branch}.
     */
    public void save() {
        try {
            String tbText = ptPane.getText().replaceAll("\n", "\\\\n")  
                    .replaceAll("(?<!\\\\)(,)", "\\\\,").replaceAll("(?<!\\\\)(\\[)", "\\\\[").replaceAll("(?<!\\\\)(\\])", "\\\\]");
            String finStr = activeSubbranch + "[" + tbText + "]";
            branch.set(sbPane.getButtonText().indexOf(activeSubbranch), finStr);
        } catch(Exception e) {}
        finally { csv.save(); }
    }

    // TODO - deal with backslash escaping, top-level textboxes, non-top-level keys/redirects (update getBranch call to newBranch!!!), creation of new keys

    /**
     * Manages all button {@link ActionEvent}s.
     * @param a an {@link ActionEvent} sent by some sort of Button
     */
    public void actionPerformed(ActionEvent a) {
        String command = a.getActionCommand();
        char tag = command.charAt(0);
        switch(tag) {
            case '@': // redirect downward
                locBar.directoryDown(command.substring(1));
                branch = csv.getBranch(command.substring(1));
                sbPane = new SidebarPane(branch, this, false);
                ssPane.updateView(sbPane);
                break;
            case '#': // menu option
                switch(command.substring(1,5)) { // get menu
                    case "FILE":
                        switch(command.substring(5)) {
                            case "Save": // save case
                                save();
                                break;
                            default:
                                System.out.println("Menu option error (2)!");
                                break;
                        }
                        break;
                    default:
                        System.out.println("Menu option error (1)!");
                        break;
                }
                break;
            case '“': // redirect upward (back option)
                String redirect = locBar.directoryUp();
                if(redirect.equals("@")) branch = csv.getTopLevelBranch();
                else branch = csv.getBranch(redirect);
                sbPane = new SidebarPane(branch, this, redirect.equals("@"));
                ssPane.updateView(sbPane);
                break;
            case '”': // new option
                break;
            case '*': // stf enter
                break;
            default:  // display text
                activeSubbranch = command;
                String fullText = branch.get(sbPane.getButtonText().indexOf(command));

                // I can't believe all this code is needed just to backslash escape...
                int bracketIdx;
                ptPane.setText("");

                while(true) {
                    // Search for an open bracket
                    bracketIdx = fullText.indexOf("[");
                    // if there is no bracket for some reason, or it was not backslash escaped, stop looping
                    if(bracketIdx == -1 || fullText.charAt(bracketIdx - 1) != '\\') break;
                    // Continue looping if the bracket was backslash escaped
                    fullText = fullText.substring(bracketIdx + 1);
                }
                if(bracketIdx == 0) break; // no bracket - empty
                fullText = fullText.substring(bracketIdx + 1);

                int relativePos = 0;
                String tempText = fullText;
                while(true) {
                    // Search for a close bracket
                    bracketIdx = tempText.indexOf("]");
                    relativePos += bracketIdx;
                    // if there is no bracket for some reason, or it was not backslash escaped, stop looping
                    if(bracketIdx == -1 || bracketIdx == 0 || tempText.charAt(bracketIdx - 1) != '\\') break;
                    // Continue looping if the bracket was backslash escaped
                    tempText = tempText.substring(bracketIdx + 1);
                    relativePos++;
                }
                bracketIdx = relativePos;
                if(bracketIdx == -1 || bracketIdx == 0) break; // no bracket - corrupted
                fullText = fullText.substring(0, bracketIdx);

                // regex my behated
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
