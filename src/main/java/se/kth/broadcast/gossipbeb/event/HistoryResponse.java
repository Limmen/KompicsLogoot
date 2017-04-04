package se.kth.broadcast.gossipbeb.event;

import se.kth.broadcast.gossipbeb.component.Pair;
import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by 62maxime on 03/04/2017.
 */
public class HistoryResponse implements KompicsEvent, Serializable {

    private Set<Pair<KAddress, KompicsEvent>> history;

    public HistoryResponse(Set<Pair<KAddress, KompicsEvent>> history) {

        this.history = history;
    }

    public Set<Pair<KAddress, KompicsEvent>> getHistory() {
        return history;
    }
}
