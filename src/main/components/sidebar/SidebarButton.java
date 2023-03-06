package components.sidebar;

import util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * {@code SidebarButton} is a class to create a {@code JButton} to be used in the navigation sidebar of the application.
 * 
 * @author Christian Azinn
 * @version 0.1
 * @since 0.0.3
 */
public class SidebarButton extends JButton {
    public SidebarButton(String text, ActionListener a) {

        // Default constructor - text will be set later
        super();
        // Set action command first to capture @ operator
        setActionCommand(text);
        // Set font
        setFont(Constants.FontConstants.SFONT);

        // Handle @ operator
        if(text.charAt(0) == '@') {
            // Set custom text to a directory indication
            text = "> " + text.substring(1);

            // Hover tooltip indicates the redirect
            setToolTipText("Go to " + text.substring(2) + ".");
        } else { // Handle not-@-operator
            // Set custom text just to... well, text
            setText(text);

            // Hover tooltip indicates info display
            setToolTipText("Display information of " + text + ".");
        }

        // Add the indicated ActionListener
        addActionListener(a);
        // Set size (using GraphicsConstants) and visibility
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SBWIDTH, Constants.GraphicsConstants.SBHEIGHT));
        setVisible(true);
    }
}