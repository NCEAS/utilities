/**
 *  '$RCSfile: FileUtilTest.java,v $'
 *  Copyright: 2003 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *
 *   '$Author: rnahf $'
 *     '$Date: 2005-08-29 22:52:46 $'
 * '$Revision: 1.2 $'
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.framework.Assert;

/**
 * A JUnit test for testing the Options class for property handling
 */
public class FileUtilTest extends TestCase
{

    /**
     * Constructor to build the test
     *
     * @param name the name of the test method
     */
    public FileUtilTest(String name)
    {
        super(name);
    }

    /**
     * Establish a testing framework by initializing appropriate objects
     */
    public void setUp()
    {
    }
  
    /**
     * Release any objects after tests are complete
     */
    public void tearDown()
    {
    }
  
    /**
     * Create a suite of tests to be run together
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest(new FileUtilTest("initialize"));
	// TODO: create unit tests for remaining methods
//        suite.addTest(new FileUtilTest("createDirectory"));
//        suite.addTest(new FileUtilTest("createFile"));
//        suite.addTest(new FileUtilTest("deleteFile"));
//        suite.addTest(new FileUtilTest("extractJarFile"));
//        suite.addTest(new FileUtilTest("getFileSize"));
//        suite.addTest(new FileUtilTest("getFileStatus"));
//        suite.addTest(new FileUtilTest("getFS"));
//        suite.addTest(new FileUtilTest("getJarEntry"));
//        suite.addTest(new FileUtilTest("getJarEntryReader"));
//        suite.addTest(new FileUtilTest("getJarInfoList"));
//        suite.addTest(new FileUtilTest("isDirectory"));
//        suite.addTest(new FileUtilTest("normalizePath"));
        suite.addTest(new FileUtilTest("readFileToString"));
//        suite.addTest(new FileUtilTest("replaceInFile"));
//        suite.addTest(new FileUtilTest("writeFile"));
//        suite.addTest(new FileUtilTest("writeNewFile"));
        
        return suite;
    }
  
    /**
     * Run an initial test that always passes to check that the test
     * harness is working.
     */
    public void initialize()
    {
        assertTrue(1 == 1);
    }
  
    /**
     * Test the readFileToString function under different file circumstances,
     * mainly the character encoding and the OS-dependent line-separator handling
     * (especially handling of a trailing line-separator)
     * 
     * Expecting default encoding to be utf-8, and so pass...
     * Expecting other encodings against test files to fail.
     * Testing 0, 1 and 2 line-separators at the end of the content.
     */
    public void readFileToString()
    {
    	String[] encodings = {null, "UTF-8", "UTF-16"};
    	for (int i=0; i<encodings.length; i++) {
    		String encoding = encodings[i];
    		boolean expectEqual;
    		if (encoding == null || encoding.equals("UTF-8"))
    			expectEqual = true;
    		else
    			expectEqual = false;
    		
    		System.out.println("\n******* Character encoding specification: " + encoding + "************************");
    		// Unix / Mac OSX style line-separators
    		runFileToStringTest("tests/resources/fileUtil/LF0.utf8.txt", encoding, expectEqual);
    		runFileToStringTest("tests/resources/fileUtil/LF1.utf8.txt", encoding, expectEqual);
    		runFileToStringTest("tests/resources/fileUtil/LF2.utf8.txt", encoding, expectEqual);
    		// DOS (Windows) style line-separators 
    		runFileToStringTest("tests/resources/fileUtil/CRLF0.utf8.txt", encoding, expectEqual);
    		runFileToStringTest("tests/resources/fileUtil/CRLF1.utf8.txt", encoding, expectEqual);
    		runFileToStringTest("tests/resources/fileUtil/CRLF2.utf8.txt", encoding, expectEqual);
       		// Mac OS9 (and earlier) style line-separators 
    		runFileToStringTest("tests/resources/fileUtil/CR0.utf8.txt", encoding, expectEqual);
    		runFileToStringTest("tests/resources/fileUtil/CR1.utf8.txt", encoding, expectEqual);
    		runFileToStringTest("tests/resources/fileUtil/CR2.utf8.txt", encoding, expectEqual);
    	}
    }

    private void runFileToStringTest(String resourceFile, String encoding, boolean expectEqual) {
    	 System.out.println("file: " + resourceFile);
   	  
   	  String fileContents = null;
   	  // detect and use correct encoding
   	  try {
   		  FileInputStream fis = new FileInputStream(resourceFile);
   		  String cs = checksum(fis, "MD5");
   		  System.out.println("    checksum from original file:    " + cs);

   		  fileContents = FileUtil.readFileToString(resourceFile,encoding);

   		  String cs2 = null;
   		  if (encoding == null)
   			  cs2 = checksum(fileContents.getBytes(), "MD5");
   		  else 
   			  cs2 = checksum(fileContents.getBytes(encoding), "MD5");
   		  System.out.println("    checksum from stringified file: " + cs2);
   		  System.out.println("    (file-string length = " + fileContents.length() + ")");	
   		  
   		  if (expectEqual)
   			  assertEquals("checksum comparison", cs,cs2);
   		  else 
   			  assertFalse("checksum comparison", cs.equals(cs2));
   	  } catch (Exception e) {
   		  e.printStackTrace();
   	  }
    }
    
    /**
     * produce a checksum for item using the given algorithm
     */
    protected static String checksum(byte[] object, String algorithm) throws NoSuchAlgorithmException 
    {
    	MessageDigest complete = MessageDigest.getInstance(algorithm);
    	complete.update(object);
    	return getHex(complete.digest());
    }
    
    
    /**
     * produce a checksum for item using the given algorithm
     */
    protected static String checksum(byte[][] baa, String algorithm) throws NoSuchAlgorithmException 
    {
    	MessageDigest complete = MessageDigest.getInstance(algorithm);
    	for(int i=0; i<baa.length;i++) {
    		complete.update(baa[i]);
    	}
    	return getHex(complete.digest());
    }
    
    
    /**
     * produce a checksum for item using the given algorithm
     * @throws IOException 
     * @throws NoSuchAlgorithmException 
     */
    protected static String checksum(InputStream is, String algorithm) throws IOException, NoSuchAlgorithmException
    {        
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance(algorithm);
        int numRead;
        
        do 
        {
          numRead = is.read(buffer);
          if (numRead > 0) 
          {
            complete.update(buffer, 0, numRead);
          }
        } while (numRead != -1);
        
        
        return getHex(complete.digest());
    }
    
    /**
     * convert a byte array to a hex string
     */
    private static String getHex( byte [] raw ) 
    {
        final String HEXES = "0123456789ABCDEF";
        if ( raw == null ) {
          return null;
        }
        final StringBuilder hex = new StringBuilder( 2 * raw.length );
        for ( final byte b : raw ) {
          hex.append(HEXES.charAt((b & 0xF0) >> 4))
             .append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }
    
}
