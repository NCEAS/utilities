/**
 *  '$RCSfile: XMLUtilitiesTest.java,v $'
 *  Copyright: 2000 Regents of the University of California and the
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import javax.xml.transform.TransformerException;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;

import edu.ucsb.nceas.utilities.IOUtil;
import edu.ucsb.nceas.utilities.OrderedMap;
import edu.ucsb.nceas.utilities.StringUtil;
import edu.ucsb.nceas.utilities.XMLUtilities;

import org.apache.log4j.Logger;

/**
 * A JUnit test for testing the metadisplay plugin.
 */
public class XMLUtilitiesTest extends TestCase
{

  private static final String XML_TEST_FILENAME  = "/edu/ucsb/nceas/utilities/junit_testfile.xml";

  private static boolean errorFlag;
  private static Node testRootNode = null;

  private static Logger log = Logger.getLogger(XMLUtilitiesTest.class.getName());
  

  private static String TEST_XML;
  private static String TEST_XML_NOHEADER;

  static  {
    testRootNode = getNewTestRootNode();
  }
  
 /**
  * Constructor to build the test
  *
  * @param name the name of the test method
  */
  public XMLUtilitiesTest(String name) { super(name); }

  /**
  * NOTE - this gets called before *each* *test* 
  */
  public void setUp() {
    InputStreamReader isr 
    = IOUtil.getResourceAsInputStreamReader(XML_TEST_FILENAME);
    try {
      TEST_XML = IOUtil.getAsStringBuffer(isr, true).toString();
    } catch (Exception e) {
      e.printStackTrace();
      fail("Exception trying to load XML Sample " +XML_TEST_FILENAME
        +"\nException was: "+e);
    }

    //get rid of <xml...> tag
    TEST_XML_NOHEADER 
    = TEST_XML.substring(1+TEST_XML.indexOf("\n"));
  }

  /**
  * Release any objects after tests are complete
  */
  public void tearDown() {
  }

  private static Node getNewTestRootNode() {
  
    Node testRootNode = null;
    try {
      testRootNode = XMLUtilities.getXMLAsDOMTreeRootNode(XML_TEST_FILENAME);
    } catch (Exception e) {
      e.printStackTrace();
      fail("unexpected exception while trying to initialize testRootNode by "
          +"calling getXMLAsDOMTreeRootNode with filename: "+XML_TEST_FILENAME);
    }
    return testRootNode;
  }


  ////////////////////////////////////////////////////////////////////////////////
  //                    S T A R T   T E S T   M E T H O D S                     //
  ////////////////////////////////////////////////////////////////////////////////



  public void testGetXMLAsDOMTreeRootNode() {
  
    System.out.println("getXMLAsDOMTreeRootNode() test...");

    Node rootNode = null;

    ////////////////
    System.out.println("calling with valid filename...");
    try {
      rootNode = XMLUtilities.getXMLAsDOMTreeRootNode(XML_TEST_FILENAME);
    } catch (Exception e) {
      e.printStackTrace();
      fail("unexpected exception while trying to call"
        +" getXMLAsDOMTreeRootNode with VALID filename");
    }
    assertNotNull(rootNode);
    assertTrue(rootNode.hasChildNodes());
    assertEquals( StringUtil.stripAllWhiteSpace(TEST_XML), 
                  StringUtil.stripAllWhiteSpace(XMLUtilities.getDOMTreeAsString(rootNode)));

    ////////////////
    System.out.println("calling with NULL filename...");
    rootNode  = null;
    errorFlag = false;
    try {
          rootNode = XMLUtilities.getXMLAsDOMTreeRootNode(null);
    } catch (IOException fe) {
          System.out.println("OK - *expected* exception while trying to call "
                              +" getXMLAsDOMTreeRootNode with NULL filename");
    errorFlag = true;
    } catch (Exception e) {
          fail("unexpected exception while trying to call"
                              +" getXMLAsDOMTreeRootNode with NULL filename");
    }
    assertTrue(errorFlag);
    errorFlag = false;
    assertNull(rootNode);
    rootNode = null;
  }

