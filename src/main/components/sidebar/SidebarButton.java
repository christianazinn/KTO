package components.sidebar;

import util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * {@code SidebarButton} is a class to create a {@link JButton} to be used in the navigation sidebar of the application.
 * 
 * @author Christian Azinn
 * @version 0.6
 * @since 0.0.3
 */
public class SidebarButton extends JButton {
    public SidebarButton(String text, ActionListener a, MouseListener m) {
        // Default constructor - text will be set later
        super();
        // Set font
        setFont(Constants.FontConstants.SFONT);
        
        if(update(text)) addMouseListener(m);

        // Add the indicated ActionListener
        addActionListener(a);
        // Set visibility
        setVisible(true);
    }

    public boolean update(String text) {
        // Set action command first to capture @ operator
        setActionCommand(text);

        if(text.charAt(text.length() - 1) == '|') { // Handle | (Disabled) operator
            setEnabled(false);
            return update(text.substring(0, text.length() - 1));
        }

        // Handle @ operator
        char e = text.charAt(0);
        switch(e) {
            case '@': // Handle @ (Redirect) operator
                // Set custom text to a directory indication
                setText("> " + text.substring(1));
                // Hover tooltip indicates the redirect
                setToolTipText("Go to " + text.substring(1) + ".");
                // Set border
                setBackground(Color.BLACK);
                return true;
            case '“': // Handle “ (Back) operator
                // Set custom text
                setText("< Back...");
                // Hover tooltip indicates going back
                setToolTipText("Return to the previous directory.");
                // Set border
                setBackground(Color.YELLOW);
                return false;
            case '”': // Handle ” (New) operator
                // Set custom text
                setText("+ New...");
                // Hover tooltip indicates creating new
                setToolTipText("Create a new tab or redirect.");
                // Set border
                setBackground(Color.GREEN);
                return false;
            case '$': // Handle $ (Back to Top) operator
                // Set custom text
                setText("<< Top...");
                // Hover tooltip indicates going back
                setToolTipText("Return to the root directory.");
                // Set border
                setBackground(Color.ORANGE);
                return false;
            default: // Handle not-@-operator
                // Set custom text
                setText(text);
                // Hover tooltip indicates info display
                setToolTipText("Display information of " + text + ".");
                // Set border
                setBackground(Color.LIGHT_GRAY);
                return true;
        }
    }
}