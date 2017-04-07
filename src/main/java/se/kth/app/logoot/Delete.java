package se.kth.app.logoot;

import java.io.Serializable;

/**
 * @author Maxime Dufour on 2017-04-07.
 */
public class Delete implements Operation, Serializable {

    private Id id;
    private String content;

    public Delete(Id id, String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Delete{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.DELETE;
    }

    @Override
    public Id getId() {
        return null;
    }

    @Override
    public String getContent() {
        return null;
    }
}
