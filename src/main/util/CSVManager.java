package util;

import java.io.*;
import java.util.*;

/**
 * {@code CSVManager} is a class containing methods to handle the comma-separated value files used to store KTO data in the long term.
 * 
 * @author Christian Azinn
 * @version 1.5
 * @since 0.0.1
 */
public class CSVManager {

    // instance variables
    private TreeMap<String, ArrayList<String>> activeCsv;
    private String activeFilename;
    private String activeDirectory;
    private boolean isSaved;


    /**
     * Constructor for a CSVManager object. 
     * Each CSVManager can hold and interact with a single CSV file (in {@code TreeMap<String, ArrayList<String>>} form) at a time.
     */
    public CSVManager(String activeDirectory) {

        // comparator for sorting
        Comparator<String> c = new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.toLowerCase().compareTo(o2.toLowerCase());
            }
        };

        // set instance variables
        this.activeCsv = new TreeMap<String, ArrayList<String>>(c);
        this.activeFilename = "";
        this.activeDirectory = activeDirectory;
        this.isSaved = false;
    }

    // REMINDER THAT FILE DIRECTORIES ARE RELATIVE TO KTOJF.java NOT CSVManager.java

    /**
     * Finds the first occurrence of a {@code String search} in a {@code String line}.
     * @param line the {@code String} to be searched
     * @param search the {@code String} to search for
     * @return the index of {@code search}
     */
    public static int findNotBackslashed(String line, String search) {
        int relativePos = 0;
        int idx = 0;
        while(true) {
            idx = line.indexOf(search);
            relativePos += idx;
            if(idx <= 0 || line.charAt(idx - 1) != '\\') break;
            line = line.substring(idx + 1);
            relativePos++;
        }
        return relativePos;
    }

    /**
     * Reads a comma-separated value file with the specified filename into a {@code TreeMap<String, ArrayList<String>}.
     * I can't be bothered to set up Maven to use OpenCSV, so I wrote my own methods.
     * @param filename the name of the file to be read (sans file extension)
     * @return whether or not the file read was successful
     * @throws FileNotFoundException if filename is invalid
     * @throws IOException if something else goes wrong
     */
    public boolean open(String filename) {
        try {
            // initialize filereader and reset csv treemap
            BufferedReader r = new BufferedReader(new FileReader(activeDirectory + filename));
            activeCsv.clear();
            activeFilename = filename;

            // add each line
            while(r.ready()) {
                // get line and set up arraylist
                String line = r.readLine();
                ArrayList<String> values = new ArrayList<String>();

                // start separating by commas - first value is the key
                int idx = findNotBackslashed(line, ",");
                
                String key = line.substring(0, idx);
                line = line.substring(idx + 1);
                idx = findNotBackslashed(line, ",");

                // keep separating by commas - each later value is a value
                while(idx != -1) {
                    values.add(line.substring(0, idx));
                    line = line.substring(idx + 1);
                    idx = findNotBackslashed(line, ",");
                }

                // last element has no comma after it
                if(line.length() != 0) values.add(line);

                // add to treemap
                activeCsv.put(key, values);
            }
            // flush and return
            r.close();
            this.isSaved = true;
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
            PrintWriter pw = new PrintWriter(new FileWriter(activeDirectory + activeFilename));

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
            this.isSaved = true;
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
            File file = new File(activeDirectory + filename);
            file.createNewFile();
            activeFilename = filename;
            this.isSaved = true;
            return true;
        } catch(Exception e) { return false; }
    }


    /**
     * Clears all information Strings attached to a branch in the {@code TreeMap<String, ArrayList<String>>} given its {@code key}.
     * @param key the key of the branch to be cleared
     * @return whether or not the clear was successful
     */
    public boolean clearBranch(String key) {
        try {
            activeCsv.get(key).clear();
            this.isSaved = false;
            return true;
        } catch(Exception e) { return false; }
    }


    /**
     * Deletes a String of information from a branch in the {@code TreeMap<String, ArrayList<String>>} given its {@code idx} in the {@code ArrayList<String>} of information.
     * @param key the key of the branch containing the String of information
     * @param idx the index of the String of information to be deleted
     * @return whether or not the deletion was successful
     */
    public int deleteFromBranch(String key, int idx) {
        idx -= 1;
        try {
            ArrayList<String> keys = activeCsv.get(key);
            if(idx >= keys.size()) return -2;
            keys.remove(idx);
            this.isSaved = false;
            return 0;
        } catch(Exception e) { return -1; }
    }


    /**
     * Creates a new branch in the {@code TreeMap<String, ArrayList<String>>} given its {@code key}.
     * @param key the key of the line to be created
     */
    public void newBranch(String key) {
        activeCsv.put(key, new ArrayList<String>());
        this.isSaved = false;
    }


    /**
     * Deletes an entire branch from the {@code TreeMap<String, ArrayList<String>>} given its {@code key}.
     * @param key the key of the branch to be deleted
     * @return whether or not the deletion was successful
     */
    public boolean deleteBranch(String key) {
        try {
            activeCsv.remove(key);
            this.isSaved = false;
            return true;
        } catch(Exception e) { return false; }
    }


    /**
     * Retrieves an {@code ArrayList<String>} from the {@code TreeMap<String, ArrayList<String>>} given its {@code key}. 
     * Remember that this is PBR!
     * @param key the key of the {@code ArrayList<String>} to be retrieved
     * @return an {@code ArrayList<String>} corresponding to the {@code key}, or an empty {@code ArrayList<String>} if an error is encountered
     */
    public ArrayList<String> getBranch(String key) {
        try {
            if(activeCsv.get(key) != null) return activeCsv.get(key);
            else {
                newBranch(key);
                return activeCsv.get(key);
            }
        } catch(Exception e) { return new ArrayList<String>(); }
    }


    /**
     * Retrieves the top-level branch of the {@code TreeMap<String, ArrayList<String>>}.
     * @return the top level branch of the {@code TreeMap<String, ArrayList<String>>}
     */
    public ArrayList<String> getTopLevelBranch() {
        ArrayList<String> keys = new ArrayList<String>(activeCsv.keySet().size());
        for(String key : activeCsv.keySet()) keys.add("@" + key);
        return keys;
    }


    /**
     * Provides a {@code String} representation of this {@code CSVManager}, which is just the active CSV's {@code String} representation.
     */
    public String toString() {
        return activeCsv.toString();
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
            this.isSaved = false;
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
            this.isSaved = false;
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
     * Setter method for the save status of the {@code TreeMap<String, ArrayList<String>>}.
     * @param saved the save status of the {@code TreeMap<String, ArrayList<String>>}
     */
    public void setSaved(boolean saved) {
        this.isSaved = saved;
    }


    /**
     * Getter method for the save status of the {@code TreeMap<String, ArrayList<String>>}.
     * @return the save status of the {@code TreeMap<String, ArrayList<String>>}
     */
    public boolean getSaved() {
        return this.isSaved;
    }

    
    /**
     * Setter method for the active directory path.
     * @param activeDirectory the active directory path
     */
    public void setDirectory(String activeDirectory) {
        this.activeDirectory = activeDirectory;
    }


    /**
     * Getter method for the active directory path.
     * @return the active directory path
     */
    public String getDirectory() {
        return activeDirectory;
    }
}