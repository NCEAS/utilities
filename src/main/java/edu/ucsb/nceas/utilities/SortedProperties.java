/**
 *  '$RCSfile: SortedProperties.java,v $'
 *  Copyright: 2008 Regents of the University of California 
 *
 *   '$Author: daigle $'
 *     '$Date: 2009-02-19 00:31:34 $'
 * '$Revision: 1.7 $'
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Vector;

/**
 * A class that reads options from a properties file.  Comments, spaces and order
 * are preserved.  
 */
public class SortedProperties 
{
	private String propertiesDirName = null;
	private String propertiesFileName = null;
	private LinkedHashMap<String, String> allLinesMap = null;
	private LinkedHashMap<String, String> propertiesMap = null;

	// Identifiers for non-property lines in file
	private static String IS_PROPERTY = "true";
	private static String IS_NOT_PROPERTY = "false";
	private static String SPACE = "space";
    private static String COMMENT = "#";
    private static String UNKNOWN = "???"; 
    private static String IS_CONTINUED = "continued";
    private static String IS_NOT_CONTINUED = "not-continued";
    
    // This is incremented and used with the non-property identifiers to
    // create unique keys for those lines.
    private int nonPropertyCount = 0;

    /**
     * Constructor.  Populates the file and directory names of the properties
     * file and creates the map which will hold the properties.  Loading of 
     * the file will need to be done explicitly.  This is because we may be
     * creating a new properties file.
     *
     * @param propertyFileName the name of file from which to read properties
     */
    public SortedProperties(String propFileName) {
		File propFile = new File(propFileName);
		propertiesFileName = propFileName;
		propertiesDirName = propFile.getParent();
		allLinesMap = new LinkedHashMap<String, String>();
		propertiesMap = new LinkedHashMap<String, String>();

	}
    
    /**
	 * Load the values from the properties file into the map which will hold the
	 * properties.
	 * 
	 * @return true if the file exists and is readable, false otherwise.
	 */
    public boolean load() throws IOException {		
    	
    	if (FileUtil.getFileStatus(propertiesFileName) < FileUtil.EXISTS_READABLE) {
    		return false;
    	}
    	
    	FileInputStream fin = null;    	
    	try {
    	    fin = new FileInputStream (propertiesFileName);

		    BufferedReader reader = 
			   new BufferedReader(new InputStreamReader(fin));
		    
		    // Read in file
		    String fileLine;
		    boolean processingPartialLine = false;
		    String partialLineKey = null;
		    String partialLineValue = "";
		    while ((fileLine = reader.readLine()) != null) {
			    String parsedLine[] = parseLine(fileLine);
			    allLinesMap.put(parsedLine[0], parsedLine[1]);
			    if (processingPartialLine) {
			    	if (parsedLine[3].equals(IS_CONTINUED)) {
			    		partialLineValue += parsedLine[1].substring(0, parsedLine[1].length() - 1).trim();
			    	} else {
			    		partialLineValue += parsedLine[1];
			    		propertiesMap.put(partialLineKey, partialLineValue);
			    		processingPartialLine = false;
					    partialLineKey = null;
					    partialLineValue = "";
			    	}
			    } else if (parsedLine[2].equals(IS_PROPERTY)) {
			    	if (parsedLine[3].equals(IS_CONTINUED)) {
			    		processingPartialLine = true;
			    		partialLineKey = parsedLine[0];
			    		partialLineValue = parsedLine[1].substring(0, parsedLine[1].length() - 1).trim();
			    	} else {
			    		propertiesMap.put(parsedLine[0], parsedLine[1]);
			    	}
			    }
		    }
		} finally {
		    // Close our input stream
		    fin.close();	
		}
		
		return true;
    }
    
