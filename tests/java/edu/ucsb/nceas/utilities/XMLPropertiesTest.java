/**
 *  '$RCSfile: XMLPropertiesTest.java,v $'
 *  Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: brooke $'
 *     '$Date: 2003-07-17 21:30:00 $'
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

package edu.ucsb.nceas.utiltest;

import edu.ucsb.nceas.utilities.IOUtil;
import edu.ucsb.nceas.utilities.StringUtil;
import edu.ucsb.nceas.utilities.XMLProperties;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

import javax.xml.transform.TransformerException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.log4j.Logger;

/**
 *
 * @author brooke
 */
public class XMLPropertiesTest extends TestCase {
  
  private static String xmlInputFileName = "/edu/ucsb/nceas/utilities/test_xmlproperties.xml";
  
  private static XMLProperties xmlPropsObject = new XMLProperties();

  private static InputStream xmlInputStream;

  private final String[][] testprops =
  { 
      { "/properties/does_not_exist",                        null },
      { "/properties/nodetype_definitions_filename",
                                    "/edu/ucsb/nceas/jalama/nodetype_defs.xml" },
      { "/properties/jalama_gui/show_renderer_menu",         "false" },
      { "/properties/jalama_gui/xmldisplay_bg_color/red",    "244" },
      { "/properties/jalama_gui/xmldisplay_bg_color/green",  "244" },
      { "/properties/jalama_gui/xmldisplay_bg_color/blue",   "244" },
      { "/properties/jalama_gui/initial_xml",                null },
      { "/properties/new_wizard_document_xml",
             "       <eml:eml packageId=\"eml.1.1\" system=\"knb\" \n"
            +"                xmlns:eml=\"eml://ecoinformatics.org/eml-2.0.0\" \n"
            +"                xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n"
            +"                xmlns:ds=\"eml://ecoinformatics.org/dataset-2.0.0\" \n"
            +"                xsi:schemaLocation=\"eml://ecoinformatics.org/eml-2.0.0 eml.xsd\"> \n"
            +"          <dataset/> \n"
            +"       </eml:eml>" }
  };

       
  static {
    xmlInputStream = XMLPropertiesTest.class.getResourceAsStream(xmlInputFileName);
    assertNotNull(xmlInputStream);
    assertNotNull(xmlPropsObject);
    try {
      xmlPropsObject.load(xmlInputStream);
    } catch (Exception e) {
      fail("unexpected exception doing xmlPropsObject.load() with xmlInputStream="
                                                               +xmlInputStream);
    }
  }
  
  public XMLPropertiesTest(java.lang.String testName) {
    super(testName);
  }
  
  public static Test suite() {
    TestSuite suite = new TestSuite(XMLPropertiesTest.class);
    return suite;
  }
  
  /** Test of load method, of class edu.ucsb.nceas.utilities.XMLProperties. */
  public void testLoad() {
    
    System.out.println("testLoad");
    xmlInputStream = XMLPropertiesTest.class.getResourceAsStream(xmlInputFileName);
    assertNotNull(xmlPropsObject);
    assertNotNull(xmlInputStream);
    try {
      xmlPropsObject.load(xmlInputStream);       
    } catch (Exception e) {
      e.printStackTrace();
      fail("unexpected exception doing xmlPropsObject.load() with xmlInputStream="
                                                               +xmlInputStream);
    }
  }
  
  /** Test of getProperty method, of class edu.ucsb.nceas.utilities.XMLProperties. */
  public void testGetProperty() {
    System.out.println("testGetProperty");
    assertNotNull(xmlPropsObject);
    assertNotNull(testprops);
    assertTrue(testprops.length > 0);
    
    for (int i=0; i<testprops.length; i++) {
      nextGetTest(testprops[i][0], testprops[i][1]);
    }
  }
  
