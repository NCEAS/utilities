/**
 *  '$RCSfile: PropertiesMetaData.java,v $'
 *  Copyright: 2008 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *
 *   '$Author: daigle $'
 *     '$Date: 2009-01-20 18:16:29 $'
 * '$Revision: 1.8 $'
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

import java.io.File;
import java.io.IOException;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.transform.TransformerException;

/**
 * Encapsulate the meta-informaiton about Options so that this information can
 * be used by applications for managing options in a usable way.  The metadata
 * managed for each option includes a human-readable label, a description of
 * the option, an index into the order of the option relative to others in its
 * group, and a category (group) for the option. This information is stored in a
 * file which can be accessed using the load() and store() methods.
 * 
 * @author Matt Jones
 */
public class PropertiesMetaData {
 
    /** 
     * A hash containing the metadata for each option, indexed by the key
     * used to store the option. Values are instances of the Metadata inner
     * class.
     */
    private TreeMap<String, MetaDataProperty> propertyMap;
    private TreeMap<Integer, MetaDataGroup> groupMap;
    
    /**
     * Construct a new instance of the optionsMap class.
     * @param xmlPropsFile the file object containing the metadata to be loaded (XML format)
     */
    public PropertiesMetaData(String propFileName) throws IOException, TransformerException {
    	File xmlPropsFile = new File(propFileName);
    	
    	propertyMap = new TreeMap<String, MetaDataProperty>();
        groupMap = new TreeMap<Integer, MetaDataGroup>();
        
        XMLProperties metadataProperties = new XMLProperties();
        metadataProperties.load(xmlPropsFile);
        
        // Load the metadata from the file
        load(metadataProperties);
    }
    
    /**
     * Insert or update a metadata entry for a given key.
     * 
     * @param key the key of the property to be updated
     * @param label a human-readable lable for the property
     * @param group the human-readable group for the property
     * @param index the within-group index in which the property should be displayed
     * @param description a human-readable description of the property
     */
    public synchronized void setMetadata(String key, String label, int groupId,
            int index, String description, String helpFile) {
    	MetaDataProperty metaData = (MetaDataProperty)propertyMap.get(key);
        if (metaData == null) {
        	metaData = new MetaDataProperty(key, label, groupId, index, description, helpFile);
        } else {
        	metaData.setKey(key);
        	metaData.setLabel(label);
        	metaData.setGroupId(groupId);
        	metaData.setIndex(index);
        	metaData.setDescription(description);
        	metaData.setHelpFile(helpFile);
        }
        propertyMap.put(key, metaData);
    }

    /**
     * Get a Set of the groups that are common across all of the properties.
     * @return Set of the groups found for all properties
     */
    public synchronized MetaDataGroup getGroup(int groupId) {       
        return getGroups().get(new Integer(groupId));
    }
    
    /**
     * Get a Map of the groups that are common across all of the properties.
     * @return Set of the groups found for all properties
     */
    public synchronized Map<Integer, MetaDataGroup> getGroups() {       
        return groupMap;
    }
    
    /**
     * Get a Map of all properties metadata
     * 
     * @return a Map of properties metadata
     */
    public synchronized Map<String, MetaDataProperty> getProperties() {
    	return propertyMap;
    }
           
    /**
     * Get a Set of all keys in the properties
     * @return Set of all keys in the properties
     */
    public synchronized Set<String> getKeys() {        
        return propertyMap.keySet();
    }
    
    /**
	 * Get a Set of the keys for properties that are members of a given group.
	 * 
	 * @param target
	 *            the target group to be searched for matching keys
	 * @return Set of the keys found within the given group
	 */
	public synchronized SortedMap<Integer, MetaDataProperty> getPropertiesInGroup(
			int groupId) {
		TreeMap<Integer, MetaDataProperty> groupPropertyMap =
			new TreeMap<Integer, MetaDataProperty>();

		for (String key : propertyMap.keySet()) {
			MetaDataProperty metadata = propertyMap.get(key);
			if ((metadata != null) && (metadata.getGroupId() == groupId)) {
				groupPropertyMap.put(new Integer(metadata.getIndex()), metadata);
			}
		}
		return groupPropertyMap;
	}
    
