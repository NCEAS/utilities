/**
 *  '$RCSfile: OptionsMetadataTest.java,v $'
 *  Copyright: 2003 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-07-01 17:40:13 $'
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

    /**
     * Create a new properties fiel for use in testing.
     */
    private void createTestMetadataFile() {
        StringBuffer metadata = new StringBuffer();
        metadata.append("# o1,Label1,Group1,1,Description of option 1\n");
        metadata.append("# o2,Label2,Group1,2,Description of option 2\n");
        metadata.append("# o3,Label3,Group2,1,Description of option 3\n");
        mdFile = new File("mdfile.properties.metadata");
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
    public void testOptionsMetadata() {
        
        // Also see if a metadata file exists for this properties file, and
        // if so then open it and load the metadata about each property
        // if it doesn't exist, then create it for use
        try {
            FileReader reader = new FileReader(mdFile);
            PropertiesMetaData metadata = new PropertiesMetaData(mdFile);
            reader.close();
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
    public void testSetMetadata() {
        try {
            PropertiesMetaData metadata = new PropertiesMetaData(mdFile);
            assertTrue(metadata != null);
            metadata.setMetadata("test", "Test", 1, 2, "Test option", "/testfile.html");
            assertTrue(metadata.getOptionLabel("test").equals("Test"));
        } catch (IOException e) {
            fail("Could not open the metadata file.");
        } catch (TransformerException te) {
            fail("Could not transform metadata.");
        }
    }

    /**
     * Test method for {@link edu.ucsb.nceas.utilities.OptionsMetadata#getOptionLabel(java.lang.String)}.
     */
    public void testGetOptionLabel() {
        try {
            PropertiesMetaData metadata = new PropertiesMetaData(mdFile);
            assertTrue(metadata != null);
            metadata.setMetadata("test", "Test", 1, 2, "Test option", "/testfile.html");
            assertTrue(metadata.getOptionLabel("test").equals("Test"));
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
//            PropertiesMetaData metadata = new PropertiesMetaData(mdFile);
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
    public void testGetOptionIndex() {
        try {
            PropertiesMetaData metadata = new PropertiesMetaData(mdFile);
            assertTrue(metadata != null);
            metadata.setMetadata("test", "Test", 1, 2, "Test option", "/testfile.html");
            assertTrue(metadata.getOptionIndex("test") == 2);
        } catch (IOException e) {
            fail("Could not open the metadata file.");
        } catch (TransformerException te) {
            fail("Could not transform metadata.");
        }
    }

    /**
     * Test method for {@link edu.ucsb.nceas.utilities.OptionsMetadata#getOptionDescription(java.lang.String)}.
     */
    public void testGetOptionDescription() {
        try {
            PropertiesMetaData metadata = new PropertiesMetaData(mdFile);
            assertTrue(metadata != null);
            metadata.setMetadata("test", "Test", 1, 2, "Test option", "/testfile.html");
            assertTrue(metadata.getOptionDescription("test").equals("Test option"));
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
    public void testLoad() {
        testOptionsMetadata();
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
