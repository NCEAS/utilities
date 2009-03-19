/**
 *  '$RCSfile: FileUtil.java,v $'
 *  Copyright: 2000 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: daigle $'
 *     '$Date: 2009-03-19 17:09:05 $'
 * '$Revision: 1.10 $'
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    
    private static Character FILE_SEPARATOR = null;
    
    /**
     *  constructor
     */
    private FileUtil() {}

    /**
	 * Get the character that separates directories in a path
	 * 
	 * @returns character representation of the file separator
	 */
    public static char getFS() {
    	if (FILE_SEPARATOR == null)  {
    		FILE_SEPARATOR = File.separatorChar;
    	}
    	
    	return FILE_SEPARATOR;
    }
    
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
     * Get the file size in bytes
     * @param the path to the file
     * @return the size of the file in bytes.
     */
    public static long getFileSize(String filePath) {
    	File file = new File(filePath);
    	
    	if (!file.exists()) {
    		return -1;
    	}
    	
    	return file.length();
    }

    /**
	 * Wrap the File.isDirectory method
	 * 
	 * @param path
	 *            the path to check
	 * 
	 * @return true if the path is a directory. False otherwise.
	 */
    public static boolean isDirectory(String path) {
    	File file = new File(path);
    	return file.isDirectory();
    }
     
    /**
	 * Create a directory if it does not already exist.  This will attempt
	 * to create any parent directories if necessary.
	 * 
	 * @param dirPath the full pathname of the directory to create
	 * @returns boolean representing success or failure of directory creation
	 */
	public static void createDirectory(String dirPath) throws IOException {
		File file = new File(dirPath);
		if (file.exists() && file.isDirectory()) {
			return;
		}
		if (!file.mkdirs()) {
			throw new IOException("Could not create directory: " + dirPath);
		}
	}	
    
    /**
	 * Create a file if it does not already exist.
	 * 
	 * @param filePath the full pathname of the file to create
	 * @returns boolean representing success or failure of file creation
	 */
	public static void createFile(String filePath) throws IOException {
		File file = new File(filePath);
		if (file.exists() && !file.isDirectory()) {
			return;
		}
		if (!file.createNewFile()) {
			throw new IOException("Could not create file: " + filePath);
		}
	}
	
    /**
	 * Delete a file
	 * 
	 * @param filePath the full pathname of the file to delete
	 * @returns boolean representing success or failure of file creation
	 */
	public static void deleteFile(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException("Could not find and delete file: " + filePath);
		}
		if (!file.delete()) {
			throw new IOException("Could not delete file: " + filePath);
		}
	}
	
    /**
	 * Replace values in a file and overwrite the original file.
	 * 
	 * @param filePath
	 *            the full pathname of the file to modify
	 * @param replacementList
	 *            a hashtable of original and replacement values
	 */
	public static void replaceInFile(String filePath,
			Hashtable<String, String> replacementList) throws IOException {
		if (getFileStatus(filePath) != EXISTS_READ_WRITABLE) {
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
	}
	
    /**
	 * Create and write content to a new file with default character encoding
	 * 
	 * @param filePath
	 *            the full pathname of the file to create
	 * @param content
	 *            a string holding the contents to be written to the file
	 * @returns boolean representing success or failure of file creation
	 */
	public static void writeNewFile(String filePath, String content) throws IOException {
		writeNewFile(filePath, content, null);
	}
	
    /**
	 * Create and write content to a new file
	 * 
	 * @param filePath
	 *            the full pathname of the file to create
	 * @param content
	 *            a string holding the contents to be written to the file
	 * @param charset
	 * 				the character encoding to use for writing (i.e. "UTF8") 
	 * @returns boolean representing success or failure of file creation
	 */
	public static void writeNewFile(String filePath, String content, String charset) throws IOException {
		if (getFileStatus(filePath) != DOES_NOT_EXIST) {
			throw new IOException("Cannot create file: " + filePath 
					+ ". File already exists.");
		}
		
		createFile(filePath);

		PrintWriter output = null;
		try {
			if (charset != null) {
				output = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(filePath), charset)));
			}
			else {
				output = new PrintWriter(new BufferedWriter(
						new FileWriter(filePath, false)));
			}

			output.print(content);
		} finally {
			output.close();
		}
	}

	 /**
	 * Replace a file or create a new one if it does not exist
	 * 
	 * @param filePath
	 *            the full pathname of the file to create/replace
	 * @param content
	 *            a string holding the contents to be written to the file
	 * @returns boolean representing success or failure of file creation
	 */
	public static void writeFile(String filePath, String content) throws IOException {	
		FileUtil.writeFile(filePath, content, null);
	}
	
    /**
	 * Replace a file or create a new one if it does not exist
	 * 
	 * @param filePath
	 *            the full pathname of the file to create/replace
	 * @param content
	 *            a string holding the contents to be written to the file
	 * @param charset
	 * 				the character encoding to use for writing	 
	 * @returns boolean representing success or failure of file creation
	 */
	public static void writeFile(String filePath, String content, String charset) throws IOException {	
		if (content == null || content.equals("")) {
			throw new IOException("Attempting to write a file with no content: " + filePath);
		}
		
		File file = new File(filePath);
		file.createNewFile();

		PrintWriter output = null;
		try {
			if (charset != null) {
				output = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(file), charset)));
			}
			else {
				output = new PrintWriter(new BufferedWriter(
						new FileWriter(file, false)));
			}

			output.print(content);
		} finally {
			output.close();
		}
	}
	
	/**
	 * Read the contents of a file - uses default charset
	 * 
	 * @param filePath
	 *            the full pathname of the file to read    
	 * @returns a string holding the contents of the file
	 */
	public static String readFileToString(String filePath) throws IOException {
		return readFileToString(filePath, null);
	}
	
    /**
	 * Read the contents of a file
	 * 
	 * @param filePath
	 *            the full pathname of the file to read
	 * @param charset
	 * 			the optional character set to use for reading the file           
	 * @returns a string holding the contents of the file
	 */
	public static String readFileToString(String filePath, String charset) throws IOException {
		if (getFileStatus(filePath) < EXISTS_READABLE) {
			throw new IOException("Cannot read file: " + filePath);
		}

		File file = new File(filePath);
		int fileLength = new Long(file.length()).intValue();
		BufferedReader input = null;
		StringBuffer contents = null;
		
		// If fileLength is < 0, the length was greater that an integer could hold
		if (fileLength >= 0) {
			contents = new StringBuffer(fileLength);
		} else {
			contents = new StringBuffer(100000);
		}
		String nextLine;

		try {
			if (charset != null) {
				input = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), charset));
			}
			else {
				input = new BufferedReader(new FileReader(filePath));
			}
			boolean firstLine = true;
			while ((nextLine = input.readLine()) != null) {
				if (!firstLine) {
					contents.append("\n");
				} else {
					firstLine = false;
				}
				contents.append(nextLine);
			}

		} finally {
			input.close();
		}
		
		if (contents.equals("")) {
			return null;
		}

		return contents.toString();
	}
	
	/**
	 * Adapt a path to the current OS. Currently this only replaces forward
	 * slashes.
	 * 
	 * @param path
	 *            the path that needs to be normalized
	 * @return a path that will work on the current OS.
	 */
	public static String normalizePath(String path) {
		return path.replace('/', getFS());
	}
}


