package se.kth.app.logoot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Maxime Dufour on 2017-04-07.
 */
public class Document {

    private ArrayList<String> documentLines;
    private ArrayList<Id> idTable;
    private HashMap<Id, Operation> hb;
    private HashMap<Id, Integer> cemetery;

    public Document() {
        documentLines = new ArrayList<>();
        idTable = new ArrayList<>();
        hb = new HashMap<>();
        cemetery = new HashMap<>();
        idTable.add(new Id(0, null, null));
        idTable.add(new Id(Integer.MAX_VALUE - 1, null, null));
    }


    void execute(List<Operation> patch) {

    }

}
