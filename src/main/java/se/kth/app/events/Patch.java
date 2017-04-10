package se.kth.app.events;

import com.google.common.collect.ImmutableList;
import se.kth.app.logoot.Operation;
import se.sics.kompics.KompicsEvent;

import java.io.Serializable;
import java.util.List;

/**
 * @author Maxime Dufour on 2017-04-07.
 */
public class Patch implements KompicsEvent, Serializable {

    private ImmutableList<Operation> ops;

    public Patch(List<Operation> ops) {
        this.ops = ImmutableList.copyOf(ops);
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
