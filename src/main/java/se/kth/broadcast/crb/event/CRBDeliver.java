package se.kth.broadcast.crb.event;

import se.sics.kompics.KompicsEvent;
import se.sics.kompics.PatternExtractor;
import se.sics.ktoolbox.util.network.KAddress;

import java.io.Serializable;

/**
 * Created by 62maxime on 04/04/2017.
 */
public class CRBDeliver implements KompicsEvent, Serializable, PatternExtractor<Class, KompicsEvent> {

    private KAddress source;
    private KompicsEvent msg;

    public CRBDeliver(KAddress source, KompicsEvent msg) {
        this.source = source;
        this.msg = msg;
    }

    public KAddress getSource() {
        return source;
    }

    public KompicsEvent getMsg() {
        return msg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CRBDeliver that = (CRBDeliver) o;

        if (!source.equals(that.source)) return false;
        return msg.equals(that.msg);
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + msg.hashCode();
        return result;
    }

    @Override
    public Class extractPattern() {
        return msg.getClass();
    }

    @Override
    public KompicsEvent extractValue() {
        return msg;
    }
}
