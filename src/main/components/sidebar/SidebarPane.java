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
 * @version 0.1
 * @since 0.0.3
 */
public class SidebarPane extends JPanel implements ActionListener {

    // Instance variable for the button command prompts, for reference by actionPerformed
    private ArrayList<String> buttonText;

    public SidebarPane(ArrayList<String> keys) {

        // Absolute positioning layout, layout managers are annoying
        super(null);

        // Set size (don't ask)
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SBWIDTH, keys.size() * Constants.GraphicsConstants.SBHEIGHT + (keys.size() - 1) * Constants.GraphicsConstants.VPADDING));

        // Set up values for button positioning
        int vOffset = 0;
        Insets insets = getInsets();
        Dimension size = new Dimension(Constants.GraphicsConstants.SBWIDTH, Constants.GraphicsConstants.SBHEIGHT);

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
            // If there was a bracket present (it was CORRECTLY FORMATTED, thank you) truncate the String
            if(relativeIdx != -1) keyText = str.substring(0, relativeIdx);
            // Add that String to the button command prompt ArrayList
            buttonText.add(keyText);

            // Create a new SidebarButton using that text and this object as an ActionListener (change that at some point?)
            SidebarButton button = new SidebarButton(keyText, this);
            // Positioning and adding
            button.setBounds(insets.left, insets.top + vOffset, size.width, size.height);
            add(button);
            // Adding vertical offset for the next button
            vOffset += Constants.GraphicsConstants.SBHEIGHT + Constants.GraphicsConstants.VPADDING;
        }

        // Set visible (duh)
        setVisible(true);
    }

    
    public void actionPerformed(ActionEvent a) {
        // PLACEHOLDER
    }
}
