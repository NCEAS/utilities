/**
 *  '$RCSfile: MetaDataElement.java,v $'
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
 * Encapsulate the meta-information about Options so that this information can
 * be used by applications for managing options in a usable way.  The metadata
 * managed for each option includes a human-readable label, a description of
 * the option, an index into the order of the option relative to others in its
 * group, and a category (group) for the option. This information is stored in a
 * file which can be accessed using the load() and store() methods.
 * 
 * @author Matt Jones
 */
/**
 * A data structure to encapsulate the metadata about a property, including
 * accessor methods for all fields.
 */
/**
 * A data structure to encapsulate the information about a group
 */
public class MetaDataElement {
    protected int index;
    protected String description = null;
    protected String helpFile = null;
    
    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * @return the help file name
     */
    public String getHelpFile() {
        return helpFile;
    }

    /**
     * @param description the help file location to set
     */
    public void setHelpFile(String helpFile) {
        this.helpFile = helpFile;
    }
}
