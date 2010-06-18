/**
 *  '$RCSfile: OptionsTest.java,v $'
 *  Copyright: 2003 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *
 *   '$Author: tao $'
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