    /**
	 * Read the options from an XMLProperties object and store in memory.
	 * 
	 * @param metadataProperties
	 *            an XMLProperties object holding the metadata information.
	 */
    public synchronized void load(XMLProperties metadataProperties) throws TransformerException{
        // populate the group information  	
        String[] groupArray = 
        	metadataProperties.getProperty("/metadataConfig/group");
        for (int i = 1; i <= groupArray.length; i++ ) {
        	String xPathPrefix = "/metadataConfig/group[" + i + "]";
        	String[] indexArray = metadataProperties.getProperty(xPathPrefix + "/index");
        	String[] nameArray = metadataProperties.getProperty(xPathPrefix + "/name");
        	String[] commentArray = metadataProperties.getProperty(xPathPrefix + "/comment");
        	String[] descriptionArray = metadataProperties.getProperty(xPathPrefix + "/description");
        	String[] helpFileArray = metadataProperties.getProperty(xPathPrefix + "/helpFile");
            if (indexArray == null || nameArray == null) {
            	throw new TransformerException("Could not process a metadata group properties +" +
            			"record. One of the following values is null: name or index");
            }
            
            Integer intIndex;
            try {
        	    intIndex = Integer.parseInt(indexArray[0]);
        	} catch (NumberFormatException nfe) {
        		throw new TransformerException("Could not process a metadata properties record. " +
        				"index was not a valid integer for group: " + nameArray[0]);
        	}
        	MetaDataGroup group = new MetaDataGroup();
        	group.setIndex(intIndex);
        	group.setName(nameArray[0]);
        	if (commentArray != null) {
        		group.setComment(commentArray[0]);
        	}
        	if (descriptionArray != null) {
        		group.setDescription(descriptionArray[0]);
        	}
        	if (helpFileArray != null) {
        		group.setHelpFile(helpFileArray[0]);
        	}
        	
        	groupMap.put(intIndex, group);
        }
    	
        // populate the property information 
    	String[] configArray = 
        	metadataProperties.getProperty("/metadataConfig/config");
    	int configArrayLength = 0;
    	if (configArray != null) {
    		configArrayLength = configArray.length;
    	}
        for (int i = 1; i <= configArrayLength; i++) {
			String xPathPrefix = "/metadataConfig/config[" + i + "]";
			String[] keyArray = 
				metadataProperties.getProperty(xPathPrefix + "/key");
			String[] labelArray = 
				metadataProperties.getProperty(xPathPrefix + "/label");
			String[] groupIdArray = 
				metadataProperties.getProperty(xPathPrefix + "/group");
			String[] indexArray = 
				metadataProperties.getProperty(xPathPrefix + "/index");
			String[] descriptionArray = 
				metadataProperties.getProperty(xPathPrefix + "/description");
			String[] helpFileArray = 
				metadataProperties.getProperty(xPathPrefix + "/helpFile");
			String[] fieldTypeArray = 
				metadataProperties.getProperty(xPathPrefix + "/fieldType");
			String[] isRequired = 
				metadataProperties.getProperty(xPathPrefix + "/required");

			if (keyArray == null || labelArray == null || groupIdArray == null
					|| indexArray == null) {
				throw new TransformerException("Could not process a metadata properties +"
						+ "record. One of the following values is null: key, label, group or index");
			}

			Integer intIndex;
			Integer intGroupId;
			try {
				intIndex = Integer.parseInt(indexArray[0]);
				intGroupId = Integer.parseInt(groupIdArray[0]);
			} catch (NumberFormatException nfe) {
				throw new TransformerException("Could not process a metadata properties record. "
						+ "index was not a valid integer for key: " + keyArray[0]);
			}

			MetaDataProperty metadata = new MetaDataProperty();
			metadata.setKey(keyArray[0]);
			metadata.setLabel(labelArray[0]);
			metadata.setGroupId(intGroupId);
			metadata.setIndex(intIndex);
			if (descriptionArray != null) {
				metadata.setDescription(descriptionArray[0]);
			}
			if (helpFileArray != null) {
				metadata.setHelpFile(helpFileArray[0]);
			}

			try {
				if (fieldTypeArray != null) {
					metadata.setFieldType(fieldTypeArray[0]);
					Vector<String> fieldOptionNames = new Vector<String>();
					Vector<String> fieldOptionValues = new Vector<String>();
					if (fieldTypeArray[0].equals("select")) {
						String[] optionArray = 
							metadataProperties.getProperty(xPathPrefix + "/option");
						for (int j = 1; j <= optionArray.length; j++) {
							String[] fieldOptionArray = 
								metadataProperties.getProperty(xPathPrefix + "/option[" + j + "]/name");
							String[] fieldValueArray = 
								metadataProperties.getProperty(xPathPrefix + "/option[" + j + "]/value");
							if (fieldOptionArray == null || fieldValueArray == null) {
								throw new TransformerException("Both name and value must be specified for "
										+ "metadata element: ");
							}
							fieldOptionNames.add(fieldOptionArray[0]);
							fieldOptionValues.add(fieldValueArray[0]);
						}
					}
					metadata.setFieldOptionNames(fieldOptionNames);
					metadata.setFieldOptionValues(fieldOptionValues);
				} else {
					metadata.setFieldType("text");
				}
			} catch (GeneralPropertyException gpe) {
				throw new TransformerException("Property error while processing key: " + keyArray[0]);
			}

			if (isRequired != null && isRequired[0].equals("true")) {
				metadata.setIsRequired(true);
			}
        	
            propertyMap.put(keyArray[0], metadata);
        }   	
    }
}