  public void testGetXMLReaderAsDOMTreeRootNode() {
  
    System.out.println("getXMLReaderAsDOMTreeRootNode() test...");

    Node rootNode = null;

    try {
      rootNode = XMLUtilities.getXMLReaderAsDOMTreeRootNode(
                                          new StringReader(XPATH_MAP_TEST_XML));
    } catch (Exception e) {
      e.printStackTrace();
      fail( "unexpected exception while trying to call"
           +" getXMLReaderAsDOMTreeRootNode");
    }

    assertNotNull(rootNode);
    assertTrue(rootNode.hasChildNodes());

    assertEquals( StringUtil.stripAllWhiteSpace(
                                    XMLUtilities.getDOMTreeAsString(rootNode)), 
                  StringUtil.stripAllWhiteSpace(XPATH_MAP_TEST_XML));

    rootNode = null;

    ////////////////
    System.out.println("calling with NULL rootnode...");
    
    try {
      XMLUtilities.getXMLReaderAsDOMTreeRootNode(null);
    } catch (IOException ioe) {
      System.out.println("OK - *expected* exception while trying to call "
                        +" getXMLReaderAsDOMTreeRootNode with NULL filename");
    }
  }
  
  

  
  public void testGetXMLAsDOMDocument() {
  
    System.out.println("getXMLAsDOMDocument() test...");

    Document doc = null;

    ////////////////
    System.out.println("calling with valid filename...");

    try {
      doc = XMLUtilities.getXMLAsDOMDocument(XML_TEST_FILENAME);
    } catch (Exception e) {
      e.printStackTrace();
      fail("unexpected exception while trying to call"
        +" getXMLAsDOMDocument with VALID filename");
    }
    assertNotNull(doc);
    assertTrue(doc.hasChildNodes());
    assertEquals( StringUtil.stripAllWhiteSpace(TEST_XML), 
                  StringUtil.stripAllWhiteSpace(XMLUtilities.getDOMTreeAsString(doc.getDocumentElement())));

    ////////////////
    System.out.println("calling with NULL filename...");
    doc  = null;
    errorFlag = false;
        try {
            doc = XMLUtilities.getXMLAsDOMDocument(null);
      } catch (IOException fe) {
            System.out.println("OK - *expected* exception while trying to call "
                                +" getXMLAsDOMDocument with NULL filename");
      errorFlag = true;
      } catch (Exception e) {
            fail("unexpected exception while trying to call"
                                +" getXMLAsDOMDocument with NULL filename");
      }
        assertTrue(errorFlag);
    errorFlag = false;
    assertNull(doc);
    doc = null;
  }



