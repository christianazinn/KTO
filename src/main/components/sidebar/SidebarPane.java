package components.sidebar;

import util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * {@code SidebarPane} is a class to create a {@link JPanel} to be used as the navigation sidebar of the application.
 * 
 * @author Christian Azinn
 * @version 0.4
 * @since 0.0.3
 */
public class SidebarPane extends JPanel {

    // Instance variable for the button command prompts, for reference by actionPerformed
    private ArrayList<String> buttonText;

    public SidebarPane(ArrayList<String> kiys, ActionListener a, boolean isTopLevel) {
        // Absolute positioning layout, layout managers are annoying
        super(null);

        // I messed up and forgot ArrayLists were PBR so this is a hotfix lmao
        ArrayList<String> keys = new ArrayList<String>(kiys);

        // Set up values for button positioning
        int vOffset = 0;
        Insets insets = getInsets();
        Dimension size = new Dimension(Constants.GraphicsConstants.SBWIDTH, Constants.GraphicsConstants.SBHEIGHT);

        // Add the "add" button value - the funny MS Word back double quote, which should never be input anywhere else
        keys.add("”");
        // Add the "back" button value - the funny MS Word forward double quote, which should never be input anywhere else, but not at top level
        if(!isTopLevel) keys.add("“");

        // Set size (don't ask)
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SBWIDTH, keys.size() * Constants.GraphicsConstants.SBHEIGHT + (keys.size() - 1) * Constants.GraphicsConstants.VPADDING));

        // Initialize the ArrayList for button command prompts
        buttonText = new ArrayList<String>(keys.size());

        // Iterate through each of the Strings in the ArrayList
        for(String str : keys) {
            // Since keys are formatted key[text], extract the key
            int relativeIdx = 0;
            String tempstr = str;

            // I can't believe all this code is needed just to backslash escape...
            while(true) {
                // Search for an open bracket
                int bracketIdx = tempstr.indexOf("[") + 1;
                relativeIdx += bracketIdx;

                // if there is no bracket for some reason, or it was not backslash escaped, stop looping
                if(relativeIdx == 0 || tempstr.charAt(bracketIdx - 2) != '\\') break;

                // Continue looping if the bracket was backslash escaped
                tempstr = tempstr.substring(bracketIdx);
            }

            // Copy the key into a new String
            String keyText = str;
            // If there was a bracket present, truncate the String
            if(relativeIdx != 0) keyText = str.substring(0, relativeIdx - 1);
            // Add that String to the button command prompt ArrayList
            buttonText.add(keyText);

            // Create a new SidebarButton using that text and this object as an ActionListener (change that at some point?)
            SidebarButton button = new SidebarButton(keyText, a);
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
