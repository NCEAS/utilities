/**
 *  '$RCSfile: OptionsMetadata.java,v $'
 *  Copyright: 2003 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *
 *   '$Author: jones $'
 *     '$Date: 2006-11-23 08:14:37 $'
 * '$Revision: 1.1 $'
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
        optionsMetadata = new HashMap();
        
        // Load the metadata from the file
        load(reader);
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
     * Get a Set of the groups that are common across all of the properties.
     * @return Set of the groups found for all properties
     */
    public synchronized Set getGroups() {
        HashSet groups = new HashSet();
        Iterator iter = optionsMetadata.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String)iter.next();
            String group = getOptionGroup(key);
            groups.add(group);
        }
        
        return groups;
    }
    
    /**
     * Get a Set of the keys for properties that are members of a given group.
     * @param target the target group to be searche for matching keys
     * @return Set of the keys found within the given group
     */
    public synchronized SortedMap getKeysInGroup(String target) {
        TreeMap options = new TreeMap();
        Iterator iter = optionsMetadata.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String)iter.next();
            String group = getOptionGroup(key);
            int index = getOptionIndex(key);
            if (group.equals(target)) {
                options.put(index, key);
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
     * Write out a serialized version of the OptionsMetadata in CSV format.
     * @param writer the Writer to which the metadata should be written
     */
    public synchronized void store(Writer writer) {
         BufferedWriter osr = new BufferedWriter(writer);
         Iterator iter = optionsMetadata.keySet().iterator();
         while (iter.hasNext()) {
             String key = (String)iter.next();
             Metadata md = (Metadata)optionsMetadata.get(key);
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
    private HashMap optionsMetadata;
    
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
    }
}