  public void testGetXMLReaderAsDOMDocument() {
  
    System.out.println("getXMLReaderAsDOMDocument() test...");

    Document doc = null;

    ////////////////
    System.out.println("calling with valid filename...");

    try {
      doc = XMLUtilities.getXMLReaderAsDOMDocument(
                                          new StringReader(TEST_XML));
    } catch (Exception e) {
      e.printStackTrace();
      fail("unexpected exception while trying to call"
        +" getXMLAsDOMDocument with VALID filename");
    }
    assertNotNull(doc);
    assertTrue(doc.hasChildNodes());
    assertEquals( StringUtil.stripAllWhiteSpace(
                    XMLUtilities.getDOMTreeAsString(doc.getDocumentElement())),
                  StringUtil.stripAllWhiteSpace(TEST_XML));

    ////////////////
    System.out.println("calling with NULL filename...");
    doc  = null;
    errorFlag = false;
    try {
          doc = XMLUtilities.getXMLReaderAsDOMDocument(null);
    } catch (IOException fe) {
          System.out.println("OK - *expected* exception while trying to call "
                              +" getXMLAsDOMDocument with NULL filename");
    errorFlag = true;
    } catch (Exception e) {
          fail("unexpected exception while trying to call"
                              +" getXMLAsDOMDocument with NULL filename");
    }
    assertTrue(errorFlag);
    errorFlag = false;
    assertNull(doc);
    doc = null;
  }


  
  public void testAddTextNodeToDOMTree() {
  
    String xpath = "/acc:access/test_element/sub_test_element/text_element";
    String textVal1 = "ACTUAL_TEXT_VALUE_1";
    String textVal2 = "ACTUAL_TEXT_VALUE_2";
    assertNotNull(testRootNode);
    
    // set first new val /////////
    try {
      XMLUtilities.addTextNodeToDOMTree(testRootNode, xpath, textVal1);
    } catch (Exception e) {
      e.printStackTrace();
      fail("unexpected exception while trying to call"
            +" addTextNodeToDOMTree with testRootNode = "+testRootNode
            +"\n xpath = "+xpath
            +"\n value = ACTUAL_TEXT_VALUE");
    }
    
    // now get it again from doc
    String actualVal1 = null;
    try {
      actualVal1 = 
          XMLUtilities.getTextNodeWithXPath(testRootNode, xpath).getNodeValue(); 
    } catch (Exception e) {
      e.printStackTrace();
      fail("unexpected exception while trying to call"
            +" getTextNodeWithXPath() with testRootNode = "+testRootNode
            +"\n xpath = "+xpath);    
    }
    assertEquals( textVal1, actualVal1);
    
    ////////////////////////////////////////////////////////////////////////////
    
    // set second new val (should overwrite first val)/////
    try {
      XMLUtilities.addTextNodeToDOMTree(testRootNode, xpath, textVal2);
    } catch (Exception e) {
      e.printStackTrace();
      fail("unexpected exception while trying to call"
            +" addTextNodeToDOMTree with testRootNode = "+testRootNode
            +"\n xpath = "+xpath
            +"\n value = ACTUAL_TEXT_VALUE");
    }

    // now get it again from doc
    String actualVal2 = null;
    try {
      actualVal2 = 
          XMLUtilities.getTextNodeWithXPath(testRootNode, xpath).getNodeValue(); 
    } catch (Exception e) {
      e.printStackTrace();
      fail("unexpected exception while trying to call"
            +" getTextNodeWithXPath() with testRootNode = "+testRootNode
            +"\n xpath = "+xpath);    
    }
    assertEquals( textVal2, actualVal2);
    
  }
  


  
  public void testAddAttributeNodeToDOMTree() {
  
    String xpath1 = "/acc:access/test_element/sub_test_element/@testAttrib1";
    String xpath2 = "/acc:access/test_element/sub_test_element/testAttrib2";
    String attribVal1 = "ACTUAL_ATTRIB_VALUE_1";
    String attribVal2 = "ACTUAL_ATTRIB_VALUE_2";
    assertNotNull(testRootNode);
    
    // set first new val /////////
    try {
      System.out.println("// set first new val ///////// = "+attribVal1);
      XMLUtilities.addAttributeNodeToDOMTree(testRootNode, xpath1, attribVal1);
    } catch (Exception e) {
      e.printStackTrace();
      fail("unexpected exception while trying to call"
            +" addAttributeNodeToDOMTree with testRootNode = "+testRootNode
            +"\n xpath = "+xpath1
            +"\n value = "+attribVal1);
    }

    // now get it again from doc
    System.out.println("// now get it again from doc /////////");
    String actualVal1 = null;
    try {
      actualVal1 = 
          XMLUtilities.getAttributeNodeWithXPath(testRootNode, xpath1).getNodeValue(); 
    } catch (Exception e) {
      e.printStackTrace();
      fail("unexpected exception while trying to call"
          +" getAttributeNodeWithXPath with testRootNode = "+testRootNode
          +"\n xpath = "+xpath1);
    }
    System.out.println("doing assertEquals( "+attribVal1+", "+actualVal1+")");
    assertEquals( attribVal1, actualVal1);
    
    ////////////////////////////////////////////////////////////////////////////
    
    // set second new val (should overwrite first val)/////
    try {
      System.out.println("// set second new val (should overwrite first val) ///////// = "+attribVal2);
      XMLUtilities.addAttributeNodeToDOMTree(testRootNode, xpath1, attribVal2);
    } catch (Exception e) {
      e.printStackTrace();
      fail("unexpected exception while trying to call"
    +" addAttributeNodeToDOMTree with testRootNode = "+testRootNode
    +"\n xpath = "+xpath1
    +"\n value = "+attribVal2);
    }

    // now get it again from doc
    String actualVal2 = null;
    System.out.println("// now get it again from doc /////////");
    try {
      actualVal2 = 
          XMLUtilities.getAttributeNodeWithXPath(testRootNode, xpath1).getNodeValue(); 
    } catch (Exception e) {
      e.printStackTrace();
      fail("unexpected exception while trying to call"
          +" getAttributeNodeWithXPath with testRootNode = "+testRootNode
          +"\n xpath = "+xpath1);
    }
    System.out.println("doing assertEquals( "+attribVal2+", "+actualVal2+")");
    assertEquals( attribVal2, actualVal2);
    
  ////////////////////////////////////////////////////////////////////////////
  
    // try an xpath with no "@" in it (should throw a DOMException) /////
    try {
      XMLUtilities.addAttributeNodeToDOMTree(testRootNode, xpath2, attribVal2);
    } catch (DOMException e) {
      System.out.println("OK - *expected* exception while trying to call"
            +" addAttributeNodeToDOMTree with testRootNode = "+testRootNode
            +"\n xpath = "+xpath2
            +"\n value = "+attribVal2);
            
    } catch (Exception e) {
    
      e.printStackTrace();
      fail("unexpected exception while trying to call"
          +" addAttributeNodeToDOMTree with testRootNode = "+testRootNode
          +"\n xpath = "+xpath2
          +"\n value = "+attribVal2);
    }
  }
  

