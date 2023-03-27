package util;

import java.awt.*;

/**
 * {@code Constants} is a container class for all constants, each individually categorized in their own class.
 * 
 * @author Christian Azinn
 * @version 0.8
 * @since 0.0.2
 */
public class Constants {

    
    /**
     * {@code GraphicsConstants} is a container class for graphics-related constants such as padding values.
     * 
     * @author Christian Azinn
     * @version 0.6
     * @since 0.0.2
     */
    public static class GraphicsConstants {

        public static final int SCREENWIDTH = 960;
        public static final int SCREENHEIGHT = 540;

        public static final int MENUBARHEIGHT = 23;
        public static final int BARHEIGHT = 18;

        public static final int HPADDING = 5;
        public static final int VPADDING = 5;

        public static final int SBWIDTH = 160;
        public static final int SBHEIGHT = 40;

        public static final int ICONSIZE = 16;

        public static final Dimension MENUBSIZE = new Dimension(130, 25);
        
        public static final int PSPVOFFSET = (MENUBARHEIGHT + BARHEIGHT) / 2;
    }


    /**
     * {@code FontConstants} is a container class for font-related constants such as font size and font name.
     * 
     * @author Christian Azinn
     * @version 0.3
     * @since 0.0.2
     */
    public static class FontConstants {
        public static final int SMALLFONTSIZE = 12;
        public static final int MEDIUMFONTSIZE = 18;
        public static final int LARGEFONTSIZE = 24;

        public static final String FONTNAME = "Helvetica";

        public static final Font SFONT = new Font(FONTNAME, Font.PLAIN, SMALLFONTSIZE);
        public static final Font MFONT = new Font(FONTNAME, Font.PLAIN, MEDIUMFONTSIZE);
        public static final Font LFONT = new Font(FONTNAME, Font.PLAIN, LARGEFONTSIZE);

        public static final Font SBOLD = new Font(FONTNAME, Font.BOLD, SMALLFONTSIZE);
        public static final Font MBOLD = new Font(FONTNAME, Font.BOLD, MEDIUMFONTSIZE);
        public static final Font LBOLD = new Font(FONTNAME, Font.BOLD, LARGEFONTSIZE);

        public static final Font SITAL = new Font(FONTNAME, Font.ITALIC, SMALLFONTSIZE);
        public static final Font MITAL = new Font(FONTNAME, Font.ITALIC, MEDIUMFONTSIZE);
        public static final Font LITAL = new Font(FONTNAME, Font.ITALIC, LARGEFONTSIZE);

        public static final Font FFONT = new Font(FONTNAME, Font.PLAIN, 12);
    }

    /**
     * {@code GeneralConstants} is a container class for generic constants that don't fall under the other constant classes.
     * 
     * @author Christian Azinn
     * @version 0.2
     * @since 0.2.0
     */
    public static class GeneralConstants {
        public static final int AUTOSAVEREFRESH = 1000; // ms
        public static final String EMPTYICONPATH = "img/empty.png";
    }
}
