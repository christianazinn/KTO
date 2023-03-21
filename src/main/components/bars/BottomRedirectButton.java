package components.bars;

import util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * {@code BottomRedirectButton} is a class to create a {@link JButton} at the bottom of the screen to redirect off in-text redirects in a {@link PrimaryTextPane}.
 * 
 * @author Christian Azinn
 * @version 0.1
 * @since 0.2.2
 */
public class BottomRedirectButton extends JButton {
    public BottomRedirectButton(ActionListener a) {
        super();
        
        // Set font
        setFont(Constants.FontConstants.SFONT);

        // Initialize stuff
        update("");

        // Set preferred size using GraphicsConstants
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SBWIDTH, Constants.GraphicsConstants.BARHEIGHT));

        // Add the indicated ActionListener
        addActionListener(a);
        // Set visibility
        setVisible(true);
    }


    public void update(String text) {
        // Check whether it's a redirect
        if(text.length() == 0 || text.charAt(0) != '@') {
            setActionCommand("");

            // Set custom text to nothing
            setText("");
            // Hover tooltip indicates this will activate when over a redirect
            setToolTipText("This will activate when your text cursor is over a redirect.");
        } else {
            setActionCommand(text);

            // Set custom text to a directory indication
            setText("> " + text.substring(1));
            // Hover tooltip indicates the redirect
            setToolTipText("Go to " + text.substring(1) + ".");
        }
    }
}
