package se.kth.app.test;

import com.google.common.base.Objects;
import se.sics.kompics.KompicsEvent;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Kim Hammar on 2017-04-04.
 */
public class Pang implements KompicsEvent, Serializable {
    public UUID id = UUID.randomUUID();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pang pang = (Pang) o;
        return Objects.equal(id, pang.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pang{" +
                "id=" + id +
                '}';
    }
}
