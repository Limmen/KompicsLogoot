package se.kth.app.logoot;

import se.sics.ktoolbox.util.identifiable.Identifier;

import java.io.Serializable;
import java.util.List;

/**
 * @author Maxime Dufour on 2017-04-07.
 */
public class Id implements Serializable {

    private int i;
    private Identifier peerId;
    private Integer clock;

    public Id(int i, Identifier peerId, Integer clock) {
        this.i = i;
        this.peerId = peerId;
        this.clock = clock;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public Identifier getPeerId() {
        return peerId;
    }

    public void setPeerId(Identifier peerId) {
        this.peerId = peerId;
    }

    public Integer getClock() {
        return clock;
    }

    public void setClock(Integer clock) {
        this.clock = clock;
    }

    @Override
    public String toString() {
        return "Id{" +
                "i=" + i +
                ", peerId=" + peerId +
                ", clock=" + clock +
                '}';
    }

    public List<Id> generateLineId(Id p, Id q, int N, int boundary, Identifier site) {
        return null;
    }

    public Id constructId(int r, Id p, Id q, Identifier site) {
        return null;
    }
}
