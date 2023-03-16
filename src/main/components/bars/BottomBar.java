package components.bars;

import util.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * {@code BottomBar} is a class to create a simple bar at the bottom of the screen to give extra info to the user.
 * 
 * @author Christian Azinn
 * @version 0.1
 * @since 0.1.7
 */

public class BottomBar extends JPanel {
    
    // Instance variables
    private String text;
    private JLabel label;
    private Component parent;

    public BottomBar(String initialText, Component parent) {
        // Absolute positioning layout, layout managers are annoying
        super(null);

        // Set preferred size using GraphicsConstants
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SCREENWIDTH, Constants.GraphicsConstants.BARHEIGHT));

        // Set instance variables
        text = initialText;
        this.parent = parent;

        // Create label, set font, and set position/size
        label = new JLabel();
        label.setFont(Constants.FontConstants.SFONT);
        label.setText(text);
        setLabelSize();
        add(label);

        // Set border and visibility
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setVisible(true);
    } // TODOST - figure out what else to put here lmao
    

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
