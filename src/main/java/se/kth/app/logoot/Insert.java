package se.kth.app.logoot;

import java.io.Serializable;

/**
 * @author Maxime Dufour on 2017-04-07.
 */
public class Insert implements Operation, Serializable {

    private Position id;
    private String content;

    public Insert(Position id, String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Insert{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.INSERT;
    }

    @Override
    public Position getId() {
        return null;
    }

    @Override
    public String getContent() {
        return null;
    }
}
