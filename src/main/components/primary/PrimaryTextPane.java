package components.primary;

import util.Constants;

import javax.swing.*;

/**
 * {@code PrimaryTextPane} is a class to create an editable, markup-able {@link JTextArea} for use as the main frame of the application.
 * 
 * @author Christian Azinn
 * @version 0.2
 * @since 0.0.4
 */
public class PrimaryTextPane extends JTextArea {

    public PrimaryTextPane(String initialText) {
        super();
        setFont(Constants.FontConstants.SFONT);
        setText(initialText);
        setLineWrap(true);
        setWrapStyleWord(true);
        // there doesn't seem to be much of a reason right now to create a custom class but it's more versatile for later
    }

    // getText() is a built-in function
}
