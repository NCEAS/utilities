/**
 *  '$RCSfile: MetaDataGroup.java,v $'
 *  Copyright: 2003 Regents of the University of California and the
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
public class MetaDataGroup extends MetaDataElement{
    private String name = null;
    private String comment = null;
  
    public MetaDataGroup() {}
    
    public MetaDataGroup(String name, String comment, int index, String description, String helpFile) {
        this.name = name;
        this.comment = comment;
        this.index = index;
        this.description = description;
        this.helpFile = helpFile;
    }
    
    /**
     * @return the group
     */
    public String getName() {
        return name;
    }

    /**
     * @param group the group to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the group
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param group the group to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    } 

}