  public void testGetTextNodeWithXPath() {
  
    System.out.println("getTextNodeWithXPath() test...");

    String xpath = "/acc:access/allow[1]/principal[1]";
    String textVal1 = "uid=brooke,o=NCEAS,dc=ecoinformatics,dc=org";

    Node textNode = null;

    try {
      textNode = XMLUtilities.getTextNodeWithXPath(testRootNode, xpath);
    } catch (Exception e) {
      e.printStackTrace();
      fail( "unexpected exception while trying to call"
           +" getTextNodeWithXPath");
    }

    assertNotNull(textNode);
    
    assertEquals(textNode.getNodeValue(), textVal1);

    textNode = null;

  }
  
  

  public void testGetAttributeNodeWithXPath() {
  
    System.out.println("getAttributeNodeWithXPath() test...");

    String xpath = "/acc:access/@id";
    String attribVal1 = "brooke.124.1";

    Node attNode = null;

    try {
      attNode = XMLUtilities.getAttributeNodeWithXPath(testRootNode, xpath);
    } catch (Exception e) {
      e.printStackTrace();
      fail( "unexpected exception while trying to call"
           +" getAttributeNodeWithXPath");
    }

    assertNotNull(attNode);
    
    assertEquals(attNode.getNodeValue(), attribVal1);

    attNode = null;

  }
  
  

  public void testGetNodeWithXPath() {
  
    System.out.println("getNodeWithXPath() test...");

    String xpath = "/acc:access/allow[1]/principal[1]";
    String textVal1 = "uid=brooke,o=NCEAS,dc=ecoinformatics,dc=org";

    Node testNode = null;

    try {
      testNode = XMLUtilities.getNodeWithXPath(testRootNode, xpath);
    } catch (Exception e) {
      e.printStackTrace();
      fail( "unexpected exception while trying to call"
           +" getNodeWithXPath");
    }

    assertNotNull(testNode);
    
    //assume first child is the text node containing this node's "value"
    assertEquals(textVal1, testNode.getFirstChild().getNodeValue());

    testNode = null;

    try {
          XMLUtilities.getNodeWithXPath(testRootNode, null);
    } catch (TransformerException te) {
          System.out.println("OK - *expected* exception while trying to call "
                              +" getNodeWithXPath with NULL xpath");
    } catch (Exception e) {
          fail("unexpected exception while trying to call"
                              +" getNodeWithXPath with NULL xpath");
    }
    try {
          XMLUtilities.getNodeWithXPath(null, "/some/xpath");
    } catch (TransformerException te) {
          System.out.println("OK - *expected* exception while trying to call "
                              +" getNodeWithXPath with NULL root node");
    } catch (Exception e) {
          e.printStackTrace();
          fail("unexpected exception ("+e+")while trying to call"
                              +" getNodeWithXPath with NULL root node");
    }
  }
  


  public void testGetNodeListWithXPath() {
  
    System.out.println("getNodeListWithXPath() test...");

    String xpath = "/acc:access/allow";

    NodeList testList = null;

    try {
      testList = XMLUtilities.getNodeListWithXPath(testRootNode, xpath);
    } catch (Exception e) {
      e.printStackTrace();
      fail( "unexpected exception while trying to call"
           +" getNodeListWithXPath");
    }

    assertNotNull(testList);
    System.out.println("nodeList.getLength() returned "+testList.getLength());
    assertTrue(testList.getLength() > 0);
    assertTrue(testList.getLength()==2);

    assertNotNull(testList.item(0));
    assertNotNull(testList.item(1));

    assertTrue(testList.item(0).getNodeName().equals("allow"));
    assertTrue(testList.item(1).getNodeName().equals("allow"));

    testList = null;

    try {
          XMLUtilities.getNodeListWithXPath(testRootNode, null);
    } catch (TransformerException te) {
          System.out.println("OK - *expected* exception while trying to call "
                              +" getNodeListWithXPath with NULL xpath");
    } catch (Exception e) {
          fail("unexpected exception while trying to call"
                              +" getNodeListWithXPath with NULL xpath");
    }
    try {
          XMLUtilities.getNodeListWithXPath(null, "/some/xpath");
    } catch (TransformerException te) {
          System.out.println("OK - *expected* exception while trying to call "
                              +" getNodeListWithXPath with NULL root node");
    } catch (Exception e) {
          fail("unexpected exception while trying to call"
                              +" getNodeListWithXPath with NULL root node");
    }
  }
  


