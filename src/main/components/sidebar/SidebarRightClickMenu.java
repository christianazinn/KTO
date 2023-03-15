package components.sidebar;

import util.Constants;

import javax.swing.*;
import java.awt.event.*;

/**
 * {@code SidebarRightClickMenu} is a class to create a right-click menu for an {@link SidebarButton}.
 * 
 * @author Christian Azinn
 * @version 0.3
 * @since 0.1.4
 */
public class SidebarRightClickMenu extends JPopupMenu {
    public SidebarRightClickMenu(ActionListener a, boolean isRedirect) {
        super("Options");
        String prefix = "*";
        
        if(!isRedirect) {
            JMenuItem copyItem = new JMenuItem("Copy...");
            copyItem.getAccessibleContext().setAccessibleDescription("Copies this subbranch.");
            copyItem.setFont(Constants.FontConstants.FFONT);
            copyItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
            add(copyItem);
            copyItem.setActionCommand(prefix + "Copy");
            copyItem.addActionListener(a);
        }
        
        JMenuItem renameItem = new JMenuItem("Rename...");
        renameItem.getAccessibleContext().setAccessibleDescription("Renames this subbranch.");
        renameItem.setFont(Constants.FontConstants.FFONT);
        renameItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
        add(renameItem);
        renameItem.setActionCommand(prefix + "Rnme");
        renameItem.addActionListener(a);
        
        JMenuItem deleteItem = new JMenuItem("Delete...");
        deleteItem.getAccessibleContext().setAccessibleDescription("Deletes this subbranch.");
        deleteItem.setFont(Constants.FontConstants.FFONT);
        deleteItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
        add(deleteItem);
        deleteItem.setActionCommand(prefix + "Delt");
        deleteItem.addActionListener(a);
    }
}
