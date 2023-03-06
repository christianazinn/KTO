import java.util.ArrayList;

/**
 * {@code KTOCL.java} is the main class of the KTO project.
 * Uses a single instance of {@link CSVManagerCL} and {@link InputHandlerCL} each run to interface with CSV files and handle user input, respectively.
 * This is the main (command-line) file to be run, and contains the main program flow.
 * 
 * @author Christian Azinn
 * @version 1.0
 * @since 0.1
 */
public class KTOCL {

    /**
     * Main method. Handles all program flow and logic.
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        // instantiate csvmanager and inputhandler
        CSVManagerCL csv = new CSVManagerCL();
        InputHandlerCL input = new InputHandlerCL();
        boolean keepLooping = true;

        // strings for use later
        String id = "    ";
        String ii = "Invalid input! ";
        String ik = "Invalid key! ";
        String ia = "Invalid filename! ";
        String help = "List of commands is:\n" +
            id + "help       | Displays this message.\n" +
            id + "exit       | Exits the program.\n" +
            id + "write      | Saves progress in the current KTOframe.\n" +
            id + "showkeys   | Shows the list of keys in the current KTOframe.\n" +
            id + "getname    | Gets the name of the currently active file.\n" +
            id + "read       | Reads a file into the KTOframe. Param: filename\n" +
            id + "create     | Creates a new file and loads it into the KTOframe. Param: filename\n" +
            id + "newline    | Creates a new line with a given key. Param: key\n" +
            id + "clear      | Clears all data in a line. Param: key\n" +
            id + "delline    | Completely deletes a line. Param: key\n" +
            id + "getline    | Retrieves all notes in a line. Param: key\n" +
            id + "add        | Add a note to a line. Params: key, note\n" +
            id + "del        | Delete a note from a line. Params: key, index\n" +
            id + "changekey  | Change the key of a line. Params: key, newkey\n" +
            id + "copy       | Copy a line with a new key. Params: key, newkey";

        while(keepLooping) {
            System.out.print(">>");
            ArrayList<String> commands = input.getCommands(input.getInput());
            if(commands.size() == 0 || commands.size() > 3) System.out.println(id + ii + help);
            else if(commands.size() == 1) {

                String command = commands.get(0);

                if(command.equals(InputHandlerCL.Command.WRITEFILE.prompt)) {
                    if(!csv.write()) System.out.println(id + "An error occurred during writing. This shouldn't have happened.");
                    else System.out.println(id + "Saved successfully.");
                } else if(command.equals(InputHandlerCL.Command.SHOWKEYS.prompt)) {
                    System.out.println(id + "List of keys for file \"" + csv.getF() + "\":");
                    System.out.println(id + "---------------------------------------------------------------");
                    int i = 0;
                    for(String key : csv.getKeys()) {
                        if(i == 0) System.out.print(id);
                        System.out.print(key);
                        if(i == 4) {
                            System.out.println();
                            i = 0;
                        }
                        else {
                            if(key.length() <= 15) for(int j = 0; j < 15 - key.length(); j++) System.out.print(" ");
                            else System.out.print(" ");
                            System.out.print("| ");
                            i++;
                        }
                    }
                    if(i != 0) System.out.println();
                } else if(command.equals(InputHandlerCL.Command.EXIT.prompt)) {
                    System.out.println(id + "Goodbye!");
                    keepLooping = false;
                }
                else if(command.equals(InputHandlerCL.Command.GETFILENAME.prompt)) System.out.println(id + "Active filename: \"" + csv.getF() + "\"");
                else if(command.equals(InputHandlerCL.Command.HELP.prompt)) System.out.println(id + help);
                else System.out.println(id + ii + help);

            } else if(commands.size() == 2) {

                String command = commands.get(0);
                String key = commands.get(1);

                if(command.equals(InputHandlerCL.Command.READFILE.prompt)) { 
                    try {
                        if(csv.read(key)) System.out.println(id + "File \"" + key + "\" read successfully.");
                        else System.out.println(id + ia);
                    }  catch(Exception e) { System.out.println(id + ia); }
                } else if(command.equals(InputHandlerCL.Command.CREATEFILE.prompt)) {
                    if(csv.create(key)) System.out.println(id + "File \"" + key + "\" created successfully.");
                    else System.out.println(id + ia);
                } else if(command.equals(InputHandlerCL.Command.CLEARLINE.prompt)) {
                    if(csv.clearLine(key)) System.out.println(id + "Line \"" + key + "\" cleared successfully.");
                    else System.out.println(id + ik);
                } else if(command.equals(InputHandlerCL.Command.DELETELINE.prompt)) {
                    if(csv.deleteLine(key)) System.out.println(id + "Line \"" + key + "\" deleted successfully.");
                    else System.out.println(id + ik);
                } else if(command.equals(InputHandlerCL.Command.GETLINE.prompt)) {
                    System.out.println(id + "List of notes for key \"" + key + "\":");
                    System.out.println(id + "---------------------------------------------------------------");
                    int i = 0;
                    for(String note : csv.getLine(key)) {
                        i++;
                        System.out.println(id + "(" + i + ") " + note);
                    }
                } else if(command.equals(InputHandlerCL.Command.NEWLINE.prompt)) {
                    csv.newLine(key);
                    System.out.println(id + "New line successfully created with key \"" + key + "\".");
                } else System.out.println(id + ii + help);

            } else {

                String command = commands.get(0);
                String key = commands.get(1);
                String secondary = commands.get(2);

                if(command.equals(InputHandlerCL.Command.ADDTOLINE.prompt)) {
                    if(csv.addToLine(key, secondary)) System.out.println(id + "Info added successfully to line \"" + key + "\".");
                    else System.out.println(id + ik);
                } else if(command.equals(InputHandlerCL.Command.DELETEFROMLINE.prompt)) {
                    int status = csv.deleteFromLine(key, Integer.parseInt(secondary));
                    if(status == 0) System.out.println(id + "Info deleted successfully from line \"" + key + "\".");
                    else if(status == -1) System.out.println(id + ik);
                    else if(status == -2) System.out.println(id + "Invalid index!");
                } else if(command.equals(InputHandlerCL.Command.CHANGEKEY.prompt)) {
                    if(csv.changeKey(key, secondary)) System.out.println(id + "Key of line \"" + key + "\" changed successfully to \"" + secondary + "\".");
                    else System.out.println(id + ik);
                } else if(command.equals(InputHandlerCL.Command.COPY.prompt)) {
                    if(csv.copy(key, secondary)) System.out.println(id + "Line \"" + key + "\" copied successfully to line at key \"" + secondary + "\".");
                    else System.out.println(id + ik);
                } else System.out.println(id + ii + help);
            }
        }
    }
}