  public void testGetNodeListAsNodeArray() {
  
    System.out.println("getNodeListAsNodeArray() test...");

    String xpath = "/acc:access/allow";

    Node[] testArray = null;

    try {
      testArray = XMLUtilities.getNodeListAsNodeArray(
                      XMLUtilities.getNodeListWithXPath(testRootNode, xpath));
    } catch (Exception e) {
      e.printStackTrace();
      fail( "unexpected exception while trying to call"
           +" getNodeListAsNodeArray");
    }

    assertNotNull(testArray);
    System.out.println("testArray.length returned "+testArray.length);
    assertTrue(testArray.length > 0);
    assertTrue(testArray.length==2);

    assertNotNull(testArray[0]);
    assertNotNull(testArray[1]);

    assertTrue(testArray[0].getNodeName().equals("allow"));
    assertTrue(testArray[1].getNodeName().equals("allow"));

    testArray = null;

    assertNull(XMLUtilities.getNodeListAsNodeArray(null));
  }
  
  


  public void testGetDOMTreeAsString() {
  
    System.out.println("getDOMTreeAsString() test...");

    String xmlString = null;
    testRootNode = getNewTestRootNode();
    assertNotNull(testRootNode);
    try {
      xmlString = XMLUtilities.getDOMTreeAsString(testRootNode);
    } catch (Exception e) {
      e.printStackTrace();
      fail("unexpected exception while trying to call"
        +" getDOMTreeAsNVP with VALID params");
    }
    assertNotNull(xmlString);

    assertEquals( StringUtil.stripAllWhiteSpace(TEST_XML),
                  StringUtil.stripAllWhiteSpace(xmlString));

    xmlString = null;

    ////////////////
    System.out.println("calling with NULL rootnode...");
    assertNull(XMLUtilities.getDOMTreeAsString(null));
  }

  
  public void testPrint() {

    String resultString         = null;
    ByteArrayOutputStream baos  = null;
    PrintWriter printWriter     = null;


    ////////////////////////////////////////////////////////////////////////////
    System.out.println("print(testRootNode, printWriter) test...");
    
    baos = new ByteArrayOutputStream();
    printWriter = new PrintWriter(baos);
    XMLUtilities.print(testRootNode, printWriter);
    
    resultString = baos.toString();
    
    assertNotNull(resultString);

    assertEquals( StringUtil.stripAllWhiteSpace(TEST_XML),
                  StringUtil.stripAllWhiteSpace(resultString));

    resultString = null;


    ////////////////////////////////////////////////////////////////////////////
    System.out.println("print(testRootNode, printWriter, \"UTF-8\") test...");
    
    baos = new ByteArrayOutputStream();
    printWriter = new PrintWriter(baos);
    XMLUtilities.print(testRootNode, printWriter, "UTF-8");
    
    resultString = baos.toString();
    
    assertNotNull(resultString);

    assertEquals( StringUtil.stripAllWhiteSpace(TEST_XML),
                  StringUtil.stripAllWhiteSpace(resultString));

    resultString = null;


    ////////////////////////////////////////////////////////////////////////////
    baos = new ByteArrayOutputStream();
    printWriter = new PrintWriter(baos);
  
    System.out.println("print(null, printWriter, \"UTF-8\") test...");
    try {
      XMLUtilities.print(null, printWriter, "UTF-8"); 
    } catch (Exception e) {
      e.printStackTrace();
      fail( "unexpected exception while trying to call"
           +" print(null, printWriter, \"UTF-8\")");
    }
    assertNotNull(printWriter);
    resultString = baos.toString();
    assertEquals( "", StringUtil.stripAllWhiteSpace(resultString));

    resultString = null;

    ///
    System.out.println("print(testRootNode, null, \"UTF-8\") test...");
    PrintWriter nullPW = null;
    try {
      XMLUtilities.print(testRootNode, nullPW, "UTF-8"); 
    } catch (Exception e) {
      e.printStackTrace();
      fail( "unexpected exception while trying to call"
           +" print(testRootNode, null, \"UTF-8\")");
    }
    assertNull(nullPW);

    ////////////////////////////////////////////////////////////////////////////
    System.out.println("print(testRootNode, printWriter, \"\") test...");
  
    baos = new ByteArrayOutputStream();
    printWriter = new PrintWriter(baos);
    
    XMLUtilities.print(testRootNode, printWriter, "");
  
    resultString = baos.toString();
  
    assertNotNull(resultString);

    assertEquals( StringUtil.stripAllWhiteSpace(TEST_XML),
                  StringUtil.stripAllWhiteSpace(resultString));

    resultString = null;


    ////////////////////////////////////////////////////////////////////////////
    System.out.println("print(testRootNode, printWriter, null) test...");

    baos = new ByteArrayOutputStream();
    printWriter = new PrintWriter(baos);
  
    XMLUtilities.print(testRootNode, printWriter, null);

    resultString = baos.toString();

    assertNotNull(resultString);

    assertEquals( "", StringUtil.stripAllWhiteSpace(resultString));

    resultString = null;


  }
  

