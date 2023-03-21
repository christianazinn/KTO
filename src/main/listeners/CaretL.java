package listeners;

import util.*;

import javax.swing.event.*;

/**
 * {@code CaretL} is a class to implement a {@link CaretListener} to handle {@link JTextArea} events for {@link KTOJF}.
 * 
 * @author Christian Azinn
 * @version 0.1
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
        int end = pos + search.substring(pos).indexOf(" ");
        if(end == pos - 1) end = search.length(); 
        // find the start position
        int start = search.substring(0, pos).lastIndexOf(" ");

        // return the word
        return search.substring(start + 1, end);
    }


    /**
     * Handles CaretEvents.
     * @param e a {@link CaretEvent} sent by a {@link PrimaryTextPane}
     */
    public void caretUpdate(CaretEvent e) {
        cc.brBut.update(findWord(e.getDot()));
    }
}
