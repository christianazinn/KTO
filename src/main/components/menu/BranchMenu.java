package components.menu;

import javax.swing.*;
import java.awt.event.*;

/**
 * {@code BranchMenu} is a class to create a {@code JMenu} containing branch-related options for the {@link MainMenuBar} to use.
 * 
 * @author Christian Azinn
 * @version 0.1
 * @since 0.3.2
 */
public class BranchMenu extends MenuBase {
    public BranchMenu(ActionListener a) {
        super("Branch", "#BRCH", a);
        getAccessibleContext().setAccessibleDescription("Menu for branch interactions.");

        // TODOST - ADD ICONS

        // Activate all
        JMenuItem activateItem = newButton("Activate All...");
        format(activateItem, "Activates all buttons in the current branch.", "Aval");
        add(activateItem);

        // Deactivate all
        JMenuItem deactivateItem = newButton("Deactivate All..."); 
        format(deactivateItem, "Deactivates all buttons in the current branch.", "Dval");
        add(deactivateItem);

        // Unfavorite all
        JMenuItem unfavoriteItem = newButton("Unfavorite All...");
        format(unfavoriteItem, "Unfavorites all buttons in the current branch.", "Ufal");
        add(unfavoriteItem);

        // Delete all / reset branch to empty
        JMenuItem resetItem = newButton("Delete All...");
        format(resetItem, "Deletes all buttons in the current branch.", "Rsal");
        add(resetItem);
    }
}