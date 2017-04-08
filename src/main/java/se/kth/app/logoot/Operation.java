package se.kth.app.logoot;

/**
 * @author Maxime Dufour on 2017-04-07.
 */
public interface Operation {

    OperationType getOperationType();

    Position getId();

    String getContent();
}
