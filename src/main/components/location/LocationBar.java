package components.location;

import util.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * {@code LocationBar} is a class to create a simple bar at the top of the screen to indicate where the user is.
 * 
 * @author Christian Azinn
 * @version 0.2
 * @since 0.0.2
 */
public class LocationBar extends JPanel {

    private String text;
    private int directoriesDown;
    private JLabel label;

    public LocationBar(String filename) {
        super(null); // abspos

        Insets insets = getInsets();

        setPreferredSize(new Dimension(Constants.GraphicsConstants.SCREENWIDTH, Constants.GraphicsConstants.LOCBARHEIGHT));

        label = new JLabel("For some reason this placeholder text has to be really long or otherwise the label doesn't fully show. There's probably a method to fix this but I'm lazy so I'm just writing this placeholder text.");
        add(label);
        label.setFont(Constants.FontConstants.SBOLD);
        Dimension size = label.getPreferredSize();
        label.setBounds(Constants.GraphicsConstants.HPADDING + insets.left, insets.top, size.width, size.height);
        
        reset(filename);

        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setVisible(true);
    }

    public void directoryDown(String directory) {
        text += " > " + directory;
        label.setText(text);
        directoriesDown++;
    }

    public boolean directoryUp(int reps) {
        for(; reps > 0; reps--) {
            if(directoriesDown == 0) return false;
            int i = text.length();
            for(; i >= 0; i--) if(text.charAt(i) == '>') break;
            text = text.substring(0, text.length() - i - 1);
            directoriesDown--;
        }
        label.setText(text);
        return true;
    }

    public void reset(String filename) {
        text = filename;
        directoriesDown = 0;
        label.setText("> " + text);
    }
}
