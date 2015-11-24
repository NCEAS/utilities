/**
 *  '$RCSfile: OptionsMetadataTest.java,v $'
 *  Copyright: 2003 Regents of the University of California 
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-07-07 04:27:05 $'
 * '$Revision: 1.2 $'
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.transform.TransformerException;

import junit.framework.TestCase;

import edu.ucsb.nceas.utilities.PropertiesMetaData;

/**
 * Test cases for the OptionsMetadata class.
 * @author Matt Jones
 */
public class OptionsMetadataTest extends TestCase {

    private static String MDFILE = "mdfile.properties.metadata";

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        createTestMetadataFile();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        // TODO: remove the mdFile
    }

    /** Dummy test showing that the test framework starts properly.
      */
    public void testFrameworkRuns() {
        assert(1==1);
    }

    /**
     * Create a new properties fiel for use in testing.
     */
    private void createTestMetadataFile() {
        StringBuffer metadata = new StringBuffer();
        metadata.append("# o1,Label1,Group1,1,Description of option 1\n");
        metadata.append("# o2,Label2,Group1,2,Description of option 2\n");
        metadata.append("# o3,Label3,Group2,1,Description of option 3\n");
        mdFile = new File("mdfile.properties.metadata");
        MDFILE = mdFile.getAbsolutePath();
        System.out.println("PROPERTIES FILE IS: " + MDFILE);
        try {
            FileWriter fw = new FileWriter(mdFile);
            fw.write(metadata.toString());
            fw.close();
        } catch (IOException e) {
            fail("Could not create test metadata file.");
        }        
    }

    /**
     * Test method for {@link edu.ucsb.nceas.utilities.OptionsMetadata#OptionsMetadata(java.io.InputStream)}.
     */
    public void oldtestOptionsMetadata() {
        
        // Also see if a metadata file exists for this properties file, and
        // if so then open it and load the metadata about each property
        // if it doesn't exist, then create it for use
        try {
            //FileReader reader = new FileReader(mdFile);
            PropertiesMetaData metadata = new PropertiesMetaData(MDFILE);
            //reader.close();
            assertTrue(metadata != null);
        } catch (IOException e) {
            fail("Could not open the metadata file.");
        }   catch (TransformerException te) {
            fail("Could not transform metadata.");
        }
    }

    /**
     * Test method for {@link edu.ucsb.nceas.utilities.OptionsMetadata#setMetadata(java.lang.String, java.lang.String, java.lang.String, int, java.lang.String)}.
     */
    public void oldtestSetMetadata() {
        try {
            PropertiesMetaData metadata = new PropertiesMetaData(MDFILE);
            assertTrue(metadata != null);
            metadata.setMetadata("test", "Test", 1, 2, "Test option", "/testfile.html");
            //assertTrue(metadata.getOptionLabel("test").equals("Test"));
        } catch (IOException e) {
            fail("Could not open the metadata file.");
        } catch (TransformerException te) {
            fail("Could not transform metadata.");
        }
    }

    /**
     * Test method for {@link edu.ucsb.nceas.utilities.OptionsMetadata#getOptionLabel(java.lang.String)}.
     */
    public void oldtestGetOptionLabel() {
        try {
            PropertiesMetaData metadata = new PropertiesMetaData(MDFILE);
            assertTrue(metadata != null);
            metadata.setMetadata("test", "Test", 1, 2, "Test option", "/testfile.html");
            //assertTrue(metadata.getOptionLabel("test").equals("Test"));
        } catch (IOException e) {
            fail("Could not open the metadata file.");
        } catch (TransformerException te) {
            fail("Could not transform metadata.");
        }
    }

