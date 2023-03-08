package components.sidebar;

import util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * {@code SidebarTextField} is a class to create a {@link JTextField} to be used in the navigation sidebar of the application.
 * 
 * @author Christian Azinn
 * @version 0.1
 * @since 0.0.6
 */
public class SidebarTextField extends JTextField {

    // there should only ever be one stf onscreen at a given time
    // decide what clicking off does
    public SidebarTextField(SidebarButton parent, ActionListener a) {
        super("");
        setFont(Constants.FontConstants.SFONT);
        setPreferredSize(new Dimension((int) (Constants.GraphicsConstants.STWIDTH), (int) (Constants.GraphicsConstants.STHEIGHT)));
        setActionCommand("*");
        addActionListener(a);
    }
}
