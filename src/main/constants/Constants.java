package constants;

import java.awt.*;

/**
 * Container class for all constants.
 * 
 * @author Christian Azinn
 * @version 0.1
 * @since 0.0.2
 */
public class Constants {

    /**
     * Container class for graphics-related constants such as padding values.
     * 
     * @author Christian Azinn
     * @version 0.1
     * @since 0.0.2
     */
    public static class GraphicsConstants {
        public static final int HPADDING = 5;
        public static final int VPADDING = 5;
    }

    /**
     * Container class for font-related constants such as font size and font name.
     * 
     * @author Christian Azinn
     * @version 0.1
     * @since 0.0.2
     */
    public static class FontConstants {
        public static final int SMALLFONTSIZE = 12;
        public static final int MEDIUMFONTSIZE = 18;
        public static final int LARGEFONTSIZE = 24;

        public static final String FONTNAME = "Helvetica";

        public static final Font SMALLFONT = new Font(FONTNAME, 0, SMALLFONTSIZE);
        public static final Font MEDIUMFONT = new Font(FONTNAME, 0, MEDIUMFONTSIZE);
        public static final Font LARGEFONT = new Font(FONTNAME, 0, LARGEFONTSIZE);
    }
}
