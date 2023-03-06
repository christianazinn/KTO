package components.menu;

import javax.swing.*;
//import java.awt.*;
import java.awt.event.*;

/**
 * {@code MainMenuBar} is a class to create a {@code JMenuBar} for the main window to use.
 * 
 * @author Christian Azinn
 * @version 0.2
 * @since 0.0.1
 */
public class MainMenuBar extends JMenuBar implements ActionListener {
    public MainMenuBar() {
        JMenu fileMenu = new FileMenu(this);
        add(fileMenu);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("item1")) {
            System.out.println("mris");
        }
        // PLACEHOLDER
    }
}
