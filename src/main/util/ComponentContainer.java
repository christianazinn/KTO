package util;

import components.bars.*;
import components.menu.*;
import components.primary.*;
import components.sidebar.*;

/**
 * {@code ComponentContainer} is a class to house all custom Components used by {@link KTOJF} for easier interoperability and flexibility.
 * 
 * @author Christian Azinn
 * @version 0.1
 * @since 0.2.0
 */
public class ComponentContainer {

    public BottomBar botBar;
    public LocationBar locBar;
    public MainMenuBar mmBar;
    public PrimaryScrollPane psPane;
    public PrimaryTextPane ptPane;
    public SidebarPane sbPane;
    public SidebarScrollPane ssPane;
    public CSVManager csv;

    public ComponentContainer(CSVManager csv) {
        this.csv = csv;
    }
}
