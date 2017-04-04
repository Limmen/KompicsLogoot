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
package se.kth.sim.common.util;

import se.sics.kompics.simulator.network.identifier.Identifier;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class SimNodeIdentifier implements Identifier {
    public final int nodeId;
    
    public SimNodeIdentifier(int nodeId) {
        this.nodeId = nodeId;
    }
    
    @Override
    public int partition(int nrPartitions) {
        return nodeId % nrPartitions;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.nodeId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimNodeIdentifier other = (SimNodeIdentifier) obj;
        if (this.nodeId != other.nodeId) {
            return false;
        }
        return true;
    }
}
