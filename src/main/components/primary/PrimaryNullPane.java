package components.primary;

import javax.swing.*;
import java.awt.*;

/**
 * {@code PrimaryNullPane} is a class to create a clear, customizable {@link JPanel} for use as a placeholder when the main frame of the application needs to be empty.
 * 
 * @author Christian Azinn
 * @version 0.2
 * @since 0.2.1
 */
public class PrimaryNullPane extends JPanel {
    public PrimaryNullPane() {
        super();
        setBackground(Color.LIGHT_GRAY);
    }
    // there doesn't seem to be much of a reason right now to create a custom class but it's more versatile for later
}
