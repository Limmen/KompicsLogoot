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
package se.kth.app.mngr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.croupier.util.NoView;
import se.kth.app.AppComp;
import se.sics.kompics.Channel;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.network.Network;
import se.sics.kompics.timer.Timer;
import se.sics.ktoolbox.croupier.CroupierPort;
import se.sics.ktoolbox.omngr.bootstrap.BootstrapClientComp;
import se.sics.ktoolbox.overlaymngr.OverlayMngrPort;
import se.sics.ktoolbox.overlaymngr.events.OMngrCroupier;
import se.sics.ktoolbox.util.identifiable.overlay.OverlayId;
import se.sics.ktoolbox.util.network.KAddress;
import se.sics.ktoolbox.util.overlays.view.OverlayViewUpdate;
import se.sics.ktoolbox.util.overlays.view.OverlayViewUpdatePort;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class AppMngrComp extends ComponentDefinition {

  private static final Logger LOG = LoggerFactory.getLogger(BootstrapClientComp.class);
  private String logPrefix = "";
  //*****************************CONNECTIONS**********************************
  Positive<OverlayMngrPort> omngrPort = requires(OverlayMngrPort.class);
  //***************************EXTERNAL_STATE*********************************
  private ExtPort extPorts;
  private KAddress selfAdr;
  private OverlayId croupierId;
  //***************************INTERNAL_STATE*********************************
  private Component appComp;
  //******************************AUX_STATE***********************************
  private OMngrCroupier.ConnectRequest pendingCroupierConnReq;
  //**************************************************************************

  public AppMngrComp(Init init) {
    selfAdr = init.selfAdr;
    logPrefix = "<nid:" + selfAdr.getId() + ">";
    LOG.info("{}initiating...", logPrefix);

    extPorts = init.extPorts;
    croupierId = init.croupierOId;

    subscribe(handleStart, control);
    subscribe(handleCroupierConnected, omngrPort);
  }

  Handler handleStart = new Handler<Start>() {
    @Override
    public void handle(Start event) {
      LOG.info("{}starting...", logPrefix);

      pendingCroupierConnReq = new OMngrCroupier.ConnectRequest(croupierId, false);
      trigger(pendingCroupierConnReq, omngrPort);
    }
  };

  Handler handleCroupierConnected = new Handler<OMngrCroupier.ConnectResponse>() {
    @Override
    public void handle(OMngrCroupier.ConnectResponse event) {
      LOG.info("{}overlays connected", logPrefix);
      connectAppComp();
      trigger(Start.event, appComp.control());
      trigger(new OverlayViewUpdate.Indication<>(croupierId, false, new NoView()), extPorts.viewUpdatePort);
    }
  };

  private void connectAppComp() {
    appComp = create(AppComp.class, new AppComp.Init(selfAdr, croupierId));
    connect(appComp.getNegative(Timer.class), extPorts.timerPort, Channel.TWO_WAY);
    connect(appComp.getNegative(Network.class), extPorts.networkPort, Channel.TWO_WAY);
    connect(appComp.getNegative(CroupierPort.class), extPorts.croupierPort, Channel.TWO_WAY);
  }

  public static class Init extends se.sics.kompics.Init<AppMngrComp> {

    public final ExtPort extPorts;
    public final KAddress selfAdr;
    public final OverlayId croupierOId;

    public Init(ExtPort extPorts, KAddress selfAdr, OverlayId croupierOId) {
      this.extPorts = extPorts;
      this.selfAdr = selfAdr;
      this.croupierOId = croupierOId;
    }
  }

  public static class ExtPort {

    public final Positive<Timer> timerPort;
    public final Positive<Network> networkPort;
    public final Positive<CroupierPort> croupierPort;
    public final Negative<OverlayViewUpdatePort> viewUpdatePort;

    public ExtPort(Positive<Timer> timerPort, Positive<Network> networkPort, Positive<CroupierPort> croupierPort,
      Negative<OverlayViewUpdatePort> viewUpdatePort) {
      this.networkPort = networkPort;
      this.timerPort = timerPort;
      this.croupierPort = croupierPort;
      this.viewUpdatePort = viewUpdatePort;
    }
  }
}
