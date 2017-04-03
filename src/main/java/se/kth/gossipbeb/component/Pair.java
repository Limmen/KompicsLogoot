package se.kth.gossipbeb.component;

/**
 * Created by 62maxime on 03/04/2017.
 */
public class Pair<T, T1> {
    T p1;
    T1 p2;

    public Pair(T p1, T1 p2) {
        this.p1 = p1;
        this.p2 = p2;
    }
}