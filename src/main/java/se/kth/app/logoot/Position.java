package se.kth.app.logoot;

import se.sics.ktoolbox.util.identifiable.Identifier;

import java.io.Serializable;

/**
 * PositionIdentifier (digit, site, clock)
 *
 * @author Maxime Dufour on 2017-04-07.
 */
public class Position implements Serializable, Comparable {

    private final int digit;
    private final Identifier siteId;
    private final Integer clock;

    public Position(int digit, Identifier siteId, Integer clock) {
        this.digit = digit;
        this.siteId = siteId;
        this.clock = clock;
    }

    public int getDigit() {
        return digit;
    }

    public Identifier getSiteId() {
        return siteId;
    }

    public Integer getClock() {
        return clock;
    }

    @Override
    public String toString() {
        return "Position{" +
                "digit=" + digit +
                ", siteId=" + siteId +
                ", clock=" + clock +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        Position q = (Position) o;
        if(lessThan(q))
            return -1;
        if(q.lessThan(this))
            return 1;
        return 0;
    }

    public boolean lessThan(Position q){
        if(digit < q.getDigit())
            return true;
        if(digit == q.getDigit() && q.getSiteId() != null && siteId.compareTo(q.getSiteId()) == -1)
            return true;
        if(digit == q.getDigit() && q.getSiteId() != null && siteId.compareTo(q.getSiteId()) == 0 && q.getClock() != null && clock < q.getClock())
            return true;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        if(this.compareTo(position) == 0)
            return true;
        else return false;
    }
}
