package components.sidebar;

import util.Constants;

import javax.swing.*;
import java.awt.event.*;

/**
 * {@code SidebarRightClickMenu} is a class to create a right-click menu for an {@link SidebarButton}.
 * 
 * @author Christian Azinn
 * @version 0.4
 * @since 0.1.4
 */
public class SidebarRightClickMenu extends JPopupMenu {
    public SidebarRightClickMenu(ActionListener a, boolean isRedirect, boolean isEnabled) {
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

        if(isEnabled) {
            JMenuItem deactivateItem = new JMenuItem("Deactivate...");
            deactivateItem.getAccessibleContext().setAccessibleDescription("Deactivates this button.");
            deactivateItem.setFont(Constants.FontConstants.FFONT);
            deactivateItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
            add(deactivateItem);
            deactivateItem.setActionCommand(prefix + "Dacv");
            deactivateItem.addActionListener(a);
        } else {
            JMenuItem activateItem = new JMenuItem("Activate...");
            activateItem.getAccessibleContext().setAccessibleDescription("Activates this button.");
            activateItem.setFont(Constants.FontConstants.FFONT);
            activateItem.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
            add(activateItem);
            activateItem.setActionCommand(prefix + "Actv");
            activateItem.addActionListener(a);
        }
    }
}