  private void nextGetTest(String key, String expectedValue) {
          
    System.out.println("KEY = "+key);
    System.out.println("EXPECTED = "+expectedValue);

    String[] returnedValsArray = null;
    try {
      returnedValsArray = xmlPropsObject.getProperty(key);
    } catch (TransformerException e) {
      fail("unexpected exception doing xmlPropsObject.getProperty() with key="
                                                                        +key);
    }
    String returnedVal = null;
    
    if (expectedValue!=null) {
      assertNotNull(returnedValsArray);
      assertTrue(returnedValsArray.length == 1);
      returnedVal = returnedValsArray[0];
    } else {
      returnedVal = (returnedValsArray!=null && returnedValsArray.length>0)? 
                                                    returnedValsArray[0] : null;    
    }
    System.out.println("RETURNED = "+returnedVal);
    assertEquals( StringUtil.stripAllWhiteSpace(expectedValue), 
                  StringUtil.stripAllWhiteSpace(returnedVal));
  }
  
  
  /** Test of setProperty method, of class edu.ucsb.nceas.utilities.XMLProperties. */
  public void testSetProperty() {
    System.out.println("testSetProperty");
    String xpathA = "/properties/new_setproperty_test/element_A"; 
    String xpathB = "/properties/new_setproperty_test/subelement/element_B";
    String val1   = "SET_PROP_TEST_VAL_1";
    String val2   = "SET_PROP_TEST_VAL_2";
    String prevVal = null;
    try {
      prevVal = xmlPropsObject.setProperty(xpathA, val1);
    } catch (Exception e) {
      fail("unexpected exception doing xmlPropsObject.setProperty() ");
    }
    assertNull(prevVal);
    
    try {
      prevVal = xmlPropsObject.setProperty(xpathA, val2);
    } catch (Exception e) {
      fail("unexpected exception doing xmlPropsObject.setProperty() ");
    }
    assertNotNull(prevVal);
    assertEquals(val1, prevVal);

    try {
      prevVal = xmlPropsObject.setProperty(xpathB, val1);
    } catch (Exception e) {
      fail("unexpected exception doing xmlPropsObject.setProperty() ");
    }
    assertNull(prevVal); 
}
  
  /** Test of store method, of class edu.ucsb.nceas.utilities.XMLProperties. */
  public void testStore() {
    System.out.println("testStore");

    // re-initialize xml to original file contents:
    InputStream is = XMLPropertiesTest.class.getResourceAsStream(xmlInputFileName);
    try {
      xmlPropsObject.load(is);       
    } catch (Exception e) {
      fail("unexpected exception doing xmlPropsObject.load() with InputStream="
                                                               +is);
    } finally {
      try { if (is!=null) is.close(); }
      catch (Exception e) {}
    }
    
    // call method
    OutputStream out = new ByteArrayOutputStream();
    xmlPropsObject.store(out);
    assertNotNull(out);
    try {
      out.flush();
      out.close();      
    } catch (Exception e) {
      fail("unexpected exception doing out.flush() & out.close() ");
    }

    //re-init InputStream, since it has already been used
    is = XMLPropertiesTest.class.getResourceAsStream(xmlInputFileName);
    StringBuffer buff = null;
    try {
      buff = IOUtil.getAsStringBuffer(new InputStreamReader(is),true);
    } catch (Exception e) {
      fail("unexpected exception doing IOUtil.getAsStringBuffer() ");
    }
    assertNotNull(buff);
    String expected = buff.toString();
    assertNotNull(expected);
    assertEquals( StringUtil.stripAllWhiteSpace(expected),
                  StringUtil.stripAllWhiteSpace(out.toString()) );
  }
  
