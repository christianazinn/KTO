package components.sidebar;

import util.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * {@code SidebarScrollPane} is a class to create a {@link SidebarPane} to be used to navigate the navigation sidebar of the application.
 * 
 * @author Christian Azinn
 * @version 0.1
 * @since 0.0.3
 */
public class SidebarScrollPane extends JScrollPane {
    public SidebarScrollPane(SidebarPane view) {

        // Set SidebarPane to be used as the viewport target, and set scrollbar constants
        super(view, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);

        // Set size and visibility
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SBWIDTH, Constants.GraphicsConstants.SCREENHEIGHT - (Constants.GraphicsConstants.LOCBARHEIGHT + Constants.GraphicsConstants.MENUBARHEIGHT) * 2 + 7));
        setVisible(true);
    }

    
    /**
     * Update the {@link SidebarPane} targetted by the {@link JViewport} of this {@code SidebarScrollPane}.
     * @param view the new {@link SidebarPane} to target
     */
    public void updateView(SidebarPane view) {
        setViewportView(view);
    }
}
