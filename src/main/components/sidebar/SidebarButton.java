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
        super();
        setActionCommand(text);
        setFont(Constants.FontConstants.SFONT);
        if(text.charAt(0) == '@') {
            text = "> " + text.substring(1);
            setToolTipText("Go to " + text.substring(2) + ".");
        } else {
            setText(text);
            setToolTipText("Display information of " + text + ".");
        }
        addActionListener(a);
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SBWIDTH, Constants.GraphicsConstants.SBHEIGHT));
        setVisible(true);
    }
}