/**
 *  '$RCSfile: SortedProperties.java,v $'
 *  Copyright: 2008 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-07-01 17:38:28 $'
 * '$Revision: 1.1.2.3 $'
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
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;

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
    public SortedProperties(String propFileName) 
    {
    	File propFile = new File(propFileName);
    	propertiesFileName = propFileName;
    	propertiesDirName = propFile.getParent();
    	allLinesMap = new LinkedHashMap<String, String>();     
    	propertiesMap = new LinkedHashMap<String, String>();
        
    }
    
    /**
     * Load the values from the properties file into the map which will
     * hold the properties.
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
		    while ((fileLine = reader.readLine()) != null) {
			    String parsedLine[] = parseLine(fileLine);
			    allLinesMap.put(parsedLine[0], parsedLine[1]);
			    if (parsedLine[2].equals("true")) {
			    	propertiesMap.put(parsedLine[0], parsedLine[1]);
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
     * @param rawLine a string holding the line read from a properties file.
     * @return a two element string array holding the key and value
     */
    private String[] parseLine(String rawLine) {
        
    	int equalIndex;
    	String line = rawLine.trim();
    	String[] parsedString = new String[3];    	
               
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
        		parsedString[1] = line.substring(equalIndex + 1);
            	parsedString[2] = IS_PROPERTY;
        	}        	
        } else {                                     // something else going on here
        	nonPropertyCount++;
        	parsedString[0] = UNKNOWN + nonPropertyCount;
        	parsedString[1] = line;            	
        	parsedString[2] = IS_NOT_PROPERTY;
        }
    	
    	return parsedString;
    }
    
    /**
     * Reset value for given option name or set a new option pair in the
     * property file. This method will also call a store method to save
     * the new values into properties file. Note: the comments in property
     * file will be gone and order of keys will be changed
     * @param key  the option name
     * @param value  the new option value
     */
    public synchronized void setProperty(String key, String value) throws IOException {
    	allLinesMap.put(key, value);
    	propertiesMap.put(key, value);
    	store();
    }
    
    /**
     * Reset value for given option name or set a new option pair in the
     * property file. This method not call the store method to save
     * the new values into properties file. Note: the comments in property
     * file will be gone and order of keys will be changed
     * @param key  the option name
     * @param value  the new option value
     */
    public synchronized void setPropertyNoPersist(String key, String value) {
    	allLinesMap.put(key, value);
    	propertiesMap.put(key, value);
    } 
    
    /**
     * Get an option value from the properties file
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
     * Get an enumeration of all property names that start with the 
     * groupName prefix.
     * 
     * @param groupName the key prefix to look for.
     * @return enumeration of property names  
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
     * Save the properties to a properties file. Ordering, comments and 
     * blank lines will be preserved.
     * @param comment - The comment to be added at the top of the file 
     */
    public synchronized void store() throws IOException {
        int fileStatus = FileUtil.getFileStatus(propertiesFileName);
        
        if (fileStatus == FileUtil.DOES_NOT_EXIST) {
        	int dirStatus = FileUtil.getFileStatus(propertiesDirName);
        	if (dirStatus == FileUtil.DOES_NOT_EXIST) {
        		throw new IOException("Could not write property file. Directory: " + 
        				propertiesDirName + " not found.");
        	} else if (dirStatus < FileUtil.EXISTS_READ_WRITABLE ) {
        		throw new IOException("Could not write property file. Directory: " + 
        				propertiesDirName + " is not writable.");
        	}
        } else if (fileStatus < FileUtil.EXISTS_READ_WRITABLE ) {
        	throw new IOException("Could not write property file. File: " + 
    				propertiesFileName + " is not writable.");
        }
        
        PrintWriter output = null;      
	    try {
	    	output = new PrintWriter(new BufferedWriter(new FileWriter(propertiesFileName)));
	    	
            for (Iterator<String> it = allLinesMap.keySet().iterator(); it.hasNext(); ) {
    	        String key = it.next();
    	        if (key.startsWith(SPACE)) {
    	        	output.println("");
    	        } else if (key.startsWith(COMMENT) || key.startsWith(UNKNOWN)) {
    	        	output.println(allLinesMap.get(key));
    	        } else {
    	            output.println(key + "=" + allLinesMap.get(key));
    	        }
            }
    	} finally {
    	    output.close();
    	}
    }        
}