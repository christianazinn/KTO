import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * {@code MainMenuBar} is a class to create a {@code JMenuBar} for the main window to use.
 * 
 * @author Christian Azinn
 * @version 0.1
 * @since 0.0.1
 */
public class MainMenuBar extends JMenuBar implements ActionListener {
    public MainMenuBar() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.getAccessibleContext().setAccessibleDescription("Test!");
        add(fileMenu);

        JMenuItem menuItem = new JMenuItem("A text-only menu item");
        menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        fileMenu.add(menuItem);
        menuItem.setActionCommand("item1");
        menuItem.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("item1")) {
            System.out.println("mris");
        }
    }
}
