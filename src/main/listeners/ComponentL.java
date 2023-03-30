package listeners;

import util.*;

import java.awt.*;
import java.awt.event.*;

/**
 * {@code ComponentL} is a class to implement a {@link ComponentListener} to handle resize events for {@link KTOJF}.
 * 
 * @author Christian Azinn
 * @version 0.3
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
        cc.locBar.setSize(new Dimension((int) newSize.width, Constants.GraphicsConstants.BARHEIGHT));
        cc.locBar.setLabelSize();
        cc.botBar.setBounds(0, (int) newSize.height - 76, (int) newSize.width, Constants.GraphicsConstants.BARHEIGHT);
        cc.ssPane.setSize(new Dimension(Constants.GraphicsConstants.SBWIDTH, (int) newSize.height - Constants.GraphicsConstants.PSPVOFFSET * 4 - Constants.GraphicsConstants.BARHEIGHT));
        cc.ssPane.repaint();
        cc.psPane.setSize(new Dimension((int) newSize.width - Constants.GraphicsConstants.SBWIDTH - 12, (int) newSize.height - Constants.GraphicsConstants.PSPVOFFSET * 4 - Constants.GraphicsConstants.BARHEIGHT));
        cc.ptPane.setSize(new Dimension((int) newSize.width - Constants.GraphicsConstants.SBWIDTH - 12, (int) newSize.height - Constants.GraphicsConstants.PSPVOFFSET * 4 - Constants.GraphicsConstants.BARHEIGHT));
        cc.pnPane.setSize(new Dimension((int) newSize.width - Constants.GraphicsConstants.SBWIDTH - 12, (int) newSize.height - Constants.GraphicsConstants.PSPVOFFSET * 4 - Constants.GraphicsConstants.BARHEIGHT));
        }
    public void componentHidden(ComponentEvent e) {}
    public void componentMoved(ComponentEvent e) {}
    public void componentShown(ComponentEvent e) {}
}
