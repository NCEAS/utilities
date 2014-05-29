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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
public class LSIDUtilitiesTest extends TestCase 
{
	/**
     * Constructor to build the test
     *
     * @param name the name of the test method
     */
    public LSIDUtilitiesTest(String name)
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
        suite.addTest(new LSIDUtilitiesTest("initialize"));
        suite.addTest(new LSIDUtilitiesTest("testTrasnformToDocID"));
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
     * Test method transformToDocID()
     */
    public void testTrasnformToDocID()
    {
    	String LISD1= null;
    	String LISD2= "gamma.msi.ucsb.edu/OepnAuth/:286:9";
    	String LISD3= "urn:lsid:gamma.msi.ucsb.edu/OepnAuth/:286:9";
    	String LISD4= "urn:lsid:gamma.msi.ucsb.edu/OepnAuth/:286:5:1";
    	String LISD5 = "tao.1.1";
    	String result = LSIDUtil.transformToDocID(LISD1);
    	assertTrue(result == null);
    	result = LSIDUtil.transformToDocID(LISD2);
    	assertTrue(result == null);
    	result = LSIDUtil.transformToDocID(LISD3);
    	assertTrue(result.equals("286.9"));
    	result = LSIDUtil.transformToDocID(LISD4);
    	assertTrue(result.equals("286.5.1"));
    	result = LSIDUtil.transformToDocID(LISD5);
        assertTrue(result == null);
    }

}
