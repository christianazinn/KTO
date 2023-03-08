package components.sidebar;

import util.Constants;

import javax.swing.*;
import java.awt.event.*;

/**
 * {@code SidebarButton} is a class to create a {@link JButton} to be used in the navigation sidebar of the application.
 * 
 * @author Christian Azinn
 * @version 0.3
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
            setText("> " + text.substring(1));
            // Hover tooltip indicates the redirect
            setToolTipText("Go to " + text.substring(1) + ".");
        } else if(text.charAt(0) == '“') { // Handle “ (Back) operator
            // Set custom text
            setText("< Back...");
            // Hover tooltip indicates going back
            setToolTipText("Return to the previous directory.");
        } else if(text.charAt(0) == '”') { // Handle ” (New) operator
            // Set custom text
            setText("+ New...");
            // Hover tooltip indicates creating new
            setToolTipText("Create a new tab or redirect.");
        } else { // Handle not-@-operator
            // Set custom text
            setText(text);
            // Hover tooltip indicates info display
            setToolTipText("Display information of " + text + ".");
        }

        // Add the indicated ActionListener
        addActionListener(a);
        // Set visibility
        setVisible(true);
    }
}