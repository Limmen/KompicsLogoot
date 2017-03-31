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
package se.kth.system;

import static se.kth.app.sim.ScenarioSetup.scenarioSeed;
import se.sics.ktoolbox.util.identifiable.BasicBuilders;
import se.sics.ktoolbox.util.identifiable.BasicIdentifiers;
import se.sics.ktoolbox.util.identifiable.IdentifierFactory;
import se.sics.ktoolbox.util.identifiable.IdentifierRegistry;
import se.sics.ktoolbox.util.identifiable.overlay.OverlayId;
import se.sics.ktoolbox.util.identifiable.overlay.OverlayIdFactory;
import se.sics.ktoolbox.util.identifiable.overlay.OverlayRegistry;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class SystemSetup {
   public static OverlayId setup() {
    BasicIdentifiers.registerDefaults2(scenarioSeed);
    OverlayRegistry.initiate(new OverlayId.BasicTypeFactory((byte) 0), new OverlayId.BasicTypeComparator());
    byte croupierOwnerId = 1;
    OverlayRegistry.registerPrefix(OverlayId.BasicTypes.CROUPIER.name(), croupierOwnerId);

    IdentifierFactory croupierBaseIdFactory = IdentifierRegistry.lookup(BasicIdentifiers.Values.OVERLAY.toString());
    OverlayIdFactory croupierIdFactory = new OverlayIdFactory(croupierBaseIdFactory, OverlayId.BasicTypes.CROUPIER,
      croupierOwnerId);
    return croupierIdFactory.id(new BasicBuilders.StringBuilder("0"));
  }
}
