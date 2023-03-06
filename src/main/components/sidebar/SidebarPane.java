package components.sidebar;

import util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * {@code SidebarPane} is a class to create a {@code JPanel} to be used as the navigation sidebar of the application.
 * 
 * @author Christian Azinn
 * @version 0.1
 * @since 0.0.3
 */
public class SidebarPane extends JPanel implements ActionListener {
    public SidebarPane(ArrayList<String> keys) {
        super(null);
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SBWIDTH, keys.size() * Constants.GraphicsConstants.SBHEIGHT + (keys.size() - 1) * Constants.GraphicsConstants.VPADDING));

        int vOffset = 0;
        Insets insets = getInsets();
        Dimension size = new Dimension(Constants.GraphicsConstants.SBWIDTH, Constants.GraphicsConstants.SBHEIGHT);
        ArrayList<String> buttonText = new ArrayList<String>(keys.size());

        for(String str : keys) {
            int relativeIdx = 0;
            
            String tempstr = str;
            while(true) {
                int bracketIdx = tempstr.indexOf("[");
                relativeIdx += bracketIdx;
                if(relativeIdx == -1 || tempstr.charAt(bracketIdx - 1) != '\\') break;
                tempstr = tempstr.substring(bracketIdx + 1);
                relativeIdx++;
            }

            String keyText = str;
            if(relativeIdx != -1) keyText = str.substring(0, relativeIdx);
            buttonText.add(keyText);

            SidebarButton button = new SidebarButton(keyText, this);
            add(button);
            button.setBounds(insets.left, insets.top + vOffset, size.width, size.height);
            vOffset += Constants.GraphicsConstants.SBHEIGHT + Constants.GraphicsConstants.VPADDING;

            setVisible(true);
        }
    }

    public void actionPerformed(ActionEvent a) {
        // PLACEHOLDER
    }
}
