package se.kth.app.test;

import se.kth.app.logoot.Id;
import se.sics.kompics.KompicsEvent;

import java.io.Serializable;

/**
 * @author Maxime Dufour on 2017-04-07.
 */
public class Redo implements KompicsEvent, Serializable {

    private Id patchId;

    public Redo(Id patchId) {
        this.patchId = patchId;
    }

    public Id getPatchId() {
        return patchId;
    }

    @Override
    public String toString() {
        return "Redo{" +
                "patchId=" + patchId +
                '}';
    }
}