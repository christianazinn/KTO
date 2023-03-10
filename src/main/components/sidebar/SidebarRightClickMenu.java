package components.sidebar;

import util.Constants;

import javax.swing.*;
import java.awt.event.*;

/**
 * {@code SidebarRightClickMenu} is a class to create a right-click menu for an {@link SidebarButton}.
 * 
 * @author Christian Azinn
 * @version 0.1
 * @since 0.1.4
 */
public class SidebarRightClickMenu extends JPopupMenu {
    public SidebarRightClickMenu(ActionListener a) {
        super("Options");
        String prefix = "*";
        
        JMenuItem copyItem = new JMenuItem("Copy...");
        copyItem.getAccessibleContext().setAccessibleDescription("Copies this subbranch.");
        copyItem.setFont(Constants.FontConstants.FFONT);
        copyItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
        add(copyItem);
        copyItem.setActionCommand(prefix + "Copy");
        copyItem.addActionListener(a);
        
        JMenuItem renameItem = new JMenuItem("Rename...");
        renameItem.getAccessibleContext().setAccessibleDescription("Renames this subbranch.");
        renameItem.setFont(Constants.FontConstants.FFONT);
        renameItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
        add(renameItem);
        renameItem.setActionCommand(prefix + "Rnme");
        renameItem.addActionListener(a);
    }
}
