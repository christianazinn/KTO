import java.util.Scanner;
import java.util.ArrayList;

/**
 * {@code InputHandler} is a class containing methods to handle user input.
 * A single instance of a {@code InputHandler} is created in each run of {@link KTOCL} to process user input.
 * 
 * @author Christian Azinn
 * @version 1.0
 * @since 0.1
 * @deprecated Incompatible with the JFrame application. Use that instead.
 */
public class InputHandlerCL {

    // only instance variable
    private Scanner s;


    /**
     * Constructor for an InputHandler object.
     * Only declares a {@code Scanner} object.
     */
    public InputHandlerCL() {
        s = new Scanner(System.in);
    }


    /**
     * Gets user input from the command line.
     * A glorified Scanner.nextLine().
     * @return user input from the command line
     */
    public String getInput() {
        return s.nextLine();
    }


    /**
     * Processes the commands within a String of command-line input for further processing by {@link KTOCL}. 
     * Can process all 1, 2, and 3-context commands.
     * @param input user input from the command line
     * @return an {@code ArrayList<String>} containing the input commands and context
     */
    public ArrayList<String> getCommands(String input) {

        // initialize arraylists
        ArrayList<String> commands = new ArrayList<String>();

        // process first command
        int spIdx = input.indexOf(" ");
        
        // write, showkeys, help, and exit have no space
        if(spIdx == -1) {
            String inputL = input.toLowerCase();
            if(inputL.equals(Command.WRITEFILE.prompt) || inputL.equals(Command.SHOWKEYS.prompt) 
                 || inputL.equals(Command.HELP.prompt) || inputL.equals(Command.EXIT.prompt)) commands.add(input);
            return commands;
        }

        // everything else goes normally
        String firstCommand = input.substring(0, spIdx).toLowerCase();
        input = input.substring(spIdx + 1);

        // check for invalid input
        boolean isValid = false;
        for(Command command : Command.values()) if(firstCommand.equals(command.prompt)) {
                isValid = true;
                break;
        }
        if(!isValid) return new ArrayList<String>();

        commands.add(firstCommand);

        // check whether the command is one that requires a secondary input
        // primary inputs in that case are enclosed in quotes
        if  (  firstCommand.equals(Command.ADDTOLINE.prompt)
            || firstCommand.equals(Command.DELETEFROMLINE.prompt)
            || firstCommand.equals(Command.CHANGEKEY.prompt)
            || firstCommand.equals(Command.COPY.prompt)    ) {
                input = input.substring(1);
                spIdx = input.indexOf("\"");
                if(spIdx == -1) return new ArrayList<String>();
                String secondCommand = input.substring(0, spIdx);
                input = input.substring(spIdx + 2);
                commands.add(secondCommand);
            }

        // add whatever's left and return
        commands.add(input);
        return commands;
    }




    /**
     * {@code InputHandler.Command} is an enum for all possible commands. 
     * Stored in enum form for easier modification later down the line.
     */
    public static enum Command {

        /**
         * 1-argument. 
         * Usage: "help" 
         * Brings up a help page containing a list of commands.
         */
        HELP            ("help"),

        /**
         * 1-argument. 
         * Usage: "exit" 
         * Exits the program.
         */
        EXIT            ("exit"),

        /**
         * 1-argument. 
         * Usage: "write"
         * @see CSVManagerCL#write()
         */
        WRITEFILE       ("write"),

        /**
         * 1-argument. 
         * Usage: "showkeys" 
         * @see CSVManagerCL#getKeys()
         */
        SHOWKEYS        ("showkeys"),

        /**
         * 1-argument. 
         * Usage: "getname" 
         * @see CSVManagerCL#getF()
         */
        GETFILENAME     ("getname"),

        /**
         * 2-argument. 
         * Usage: "read [filename]" 
         * @see CSVManagerCL#read(String)
         */
        READFILE        ("read"), 

        /**
         * 2-argument. 
         * Usage: "create [filename]" 
         * @see CSVManagerCL#create(String)
         */
        CREATEFILE      ("create"),

        /**
         * 2-argument. 
         * Usage: "newline [key]" 
         * @see CSVManagerCL#newLine(String)
         */
        NEWLINE         ("newline"),

        /**
         * 2-argument. 
         * Usage: "clear [key]" 
         * @see CSVManagerCL#clearLine(String)
         */
        CLEARLINE       ("clear"),

        /**
         * 2-argument.
         * Usage: "delline [key]"
         * @see CSVManagerCL#deleteLine(String)
         */
        DELETELINE      ("delline"), 

        /**
         * 2-argument. 
         * Usage: "getline [key]" 
         * @see CSVManagerCL#getLine(String)
         */
        GETLINE         ("getline"), 
        
        /**
         * 3-argument. 
         * Usage: "add "[key]" [information]" 
         * @see CSVManagerCL#addToLine(String, String)
         */
        ADDTOLINE       ("add"), 

        /**
         * 3-argument. 
         * Usage: "del "[key]" [idx]"
         * @see CSVManagerCL#deleteFromLine(String, int)
         */
        DELETEFROMLINE  ("del"),
        
        /**
         * 3-argument. 
         * Usage: "changekey "[key]" [newkey]" 
         * @see CSVManagerCL#changeKey(String, String)
         */
        CHANGEKEY       ("changekey"), 

        /**
         * 3-argument. 
         * Usage: "copy "[key]" [newkey]"
         * @see CSVManagerCL#copy(String, String)
         */
        COPY            ("copy"); // copy "[key]" [new key]

        /**
         * The String used to invoke a certain {@code Command}.
         */
        public final String prompt;

        /**
         * Constructor for a Command. 
         * All this contains is the prompt that is correlated with the command enum.
         * @param prompt the prompt for the command
         */
        private Command(String prompt) {
            this.prompt = prompt;
        }
    }
}
