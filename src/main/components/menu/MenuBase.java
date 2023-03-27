package components.menu;

import util.Constants;

import javax.swing.*;
import java.awt.event.*;

/**
 * {@code MenuBase} is the base class for all subclasses of {@link JMenu}, providing intermediate functionality. 
 * Also provides static methods for subclasses of {@link JPopupMenu} and other menu bases.
 * 
 * @author Christian Azinn
 * @version 0.1
 * @since 0.3.2
 */
public abstract class MenuBase extends JMenu {

    private String prefix;
    private ActionListener a;

    // only exists so other Menus can call superclass constructor
    public MenuBase(String arg, String prefix, ActionListener a) {
        super(arg);
        this.prefix = prefix;
        this.a = a;
    }


    /**
     * Handles all the repetitive formatting things such as accessible description, font, size, and ActionListener things. 
     * Used by subclasses.
     * @param i the {@link JMenuItem} to operate on
     * @param desc the accessible description for the {@link JMenuItem}
     * @param cmd the action command for the {@link JMenuItem}
     */
    public void format(JMenuItem i, String desc, String cmd) {
        format(i, prefix, desc, cmd, a);
    }

    /**
     * Handles all the repetitive formatting things such as accessible description, font, size, and ActionListener things. 
     * Used by non-subclasses (e.g. subclasses of {@link JPopupMenu}).
     * @param i the {@link JMenuItem} to operate on
     * @param prefix the action prefix for the item
     * @param desc the accessible description for the item
     * @param cmd the action command for the item
     * @param a the {@link ActionListener} for the item
     */
    public static void format(JMenuItem i, String prefix, String desc, String cmd, ActionListener a) {
        i.getAccessibleContext().setAccessibleDescription(desc);
        i.setFont(Constants.FontConstants.FFONT);
        i.setPreferredSize(Constants.GraphicsConstants.MENUBSIZE);
        i.setActionCommand(prefix + cmd);
        i.addActionListener(a);
    }


    // Button constructors

    
    /**
     * Returns a {@link JRadioButtonMenuItem} with the given {@code name} and {@code selected} status.
     * @param name the name of the button
     * @param selected the selection status of the button
     * @return a {@link JRadioButtonMenuItem} with the given parameters
     */
    public static JMenuItem newRadioButton(String name, boolean selected) {
        return new JRadioButtonMenuItem(name, selected);
    }

    /**
     * Returns a {@link JRadioButtonMenuItem} with the given {@code name}, {@code icon}, and {@code selected} status.
     * @param name the name of the button
     * @param icon the relative path of the icon image file
     * @param selected the selection status of the button
     * @return a {@link JRadioButtonMenuItem} with the given parameters
     */
    public static JMenuItem newRadioButton(String name, String icon, boolean selected) {
        return new JRadioButtonMenuItem(name, new ImageIcon(new ImageIcon(ClassLoader.getSystemResource(icon))
                    .getImage().getScaledInstance(Constants.GraphicsConstants.ICONSIZE, Constants.GraphicsConstants.ICONSIZE, java.awt.Image.SCALE_SMOOTH)), selected);
    }


    /**
     * Returns a {@link JMenuItem} with the given {@code name} and an empty icon.
     * @param name the name of the button
     * @return a {@link JMenuItem} with the given parameters
     */
    public static JMenuItem newButton(String name) {
        return newButton(name, Constants.GeneralConstants.EMPTYICONPATH);
    }

    /**
     * Returns a {@link JMenuItem} with the given {@code name} and an specified {@code icon}.
     * @param name the name of the button
     * @param icon the relative path of the icon image file
     * @return a {@link JMenuItem} with the given parameters
     */
    public static JMenuItem newButton(String name, String icon) {
        return new JMenuItem(name, new ImageIcon(new ImageIcon(ClassLoader.getSystemResource(icon))
                    .getImage().getScaledInstance(Constants.GraphicsConstants.ICONSIZE, Constants.GraphicsConstants.ICONSIZE, java.awt.Image.SCALE_SMOOTH)));
    }    
}
