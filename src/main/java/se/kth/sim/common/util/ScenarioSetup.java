package se.kth.sim.common.util;/*
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

import se.kth.system.SystemSetup;
import se.sics.ktoolbox.util.identifiable.BasicBuilders;
import se.sics.ktoolbox.util.identifiable.BasicIdentifiers;
import se.sics.ktoolbox.util.identifiable.Identifier;
import se.sics.ktoolbox.util.identifiable.overlay.OverlayId;
import se.sics.ktoolbox.util.network.KAddress;
import se.sics.ktoolbox.util.network.basic.BasicAddress;
import se.sics.ktoolbox.util.network.nat.NatAwareAddressImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Utility class used when creating simulation scenarios.
 *
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class ScenarioSetup {

    public static final long scenarioSeed = 1234;
    public static final int appPort = 12345;
    public static final KAddress bootstrapServer;
    public static final OverlayId croupierOId;

    static {
        croupierOId = SystemSetup.setup();
        Identifier bootstrapId = BasicIdentifiers.nodeId(new BasicBuilders.IntBuilder(0));
        try {
            bootstrapServer = NatAwareAddressImpl.open(new BasicAddress(InetAddress.getByName("193.0.0.1"), appPort,
                    bootstrapId));
        } catch (UnknownHostException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static KAddress getNodeAdr(String nodeIp, int baseNodeId) {
        try {
            Identifier nodeId = BasicIdentifiers.nodeId(new BasicBuilders.IntBuilder(baseNodeId));
            return NatAwareAddressImpl.open(new BasicAddress(InetAddress.getByName(nodeIp), appPort, nodeId));
        } catch (UnknownHostException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static long getNodeSeed(int nodeId) {
        return scenarioSeed + nodeId;
    }
}