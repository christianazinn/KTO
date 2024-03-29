package components.bars;

import util.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * {@code LocationBar} is a class to create a simple bar at the top of the screen to indicate where the user is.
 * 
 * @author Christian Azinn
 * @version 0.6
 * @since 0.0.2
 */
public class LocationBar extends JPanel {

    
    // Instance variables for directory text, number of directories down, and the label
    private String text;
    private int directoriesDown;
    private JLabel label;
    private Component parent;
    private boolean showsSubbranch;


    public LocationBar(String filename, Component parent) {
        // Absolute positioning layout, layout managers are annoying
        super(null);

        this.parent = parent;
        // Set preferred size using GraphicsConstants
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SCREENWIDTH, Constants.GraphicsConstants.BARHEIGHT));

        // Create label, set font, and set position/size
        label = new JLabel();
        label.setFont(Constants.FontConstants.SBOLD);
        add(label);
        
        showsSubbranch = false;
        
        // Set other instance variables
        reset(filename);

        // Set border and visibility
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setVisible(true);
    }


    /**
     * Move down a directory.
     * @param directory the next directory name
     */
    public void directoryDown(String directory) {
        // Truncate everything after the last ":", as well as the preceding space
        if(showsSubbranch) text = text.substring(0, text.lastIndexOf(":") - 1);

        // Add directory name to text and set text
        text += " > " + directory;
        label.setText(text);
        setLabelSize();
        showsSubbranch = false;

        // Keep track of how many directories in it is
        directoriesDown++;
    }


    /**
     * Move up a directory. 
     * Cannot go higher than directory 0, so returns false if that is tried.
     * @return the directory address to go to, or an empty redirect if trying to move up from top level 
     */
    public String directoryUp() {
        // Check if it's trying to go higher than directory 0
        if(directoriesDown == 0) return "@";

        // Truncate everything after the last ">", as well as the preceding space
        text = text.substring(0, text.lastIndexOf(">") - 1);
        // Search for the redirect address
        String redirect = text.substring(text.lastIndexOf(">") + 2, text.length());
        
        // Keep track of how many directories in it is
        directoriesDown--;
        showsSubbranch = false;

        // Update label text
        label.setText(text);
        setLabelSize();

        // Check if it's at directory 0
        if(directoriesDown == 0) return "@";
        return redirect;
    }


    /**
     * Move down into a subbranch.
     * @param subbranch the subbranch name
     */
    public void updateSubbranch(String subbranch) {
        // Truncate everything after the last ":", as well as the preceding space
        if(showsSubbranch) text = text.substring(0, text.lastIndexOf(":") - 1);
        
        // Add subbranch name to text and set text
        text += " : " + subbranch;
        label.setText(text);
        setLabelSize();
        showsSubbranch = true;
    }


    /**
     * Resets instance variables, particularly when a new file is opened.
     * @param filename the name of the opened file
     */
    public void reset(String filename) {
        text = "> " + filename;
        directoriesDown = 0;
        label.setText(text);
        setLabelSize();
    }

    /**
     * Changes the label size.
     */
    public void setLabelSize() {
        Dimension size = label.getPreferredSize();
        int w = (int) size.getWidth();
        int h = (int) size.getHeight();
        double pw = parent.getBounds().getWidth();
        if(pw > 0 && w > pw - 10) w = (int) parent.getBounds().getWidth() - 10;
        label.setBounds(Constants.GraphicsConstants.HPADDING, 0, w, h);
    }
}
