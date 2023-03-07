package components.sidebar;

import util.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * {@code SidebarScrollPane} is a class to contain a {@link SidebarPane} to be used to navigate the navigation sidebar of the application.
 * 
 * @author Christian Azinn
 * @version 0.3
 * @since 0.0.3
 */
public class SidebarScrollPane extends JScrollPane {

    // Instance variable for the SidebarPane to be viewed
    private SidebarPane view;

    public SidebarScrollPane(SidebarPane view) {
        // Set SidebarPane to be used as the viewport target, and set scrollbar constants
        super(view, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);

        // Save a reference
        this.view = view;

        // Set size and visibility
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SBWIDTH, Constants.GraphicsConstants.SCREENHEIGHT - (Constants.GraphicsConstants.LOCBARHEIGHT + Constants.GraphicsConstants.MENUBARHEIGHT) * 2 + 7));
        setVisible(true);
    }

    
    /**
     * Update the {@link SidebarPane} targetted by the {@link JViewport} of this {@code SidebarScrollPane}.
     * @param view the new {@link SidebarPane} to target
     */
    public void updateView(SidebarPane view) {
        this.view = view;
        setViewportView(view);
    }


    /**
     * Getter method for the {@link SidebarPane} used by this object.
     * @return a reference to the active {@link SidebarPane}
     */
    public SidebarPane getView() {
        return view;
    }
}
