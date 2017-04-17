/*
 * 2016 Royal Institute of Technology (KTH)
 *
 * LSelector is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package se.kth.app;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.events.Pang;
import se.kth.app.events.Patch;
import se.kth.app.events.Redo;
import se.kth.app.events.Undo;
import se.kth.app.logoot.Document;
import se.kth.app.logoot.Insert;
import se.kth.app.logoot.LineId;
import se.kth.app.logoot.Operation;
import se.kth.broadcast.crb.event.CRBBroadcast;
import se.kth.broadcast.crb.event.CRBDeliver;
import se.kth.broadcast.crb.port.CausalOrderReliableBroadcast;
import se.sics.kompics.*;
import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.kompics.timer.Timeout;
import se.sics.kompics.timer.Timer;
import se.sics.ktoolbox.util.identifiable.Identifier;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.List;
import java.util.UUID;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class AppComp extends ComponentDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(AppComp.class);
    private String logPrefix = " ";

    //*******************************CONNECTIONS********************************
    Positive<Timer> timerPort = requires(Timer.class);
    Positive<CausalOrderReliableBroadcast> broadcastPort = requires(CausalOrderReliableBroadcast.class);
    //**************************************************************************
    private KAddress selfAdr;
    private UUID timeoutId;
    private final long delay = config().getValue("app.delay", Long.class);
    private Document document;
    private int clock = 0;

    public AppComp(Init init) {
        selfAdr = init.selfAdr;
        logPrefix = "<nid:" + selfAdr.getId() + ">";
        LOG.info("{}initiating...", logPrefix);
        document = new Document(selfAdr.getId());

        subscribe(handleStart, control);
        subscribe(redoHandler, broadcastPort);
        subscribe(undoHandler, broadcastPort);
        subscribe(pangHandler, broadcastPort);
        subscribe(patchHandler, broadcastPort);
        subscribe(handleTimeout, timerPort);
    }

    protected Handler handleStart = new Handler<Start>() {
        @Override
        public void handle(Start event) {
            LOG.info("{}starting...", logPrefix);
            SchedulePeriodicTimeout spt = new SchedulePeriodicTimeout(delay, delay);
            spt.setTimeoutEvent(new AppCompTimeout(spt));
            trigger(spt, timerPort);
            timeoutId = spt.getTimeoutEvent().getTimeoutId();
        }
    };

    protected final Handler<AppCompTimeout> handleTimeout = new Handler<AppCompTimeout>() {
        @Override
        public void handle(AppCompTimeout event) {
            LOG.info("Timeout, triggering broadcast");
            clock++;
            LineId first = document.getIdTable().get(0);
            LineId last = document.getIdTable().get(document.getIdTable().size()-1);
            List<LineId> ids = document.generateLineId(first, last, 1, 10, clock);
            CRBBroadcast broadcast = new CRBBroadcast(new Patch(Lists.newArrayList(new Insert(ids.get(0), "Node:" + selfAdr.getId() + ": test " + "\n"))));
            trigger(broadcast, broadcastPort);
        }
    };

    protected  ClassMatchedHandler<Redo, CRBDeliver> redoHandler = new ClassMatchedHandler<Redo, CRBDeliver>() {
        @Override
        public void handle(Redo redo, CRBDeliver crbDeliver) {
            Patch patch = document.getPatch(redo.getPatchId());
            if (patch == null) {
                LOG.warn("The patch was not previously seen, ERROR");
                return;
            }
            patch.incrDegree();
            if (patch.getDegree() == 1) {
                document.execute(patch);
            }
        }
    };

    protected  ClassMatchedHandler<Undo, CRBDeliver> undoHandler = new ClassMatchedHandler<Undo, CRBDeliver>() {
        @Override
        public void handle(Undo undo, CRBDeliver crbDeliver) {
            Patch patch = document.getPatch(undo.getPatchId());
            if (patch == null) {
                LOG.warn("The patch was not previously seen, ERROR");
                return;
            }
            patch.decrDegree();
            if (patch.getDegree() == 0) {
                document.execute(patch.inverse());
            }
        }
    };

    protected ClassMatchedHandler<Patch, CRBDeliver> patchHandler = new ClassMatchedHandler<Patch, CRBDeliver>() {
        @Override
        public void handle(Patch patch, CRBDeliver crbDeliver) {
            for (Operation op: patch.getOps()) {
                int clock = op.getId().getPositions().get(op.getId().getPositions().size()-1).getClock();
                mergeClock(clock);
            }
            clock++;
            document.execute(patch);
            Patch newPatch = document.insertPatchHB(patch);
            newPatch.setDegree(1);
            LOG.info(document.toString());
        }
    };

    protected ClassMatchedHandler<Pang, CRBDeliver> pangHandler = new ClassMatchedHandler<Pang, CRBDeliver>() {
        @Override
        public void handle(Pang pang, CRBDeliver crbDeliver) {

        }
    };

    private void mergeClock(int c){
        clock = Math.max(clock, c);
    }

    public static class Init extends se.sics.kompics.Init<AppComp> {

        public final KAddress selfAdr;
        public final Identifier gradientOId;

        public Init(KAddress selfAdr, Identifier gradientOId) {
            this.selfAdr = selfAdr;
            this.gradientOId = gradientOId;
        }
    }

    public class AppCompTimeout extends Timeout {

        public AppCompTimeout(SchedulePeriodicTimeout request) {
            super(request);
        }
    }
}
