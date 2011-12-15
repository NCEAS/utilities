/**
 *  '$RCSfile: OptionsTest.java,v $'
 *  Copyright: 2003 Regents of the University of California 
 *
 *   '$Author: tao $'
 *     '$Date: 2005-08-29 22:52:46 $'
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
        suite.addTest(new OptionsTest("setProperty"));
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
            // don't want to test this one, since it gets changed in the setProperty test
            // and this test might be run more than once
//            String prop2 = options.getOption("my.first");
//            assertTrue(prop2.equals("1"));
            String prop3 = options.getOption("my.second");
            assertTrue(prop3.equals("2"));
            String prop4 = options.getOption("my.third");
            assertTrue(prop4.equals("three"));
        } catch (IOException ioe) {
            fail("Initialization of instance failed:\n" + ioe.getMessage());
        }
    }
    
    /**
     * This method will test reset peroperties.
     *
     */
    public void setProperty()
    {
        try
        {
           Options options = Options.initialize(propertyFile);
           assertNotNull(options);
           String key = "my.first";
           String retrievedValue = options.getOption(key);
           String newValue;
           if (retrievedValue.equals("1")) {
        	   // original value
        	   newValue = "one";
           } else {
        	   // "newValue" already there, so restore to original
        	   newValue = "1";
           }
           options.setOption(key, newValue);
           retrievedValue = options.getOption(key);
           System.out.println("the new retrieved value is "+ retrievedValue);
           assertTrue(retrievedValue.equals(newValue));
        }
        catch(IOException e)
        {
            fail("Initialization of instance failed:\n" + e.getMessage());
        }
    }
}