    /**
     * Parse a single line from a properties file.  Preserve comments, blank
     * lines and unknown (un-parsable) values using unique keys that look like:
     *                     Key                        value
     * -- Comments:   #<unique number>       <the whole comment line>
     * -- Blank line space<unique number>               ""
     * -- Unknown    ???<unique number>      <the whole comment line>
     * 
     * @param rawLine a string holding the line read from a properties file.
     * @return a two element string array holding the key and value
     */
    private String[] parseLine(String rawLine) {
        
    	int equalIndex;
    	String line = rawLine.trim();
    	String[] parsedString = new String[4];  
    	parsedString[3] = IS_NOT_CONTINUED;
               
    	if (line.matches("^#.*")) {                // line is a comment
    		nonPropertyCount++;
			parsedString[0] = COMMENT + nonPropertyCount;
			parsedString[1] = line;
			parsedString[2] = IS_NOT_PROPERTY;
        } else if (line.matches("^$")) {          // line is blank
        	nonPropertyCount++;
        	parsedString[0] = SPACE + nonPropertyCount;
        	parsedString[1] = "";
        	parsedString[2] = IS_NOT_PROPERTY;
        } else if ((equalIndex = line.indexOf('=')) != -1) {   // there is an = in the line
        	if (equalIndex == 0 ) {                     // the line starts with = 
        		nonPropertyCount++;
        		parsedString[0] = UNKNOWN + nonPropertyCount;
            	parsedString[1] = line;
            	parsedString[2] = IS_NOT_PROPERTY;
        	} else if (equalIndex == line.length()) {    // the line ends with =
        		parsedString[0] = line.substring(0, equalIndex);
        		parsedString[1] = "";
            	parsedString[2] = IS_PROPERTY;
        	} else {                                    // there is a key and a value
        		parsedString[0] = line.substring(0, equalIndex);
        		parsedString[1] = line.substring(equalIndex + 1).trim();
        		parsedString[2] = IS_PROPERTY;
        	}     	
        } else {                                     // something else going on here
        	nonPropertyCount++;
        	parsedString[0] = UNKNOWN + nonPropertyCount;
        	parsedString[1] = line;            	
        	parsedString[2] = IS_NOT_PROPERTY;
        }
    	
   		if (line.endsWith("\\")) {
			parsedString[3] = IS_CONTINUED;
		} 
    	
    	return parsedString;
    }
    
    /**
     * Reset value for given property.  A GeneralPropertyException will
     * be thrown if the property name does not already exist.  This method 
     * will also call a store method to save the new values into properties file. 
     * 
     * @param key  the property name
     * @param value  the new property value
     */
    public synchronized void setProperty(String key, String value) throws GeneralPropertyException {
    	if (!propertiesMap.containsKey(key)) {
    		throw new PropertyNotFoundException("Property: " + key + " could not be updated to: " 
    				+ value + " because it does not already exist in properties.");
    	}
    	allLinesMap.put(key, value);
    	propertiesMap.put(key, value);
    	store();
    }
    
    /**
     * Reset value for given property.  A GeneralPropertyException will
     * be thrown if the property name does not already exist. The property will not be  
     * persisted to the properties file.  the store() method should be called to
     * do this.
     * 
     * @param key  the property name
     * @param value  the new property value
     */
    public synchronized void setPropertyNoPersist(String key, String value) throws PropertyNotFoundException{
    	if (!propertiesMap.containsKey(key)) {
    		throw new PropertyNotFoundException("Property: " + key + " could not be updated to: " 
    				+ value + " because it does not already exist in properties.");
    	}
    	allLinesMap.put(key, value);
    	propertiesMap.put(key, value);
    } 
    
    /**
     * Add a new property name to properties. A GeneralPropertyException will
     * be thrown if the property name already exists. This method will also 
     * call the store() method to save the new values into properties file. 
     * 
     * @param key  the property name
     * @param value  the new property value
     */
    public synchronized void addProperty(String key, String value) throws GeneralPropertyException {
    	if (propertiesMap.containsKey(key)) {
    		throw new PropertyExistsException("Property: " + key + " could not be added with value: " 
    				+ value + " because it already exists in properties.");
    	}
    	allLinesMap.put(key, value);
    	propertiesMap.put(key, value);
    	store();
    }
    
    /**
     * Add a new property name to properties. A GeneralPropertyException will
     * be thrown if the property name already exists. The property will not be  
     * persisted to the properties file.  the store() method should be called to
     * do this.
     * 
     * @param key  the property name
     * @param value  the new property value
     */
    public synchronized void addPropertyNoPersist(String key, String value) throws PropertyExistsException{
    	if (propertiesMap.containsKey(key)) {
    		throw new PropertyExistsException("Property: " + key + " could not be added with value: " 
    				+ value + " because it already exists in properties.");
    	}
    	allLinesMap.put(key, value);
    	propertiesMap.put(key, value);
    } 
    
