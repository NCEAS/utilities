/**
 *  '$RCSfile: MetaDataProperty.java,v $'
 *  Copyright: 2003 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-07-11 20:37:34 $'
 * '$Revision: 1.3 $'
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

import java.util.Vector;

/**
 * Encapsulate the meta-information about SortedProperties so that this information 
 * can be used by applications for managing options in a usable way.  The metadata
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
public class MetaDataProperty extends MetaDataElement {
    private String key;
    private String label;
    private int groupId;
    private String fieldType;
    private Vector<String> fieldOptionNames;
    private Vector<String> fieldOptionValues;
    
    public MetaDataProperty() {}
    
    public MetaDataProperty(String key, String label, int groupId, int index, 
            String description, String helpFile) {
        this.key = key;
        this.label = label;
        this.groupId = groupId;
        this.index = index;
        this.description = description;
        this.helpFile = helpFile;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }
    

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * @return the group
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * @param group the group to set
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
        
    /**
     * @return the field type
     */
    public String getFieldType() {
        return fieldType;
    }

    /**
     * @param fieldType the field type to set.
     */
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
    
    /**
     * @return a vector of the field options
     */
    public Vector<String> getFieldOptionNames() {
        return fieldOptionNames;
    }

    /**
     * @param fieldType a vector of the field options to set.
     */
    public void setFieldOptionNames(Vector<String> fieldOptions) {
        this.fieldOptionNames = fieldOptions;
    }
    
    /**
     * @return a vector of the field options
     */
    public Vector<String> getFieldOptionValues() {
        return fieldOptionValues;
    }

    /**
     * @param fieldType a vector of the field options to set.
     */
    public void setFieldOptionValues(Vector<String> fieldOptionValues) {
        this.fieldOptionValues = fieldOptionValues;
    }
}
