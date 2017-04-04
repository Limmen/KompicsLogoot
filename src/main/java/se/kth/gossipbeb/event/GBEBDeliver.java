package se.kth.gossipbeb.event;

import se.sics.kompics.KompicsEvent;
import se.sics.kompics.PatternExtractor;
import se.sics.ktoolbox.util.network.KAddress;

import java.io.Serializable;

/**
 * Created by 62maxime on 03/04/2017.
 */
public class GBEBDeliver implements KompicsEvent, Serializable, PatternExtractor<Class, KompicsEvent> {

    private KAddress source;
    private KompicsEvent payload;

    public GBEBDeliver(KAddress source, KompicsEvent payload) {
        this.source = source;
        this.payload = payload;
    }

    public KompicsEvent getPayload() {
        return payload;
    }

    public KAddress getSource() {
        return source;
    }

    @Override
    public Class extractPattern() {
        return payload.getClass();
    }

    @Override
    public KompicsEvent extractValue() {
        return payload;
    }
}
