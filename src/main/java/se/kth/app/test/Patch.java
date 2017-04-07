package se.kth.app.test;

import se.kth.app.logoot.Operation;
import se.sics.kompics.KompicsEvent;

import java.io.Serializable;
import java.util.List;

/**
 * @author Maxime Dufour on 2017-04-07.
 */
public class Patch implements KompicsEvent, Serializable {

    private List<Operation> ops;

    public Patch(List<Operation> ops) {
        this.ops = ops;
    }

    public List<Operation> getOps() {
        return ops;
    }

    @Override
    public String toString() {
        return "Patch{" +
                "ops=" + ops +
                '}';
    }
}
