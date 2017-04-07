package se.kth.app.test;

import se.kth.app.logoot.Id;
import se.sics.kompics.KompicsEvent;

import java.io.Serializable;

/**
 * @author Maxime Dufour on 2017-04-07.
 */
public class Undo implements KompicsEvent, Serializable {

    private Id patchId;

    public Undo(Id patchId) {
        this.patchId = patchId;
    }

    public Id getPatchId() {
        return patchId;
    }

    @Override
    public String toString() {
        return "Undo{" +
                "patchId=" + patchId +
                '}';
    }
}
