package components.location;

import util.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * {@code LocationBar} is a class to create a simple bar at the top of the screen to indicate where the user is.
 * 
 * @author Christian Azinn
 * @version 0.4
 * @since 0.0.2
 */
public class LocationBar extends JPanel {

    
    // Instance variables for directory text, number of directories down, and the label
    private String text;
    private int directoriesDown;
    private JLabel label;
    private Component parent;

    // Instance variables for insets and size
    private Insets insets;
    private Dimension size;


    public LocationBar(String filename, Component parent) {
        // Absolute positioning layout, layout managers are annoying
        super(null);

        // Get panel insets
        insets = getInsets();
        this.parent = parent;
        // Set preferred size using GraphicsConstants
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SCREENWIDTH, Constants.GraphicsConstants.LOCBARHEIGHT));

        // Create label, set font, and set position/size (read text lmao)
        label = new JLabel();
        label.setFont(Constants.FontConstants.SBOLD);
        add(label);
        
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
        // Add directory name to text and set text
        text += " > " + directory;
        label.setText(text);
        setLabelSize();

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

        // Search for the last '>'
        int i = text.length() - 1;
        for(; i >= 0; i--) if(text.charAt(i) == '>') break;
        // Truncate everything after that, as well as the preceding space
        text = text.substring(0, i - 1);
        i -= 2;
        // Search for the redirect address
        for(; i >= 0; i--) if(text.charAt(i) == '>') break;
        String redirect = text.substring(i + 2, text.length());
        
        // Keep track of how many directories in it is
        directoriesDown--;

        // Update label text
        label.setText(text);
        setLabelSize();

        // Check if it's at directory 0
        if(directoriesDown == 0) return "@";
        return redirect;
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
        size = label.getPreferredSize();
        int w = (int) size.getWidth();
        int h = (int) size.getHeight();
        if(w > parent.getBounds().getWidth() - 10) w = (int) parent.getBounds().getWidth() - 10;
        label.setBounds(Constants.GraphicsConstants.HPADDING + insets.left, insets.top, w, h);
    }
}
