/**
 *  '$RCSfile: SortedProperties.java,v $'
 *  Copyright: 2008 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-05-05 17:15:42 $'
 * '$Revision: 1.1.2.1 $'
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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * A class that reads options from a properties file.  Comments, spaces and order
 * are preserved.  
 */
public class SortedProperties 
{
	private String propertiesDirName = null;
	private String propertiesFileName = null;
	private LinkedHashMap<String,String> propertiesMap = null;

	// Identifiers for non-property lines in file
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
    	propertiesMap = new LinkedHashMap<String,String>();     
        
    }
    
    /**
     * Constructor.  Populates the file and directory names of the properties
     * file and creates the map which will hold the properties.  Loading of 
     * the file will need to be done explicitly.  This is because we may be
     * creating a new properties file.
     *
     * @param propFile the file object for the file from which to read properties.
     */
    public SortedProperties(File propFile)  
    {
        propertiesFileName = propFile.getPath();
        propertiesDirName = propFile.getParent();
    	propertiesMap = new LinkedHashMap<String,String>();       
        
    	System.out.println("MCD: Created Sorted Properties (2) with filename: " + propertiesFileName);
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
			    propertiesMap.put(parsedLine[0], parsedLine[1]);
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
     *    Comments:   #<unique number>       <the whole comment line>
     *    Blank line space<unique number>               ""
     *    Unknown    ???<unique number>      <the whole comment line>
     * @param rawLine a string holding the line read from a properties file.
     * @return a two element string array holding the key and value
     */
    private String[] parseLine(String rawLine) {
        
    	int equalIndex;
    	String line = rawLine.trim();
    	String[] parsedString = new String[2];    	
               
    	if (line.matches("^#.*")) {                // line is a comment
    		nonPropertyCount++;
			parsedString[0] = COMMENT + nonPropertyCount;
			parsedString[1] = line;
        } else if (line.matches("^$")) {          // line is blank
        	nonPropertyCount++;
        	parsedString[0] = SPACE + nonPropertyCount;
        	parsedString[1] = "";
        } else if ((equalIndex = line.indexOf('=')) != -1) {   // there is an = in the line
        	if (equalIndex == 0 ) {                     // the line starts with = 
        		nonPropertyCount++;
        		parsedString[0] = UNKNOWN + nonPropertyCount;
            	parsedString[1] = line;
        	} else if (equalIndex == line.length()) {    // the line ends with =
        		parsedString[0] = line.substring(0, equalIndex);
        		parsedString[1] = "";
        	} else {                                    // there is a key and a value
        		parsedString[0] = line.substring(0, equalIndex);
        		parsedString[1] = line.substring(equalIndex + 1);
        	}        	
        } else {                                     // something else going on here
        	nonPropertyCount++;
        	parsedString[0] = UNKNOWN + nonPropertyCount;
        	parsedString[1] = line;
        }
    	
    	return parsedString;
    }
    
    /**
     * Reset value for given option name or set a new option pair in the
     * property file. This method will also call a save method to save
     * the new values into properties file. Note: the comments in property
     * file will be gone and order of keys will be changed
     * @param key  the option name
     * @param value  the new option value
     */
    public synchronized void setProperty(String key, String value) {
    	propertiesMap.put(key, value);
    }
    
    /**
     * Get an option value from the properties file
     *
     * @param optionName the name of the option requested
     * @return the String value for the given property, or null if not found
     */
    public synchronized String get(String optionName) {
        String value = (String)propertiesMap.get(optionName);
  
        return value;
    }
    
    /**
     * Get an enumeration of all property names.
     * @return enumeration of property names  
     */
    public Enumeration<String> propertyNames() {
    	Set<String> keySet = propertiesMap.keySet();
    	
    	return (Enumeration<String>)Collections.enumeration(keySet);
    }
    
    /**
     * Save the properties to a properties file. Ordering, comments and 
     * blank lines will be preserved.  
     */
    public synchronized void store() throws IOException {
    	store(null);
    }
    
    /**
     * Save the properties to a properties file. Ordering, comments and 
     * blank lines will be preserved.  This is for compatibility with the 
     * java Properties class.  We don't actually use the writer.
     * @param outputStream - This is for backward compatibility with the 
     *                  java Properties class.  We don't actually use the writer.
     * @param comment - The comment to be added at the top of the file 
     */
    public synchronized void store(OutputStream outputStream, String comment) throws IOException {
    	store(comment);
    }
    
    /**
     * Save the properties to a properties file. Ordering, comments and 
     * blank lines will be preserved.
     * @param comment - The comment to be added at the top of the file 
     */
    public synchronized void store(String comment) throws IOException {
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
	    	
	    	if (comment != null) {
	    		output.println(comment);
	    	}
	    	
            for (Iterator<String> it = propertiesMap.keySet().iterator(); it.hasNext(); ) {
    	        String key = it.next();
    	        if (key.startsWith(SPACE)) {
    	        	output.println("");
    	        } else if (key.startsWith(COMMENT) || key.startsWith(UNKNOWN)) {
    	        	output.println(propertiesMap.get(key));
    	        } else {
    	            output.println(key + "=" + propertiesMap.get(key));
    	        }
            }
    	} finally {
    	    output.close();
    	}
    }       
    
    /**
     * Convenience method for debugging purposes.  Writes all lines including encoded
     * comments and blank lines to standard output.
     */
    public void printProperties() {
    	for (Iterator<String> it = propertiesMap.keySet().iterator(); it.hasNext(); ) {
    	    String key = it.next();
    	    String value = propertiesMap.get(key);
    	    System.out.println(key + " = " + value);
    	}
    }
}