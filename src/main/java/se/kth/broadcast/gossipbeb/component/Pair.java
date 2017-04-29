package se.kth.broadcast.gossipbeb.component;

import com.google.common.base.Objects;

/**
 * PairContainer class.
 *
 * Created by 62maxime on 03/04/2017.
 */
public class Pair<T, T1> {
    public T p1;
    public T1 p2;

    public Pair(T p1, T1 p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equal(p1, pair.p1) &&
                Objects.equal(p2, pair.p2);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(p1, p2);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "p1=" + p1 +
                ", p2=" + p2 +
                '}';
    }
}