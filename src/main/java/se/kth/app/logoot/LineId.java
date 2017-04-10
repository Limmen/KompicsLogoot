package se.kth.app.logoot;

import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.List;

/**
 * @author Kim Hammar on 2017-04-08.
 */
public class LineId implements Serializable, Comparable {

    private final ImmutableList<Position> positions;

    public LineId(List<Position> positions) {
        this.positions = ImmutableList.copyOf(positions);
    }

    public ImmutableList<Position> getPositions() {
        return positions;
    }

    @Override
    public int compareTo(Object o) {
        LineId q = (LineId) o;
        if(lessThan(q))
            return  -1;
        if (q.lessThan(this))
            return 1;
        return 0;
    }

    public boolean lessThan(LineId q){
        for (int j = 0; j < q.getPositions().size(); j++) {
            if(j < positions.size() && positions.get(j).compareTo(q.getPositions().get(j)) == -1)
                return true;
            if(j < positions.size() && positions.get(j).compareTo(q.getPositions().get(j)) == 1)
                return false;
            if(j >= positions.size())
                return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineId lineId = (LineId) o;
        if(this.compareTo(lineId) == 0)
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return "LineId{" +
                "positions=" + positions +
                '}';
    }
}
