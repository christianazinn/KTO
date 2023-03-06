package locbar;

import constants.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * {@code LocationBar} is a class to create a simple bar at the top of the screen to indicate where the user is.
 * 
 * @author Christian Azinn
 * @version 0.1
 * @since 0.0.2
 */
public class LocationBar extends JPanel {

    private String text;
    private int directoriesDown;
    private JLabel label;

    public LocationBar(String filename) {
        super(null); // abspos

        text = filename;
        directoriesDown = 0;
        Insets insets = getInsets();

        setPreferredSize(new Dimension(960, 18));

        label = new JLabel("> " + text);
        add(label);
        label.setFont(Constants.FontConstants.SMALLFONT);
        Dimension size = label.getPreferredSize();
        label.setBounds(Constants.GraphicsConstants.HPADDING + insets.left, insets.top, size.width, size.height);

        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setVisible(true);
    }

    public void directoryDown(String directory) {
        text += " > " + directory;
        label.setText(text);
        directoriesDown++;
    }

    public boolean directoryUp() {
        if(directoriesDown == 0) return false;
        int i = text.length();
        for(; i >= 0; i--) if(text.charAt(i) == '>') break;
        text = text.substring(0, text.length() - i - 1);
        label.setText(text);
        directoriesDown--;
        return true;
    }

    public void displayText() {
    }
}
