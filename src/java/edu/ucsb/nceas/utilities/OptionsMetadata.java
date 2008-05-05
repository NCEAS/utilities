/**
 *  '$RCSfile: OptionsMetadata.java,v $'
 *  Copyright: 2003 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-05-05 17:14:44 $'
 * '$Revision: 1.2.2.1 $'
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class OptionsMetadata {
    
    /**
     * Construct a new instance of the OptionsMetadata class.
     * @param reader a Reader containing the metadata to be loaded (csv format)
     */
    public OptionsMetadata(Reader reader) {
        optionsMetadata = new HashMap<String,Metadata>();
        
        // Load the metadata from the file
        load(reader);
    }
    
    /**
     * Construct a new instance of the OptionsMetadata class.
     * @param xmlPropsFile the file object containing the metadata to be loaded (XML format)
     */
    public OptionsMetadata(File xmlPropsFile) throws IOException, TransformerException {
        optionsMetadata = new HashMap<String,Metadata>();
        
        XMLProperties metadataProperties = new XMLProperties();
        metadataProperties.load(xmlPropsFile);
        
        // Load the metadata from the file
        //load(reader);
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
    public synchronized void setMetadata(String key, String label, String group,
            int index, String description) {
        Metadata md = (Metadata)optionsMetadata.get(key);
        if (md == null) {
            md = new Metadata(key, label, group, index, description);
        } else {
            md.setLabel(key);
            md.setLabel(label);
            md.setGroup(group);
            md.setIndex(index);
            md.setDescription(description);
        }
        optionsMetadata.put(key, md);
    }
    
    /**
     * Get the human-readable label for a given option based on its key.
     * @param key the key to look up
     * @return the label for this key
     */
    public synchronized String getOptionLabel(String key) {
        // TODO: error check to be sure md is not null
        Metadata md = (Metadata)optionsMetadata.get(key);
        return md.getLabel();
    }
    
    /**
     * Get the human-readable group for a given option based on its key.
     * @param key the key to look up
     * @return the group for this key
     */
    public synchronized String getOptionGroup(String key) {
        Metadata md = (Metadata)optionsMetadata.get(key);
        return md.getGroup();
    }
    
    /**
     * Get the ordering index for a given option based on its key.
     * @param key the key to look up
     * @return the index for this key
     */
    public synchronized int getOptionIndex(String key) {
        Metadata md = (Metadata)optionsMetadata.get(key);
        return md.getIndex();
    }
    
    /**
     * Get the human-readable description for a given option based on its key.
     * @param key the key to look up
     * @return the description for this key
     */
    public synchronized String getOptionDescription(String key) {
        Metadata md = (Metadata)optionsMetadata.get(key);
        return md.getDescription();
    }
    
    /**
     * Get the field type of this configuration value.
     * @param key the key to look up
     * @return the field type for this key
     */
    public synchronized String getOptionFieldType(String key) {
        Metadata md = (Metadata)optionsMetadata.get(key);
        return md.getFieldType();
    }
    
    /**
     * Get the field type of this configuration value.
     * @param key the key to look up
     * @return a vector of options for the dropdown field
     */
    public synchronized Vector<String> getFieldOptions(String key) {
        Metadata md = (Metadata)optionsMetadata.get(key);
        return md.getFieldOptions();
    }
    
    /**
     * Get a Set of the groups that are common across all of the properties.
     * @return Set of the groups found for all properties
     */
    public synchronized Set<String> getGroups() {
        HashSet<String> groups = new HashSet<String>();
        Iterator<String> iter = optionsMetadata.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String)iter.next();
            String group = getOptionGroup(key);
            groups.add(group);
        }
        
        return groups;
    }
    
    /**
     * Get a Set of all keys in the properties
     * @return Set of all keys in the properties
     */
    public synchronized Set<String> getKeys() {        
        return optionsMetadata.keySet();
    }
    
    /**
     * Get a Set of the keys for properties that are members of a given group.
     * @param target the target group to be searched for matching keys
     * @return Set of the keys found within the given group
     */
    public synchronized SortedMap<Integer,String> getKeysInGroup(String target) {
        TreeMap<Integer,String> options = new TreeMap<Integer,String>();
        Iterator<String> iter = optionsMetadata.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            String group = getOptionGroup(key);
            int index = getOptionIndex(key);
            if (group.equals(target)) {
                options.put(new Integer(index), key);
            }
        }
        return options;
    }
    
    /**
     * Read the options from a text Reader in CSV format and store in memory.
     * @param reader a Reader providing access to the CSV data
     */
    public synchronized void load(Reader reader) {
        BufferedReader isr = new BufferedReader(reader);
        try {
            String line = "";
            while (line !=  null) {
                line = isr.readLine();
                if (line != null) {
                    parseLine(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Read the options from an XMLProperties object and store in memory.
     * @param metadataProperties an XMLProperties object holding the 
     * metadata information.
     */
    public synchronized void load(XMLProperties metadataProperties) throws TransformerException{
        String[] configArray = 
        	metadataProperties.getProperty("/metadataConfig/config");
        for (int i = 1; i <= configArray.length; i++ ) {
        	String xPathPrefix = "/metadataConfig/config[" + i + "]";
        	String[] keyArray = 
        		metadataProperties.getProperty(xPathPrefix + "/key");
        	String[] labelArray = 
        		metadataProperties.getProperty(xPathPrefix + "/label");
        	String[] groupArray = 
        		metadataProperties.getProperty(xPathPrefix + "/group");
        	String[] indexArray = 
        		metadataProperties.getProperty(xPathPrefix + "/index");
        	String[] descriptionArray = 
        		metadataProperties.getProperty(xPathPrefix + "/description");
        	String[] fieldTypeArray = 
        		metadataProperties.getProperty(xPathPrefix + "/fieldType");
                	       	
            if (keyArray == null || labelArray == null || 
            		groupArray == null || indexArray == null) {
            	throw new TransformerException("Could not process a metadata properties +" +
            			"record. One of the following values is null: key, label, group or index");
            }
            
            Integer intIndex;
            try {
        	    intIndex = Integer.parseInt(indexArray[0]);
        	} catch (NumberFormatException nfe) {
        		throw new TransformerException("Could not process a metadata properties record. " +
        				"index was not a valid integer for key: " + keyArray[0]);
        	}
            	
        	Metadata metadata = new Metadata();
        	metadata.setKey(keyArray[0]);
        	metadata.setLabel(labelArray[0]);
        	metadata.setGroup(groupArray[0]);
        	metadata.setIndex(intIndex);
        	metadata.setDescription(descriptionArray[0]);
        	
            if (fieldTypeArray != null) {
                metadata.setFieldType(fieldTypeArray[0]);
                Vector<String> fieldOptions = new Vector<String>();
                if (fieldTypeArray[0].equals("select")) {
                    String[] optionArray = 
                    	metadataProperties.getProperty(xPathPrefix + "/option");
                    for(int j = 1; j <= optionArray.length; j++ ) {
                    	String[] fieldOptionArray = 
                    	    metadataProperties.getProperty(xPathPrefix + "/option[" + j + "]/name");
                	    fieldOptions.add(fieldOptionArray[0]);    
                    }
                }
                metadata.setFieldOptions(fieldOptions);
            } else {
            	metadata.setFieldType("text");
            }
        	
        	optionsMetadata.put(keyArray[0], metadata);
        }
    	
//        BufferedReader isr = new BufferedReader(reader);
//        try {
//            String line = "";
//            while (line !=  null) {
//                line = isr.readLine();
//                if (line != null) {
//                    parseLine(line);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    
    /**
     * Write out a serialized version of the OptionsMetadata in CSV format.
     * @param writer the Writer to which the metadata should be written
     */
    public synchronized void store(Writer writer) {
         BufferedWriter osr = new BufferedWriter(writer);
         Iterator<String> iter = optionsMetadata.keySet().iterator();
         while (iter.hasNext()) {
             String key = iter.next();
             Metadata md = optionsMetadata.get(key);
             StringBuffer line = new StringBuffer();
             line.append("# ");
             line.append(md.getKey()+",");
             line.append(md.getLabel()+",");
             line.append(md.getGroup()+",");
             line.append(md.getIndex()+",");
             line.append(md.getDescription());
             try {
                 osr.write(line.toString());
                 osr.newLine();
                 osr.flush();
             } catch (IOException ioe) {
                 System.out.println(ioe.getMessage());
             }
         }
    }
    
    /**
     * Parse the components of a metadata line and store each component in an
     * instance of the Metadata class
     * 
     * @param line the line containing the metadata to be parsed
     * @return the Metadata object containing the metadata fields
     */
    private void parseLine(String line) {
        Pattern p = Pattern.compile("# (.*),(.*),(.*),(.*),(.*)");
        Matcher m = p.matcher(line);
        if (m.matches()) {
            String key = m.group(1);
            String label = m.group(2);
            String group = m.group(3);
            String index = m.group(4);
            int indexNum = new Integer(index).intValue();
            String description = m.group(5);
            setMetadata(key, label, group, indexNum, description);
        } else {
            System.out.println("No match. This is not a valid metadata line.");
        }
    }
    
    /** 
     * A hash containing the metadata for each option, indexed by the key
     * used to store the option. Values are instances of the Metadata inner
     * class.
     */
    private HashMap<String,Metadata> optionsMetadata;
    
    /**
     * A data structure to encapsulate the metadata about a property, including
     * accessor methods for all fields.
     */
    private class Metadata {
        private String key;
        private String label;
        private String group;
        private int index;
        private String description;
        private String fieldType;
        private Vector<String> fieldOptions;
        
        public Metadata() {}
        
        public Metadata(String key, String label, String group, int index, 
                String description) {
            this.key = key;
            this.label = label;
            this.group = group;
            this.index = index;
            this.description = description;
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
         * @return the group
         */
        public String getGroup() {
            return group;
        }

        /**
         * @param group the group to set
         */
        public void setGroup(String group) {
            this.group = group;
        }

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
        public Vector<String> getFieldOptions() {
            return fieldOptions;
        }

        /**
         * @param fieldType a vector of the field options to set.
         */
        public void setFieldOptions(Vector<String> fieldOptions) {
            this.fieldOptions = fieldOptions;
        }
    }
}
