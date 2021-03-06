/**
 *  '$RCSfile: MetaDataProperty.java,v $'
 *  Copyright: 2003 Regents of the University of California 
 *
 *   '$Author: daigle $'
 *     '$Date: 2009-01-20 18:16:29 $'
 * '$Revision: 1.5 $'
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

import java.util.Vector;
import java.util.HashSet;

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

	public static final String TEXT_TYPE = "text";
	public static final String SELECT_TYPE = "select";
	public static final String PASSWORD_TYPE = "password";
	public static final String CHECKBOX_TYPE = "checkbox";
	public static final String RADIO_TYPE = "radio";
	public static final String HIDDEN_TYPE = "hidden";
	
	static final HashSet<String> fieldTypeSet = new HashSet<String>();
	static {
		fieldTypeSet.add(TEXT_TYPE);
		fieldTypeSet.add(SELECT_TYPE);
		fieldTypeSet.add(PASSWORD_TYPE);
		fieldTypeSet.add(CHECKBOX_TYPE);
		fieldTypeSet.add(RADIO_TYPE);
		fieldTypeSet.add(HIDDEN_TYPE);
	}
	private String key;
    private String label;
    private int groupId;
    private String fieldType = TEXT_TYPE;
    private Vector<String> fieldOptionNames;
    private Vector<String> fieldOptionValues;
    private boolean isRequired = false;
    
    
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
    public void setFieldType(String fieldType) throws GeneralPropertyException {
    	if (fieldType == null || !fieldTypeSet.contains(fieldType)) {
    		throw new GeneralPropertyException("MetaDataProperty.setFieldType - cannot set field type to: " + fieldType);
    	}
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
    
    /**
     * @return a boolean stating whether field is required
     */
    public boolean getIsRequired() {
        return isRequired;
    }

    /**
     * @param boolean stating whether field is required
     */
    public void setIsRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }
}
