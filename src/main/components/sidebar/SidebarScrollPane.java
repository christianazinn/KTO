package components.sidebar;

import util.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * {@code SidebarScrollPane} is a class to contain a {@link SidebarPane} to be used to navigate the navigation sidebar of the application.
 * 
 * @author Christian Azinn
 * @version 0.4
 * @since 0.0.3
 */
public class SidebarScrollPane extends JScrollPane {
    public SidebarScrollPane(SidebarPane view) {
        // Set SidebarPane to be used as the viewport target, and set scrollbar constants
        super(view, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);

        // Set size and visibility
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SBWIDTH, Constants.GraphicsConstants.SCREENHEIGHT - (Constants.GraphicsConstants.BARHEIGHT + Constants.GraphicsConstants.MENUBARHEIGHT) * 2 + 7));
        setVisible(true);
    }
    // there doesn't seem to be much of a reason right now to create a custom class but it's more versatile for later
}
