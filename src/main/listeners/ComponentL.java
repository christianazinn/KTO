package listeners;

import util.*;

import java.awt.*;
import java.awt.event.*;

/**
 * {@code ComponentL} is a class to implement a {@link ComponentListener} to handle resize events for {@link KTOJF}.
 * 
 * @author Christian Azinn
 * @version 0.1
 * @since 0.2.0
 */
public class ComponentL implements ComponentListener {

    private ComponentContainer cc;

    public ComponentL(ComponentContainer cc) {
        this.cc = cc;
    }

    /**
     * Handles ComponentEvents.
     * @param e a {@link ComponentEvent} sent by a resize call
     */
    public void componentResized(ComponentEvent e) {
        Dimension newSize = e.getComponent().getBounds().getSize();
        cc.locBar.setSize(new Dimension((int) newSize.getWidth(), Constants.GraphicsConstants.BARHEIGHT));
        cc.locBar.setLabelSize();
        cc.botBar.setBounds(0, (int) newSize.getHeight() - 76, (int) newSize.getWidth(), Constants.GraphicsConstants.BARHEIGHT);
        cc.ssPane.setSize(new Dimension(Constants.GraphicsConstants.SBWIDTH, (int) newSize.getHeight() - Constants.GraphicsConstants.MENUBARHEIGHT - 2 * Constants.GraphicsConstants.BARHEIGHT - 39));
        cc.psPane.setSize(new Dimension((int) newSize.getWidth() - Constants.GraphicsConstants.SBWIDTH - 12, (int) newSize.getHeight() - Constants.GraphicsConstants.PSPVOFFSET * 4 - Constants.GraphicsConstants.BARHEIGHT));
        cc.ptPane.setSize(new Dimension((int) newSize.getWidth() - Constants.GraphicsConstants.SBWIDTH - 12, (int) newSize.getHeight() - Constants.GraphicsConstants.PSPVOFFSET * 4 - Constants.GraphicsConstants.BARHEIGHT));
    }
    public void componentHidden(ComponentEvent e) {}
    public void componentMoved(ComponentEvent e) {}
    public void componentShown(ComponentEvent e) {}
}
