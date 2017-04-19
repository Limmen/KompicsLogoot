package se.kth.app.events;

import com.google.common.collect.ImmutableList;
import se.kth.app.logoot.Delete;
import se.kth.app.logoot.Insert;
import se.kth.app.logoot.Operation;
import se.sics.kompics.KompicsEvent;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author Maxime Dufour on 2017-04-07.
 */
public class Patch implements KompicsEvent, Serializable {

    private UUID uuid = UUID.randomUUID();
    private ImmutableList<Operation> ops;
    private int degree = 1;

    public Patch(List<Operation> ops) {
        this.ops = ImmutableList.copyOf(ops);
    }

    public Patch(Patch patch) {
        uuid = patch.uuid;
        degree = patch.degree;
        ops = ImmutableList.copyOf(patch.ops);
    }

    public List<Operation> getOps() {
        return ops;
    }

    public UUID getUuid() {
        return uuid;
    }


    public void setDegree(int degree) {
        this.degree = degree;
    }

    public  void incrDegree() {
        this.degree++;
    }

    public void decrDegree() {
        this.degree--;
    }

    @Override
    public String toString() {
        return "Patch{" +
                "uuid=" + uuid +
                ", degree=" + degree +
                ", ops=" + ops +
                '}';
    }

    public int getDegree() {
        return degree;
    }

    public Patch inverse() {
        LinkedList<Operation> operations = new LinkedList<>();

        for (Operation operation: ops) {
            switch (operation.getOperationType()){
                case INSERT:
                    operations.addFirst(new Delete(operation.getId(),operation.getContent()));
                    break;
                case DELETE:
                    operations.addFirst(new Insert(operation.getId(),operation.getContent()));
                    break;
            }
        }
        return new Patch(operations);
    }
}
