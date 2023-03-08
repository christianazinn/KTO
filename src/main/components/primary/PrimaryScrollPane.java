package components.primary;

import util.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * {@code PrimaryScrollPane} is a class to contain a {@link PrimaryTextPane} to be used in the main frame of the application.
 * 
 * @author Christian Azinn
 * @version 0.2
 * @since 0.0.4
 */
public class PrimaryScrollPane extends JScrollPane {

    // Instance variable for the PrimaryTextPane to be viewed
    PrimaryTextPane view;

    public PrimaryScrollPane(PrimaryTextPane view) {

        // Set viewport target and scrollbar conditions
        super(view, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);

        // Save a reference
        this.view = view;

        // Set size and visibility
        setPreferredSize(new Dimension(Constants.GraphicsConstants.SCREENWIDTH - Constants.GraphicsConstants.SBWIDTH - 12, // scrollbar size sort of
                    Constants.GraphicsConstants.SCREENHEIGHT - Constants.GraphicsConstants.PSPVOFFSET * 4)); // i have no clue why this works but ok
        setVisible(true);
    }
}