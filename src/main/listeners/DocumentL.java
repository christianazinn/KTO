package listeners;

import util.*;

import javax.swing.event.*;

/**
 * {@code DocumentL} is a class to implement a {@link DocumentListener} to handle {@link JTextArea} events for {@link KTOJF}.
 * 
 * @author Christian Azinn
 * @version 0.1
 * @since 0.2.0
 */
public class DocumentL implements DocumentListener {

    private boolean autosaveOn;
    private boolean canListen;
    private boolean saveNextCall;
    private ComponentContainer cc;

    public DocumentL(ComponentContainer cc, boolean autosaveOn) {
        this.cc = cc;
        this.autosaveOn = autosaveOn;
        canListen = true;
    }

    /**
     * Setter method for autosave.
     * @param autosaveOn the new autosave status
     */
    public void setAutosave(boolean autosaveOn) {
        this.autosaveOn = autosaveOn;
    }

    /**
     * Setter method for listening.
     * @param canListen the new listen status
     */
    public void setListen(boolean canListen) {
        this.canListen = canListen;
    }

    /**
     * Fixes a communication problem with running {@link KTOJF#save()} from {@code DocumentL}.
     * @return whether to call {@link KTOJF#save()}
     */
    public boolean getSave() {
        boolean earlier = saveNextCall;
        saveNextCall = false;
        return earlier;
    }

    /**
     * Handles DocumentEvents.
     * @param e a {@link DocumentEvent} sent by a {@link PrimaryTextPane}
     */
    public void insertUpdate(DocumentEvent e) { if(canListen) {
        if(autosaveOn) saveNextCall = true;
        else cc.csv.setSaved(false);
    }}
    /**
     * Handles DocumentEvents.
     * @param e a {@link DocumentEvent} sent by a {@link PrimaryTextPane}
     */
    public void removeUpdate(DocumentEvent e) { if(canListen) {
        if(autosaveOn) saveNextCall = true;
        else cc.csv.setSaved(false);
    }}
    /**
     * Handles DocumentEvents.
     * @param e a {@link DocumentEvent} sent by a {@link PrimaryTextPane}
     */
    public void changedUpdate(DocumentEvent e) { if(canListen) {
        if(autosaveOn) saveNextCall = true;
        else cc.csv.setSaved(false);
    }}
}