    /**
     * Get an property value from the properties file
     *
     * @param key the name of the property requested
     * @return the String value for the given property, or null if not found
     */
    public synchronized String getProperty(String key)
			throws PropertyNotFoundException {
		String value = (String) propertiesMap.get(key);

		if (value == null && !propertiesMap.containsKey(key)) {
			throw new PropertyNotFoundException("Could not find property: " + key);
		}

		return value;
	}
    
    /**
     * Get all properties
     *
     * @return a linked hash map of all properties
     */
    public synchronized LinkedHashMap<String, String> getProperties()
			throws PropertyNotFoundException {
		
		return propertiesMap;
	}
    
    /**
	 * Get a Vector of all property names.
	 * 
	 * @return Vector of property names
	 */
    public Vector<String> getPropertyNames() {
    	Vector<String> groupKeySet = new Vector<String>();
    	Set<String> keySet = propertiesMap.keySet();
    	
    	for (String key : keySet) {
    			groupKeySet.add(key);
    	}
    	
    	return groupKeySet;
    }
       
    /**
     * Get a vector of all property names that start with the 
     * groupName prefix.
     * 
     * @param groupName the key prefix to look for.
     * @return Vector of property names  
     */
    public Vector<String> getPropertyNamesByGroup(String groupName) {
    	groupName = groupName.trim();
    	if (!groupName.endsWith(".")) {
    		groupName += (".");
    	}
    	
    	Set<String> keySet = propertiesMap.keySet();    	   	
    	Vector<String> groupKeySet = new Vector<String>();  
    	
    	for (String key : keySet) {
    		if (key.startsWith(groupName)) {
    			groupKeySet.add(key);
    		}
    	}
    	
    	return groupKeySet;
    }
    
    /**
     * Get a map of all properties that start with the 
     * groupName prefix.
     * 
     * @param groupName the key prefix to look for.
     * @return map of property name / values  
     */
    public HashMap<String, String> getPropertiesByGroup(String groupName)
			throws PropertyNotFoundException {
    	 
    	HashMap<String, String> groupPropertyMap = new HashMap<String, String>();
    	Vector<String> groupKeySet = getPropertyNamesByGroup(groupName);
    	
    	for (String key : groupKeySet) {
    		groupPropertyMap.put(key, getProperty(key));
    	}
    	
    	return groupPropertyMap;
    }
    
    /**
     * Save the properties to a properties file. Ordering, comments and 
     * blank lines will be preserved.
     * @param comment - The comment to be added at the top of the file 
     */
    public synchronized void store() throws GeneralPropertyException {
		int fileStatus = FileUtil.getFileStatus(propertiesFileName);
		PrintWriter output = null;

		try {
			if (fileStatus == FileUtil.DOES_NOT_EXIST) {
				int dirStatus = FileUtil.getFileStatus(propertiesDirName);
				if (dirStatus == FileUtil.DOES_NOT_EXIST) {
					throw new IOException("Could not write property file. Directory: "
							+ propertiesDirName + " not found.");
				} else if (dirStatus < FileUtil.EXISTS_READ_WRITABLE) {
					throw new IOException("Could not write property file. Directory: "
							+ propertiesDirName + " is not writable.");
				}
			} else if (fileStatus < FileUtil.EXISTS_READ_WRITABLE) {
				throw new IOException("Could not write property file. File: "
						+ propertiesFileName + " is not writable.");
			}
			output = new PrintWriter(new BufferedWriter(
					new FileWriter(propertiesFileName)));

			for (Iterator<String> it = allLinesMap.keySet().iterator(); it.hasNext();) {
				String key = it.next();
				if (key.startsWith(SPACE)) {
					output.println("");
				} else if (key.startsWith(COMMENT) || key.startsWith(UNKNOWN)) {
					output.println(allLinesMap.get(key));
				} else {
					output.println(key + "=" + allLinesMap.get(key));
				}
			}
		} catch (IOException ioe) {
			throw new GeneralPropertyException("Could not save properties: "
					+ ioe.getMessage());
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}        
}