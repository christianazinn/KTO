package util;

import java.io.*;
import java.util.*;

// Note to self: Add link capabilities using @ references
// For example: test,lorem,ipsum adds "lorem" and "ipsum" to key "test".
// linktest,@test adds the key "test" to "linktest" which will redirect to the above stack.

// Note to self: Add textboxes and textbox titles using []
// For example: test,lorem[This text was written in a textbox.],ipsum[This was too.] adds the respective text to the respective tags under "test".
// Brackets that are actually written in text must be backslash-escaped.

/**
 * {@code CSVManager} is a class containing methods to handle the comma-separated value files used to store KTO data in the long term.
 * 
 * @author Christian Azinn
 * @version 1.0
 * @since 0.0.1
 */
public class CSVManager {

    // instance variables
    private TreeMap<String, ArrayList<String>> activeCsv;
    private String activeFilename;
    private boolean isSaved;


    /**
     * Constructor for a CSVManager object. 
     * Each CSVManager can hold and interact with a single CSV file (in {@code TreeMap<String, ArrayList<String>>} form) at a time.
     */
    public CSVManager() {

        // comparator for sorting
        Comparator<String> c = new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.toLowerCase().compareTo(o2.toLowerCase());
            }
        };

        // set instance variables
        activeCsv = new TreeMap<String, ArrayList<String>>(c);
        activeFilename = "";
        isSaved = false;
    }


    /**
     * Reads a comma-separated value file with the specified filename into a {@code TreeMap<String, ArrayList<String>}.
     * I can't be bothered to set up Maven to use OpenCSV, so I wrote my own methods.
     * @param filename the name of the file to be read (sans file extension)
     * @return whether or not the file read was successful
     * @throws FileNotFoundException if filename is invalid
     * @throws IOException if something else goes wrong
     */
    public boolean open(String filename) throws FileNotFoundException, IOException {
        try {
            // initialize filereader and reset csv treemap
            BufferedReader r = new BufferedReader(new FileReader("../../csvs/" + filename + ".csv"));
            activeCsv.clear();
            activeFilename = filename;

            // add each line
            while(r.ready()) {
                // get line and set up arraylist
                String line = r.readLine();
                ArrayList<String> values = new ArrayList<String>();

                // start separating by commas - first value is the key
                int idx = line.indexOf(",");
                String key = line.substring(0, idx);
                line = line.substring(idx + 1);
                idx = line.indexOf(",");

                // keep separating by commas - each later value is a value
                while(idx != -1) {
                    values.add(line.substring(0, idx));
                    line = line.substring(idx + 1);
                    idx = line.indexOf(",");
                }

                // last element has no comma after it
                values.add(line);

                // add to treemap
                activeCsv.put(key, values);
            }
            // flush and return
            r.close();
            isSaved = true;
            return true;
        } catch(Exception e) { return false; }
    }


    /**
     * Saves the active {@code TreeMap<String, ArrayList<String>>} to the currently active file.
     * @return whether or not the file write was successful
     */
    public boolean save() {
        try {
            // initialize filewriter
            PrintWriter pw = new PrintWriter(new FileWriter("../../csvs/" + activeFilename + ".csv"));

            // iterate through each key in the active csv
            for(String key : activeCsv.keySet()) {
                // write the key and set up arraylist for value
                pw.print(key + ",");
                ArrayList<String> values = activeCsv.get(key);

                // iterate through using a regular for loop so commas can be controlled
                for(int i = 0; i < values.size(); i++) {
                    pw.print(values.get(i));
                    if(i != values.size() - 1) pw.print(",");
                }

                // can't forget the newline
                pw.println();
            }
            // flush and return
            pw.close();
            isSaved = true;
            return true;
        }  catch(Exception e) { return false; }
    }


    /**
     * Creates a new file with the specified filename, and sets it as active.
     * @param filename the name of the file to be created
     * @return whether or not the file creation was successful
     */
    public boolean create(String filename) {
        try {
            File file = new File("../../csvs/" + filename + ".csv");
            file.createNewFile();
            activeFilename = filename;
            isSaved = true;
            return true;
        } catch(Exception e) { return false; }
    }


    /**
     * Adds a String {@code add} to the line in the {@code TreeMap<String, ArrayList<String>>} given its {@code key}.
     * @param key the key of the line to add {@code add} to
     * @param add the String to be added to the line with key {@code key}
     * @return whether or not the add was successful
     */
    public boolean addToLine(String key, String add) {
        try {
            activeCsv.get(key).add(add);
            isSaved = false;
            return true;
        } catch(Exception e) { return false; }
    }


    /**
     * Clears all information Strings attached to a line in the {@code TreeMap<String, ArrayList<String>>} given its {@code key}.
     * @param key the key of the line to be cleared
     * @return whether or not the clear was successful
     */
    public boolean clearLine(String key) {
        try {
            activeCsv.get(key).clear();
            isSaved = false;
            return true;
        } catch(Exception e) { return false; }
    }


    /**
     * Deletes a String of information from a line in the {@code TreeMap<String, ArrayList<String>>} given its {@code idx} in the {@code ArrayList<String>} of information.
     * @param key the key of the line containing the String of information
     * @param idx the index of the String of information to be deleted
     * @return whether or not the deletion was successful
     */
    public int deleteFromLine(String key, int idx) {
        idx -= 1;
        try {
            ArrayList<String> keys = activeCsv.get(key);
            if(idx >= keys.size()) return -2;
            keys.remove(idx);
            isSaved = false;
            return 0;
        } catch(Exception e) { return -1; }
    }


    /**
     * Creates a new line in the {@code TreeMap<String, ArrayList<String>>} given its {@code key}.
     * @param key the key of the line to be created
     */
    public void newLine(String key) {
        activeCsv.put(key, new ArrayList<String>());
        isSaved = false;
    }


    /**
     * Deletes an entire line from the {@code TreeMap<String, ArrayList<String>>} given its {@code key}.
     * @param key the key of the line to be deleted
     * @return whether or not the deletion was successful
     */
    public boolean deleteLine(String key) {
        try {
            activeCsv.remove(key);
            isSaved = false;
            return true;
        } catch(Exception e) { return false; }
    }


    /**
     * Retrieves an {@code ArrayList<String>} from the {@code TreeMap<String, ArrayList<String>>} given its {@code key}.
     * @param key the key of the {@code ArrayList<String>} to be retrieved
     * @return an {@code ArrayList<String>} corresponding to the {@code key}, or an empty {@code ArrayList<String>} if an error is encountered
     */
    public ArrayList<String> getLine(String key) {
        try {
            if(activeCsv.get(key) != null) return activeCsv.get(key);
            else return new ArrayList<String>();
        } catch(Exception e) { return new ArrayList<String>(); }
    }


    /**
     * Changes the {@code key} of a line in the {@code TreeMap<String, ArrayList<String>>} to a {@code newKey}.
     * @param key the former key of the line
     * @param newKey the new key of the line
     * @return whether or not the change was successful
     */
    public boolean changeKey(String key, String newKey) {
        try {
            activeCsv.put(newKey, activeCsv.get(key));
            activeCsv.remove(key);
            isSaved = false;
            return true;
        } catch(Exception e) { return false; }
    }


    /**
     * Copies a line with a particular {@code key} to a new line with key {@code newKey}.
     * @param key the key of the line to be copied from
     * @param newKey the key of the line to be copied to
     * @return whether or not the copy was successful
     */
    public boolean copy(String key, String newKey) {
        try {
            activeCsv.put(newKey, activeCsv.get(key));
            isSaved = false;
            return true;
        } catch(Exception e) { return false; }
    }


    /**
     * Getter method for the name of the currently active file.
     * @return the name of the currently active file
     */
    public String getF() {
        return activeFilename;
    } 


    /**
     * Getter method for the {@code Set<String>} of {@code key}s in the {@code TreeMap<String, ArrayList<String>>}.
     * @return the key list of the {@code TreeMap<String, ArrayList<String>>}
     */
    public Set<String> getKeys() {
        return activeCsv.keySet();
    }


    /**
     * Getter method for the save status of the {@code TreeMap<String, ArrayList<String>>}.
     * @return the save status of the {@code TreeMap<String, ArrayList<String>>}
     */
    public boolean getSaved() {
        return isSaved;
    }
}