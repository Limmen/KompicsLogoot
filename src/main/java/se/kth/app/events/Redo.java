package se.kth.app.events;

import se.kth.app.logoot.Position;
import se.sics.kompics.KompicsEvent;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Maxime Dufour on 2017-04-07.
 */
public class Redo implements KompicsEvent, Serializable {

    private UUID patchId;

    public Redo(UUID patchId) {
        this.patchId = patchId;
    }

    public UUID getPatchId() {
        return patchId;
    }

    @Override
    public String toString() {
        return "Redo{" +
                "patchId=" + patchId +
                '}';
    }
}
