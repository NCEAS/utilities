/**
 *  '$RCSfile: OptionsTest.java,v $'
 *  Copyright: 2003 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *
 *   '$Author: jones $'
 *     '$Date: 2003-12-05 18:04:33 $'
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

import java.io.File;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * A JUnit test for testing the Options class for property handling
 */
public class OptionsTest extends TestCase
{
    private String testfile = "tests/test.properties";
    private File propertyFile = null;

    /**
     * Constructor to build the test
     *
     * @param name the name of the test method
     */
    public OptionsTest(String name)
    {
        super(name);
    }

    /**
     * Establish a testing framework by initializing appropriate objects
     */
    public void setUp()
    {
        propertyFile = new File(testfile);
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
        suite.addTest(new OptionsTest("initialize"));
        suite.addTest(new OptionsTest("getInstanceInvalid"));
        suite.addTest(new OptionsTest("initInstanceInvalid"));
        suite.addTest(new OptionsTest("initInstance"));
        suite.addTest(new OptionsTest("getInstance"));
        suite.addTest(new OptionsTest("getProperty"));
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
     * Test the initialize() function with an valid property file
     */
    public void initInstance()
    {
        try {
            Options options = Options.initialize(propertyFile);
            assertNotNull(options);
        } catch (IOException ioe) {
            fail("Initialization of instance failed:\n" + ioe.getMessage());
        }
    }

    /**
     * Test the initialize() function with an invalid property file
     */
    public void initInstanceInvalid()
    {
        try {
            File invalidFile = new File("/foo/bar/baz.properties");
            Options options = Options.initialize(invalidFile);
            fail("Should have failed to find property file.");
        } catch (IOException ioe) {
            assertNotNull(ioe);
        }
    }

    /**
     * Test the getInstance() function after it has been initialized
     */
    public void getInstance()
    {
        try {
            Options options = Options.initialize(propertyFile);
            assertNotNull(options);
            Options opt = Options.getInstance();
            assertNotNull(opt);
        } catch (IOException ioe) {
            fail("Initialization of instance failed:\n" + ioe.getMessage());
        }
    }

    /**
     * Test the getInstance() function before it has been initialized
     */
    public void getInstanceInvalid()
    {
        Options opt = Options.getInstance();
        assertNull(opt);
    }

    /**
     * Test the getProperty() function with an valid property file
     */
    public void getProperty()
    {
        try {
            Options options = Options.initialize(propertyFile);
            assertNotNull(options);
            String prop1 = options.getOption("hello");
            assertTrue(prop1.equals("world"));
            String prop2 = options.getOption("my.first");
            assertTrue(prop2.equals("1"));
            String prop3 = options.getOption("my.second");
            assertTrue(prop3.equals("2"));
            String prop4 = options.getOption("my.third");
            assertTrue(prop4.equals("three"));
        } catch (IOException ioe) {
            fail("Initialization of instance failed:\n" + ioe.getMessage());
        }
    }
}
