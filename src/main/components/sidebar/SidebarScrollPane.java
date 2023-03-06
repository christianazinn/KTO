package components.sidebar;

import util.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * {@code SidebarScrollPane} is a class to create a {@code JScrollPane} to be used to navigate the navigation sidebar of the application.
 * 
 * @author Christian Azinn
 * @version 0.1
 * @since 0.0.3
 */
public class SidebarScrollPane extends JScrollPane {
    public SidebarScrollPane(SidebarPane view) {
        super(view, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);

        setPreferredSize(new Dimension(Constants.GraphicsConstants.SBWIDTH, Constants.GraphicsConstants.SCREENHEIGHT - (Constants.GraphicsConstants.LOCBARHEIGHT + Constants.GraphicsConstants.MENUBARHEIGHT) * 2 + 7));
        setSize(getPreferredSize());
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        setVisible(true);
    }

    public void updateView(SidebarPane view) {
        setViewportView(view);
    }
}
