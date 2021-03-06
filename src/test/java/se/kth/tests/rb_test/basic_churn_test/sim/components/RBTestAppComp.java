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
package se.kth.tests.rb_test.basic_churn_test.sim.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.events.Pang;
import se.kth.broadcast.crb.event.CRBBroadcast;
import se.kth.broadcast.crb.event.CRBDeliver;
import se.kth.broadcast.crb.port.CausalOrderReliableBroadcast;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.simulator.util.GlobalView;
import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.kompics.timer.Timeout;
import se.sics.kompics.timer.Timer;
import se.sics.ktoolbox.util.identifiable.Identifier;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.HashSet;
import java.util.UUID;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class RBTestAppComp extends ComponentDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(RBTestAppComp.class);
    private String logPrefix = " ";

    //*******************************CONNECTIONS********************************
    Positive<Timer> timerPort = requires(Timer.class);
    Positive<CausalOrderReliableBroadcast> broadcastPort = requires(CausalOrderReliableBroadcast.class);
    //**************************************************************************
    private KAddress selfAdr;
    private UUID timeoutId;
    private final long delay = config().getValue("app.delay", Long.class);
    private int counter = 0;
    private int broadcastMax;


    public RBTestAppComp(Init init) {
        selfAdr = init.selfAdr;
        logPrefix = "<nid:" + selfAdr.getId() + ">";
        LOG.info("{}initiating...", logPrefix);

        subscribe(handleStart, control);
        subscribe(handleBroadCast, broadcastPort);
        subscribe(handleTimeout, timerPort);

        broadcastMax = init.broadcastCount;
        GlobalView gv = config().getValue("simulation.globalview", GlobalView.class);
        HashSet<CRBDeliver> delivered = new HashSet<>();
        gv.setValue(selfAdr.getId().toString(), delivered);
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
            if (counter < broadcastMax) {
                CRBBroadcast broadcast = new CRBBroadcast(new Pang());
                trigger(broadcast, broadcastPort);
                counter++;
            }
        }
    };

    protected Handler<CRBDeliver> handleBroadCast = new Handler<CRBDeliver>() {
        @Override
        public void handle(CRBDeliver crbDeliver) {
            GlobalView gv = config().getValue("simulation.globalview", GlobalView.class);
            HashSet<CRBDeliver> delivered = gv.getValue(selfAdr.getId().toString(), HashSet.class);
            delivered.add(crbDeliver);
            gv.setValue(selfAdr.getId().toString(), delivered);
        }
    };

    public static class Init extends se.sics.kompics.Init<RBTestAppComp> {

        public final KAddress selfAdr;
        public final Identifier gradientOId;
        public final int broadcastCount;

        public Init(KAddress selfAdr, Identifier gradientOId, int broadcastCount) {
            this.selfAdr = selfAdr;
            this.gradientOId = gradientOId;
            this.broadcastCount = broadcastCount;
        }
    }

    public class AppCompTimeout extends Timeout {

        public AppCompTimeout(SchedulePeriodicTimeout request) {
            super(request);
        }
    }
}
