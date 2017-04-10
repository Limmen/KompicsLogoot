package se.kth.app.events;

import se.kth.app.logoot.Position;
import se.sics.kompics.KompicsEvent;

import java.io.Serializable;

/**
 * @author Maxime Dufour on 2017-04-07.
 */
public class Undo implements KompicsEvent, Serializable {

    private Position patchId;

    public Undo(Position patchId) {
        this.patchId = patchId;
    }

    public Position getPatchId() {
        return patchId;
    }

    @Override
    public String toString() {
        return "Undo{" +
                "patchId=" + patchId +
                '}';
    }
}
