/**
 *  '$RCSfile: LSID.java,v $'
 *  Copyright: 2011 Regents of the University of California 
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-07-07 04:27:27 $'
 * '$Revision: 1.2 $'
 *
 * Permission is hereby granted, without written agreement and without
 * license or royalty fees, to use, copy, modify, and distribute this
 * software and its documentation for any purpose, provided that the above
 * copyright notice and the following two paragraphs appear in all copies
 * of this software.
 *
 * IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
 * FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
 * ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
 * THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
 * PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
 * CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
 * ENHANCEMENTS, OR MODIFICATIONS.
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