  /** Test of list method, of class edu.ucsb.nceas.utilities.XMLProperties. */
  public void testList() {
    System.out.println("testList");

    // re-initialize xml to original file contents:
    InputStream is = XMLPropertiesTest.class.getResourceAsStream(xmlInputFileName);
    try {
      xmlPropsObject.load(is);       
    } catch (Exception e) {
      fail("unexpected exception doing xmlPropsObject.load() with InputStream="
                                                               +is);
    } finally {
      try { if (is!=null) is.close(); }
      catch (Exception e) {}
    }
    
    // call method
    OutputStream out = new ByteArrayOutputStream();
    xmlPropsObject.list(new PrintWriter(out));
    assertNotNull(out);
    try {
      out.flush();
      out.close();      
    } catch (Exception e) {
      fail("unexpected exception doing out.flush() & out.close() ");
    }

    //re-init InputStream, since it has already been used
    is = XMLPropertiesTest.class.getResourceAsStream(xmlInputFileName);
    StringBuffer buff = null;
    try {
      buff = IOUtil.getAsStringBuffer(new InputStreamReader(is),true);
    } catch (Exception e) {
      fail("unexpected exception doing IOUtil.getAsStringBuffer() ");
    }
    assertNotNull(buff);
    String expected = buff.toString();
    assertNotNull(expected);
    assertEquals( StringUtil.stripAllWhiteSpace(expected),
                  StringUtil.stripAllWhiteSpace(out.toString()) );   
  }
  
  /** Test of propertyNames method, of class edu.ucsb.nceas.utilities.XMLProperties. */
  public void testPropertyNames() {
    System.out.println("testPropertyNames");
    
    Iterator it = xmlPropsObject.propertyNames();
    assertNotNull(it);
    List resultList = new ArrayList();
    while (it.hasNext()) {
      resultList.add((String)it.next());
    }
    assertNotNull(resultList);
    assertTrue(resultList.size() > 0);
    String[] resultArray = new String[resultList.size()];
    resultList.toArray(resultArray);
    assertNotNull(resultArray); 
    assertTrue(resultArray.length > 0);
    
    for (int idx=0; idx < resultArray.length; idx++) {

      System.out.println("RESULT["+idx+"] = "+resultArray[idx]);
    }
    
    assertEquals(allTestXPathsArray.length, resultArray.length);
    
    for (int idx=0; idx < allTestXPathsArray.length; idx++) {

      assertEquals(allTestXPathsArray[idx], resultArray[idx]);
    }
  }

  
  public static void main(String args[]) {
    TestRunner.run(XMLPropertiesTest.class);
    System.exit(0);
  }
  
  private String[] allTestXPathsArray = 
  {   "/properties/nodetype_definitions_filename[1]",
      "/properties/generic_doctype[1]",
      "/properties/generic_xpath[1]",
      "/properties/jvm_registry_lib_name[1]",
      "/properties/jalama_gui[1]/show_renderer_menu[1]",
      "/properties/jalama_gui[1]/default_width[1]",
      "/properties/jalama_gui[1]/default_height[1]",
      "/properties/jalama_gui[1]/xmldisplay_bg_color[1]/red[1]",
      "/properties/jalama_gui[1]/xmldisplay_bg_color[1]/green[1]",
      "/properties/jalama_gui[1]/xmldisplay_bg_color[1]/blue[1]",
      "/properties/jalama_gui[1]/xmldisplay_fg_color[1]/red[1]",
      "/properties/jalama_gui[1]/xmldisplay_fg_color[1]/green[1]",
      "/properties/jalama_gui[1]/xmldisplay_fg_color[1]/blue[1]",
      "/properties/jalama_gui[1]/renderer_status_fg_color[1]/red[1]",
      "/properties/jalama_gui[1]/renderer_status_fg_color[1]/green[1]",
      "/properties/jalama_gui[1]/renderer_status_fg_color[1]/blue[1]",
      "/properties/jalama_gui[1]/wizard[1]/doctype[1]",
      "/properties/jalama_gui[1]/wizard[1]/xpath[1]",
      "/properties/default_renderer[1]",
      "/properties/editor_wizard[1]/width[1]",
      "/properties/editor_wizard[1]/height[1]",
      "/properties/editor_default[1]/width[1]",
      "/properties/editor_default[1]/height[1]",
      "/properties/new_wizard_document_xml[1]"
  };
    
}
