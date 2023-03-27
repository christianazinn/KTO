package components.sidebar;

import components.menu.MenuBase;

import javax.swing.*;
import java.awt.event.*;

/**
 * {@code SidebarRightClickMenu} is a class to create a right-click menu for an {@link SidebarButton}.
 * 
 * @author Christian Azinn
 * @version 0.7
 * @since 0.1.4
 */
public class SidebarRightClickMenu extends JPopupMenu {
    public SidebarRightClickMenu(ActionListener a, boolean isRedirect, boolean isEnabled, boolean isFavorited) {
        super("Options");
        String prefix = "*";
        
        if(isEnabled) {
            if(!isRedirect) {
                JMenuItem copyItem = MenuBase.newButton("Copy...");
                MenuBase.format(copyItem, prefix ,"Copies this subbranch.", "Copy", a);
                add(copyItem);
            }

            JMenuItem renameItem = MenuBase.newButton("Rename...");
            MenuBase.format(renameItem, prefix, "Renames this subbranch.", "Rnme", a);
            add(renameItem);
            
            JMenuItem deleteItem = MenuBase.newButton("Delete...");
            MenuBase.format(deleteItem, prefix, "Deletes this subbranch.", "Delt", a);
            add(deleteItem);
            
            String favorite;
            if(isFavorited) favorite = "Unfavorite...";
            else favorite = "Favorite...";
            JMenuItem favoriteItem = MenuBase.newButton(favorite);
            MenuBase.format(favoriteItem, prefix, "Toggles this subbranch's favorite status.", "Fvrt", a);
            add(favoriteItem);

            JMenuItem deactivateItem = MenuBase.newButton("Deactivate...");
            MenuBase.format(deactivateItem, prefix, "Deactivates this button.", "Dacv", a);
            add(deactivateItem);
        } else {
            JMenuItem activateItem = MenuBase.newButton("Activate...");
            MenuBase.format(activateItem, prefix, "Activates this button.", "Actv", a);
            add(activateItem);
        }
    }
}
