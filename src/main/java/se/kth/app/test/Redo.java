package se.kth.app.test;

import se.kth.app.logoot.Position;
import se.sics.kompics.KompicsEvent;

import java.io.Serializable;

/**
 * @author Maxime Dufour on 2017-04-07.
 */
public class Redo implements KompicsEvent, Serializable {

    private Position patchId;

    public Redo(Position patchId) {
        this.patchId = patchId;
    }

    public Position getPatchId() {
        return patchId;
    }

    @Override
    public String toString() {
        return "Redo{" +
                "patchId=" + patchId +
                '}';
    }
}
