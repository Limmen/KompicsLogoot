package se.kth.app.logoot;

import java.io.Serializable;

/**
 * DeleteOperation
 *
 * @author Maxime Dufour on 2017-04-07.
 */
public class Delete implements Operation, Serializable {

    private LineId id;
    private String content;

    public Delete(LineId id, String content) {
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
    public LineId getId() {
        return  id;
    }

    @Override
    public String getContent() {
        return content;
    }
}
