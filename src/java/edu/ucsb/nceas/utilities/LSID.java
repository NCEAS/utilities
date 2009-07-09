/**
 *  '$RCSfile: LSID.java,v $'
 *  Copyright: 2009 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-07-07 04:27:27 $'
 * '$Revision: 1.2 $'
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package edu.ucsb.nceas.utilities;

/**
 * Encapsulate information about an LSID
 * 
 * @author Michael Daigle
 */

public class LSID {
    private String _authority = null;
    private String _namespace = null;
    private String _objectId = null;
    private Long _version = null;
    
    /**
     * @return the authority
     */
    public String getAuthority() {
        return _authority;
    }

    /**
     * @param authority the authority to set
     */
    public void setAuthority(String authority) {
        this._authority = authority;
    }
    
    /**
     * @return the namespace
     */
    public String getNamespace() {
        return _namespace;
    }

    /**
     * @param namespace the namespace to set
     */
    public void setNamespace(String namespace) {
        this._namespace = namespace;
    }
    
    /**
     * @return the object ID
     */
    public String getObjectId() {
        return _objectId;
    }

    /**
     * @param objectId the object ID to set
     */
    public void setObjectId(String objectId) {
        this._objectId = objectId;
    }

    /**
     * @return the version
     */
    public Long getVersion() {
        return _version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Long version) {
        this._version = version;
    }
}