  public void testGetDOMTreeAsXPathMap() {
  
    System.out.println("getDOMTreeAsXPathMap() test...");

    Node rootNode = null;

    ////////////////
    System.out.println("getting test rootnode...");

    try {
      rootNode = XMLUtilities.getXMLReaderAsDOMTreeRootNode(
                                          new StringReader(XPATH_MAP_TEST_XML));
    } catch (Exception e) {
      e.printStackTrace();
      fail( "unexpected exception while trying to call"
           +" getXMLReaderAsDOMTreeRootNode");
    }

    assertNotNull(rootNode);
    assertTrue(rootNode.hasChildNodes());

    System.out.println("testing getDOMTreeAsXPathMap...");
    assertEquals(   XPATH_MAP_TEST_RESULT, 
                    XMLUtilities.getDOMTreeAsXPathMap(rootNode));

    rootNode = null;

    ////////////////
    System.out.println("calling with NULL rootnode...");
    assertNull(XMLUtilities.getDOMTreeAsXPathMap(null));
  }
  
  
  ////////////////////////////////////////////////////////////////////////////////
  //                      E N D   T E S T   M E T H O D S                       //
  ////////////////////////////////////////////////////////////////////////////////

  StringBuffer buff = new StringBuffer();

  public static void main(String args[]) {
    junit.textui.TestRunner.run(XMLUtilitiesTest.class);
    System.exit(0);
  }
  
  private final String XPATH_MAP_TEST_XML 
    = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
    + "<properties>"
    + "  <property1>THIS_IS_PROPERTY_1</property1>"
    + "  <property2>THIS_IS_PROPERTY_2[1]</property2>"
    + "  <property2>THIS_IS_PROPERTY_2[2]</property2>"
    + "  <propertyListA>"
    + "    <propertyA1>THIS_IS_PROPERTY_A_1</propertyA1>"
    + "    <propertyA2>THIS_IS_PROPERTY_A_2[1]</propertyA2>"
    + "    <propertyA2>THIS_IS_PROPERTY_A_2[2]</propertyA2>"
    + "  </propertyListA>"
    + "  <propertyListB>"
    + "    <propertySubListB>"
    + "      <propertyBS1>THIS_IS_PROPERTY_B_SUB_1</propertyBS1>"
    + "    </propertySubListB>"
    + "  </propertyListB>"
    + "</properties>";
  
  private final OrderedMap XPATH_MAP_TEST_RESULT = getXPathMapTestResult(); 
  
  private OrderedMap getXPathMapTestResult() {
  
    OrderedMap map = new OrderedMap();
    
    map.put("/properties/property1[1]",             "THIS_IS_PROPERTY_1");
    map.put("/properties/property2[1]",             "THIS_IS_PROPERTY_2[1]");
    map.put("/properties/property2[2]",             "THIS_IS_PROPERTY_2[2]");
    map.put("/properties/propertyListA[1]/propertyA1[1]",
                                                    "THIS_IS_PROPERTY_A_1");
    map.put("/properties/propertyListA[1]/propertyA2[1]",
                                                    "THIS_IS_PROPERTY_A_2[1]");
    map.put("/properties/propertyListA[1]/propertyA2[2]",
                                                    "THIS_IS_PROPERTY_A_2[2]");
    map.put("/properties/propertyListB[1]/propertySubListB[1]/propertyBS1[1]",
                                                    "THIS_IS_PROPERTY_B_SUB_1");

    return map;
  }
}