//    /**
//     * Test method for {@link edu.ucsb.nceas.utilities.OptionsMetadata#getOptionGroup(java.lang.String)}.
//     */
//    public void testGetOptionGroup() {
//        try {
//            PropertiesMetaData metadata = new PropertiesMetaData(MDFILE);
//            assertTrue(metadata != null);
//            metadata.setMetadata("test", "Test", 1, 2, "Test option", "/testfile.html");
//            assertTrue(metadata.getOptionGroup("test").equals("Tgroup"));
//        } catch (IOException e) {
//            fail("Could not open the metadata file.");
//        } catch (TransformerException te) {
//            fail("Could not transform metadata.");
//        }
//    }

    /**
     * Test method for {@link edu.ucsb.nceas.utilities.OptionsMetadata#getOptionIndex(java.lang.String)}.
     */
    public void oldtestGetOptionIndex() {
        try {
            PropertiesMetaData metadata = new PropertiesMetaData(MDFILE);
            assertTrue(metadata != null);
            metadata.setMetadata("test", "Test", 1, 2, "Test option", "/testfile.html");
            //assertTrue(metadata.getOptionIndex("test") == 2);
        } catch (IOException e) {
            fail("Could not open the metadata file.");
        } catch (TransformerException te) {
            fail("Could not transform metadata.");
        }
    }

    /**
     * Test method for {@link edu.ucsb.nceas.utilities.OptionsMetadata#getOptionDescription(java.lang.String)}.
     */
    public void oldtestGetOptionDescription() {
        try {
            PropertiesMetaData metadata = new PropertiesMetaData(MDFILE);
            assertTrue(metadata != null);
            metadata.setMetadata("test", "Test", 1, 2, "Test option", "/testfile.html");
            //assertTrue(metadata.getOptionDescription("test").equals("Test option"));
        } catch (IOException e) {
            fail("Could not open the metadata file.");
        } catch (TransformerException te) {
            fail("Could not transform metadata.");
        }
    }

//    /**
//     * Test method for {@link edu.ucsb.nceas.utilities.OptionsMetadata#getGroups()}.
//     */
//    public void testGetGroups() {
//        try {
//            FileReader reader = new FileReader(mdFile);
//            OptionsMetadata metadata = new OptionsMetadata(reader);
//            reader.close();
//            assertTrue(metadata != null);
//            assertTrue(metadata.getGroups().size() == 2);
//            assertTrue(metadata.getGroups().contains("Group1"));
//            assertTrue(metadata.getGroups().contains("Group2"));
//            assertTrue( ! metadata.getGroups().contains("InvalidGroup"));
//        } catch (FileNotFoundException e) {
//            fail("Could not locate file to open.");
//        } catch (IOException e) {
//            fail("Input/Output error while opening metadata file.");
//        }
//    }

//    /**
//     * Test method for {@link edu.ucsb.nceas.utilities.OptionsMetadata#getKeysInGroup()}.
//     */
//    public void testGetKeysInGroup() {
//        try {
//            FileReader reader = new FileReader(mdFile);
//            OptionsMetadata metadata = new OptionsMetadata(reader);
//            reader.close();
//            assertTrue(metadata != null);
//            assertTrue(metadata.getKeysInGroup("Group1").size() == 2);
//            assertTrue(metadata.getKeysInGroup("Group2").size() == 1);
//            assertTrue(metadata.getKeysInGroup("InvalidGroup").size() == 0);
//        } catch (FileNotFoundException e) {
//            fail("Could not locate file to open.");
//        } catch (IOException e) {
//            fail("Input/Output error while opening metadata file.");
//        }
//    }
    
    /**
     * Test method for {@link edu.ucsb.nceas.utilities.OptionsMetadata#load()}.
     */
    public void oldtestLoad() {
        oldtestOptionsMetadata();
    }

//    /**
//     * Test method for {@link edu.ucsb.nceas.utilities.OptionsMetadata#store()}.
//     */
//    public void testStore() {
//        try {
//            FileReader reader = new FileReader(mdFile);
//            OptionsMetadata metadata = new OptionsMetadata(reader);
//            reader.close();
//            assertTrue(metadata != null);
//            FileWriter writer = new FileWriter(mdFile);
//            metadata.setMetadata("test", "Test", "Tgroup", 2, "Test option");
//            assertTrue(metadata.getOptionDescription("test").equals("Test option"));
//            metadata.store(writer);
//            writer.close();
//            
//            // Now read in the modified file and validate it has been stored correctly
//            FileReader reader2 = new FileReader(mdFile);
//            OptionsMetadata metadata2 = new OptionsMetadata(reader2);
//            reader2.close();
//            assertTrue(metadata2 != null);
//            assertTrue(metadata2.getOptionLabel("test").equals("Test"));
//        } catch (IOException e) {
//            fail("Could not write to the metadata file.");
//        }
//    }

    // Instance fields
    private File mdFile = null;
}
