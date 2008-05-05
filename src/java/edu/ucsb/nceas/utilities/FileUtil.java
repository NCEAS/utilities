/**
 *  '$RCSfile: FileUtil.java,v $'
 *  Copyright: 2000 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-05-05 17:09:29 $'
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/**
 *  General static utilities for File operations
 */
public class FileUtil                                         
{
    // Possible statuses of file
    public static int DOES_NOT_EXIST = 0;
    public static int EXISTS_ONLY = 1;
    public static int EXISTS_READABLE = 2;
    public static int EXISTS_READ_WRITABLE = 3; 

//    private static Class classRef = FileUtil.class;
    
    /**
     *  constructor
     */
    private FileUtil() {}

    /**
     * Get the status of the file.  The status that is returned is the greatest
     * possible of:
     *      DOES_NOT_EXIST = 0;
     *      EXISTS_ONLY = 1;
     *      EXISTS_READABLE = 2;
     *      EXISTS_READ_WRITABLE = 3;  
     * 
     * @param filePath the full pathname of the file to check
     * @returns integer representing the status of the file
     */
    public static int getFileStatus(String filePath) {
    	File file = new File(filePath);
    	if (file.exists()) {
    		if (file.canRead()) {
    			if (file.canWrite()) {
    				return EXISTS_READ_WRITABLE;
    			} else {
    				return EXISTS_READABLE;
    			}
    		} else {
    			return EXISTS_ONLY;
    		}
    	} else {
    		return DOES_NOT_EXIST;
    	}
    }

    /**
	 * Create a directory if it does not already exist.  This will attempt
	 * to create any parent directories if necessary.
	 * 
	 * @param dirPath the full pathname of the directory to create
	 * @returns boolean representing success or failure of directory creation
	 */
	public static boolean createDirectory(String dirPath) throws IOException {
		File file = new File(dirPath);
		if (!file.exists()) {
			return file.mkdirs();
		}
		return true;
	}	
    
    /**
	 * Create a file if it does not already exist.
	 * 
	 * @param filePath the full pathname of the file to create
	 * @returns boolean representing success or failure of file creation
	 */
	public static boolean createFile(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			return file.createNewFile();
		}
		return true;
	}
	
    /**
	 * Replace values in a file and overwrite the original file.
	 * 
	 * @param filePath
	 *            the full pathname of the file to modify
	 * @param replacementList
	 *            a hashtable of original and replacement values
	 * @returns boolean representing success or failure of file creation
	 */
	public static boolean replaceInFile(String filePath,
			Hashtable<String, String> replacementList) throws IOException {
		if (getFileStatus(filePath) < EXISTS_READ_WRITABLE) {
			throw new IOException("File: " + filePath + " is not writeable.");
		}

		Vector<String> fileLines = new Vector<String>();

		FileInputStream fin = null;
		try {
			fin = new FileInputStream(filePath);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fin));

			// Read in file
			String fileLine;
			while ((fileLine = reader.readLine()) != null) {
				Enumeration<String> enumer = replacementList.keys();
				while (enumer.hasMoreElements()) {
					String oldValue = enumer.nextElement();
					String newValue = replacementList.get(oldValue);
					String newLine = fileLine.replace(oldValue, newValue);
					fileLine = newLine;
				}
				fileLines.add(fileLine);
			}
		} finally {
			// Close our input stream
			fin.close();
		}

		PrintWriter output = null;
		try {
			output = new PrintWriter(new BufferedWriter(
					new FileWriter(filePath)));

			Iterator<String> iter = fileLines.iterator();
			while (iter.hasNext()) {
				output.println(iter.next());
			}
		} finally {
			output.close();
		}

		return true;
	}
}


