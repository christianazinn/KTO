package components.sidebar;

import util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * {@code SidebarButton} is a class to create a {@link JButton} to be used in the navigation sidebar of the application.
 * 
 * @author Christian Azinn
 * @version 0.7
 * @since 0.0.3
 */
public class SidebarButton extends JButton {
    public SidebarButton(String text, ActionListener a, MouseListener m) {
        // Default constructor - text will be set later
        super();
        // Set font
        setFont(Constants.FontConstants.SFONT);
        
        if(update(text, false)) addMouseListener(m);

        // Add the indicated ActionListener
        addActionListener(a);
        // Set visibility
        setVisible(true);
    }


    public boolean update(String text, boolean alreadySet) {
        // Set action command first to capture @ operator
        setActionCommand(text);

        if(text.charAt(text.length() - 1) == '|') { // Handle | (Disabled) operator
            setEnabled(false);
            // Set border
            setBackground(Color.RED);
            return update(text.substring(0, text.length() - 1), true);
        }

        // Handle operators
        char e = text.charAt(0);
        switch(e) {
            case '#': // Handle # (Favorite) operator
                // Set border
                setBackground(Color.BLUE);
                return update(text.substring(1), true);
            case '@': // Handle @ (Redirect) operator
                // Set custom text to a directory indication
                setText("> " + text.substring(1));
                // Hover tooltip indicates the redirect
                setToolTipText("Go to " + text.substring(1) + ".");
                // Set border
                if(!alreadySet) setBackground(Color.BLACK);
                return true;
            case '“': // Handle “ (Back) operator
                // Set custom text
                setText("< Back...");
                // Hover tooltip indicates going back
                setToolTipText("Return to the previous directory.");
                // Set border
                if(!alreadySet) setBackground(Color.YELLOW);
                return false;
            case '”': // Handle ” (New) operator
                // Set custom text
                setText("+ New...");
                // Hover tooltip indicates creating new
                setToolTipText("Create a new tab or redirect.");
                // Set border
                if(!alreadySet) setBackground(Color.GREEN);
                return false;
            case '$': // Handle $ (Back to Top) operator
                // Set custom text
                setText("<< Top...");
                // Hover tooltip indicates going back
                setToolTipText("Return to the root directory.");
                // Set border
                if(!alreadySet) setBackground(Color.ORANGE);
                return false;
            default: // Handle not-@-operator
                // Set custom text
                setText(text);
                // Hover tooltip indicates info display
                setToolTipText("Display information of " + text + ".");
                // Set border
                if(!alreadySet) setBackground(Color.LIGHT_GRAY);
                return true;
        }
    }
}