package listeners;

import util.*;

import javax.swing.event.*;

/**
 * {@code CaretL} is a class to implement a {@link CaretListener} to handle {@link JTextArea} events for {@link KTOJF}.
 * 
 * @author Christian Azinn
 * @version 0.2
 * @since 0.2.2
 */
public class CaretL implements CaretListener {

    private ComponentContainer cc;


    public CaretL(ComponentContainer cc) {
        this.cc = cc;
    }


    /**
     * Finds the word the caret is over.
     * @param pos the caret position
     * @return the word the caret is over
     */
    private String findWord(int pos) {
        // get the full text
        String search = cc.ptPane.getText();

        // find the end position
        int spIdx = search.substring(pos).indexOf(" ");
        int nlIdx = search.substring(pos).indexOf("\n");
        int end;
        if(nlIdx == -1 && spIdx == -1) end = search.length();
        else if(nlIdx == -1) end = spIdx + pos;
        else if(spIdx == -1) end = nlIdx + pos;
        else end = (nlIdx < spIdx) ? nlIdx + pos : spIdx + pos; // oh my god i love this thing
        // you know what you could also write that as?
        // int end = (nlIdx == -1) ? ((spIdx == -1) ? search.length() : spIdx + pos) : ((spIdx == -1) ? nlIdx + pos : ((nlIdx < spIdx) ? nlIdx + pos : spIdx + pos));
        // TERADYING this is so stupid i love it

        // find the start position
        spIdx = search.substring(0, pos).lastIndexOf(" ");
        nlIdx = search.substring(0, pos).lastIndexOf("\n");
        int start = (spIdx != -1) ? ((nlIdx > spIdx) ? nlIdx : spIdx) : nlIdx;

        // truncate to last @ only
        String word = search.substring(start + 1, end);
        if(word.contains("@")) word = word.substring(word.lastIndexOf("@"));

        // return the word
        return word;
    }


    /**
     * Handles CaretEvents.
     * @param e a {@link CaretEvent} sent by a {@link PrimaryTextPane}
     */
    public void caretUpdate(CaretEvent e) {
        cc.brBut.update(findWord(e.getDot()));
    }
}
