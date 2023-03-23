package listeners;

import components.sidebar.*;

import java.awt.*;
import java.awt.event.*;

/**
 * {@code MouseL} is a class to implement a {@link MouseListener} to handle right-click events for {@link KTOJF}.
 * 
 * @author Christian Azinn
 * @version 0.4
 * @since 0.2.0
 */
public class MouseL implements MouseListener {

    // Keep a handle on the ActionListener to pass to the SBRCM and the most recent component
    private ActionListener a;
    private Component mostRecent;


    public MouseL(ActionListener a) {
        this.a = a;
        this.mostRecent = null;
    }


    /**
     * Handles MouseEvents.
     * @param e a {@link MouseEvent} sent by a right click event - {@link SidebarButton} most likely
     */
    public void mouseReleased(MouseEvent e) { if(e.isPopupTrigger()) {
        SidebarButton source = (SidebarButton) e.getSource();
        SidebarRightClickMenu sbrcm = new SidebarRightClickMenu(a, source.getText().charAt(0) == '>', source.isEnabled(), source.getFavorited());
        sbrcm.show(e.getComponent(), e.getX(), e.getY());
        mostRecent = e.getComponent();
    }}
    public void mousePressed(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}


    /**
     * Returns the most recently clicked {@link SidebarButton}.
     * @return the most recently clicked {@link SidebarButton}
     */
    public SidebarButton getMostRecent() {
        return (SidebarButton) mostRecent;
    }
}
