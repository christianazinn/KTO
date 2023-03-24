package util;

import components.bars.*;
import components.menu.*;
import components.primary.*;
import components.sidebar.*;

import javax.swing.*;

/**
 * {@code ComponentContainer} is a class to house all custom Components used by {@link KTOJF} for easier interoperability and flexibility.
 * 
 * @author Christian Azinn
 * @version 0.4
 * @since 0.2.0
 */
public class ComponentContainer {

    public BottomBar botBar;
    public BottomRedirectButton brBut;
    public LocationBar locBar;
    public MainMenuBar mmBar;
    public PrimaryScrollPane psPane;
    public PrimaryTextPane ptPane;
    public PrimaryNullPane pnPane;
    public SidebarPane sbPane;
    public SidebarScrollPane ssPane;
    public CSVManager csv;
    public JFileChooser fc;

    public ComponentContainer(CSVManager csv) {
        this.csv = csv;
    }
}
