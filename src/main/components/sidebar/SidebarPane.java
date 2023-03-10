package components.sidebar;

import util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * {@code SidebarPane} is a class to create a {@link JPanel} to be used as the navigation sidebar of the application.
 * 
 * @author Christian Azinn
 * @version 0.5
 * @since 0.0.3
 */
public class SidebarPane extends JPanel {

    // Instance variable for the button command prompts, for reference by actionPerformed
    private ArrayList<String> buttonText;


    public SidebarPane(ArrayList<String> kiys, ActionListener a, MouseListener m, boolean isTopLevel) {
        // Absolute positioning layout, layout managers are annoying
        super(null);

        // I messed up and forgot ArrayLists were PBR so this is a hotfix lmao
        ArrayList<String> keys = new ArrayList<String>(kiys);
        // Add the "add" and "back" button values - the funny MS Word double quotes, which should never be input anywhere else
        keys.add("”");
        if(!isTopLevel) keys.add("“");
        // Initialize the ArrayList for button command prompts
        buttonText = new ArrayList<String>(keys.size());

        // Set up values for button positioning and size
        int vOffset = 0;
        Insets insets = getInsets();
        Dimension size = new Dimension(Constants.GraphicsConstants.SBWIDTH, Constants.GraphicsConstants.SBHEIGHT);
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SBWIDTH, keys.size() * Constants.GraphicsConstants.SBHEIGHT + (keys.size() - 1) * Constants.GraphicsConstants.VPADDING));

        // Iterate through each of the Strings in the ArrayList
        for(String str : keys) {
            // Isolate key and add to buttonText ArrayList
            int idx = CSVManager.findNotBackslashed(str, "[");
            String keyText = str;
            if(idx != -1) keyText = str.substring(0, idx);
            buttonText.add(keyText);

            // Create a new SidebarButton using that text and this object as an ActionListener (change that at some point?)
            SidebarButton button = new SidebarButton(keyText, a, m);
            // Positioning and adding
            button.setBounds(insets.left, insets.top + vOffset, size.width - 20, size.height);
            add(button);
            // Adding vertical offset for the next button
            vOffset += Constants.GraphicsConstants.SBHEIGHT + Constants.GraphicsConstants.VPADDING;
        }
        // Set visible (duh)
        setVisible(true);
    }


    /**
     * Getter method for the {@code ArrayList<String>} containing button keywords.
     * @return the list of button keywords
     */
    public ArrayList<String> getButtonText() {
        return buttonText;
    }
}
