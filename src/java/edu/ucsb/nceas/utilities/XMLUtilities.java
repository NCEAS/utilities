/**
 *  '$RCSfile: XMLUtilities.java,v $'
 *  Copyright: 2002 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: higgins $'
 *     '$Date: 2003-09-12 23:08:43 $'
 * '$Revision: 1.3 $'
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

import edu.ucsb.nceas.utilities.OrderedMap;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.IOException;

import java.util.Stack;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.apache.log4j.Logger;


public class XMLUtilities {

  //output format used by print() method if none specified
  private static String DEFAULT_OUTPUT_FORMAT = "UTF-8";

  private static final String XPATH_SEPARATOR        = "/";
  private static final String ATTRIB_XPATH_SYMBOL    = "@";
  private static final String PREDICATE_OPEN_SYMBOL  = "[";
  private static final String PREDICATE_CLOSE_SYMBOL = "]";

  private static DocumentBuilder domParser = null;

  private static Logger log = Logger.getLogger(XMLUtilities.class.getName());

  /** 
   *  <em>NOTE - NONE OF THESE METHODS ARE THREAD_SAFE</em>. 
   *  Given a string filename, attempts to load XML text from that file and 
   *  parse it into a DOM tree. Then returns the root node of that tree
   * 
   *  @param cpRelativeFilename   <em>CLASSPATH-RELATIVE</em> name of XML 
   *                              textfile to be read and parsed
   *
   *  @return the root node of the DOM tree parsed from the input file
   *
   *  @throws IOException if file cannot be opened or processed
   */
  public static Node getXMLAsDOMTreeRootNode(String cpRelativeFilename) 
                                                            throws IOException {

    return (Node)(getXMLAsDOMDocument(cpRelativeFilename).getDocumentElement());
  }

  /** 
   *  <em>NOTE - NONE OF THESE METHODS ARE THREAD_SAFE</em>. 
   *  Given a Reader, attempts to load XML text from that Reader and 
   *  parse it into a DOM tree. Then returns the root node of that tree
   * 
   *  @param xmlReader  a <code>java.io.Reader</code> from which XML text 
   *                    can be read and parsed
   *
   *  @return the root node of the DOM tree parsed from the input file
   *
   *  @throws IOException if file cannot be opened or processed
   */
  public static Node getXMLReaderAsDOMTreeRootNode(Reader xmlReader) 
                                                            throws IOException {

    return (Node)(getXMLReaderAsDOMDocument(xmlReader).getDocumentElement());
  }


  /** 
   *  <em>NOTE - NONE OF THESE METHODS ARE THREAD_SAFE</em>. 
   *  Given a string filename, attempts to load XML text from that file and 
   *  parse it into a DOM tree. Then returns the corresponding Document
   * 
   *  @param cpRelativeFilename   <em>CLASSPATH-RELATIVE</em> name of XML 
   *                              textfile to be read and parsed
   *
   *  @return the Document corresponding to the XML parsed from the input file
   *
   *  @throws IOException if file cannot be opened or processed
   */
  public static Document getXMLAsDOMDocument(String cpRelativeFilename)
                                                            throws IOException {
    InputStreamReader isReader = null;
    try {
      isReader = IOUtil.getResourceAsInputStreamReader(cpRelativeFilename);
    } catch(Exception e) {
      FileNotFoundException fnfe = new FileNotFoundException("File \""
                +cpRelativeFilename+"\" doesn't exist or cannot be read."
                                          +"Original exceptions was: "+e);
      fnfe.fillInStackTrace();
      throw fnfe;
    }
    return getXMLReaderAsDOMDocument(isReader);
  }
  
  
  
  
  /** 
   *  <em>NOTE - NONE OF THESE METHODS ARE THREAD_SAFE</em>. 
   *  Given a Reader, attempts to load XML text from that Reader and
   *  parse it into a DOM tree. Then returns the corresponding Document
   *
   *  @param xmlReader  a <code>java.io.Reader</code> from which XML text 
   *                    can be read and parsed
   *
   *  @return the DOM Document containing the XML parsed from the input Reader
   *
   *  @throws IOException if file cannot be opened or processed
   */
  public static Document getXMLReaderAsDOMDocument(Reader xmlReader)
                                                            throws IOException {

    Document    doc = null;
    if (xmlReader==null) {
    
      IOException ioe 
          = new IOException("getXMLReaderAsDOMDocument received a null Reader");
      ioe.fillInStackTrace();
      throw ioe;
    }
    InputSource in  = new InputSource(xmlReader);
    try {
      doc = getDOMParser().parse(in);
    } catch(SAXException e) {
      log.error("SAXException parsing Reader\n"+e);
      e.printStackTrace();
    } catch(IOException ioe) {
      log.error("IOException parsing Reader\n"+ioe);
      ioe.printStackTrace();
    } finally {
      try { if (xmlReader!=null) xmlReader.close(); }
      catch (IOException i) {
        log.error("IOException closing Reader "+i);
        i.printStackTrace();
      }
    }
    return doc;
  }
  
  
  
  /** 
   *  <em>NOTE - NONE OF THESE METHODS ARE THREAD_SAFE</em>. 
   *  This method take an XPATH expression and follows it through a DOM tree, 
   *  creating nodes along the way as needed, if they don't already exist. At
   *  the end of the XPATH it will create a TEXT_NODE and populate it with the 
   *  String provided. If the text node already exists, its value is replaced
   * 
   *  @param rootNode   the root node of a DOM subtree
   *
   *  @param xpath      A <code>String</code> representation of an XPATH 
   *                    expression which defines the unique location of the 
   *                    required new node in the DOM tree. If this XPATH 
   *                    expression does not define a unique node, a 
   *                    DOMException is thrown
   *
   *  @param textValue  the text value to be insterted in the TEXT_NODE at the 
   *                    end of this xpath
   *
   *  @throws <code>org.w3c.dom.DOMException</code> if this XPATH 
   *                    expression does not define a unique node
   */
  private static Stack nodesToCreate = new Stack();
    
  public static void addTextNodeToDOMTree(Node rootNode, String xpath,
                  String textValue) throws DOMException, TransformerException { 

    log.debug("XMLUtilities.addTextNodeToDOMTree(); xpath="+xpath
              +"\nrootnode = "+rootNode);

    Node lastRealNode  = getLastExistingNodeInXPath(rootNode, xpath);

    //lastRealNode is now the last node in the xpath that actually exists

    String nextNodeName = null;
    Document doc = rootNode.getOwnerDocument();
    
    while (!nodesToCreate.isEmpty()) {
    
      nextNodeName = popNextNodeString(nodesToCreate);
      log.debug("in while loop; -> nextNodeName = "+nextNodeName);
    
      if (nextNodeName==null) {
        DOMException de2 = new DOMException(DOMException.SYNTAX_ERR, 
                                    "tried to create a node with null name!"
                                    +"\n parent = "+lastRealNode.getNodeName());
        de2.fillInStackTrace();
        throw de2;
      }
      

      Element newElement = doc.createElement(stripXPathIndex(nextNodeName));
      log.debug("in while loop; -> newElement created = "+newElement);
    
      lastRealNode.appendChild(newElement);
      log.debug("in while loop; -> DONE lastRealNode.appendChild(newElement)");
      lastRealNode = newElement;
    } 
    //check to see if last real node has any children already...
    NodeList nl = lastRealNode.getChildNodes();
    if (nl!=null && nl.getLength()>0) {
      // if so, and if one of these is a text element, change it to new value
      // NOTE: if there's more than one text node, only the first one gets 
      // changed!
      Node[] childArray = getNodeListAsNodeArray(nl);
      for (int i=0; i<childArray.length; i++) {
        if (childArray[i].getNodeType()==Node.TEXT_NODE) {
          childArray[i].setNodeValue(textValue);
          break;
        }
      }
    } else {
      //otherwise, just add a new text node
      Text newElement = doc.createTextNode(textValue);
      lastRealNode.appendChild(newElement);    
    }
  }
  
  
  /** 
   *  <em>NOTE - NONE OF THESE METHODS ARE THREAD_SAFE</em>. 
   *  This method take an XPATH expression and follows it through a DOM tree, 
   *  creating nodes along the way as needed, if they don't already exist. At
   *  the end of the XPATH it will create an ATTRIBUTE_NODE and populate it with  
   *  the String provided
   * 
   *  @param rootNode   the root node of a DOM subtree
   *
   *  @param xpath      A <code>String</code> representation of an XPATH 
   *                    expression which defines the unique location of the 
   *                    required new node in the DOM tree. If this XPATH 
   *                    expression does not define a unique node, a 
   *                    DOMException is thrown.  If the attribute exists, its 
   *                    value is changed to the new one 
   *                    <em>NOTE</em> - the xpath must define an 
   *                    attribute, using the "@" notation - for example: 
   *                      if the attribute name is my_attrib, then the xpath 
   *                      would look like this:
   *                      /root/elem1/elem1a/@my_attrib
   *
   *  @param textValue  the text value to be insterted in the ATTRIBUTE_NODE at  
   *                    the end of this xpath
   *
   *  @throws <code>org.w3c.dom.DOMException</code> if this XPATH expression
   *                    does not define a unique node
   */
  public static void addAttributeNodeToDOMTree(Node rootNode, String xpath,
                  String attribValue) throws DOMException, TransformerException { 

    log.debug("XMLUtilities.addAttributeNodeToDOMTree(); xpath="+xpath
              +"\nattribValue = "+attribValue
              +"\nrootNode = "+rootNode);

    if (xpath.indexOf("@")<0) {
      DOMException de1 = new DOMException(DOMException.SYNTAX_ERR, 
                                  "call to addAttributeNodeToDOMTree() with an "
                                  +"xpath that does not contain an attribute "
                                  +" (no @ symbol found in xpath: "
                                  +xpath+" )");
      de1.fillInStackTrace();
      throw de1;
    }
    
    Node lastRealNode  = getLastExistingNodeInXPath(rootNode, xpath);

    //lastRealNode is now the last node in the xpath that actually exists
    
    String nextNodeName = null;
    Document doc = rootNode.getOwnerDocument();
    boolean attribExists = false;
    
    // if we passed an xpath to getLastExistingNodeInXPath that *all* exists 
    // already, then nodesToCreate will be empty, but lastRealNode will have a 
    // value (it will be the node corresponding to the very last element in the 
    // xpath)
    // 
    if (nodesToCreate.isEmpty() && lastRealNode!=null) {
      
      if (lastRealNode.getNodeType()==Node.ATTRIBUTE_NODE) {
        attribExists = true;
      } else {
        DOMException de2 = new DOMException(DOMException.HIERARCHY_REQUEST_ERR, 
            "xpath defines a node that is *NOT* an attribute node"
            +"\n xpath = "+xpath
            +";\n nodeName = "+lastRealNode.getNodeName()
            +";\n nodeType = "+lastRealNode.getNodeType());
        de2.fillInStackTrace();
        throw de2;
      }
      nextNodeName = lastRealNode.getNodeName();
    }

    
    while (!nodesToCreate.isEmpty()) {
    
      nextNodeName = popNextNodeString(nodesToCreate);
    
      if (nextNodeName==null) {
        DOMException de3 = new DOMException(DOMException.SYNTAX_ERR, 
                                    "tried to create a node with null name!"
                                    +"\n parent = "+lastRealNode.getNodeName());
        de3.fillInStackTrace();
        throw de3;
      }
      
      if (nextNodeName.startsWith("@")) {
      
        //we've found the attribute - break and add it
        break;
        
      } else {
      
        //keep looping - need to add nodes to path until we get to attribute
        Element newElement = doc.createElement(stripXPathIndex(nextNodeName));
    
        lastRealNode.appendChild(newElement);
        lastRealNode = newElement;
      }
    }
    
    
    //check if it already exists...
    if (attribExists) {
    
      // if so, change the value of the existing attribute
      Attr attribNode = (Attr)lastRealNode;
      attribNode.setValue(attribValue);

    } else if (nextNodeName!=null && nextNodeName.startsWith("@")) {

      String attribName = (attribExists)? 
                                      nextNodeName : nextNodeName.substring(1);
    
      Element lrnElem = (Element)lastRealNode;
      
        //...otherwise, add it as a new attribute
        lrnElem.setAttribute(attribName, attribValue);
    } else {
    
      DOMException de2 = new DOMException(DOMException.HIERARCHY_REQUEST_ERR, 
                                  "addAttributeNodeToDOMTree() was unable to  "
                                  +"create or update attribute at this xpath: "
                                  +xpath+" )");
      de2.fillInStackTrace();
      throw de2;
    }
  }  
  

  /** 
   *  <em>NOTE - NONE OF THESE METHODS ARE THREAD_SAFE</em>. 
   *  This method returns a <em>unique</em> Node.TEXT_NODE defined by the XPATH  
   *  expression provided
   *
   *  <em>***NOTE***</em> this method will also return the contents of CDATA 
   *  sections 
   * 
   *  @param rootNode   the root node of a DOM subtree
   *
   *  @param xpath      A <code>String</code> representation of an XPATH 
   *                    expression which defines the unique location of the 
   *                    required existing node in the DOM tree. If this XPATH 
   *                    expression does not define a unique node, a DOMException
   *                    is thrown
   *
   *  @return the <code>Node</code> (that is a TEXT_NODE) uniquely defined by  
   *                    this xpath. null if the xpath does not point to a valid 
   *                    text node
   *
   *  @throws <code>org.w3c.dom.DOMException</code> if this XPATH expression
   *                    does not define a unique node
   *
   *  @throws <code>TransformerException</code> if there is a problem executing  
   *                    the XPATH expression
   */
  public static Node getTextNodeWithXPath(Node rootNode, String xpath) 
                                    throws DOMException, TransformerException { 

    log.debug("XMLUtilities.getTextNodeWithXPath() called; xpath="+xpath);
    
    Node targetNode     = getNodeWithXPath(rootNode, xpath);
    
    if (targetNode==null) {
      
      log.debug("node pointed to by xpath is null; returning null");
      return null;
    }
    // targetNode *should* only have one child node, which the actual text it 
    // contains (- this is a strange confusing DOM2 thang - the element's text
    // value is in fact considered to be a subnode of the element)
    NodeList targetList = targetNode.getChildNodes();

    // if node doesn't exist, return null
    if (targetList.getLength()==0) {
      log.debug("text node doesn't yet exist: "+xpath);
      return null;
    }

    // Note that it is possible to have "mixed" content - i.e. the text 
    // "node(s)" PLUS real sub-elements, hence the next check
    for (int nodeIndex=0; nodeIndex < targetList.getLength(); nodeIndex++) {

      Node textNode = targetList.item(nodeIndex);
      if (textNode.getNodeType()==Node.TEXT_NODE 
                          || textNode.getNodeType()==Node.CDATA_SECTION_NODE) {
        log.debug("FOUND VALUE = "+textNode.getNodeValue());
        return textNode;
      }
    }
    return null;
  } 
  
  
  /** 
   *  <em>NOTE - NONE OF THESE METHODS ARE THREAD_SAFE</em>. 
   *  This method returns a <em>unique</em> Node.ATTRIBUTE_NODE defined by the   
   *  XPATH expression provided
   * 
   *  @param rootNode   the root node of a DOM subtree
   *
   *  @param xpath      A <code>String</code> representation of an XPATH 
   *                    expression which defines the unique location of the 
   *                    required ATTRIBUTE node in the DOM tree. If this XPATH 
   *                    expression does not define a unique node, a DOMException
   *                    is thrown
   *
   *  @return the <code>Node</code> (that is an ATTRIBUTE_NODE) uniquely defined   
   *                    by this xpath. null if the xpath does not point to a  
   *                    valid attribute node
   *
   *  @throws <code>org.w3c.dom.DOMException</code> if this XPATH expression
   *                    does not define a unique node
   *
   *  @throws <code>TransformerException</code> if there is a problem executing  
   *                    the XPATH expression
   */
  public static Node getAttributeNodeWithXPath(Node rootNode, String xpath) 
                                    throws DOMException, TransformerException { 

    log.debug("XMLUtilities.getAttributeNodeWithXPath() called; xpath="+xpath);
    
    Node targetNode = getNodeWithXPath(rootNode, xpath);
    
    if (targetNode==null) {
      
      log.debug("node pointed to by xpath is null; returning null");
      return null;
    }
    
    // if not an attribute node, throw an exception
    if (targetNode!=null && targetNode.getNodeType()!=Node.ATTRIBUTE_NODE) {
      DOMException de = new DOMException(DOMException.INDEX_SIZE_ERR, 
                              "found a node at this xpath: "+xpath
                                          +" that is *NOT* an attribute node!");
      de.fillInStackTrace();
      throw de;
    }
    
    return targetNode;
  } 


  /** 
   *  <em>NOTE - NONE OF THESE METHODS ARE THREAD_SAFE</em>. 
   *  This method returns a <em>unique</em> node defined by the XPATH expression 
   *  provided
   * 
   *  @param rootNode   the root node of a DOM subtree
   *
   *  @param xpath      A <code>String</code> representation of an XPATH 
   *                    expression which defines the unique location of the 
   *                    required existing node in the DOM tree. If this XPATH 
   *                    expression does not define a unique node, a DOMException
   *                    is thrown
   *
   *  @return the Node that is uniquely defined by this xpath. null if the xpath 
   *                    does not point to a valid node  
   *
   *  @throws <code>org.w3c.dom.DOMException</code> if this XPATH expression
   *                    does not define a unique node
   *
   *  @throws <code>TransformerException</code> if there is a problem executing  
   *                    the XPATH expression
   */
  public static Node getNodeWithXPath(Node rootNode, String xpath) 
                                    throws DOMException, TransformerException {

    log.debug("XMLUtilities.getNodeWithXPath() called; xpath="+xpath);

    NodeList nodeList = getNodeListWithXPath(rootNode, xpath);

    if (nodeList==null) {
      log.debug("nodeList is null; returning null");
      return null;
    }
    log.debug("nodeList.getLength() = "+nodeList.getLength());

    if (nodeList.getLength() > 1) {
      // XPATH expression must point to a unique 
      // DOM node, otherwise we throw an exception:
      DOMException de = new DOMException(DOMException.INDEX_SIZE_ERR, 
        "Non-unique XPATH expression: "+xpath+"\n ("
        +nodeList.getLength()+" nodes match");
      de.fillInStackTrace();
      throw de;
    }
    return nodeList.item(0);
  }



  /** 
   *  <em>NOTE - NONE OF THESE METHODS ARE THREAD_SAFE</em>. 
   *  This method returns a <code>NodeList</code> of nodes matching the XPATH
   *  expression provided
   *
   *  @param rootNode   the root node of a DOM subtree
   *
   *  @param xpath      A <code>String</code> representation of an XPATH
   *                    expression which defines the location of one or more 
   *                    required existing nodes in the DOM tree. 
   *
   *  @return the <code>NodeList</code> that contains all the nodes matching the 
   *                    XPATH expression provided. null if the xpath does not 
   *                    point to any valid nodes
   *
   *  @throws <code>TransformerException</code> if there is a problem executing  
   *                    the XPATH expression
   */
  public static NodeList getNodeListWithXPath(Node rootNode, String xpath) 
                                                  throws TransformerException { 

    log.debug("XMLUtilities.getNodeListWithXPath() called; xpath="+xpath
                              +"\nrootnode = "+rootNode);

    NodeList nodeList = null;
    
    if (xpath==null) {
    
      TransformerException t 
            = new TransformerException(
                    "XMLUtilities.getNodeListWithXPath() received NULL xpath");
      t.fillInStackTrace();
      throw t;
    }
    if (rootNode==null) {
    
      TransformerException t 
            = new TransformerException(
                    "XMLUtilities.getNodeListWithXPath() received NULL rootNode");
      t.fillInStackTrace();
      throw t;
    }
    
    try {
      nodeList = XPathAPI.selectNodeList(rootNode, xpath.trim(), rootNode);
      
    } catch (TransformerException e) {
      e.printStackTrace();
      log.error("TransformerException doing XPath search for nodelist"
                                  +" at xpath: "+xpath+"\nException is: "+e);
      throw e;
    } 
    
    if (nodeList==null) {
      log.debug("NULL NodeList received - API docs say this should"
                                              +" never happen! xpath = "+xpath);
      return null;
    } else if (nodeList.getLength()<1) {
      log.debug("NodeList length = 0; No nodes exist for this xpath: "
                                                                        +xpath);
      return null;
    } 
    return nodeList;
  }

  
  /**
   *   Utility method to get a NodeList as an array of Nodes. This is needed 
   *   because we have to pull the values from the NodeList in reverse order, 
   *   since the call to the item(i) method actually *removes* that item from the 
   *   NodeList instead of just "peek"ing at it. This in turn reduces the 
   *   length of the NodeList and re-indexes all the remaining entries...
   * 
   *   @param nList        the NodeList to be converted to an array 
   * 
   *   @return the Node[]  array representation of this NodeList, in the 
   *                       original order
   */
  public static Node[] getNodeListAsNodeArray(NodeList nList) {

    if (nList==null) return null;
    
    int nListLength = nList.getLength();

    if (nListLength==0) return null;
    
    Node[] nodeArray = new Node[nListLength];
    
    for (int i = nListLength-1; i>-1; i--) {

      nodeArray[i] = nList.item(i);
    }
    return nodeArray;
  }

  

  /** 
   *  <em>NOTE - NONE OF THESE METHODS ARE THREAD_SAFE</em>. 
   *  This method can walk a DOM subtree (based at the passed Node), and 
   *  return it as a string
   * 
   *  @param node the root node of a DOM subtree
   *
   *  @return <code>String</code> representation of the DOM tree
   */
  public static String getDOMTreeAsString(Node node) {
      
      if (node==null) return null;  
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintWriter printWriter = new PrintWriter(baos);

      try {
        print(node, printWriter);
      } catch (Exception e) {
        String msg = "getDOMTreeAsString() - unexpected Exception: "+e+"\n";
        log.error(msg);
        printWriter.println(msg);
        e.printStackTrace(printWriter);
      } finally {
        try {
          printWriter.flush();
          baos.flush();
          baos.close();
          printWriter.close();
        } catch (IOException ioe) {}
      }
      return baos.toString();
  }

  /** 
   *  <em>NOTE - NONE OF THESE METHODS ARE THREAD_SAFE</em>. 
   *  This method can walk a DOM subtree (based at the passed Node), and 
   *  print it to the PrintWriter provided, using the encoding defined
   *  in the DEFAULT_OUTPUT_FORMAT variable elsewhere in this class. 
   *  Does *not* flush or close PrintWriter after use
   * 
   *  @param node the root node of a DOM subtree
   *  @param <code>PrintWriter</code> to which output will be printed
   */
   public static void print(Node node, PrintWriter printWriter) {
     
     print(node, printWriter, DEFAULT_OUTPUT_FORMAT);
   }
   
  /** 
   *  <em>NOTE - NONE OF THESE METHODS ARE THREAD_SAFE</em>. 
   *  This method can walk a DOM subtree (based at the passed Node), and 
   *  print it to the PrintWriter provided, using the encoding provided. 
   *  Does *not* flush or close PrintWriter after use
   * 
   *  @param node         the root node of a DOM subtree
   *
   *  @param printWriter  the <code>PrintWriter</code> to which output will be 
   *                      printed
   *
   *  @param encoding     the <code>String</code> defining the output format
   *                      (e.g. UTF-8 etc)
   */
   public static void print(Node node, 
                            PrintWriter printWriter, String encoding) {

      if (node==null)         return;  
      if (printWriter==null)  return;  
      if (encoding==null)     return;  
      if (encoding.trim().equals("")) encoding = DEFAULT_OUTPUT_FORMAT;  
      try {
        // Read the entire document into memory
        
        Document document = node.getOwnerDocument();
        if (document==null) return;
        OutputFormat format 
         = new OutputFormat(document, encoding, true);
        format.setLineSeparator("\r\n");
        format.setLineWidth(72);
        format.setIndent(2);
        format.setPreserveSpace(false);
        XMLSerializer serializer 
         = new XMLSerializer(printWriter, format);
        serializer.serialize(document);     
      } catch (IOException e) {
        log.error("IOException doing print(): "+e); 
        e.printStackTrace(printWriter);
      } 
  }


  /**
   *  Returns an <code>edu.ucsb.nceas.utilities.OrderedMap</code> containing the 
   *  entire DOM tree rooted at the rootNode, encoded as key/value pairs, where 
   *  the "key" is the XPath of the node, and the "value" is its text value.
   *
   *  @param rootNode      the root node of the DOM tree to be encoded as 
   *                        XPath/Value mappings
   *
   *  @return returnNVPMap  an <code>edu.ucsb.nceas.utilities.OrderedMap</code> 
   *                        containing the resulting xpath/value pairs in the 
   *                        correct order
   */
   
  public static OrderedMap getDOMTreeAsXPathMap(Node rootNode) {
  
    if (rootNode==null) return null;
    OrderedMap returnMap = new OrderedMap();
    getDOMTreeAsXPathMap(rootNode, "/"+rootNode.getNodeName(), returnMap);
    return returnMap;
  } 
   
   
   
   
 
   
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//
//  P R I V A T E   C O N V E N I E N C E   M E T H O D S 
//
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

  

  /**
   *  Given a starting node and its XPATH, returns the entire DOM tree rooted at 
   *  this node, encoded as key/value pairs in the Map provided, where the 
   *  "key" is the XPath of the node, and the "value" is its text value 
   *  (*does* include CDATA sections).
   *
   *  Procedure is as follows:
   *  Get attributes, and for each attribute, create a NVP 
   *  with xpath/@attribute=value
   *  then get this node's children
   *  get subset that are *not* text nodes; for each, RECURSE
   *  get subset that *are* text nodes; for each, check if empty (or just 
   *  \n etc.) and if so discard. Otherwise, concatenate them and create a 
   *  NVP with xpath/@attribuite=concat_values
   *
   *  @param startNode  the starting node which is the root of the DOM tree to 
   *                    be encoded
   *
   *  @param xpath the XPath of this starting node
   *
   *  @param returnNVPMap the Map in which the resulting key/value pairs will  
   *                be stored - NOTE that this method has no return value - 
   *                the results are added to this Map that you provide
   */  //re-use one String instead of creating thousands of new objects:
  private static final String BLANK = "";
  //re-use one StringBuffer instead of creating thousands of new objects:
  private static final StringBuffer buff = new StringBuffer();
  //
  private static void getDOMTreeAsXPathMap(Node startNode, String xpath, 
                                                           Map returnNVPMap) {
  
    Node next = null;
    String nextAttribVal = null;
    
    // then get this node's attributes
    NamedNodeMap attribList = startNode.getAttributes();
    Node[] attribNodeArray = XMLUtilities.getNamedNodeMapAsNodeArray(attribList);
    if (attribNodeArray!=null) {
      
      int attribNodeArrayLength = attribNodeArray.length;

      for (int i = 0; i < attribNodeArrayLength; i++) {

        // and for each attribute, create a NVP with xpath/@attribuite=value
        next = attribNodeArray[i];
        if (next!=null) {
          buff.delete(0,buff.length());
          buff.append(xpath);
          buff.append(XPATH_SEPARATOR);
          buff.append(ATTRIB_XPATH_SYMBOL);
          buff.append(next.getNodeName());
          nextAttribVal = StringUtil.stripTabsNewLines(next.getNodeValue());
          returnNVPMap.put(buff.toString(),nextAttribVal);
        }
      }
    }

    // then get this node's children
    NodeList  childNodes  = startNode.getChildNodes();
    String    nextString  = null;
    
    StringBuffer textNodeBuff = new StringBuffer(); //textNodeBuff used to
                                                    //concat textnode contents
    Node[] childNodeArray = XMLUtilities.getNodeListAsNodeArray(childNodes);

    if (childNodeArray!=null) {

      int numChildren = childNodeArray.length;
                                                    
      String[] currentNamesArray = new String[numChildren];
      
      for (int i = 0; i < numChildren; i++) {

        next = childNodeArray[i];

        if (next!=null) {
        
          switch (next.getNodeType()) {
          
            // get subset that *are* text nodes; for each, check if empty (or  
            // just \n etc.) and if so discard. Otherwise, concatenate them and  
            // create a NVP with xpath/@attribute=concat_values
            case Node.TEXT_NODE:
            case Node.CDATA_SECTION_NODE:
            {
              nextString = next.getNodeValue();
              
              if (nextString!=null 
                  && !BLANK.equals(StringUtil.stripAllWhiteSpace(nextString))) {
                textNodeBuff.append(nextString);
              } 
              break;
            }
             
            // get subset that are *not* text nodes; for each, RECURSE
            default:
            {              
              buff.delete(0,buff.length());
              buff.append(xpath);
              buff.append(XPATH_SEPARATOR);
              buff.append(next.getNodeName());
              buff.append(getNextXPathPredicate(next.getNodeName(), 
                                                currentNamesArray, i));
              getDOMTreeAsXPathMap(next, buff.toString(), returnNVPMap);

              break;
            }
          }  
        }
      }
    } 
    if (textNodeBuff.length()>0) returnNVPMap.put(xpath,textNodeBuff.toString());
  }
  
  
    
  /**
   *  Utility method to get a NamedNodeMap as an array of Nodes. This is needed 
   *  because we have to pull the values from the NamedNodeMap in reverse order, 
   *  since the call to the item(i) method actually *removes* that item from the 
   *  NamedNodeMap instead of just "peek"ing at it. This in turn reduces the 
   *  length of the NamedNodeMap and re-indexes all the remaining entries...
   *
   *  @param nMap         the NamedNodeMap to be converted to an array 
   *
   *  @return the Node[]  array representation of this NamedNodeMap, in the 
   *                      original order
   */
  private static Node[] getNamedNodeMapAsNodeArray(NamedNodeMap nMap) {

    if (nMap==null) return null;
    
    int nMapLength = nMap.getLength();

    if (nMapLength==0) return null;
    
    Node[] nodeArray = new Node[nMapLength];
    
    for (int i = nMapLength-1; i>-1; i--) {

      nodeArray[i] = nMap.item(i);
    }
    return nodeArray;
  }

   
  //
  //  utility method for getDOMTreeAsNVP() method:
  //  keeps a tally of the XPath keys that have already been used, and if the 
  //  passed key duplicates a previous one, increment the [n] predicate before 
  //  adding this one
  //
  //  @param currentNodeName  the String representation of the current node name 
  //
  //  @param currentNamesArray the String[] array containing the list of node 
  //                          names already encountered in this recursive pass
  //
  //  @param currentNamesArrayIndex the int array index of the name that was 
  //                          last added to the currentNamesArray
  //
  private static final StringBuffer predicateBuff = new StringBuffer(); 
  //
  private static String getNextXPathPredicate(String currentNodeName, 
                      String[] currentNamesArray, int currentNamesArrayIndex) {

    if (currentNodeName==null || currentNodeName.equals("")) return "";
    if (currentNamesArrayIndex > currentNamesArray.length - 1) return "";
    
    currentNamesArray[currentNamesArrayIndex] = currentNodeName;
    
    int predicate = 0;
    
    for (int i=0; i<currentNamesArrayIndex + 1; i++) {
      if (currentNamesArray[i]!=null 
                  && currentNamesArray[i].equals(currentNodeName)) predicate++;
    }
    predicateBuff.delete(0,predicateBuff.length());
    predicateBuff.append(PREDICATE_OPEN_SYMBOL);
    predicateBuff.append(String.valueOf(predicate));
    predicateBuff.append(PREDICATE_CLOSE_SYMBOL);
    return predicateBuff.toString();
  }


  /** Returns a sorted list of attributes. */
  private static Attr[] sortAttributes(NamedNodeMap attrs) {

    int len = (attrs != null) ? attrs.getLength() : 0;
    Attr array[] = new Attr[len];
    for (int i = 0; i < len; i++) {
      array[i] = (Attr) attrs.item(i);
    }
    for (int i = 0; i < len - 1; i++) {
      String name = array[i].getNodeName();
      int index = i;
      for (int j = i + 1; j < len; j++) {
        String curName = array[j].getNodeName();
        if (curName.compareTo(name) < 0) {
          name  = curName;
          index = j;
        }
      }
      if (index != i) {
        Attr temp    = array[i];
        array[i]     = array[index];
        array[index] = temp;
      }
    }
    return (array);
  }




  /** Normalizes the given string. */
  private static String normalize(String s) {

    StringBuffer str = new StringBuffer();

    int len = (s!=null) ? s.length() : 0;
    for (int i = 0; i < len; i++) {
      char ch = s.charAt(i);
      switch (ch) {
      case '<':
        str.append("&lt;");
        break;
      case '>':
        str.append("&gt;");
        break;
      case '&':
        str.append("&amp;");
        break;
      case '"':
        str.append("&quot;");
        break;
      case '\r':
      case '\n':
      default:
        str.append(ch);
      }
    }
    return (str.toString());
  }



  /**
   * Set up a DOM parser for reading an XML document
   *
   * @return   a DOM parser object for parsing
   */
  private static DocumentBuilder getDOMParser() {
  
    if (domParser==null) {
      try {
        DocumentBuilderFactory factory 
        = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        
        //ignore whitespace, CR's etc in file when parsing:
        //factory.setIgnoringContentElementWhitespace(true);
        
        ////////////////////////////////////////////////////////////////////////
        //
        // see http://xml.apache.org/xerces2-j/features.html
        ////////////////////////////////////////////////////////////////////////

        factory.setAttribute(
              "http://apache.org/xml/features/nonvalidating/load-external-dtd", 
              new Boolean(false));

        ////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////
        
        domParser = factory.newDocumentBuilder();
        if (domParser != null) {
          log.debug("Parser created is: "+domParser.getClass().getName());
        } else {
          log.error("Unable to create DOM Parser!");
        }
      } catch (ParserConfigurationException pce) {
        log.error("Exception while creating DOM parser!\n"+pce.getMessage());
      }
    }
    return domParser;
  }

  
  // pops last-added object from the Stack provided. Returns null if popped 
  // Object is null or is not an instance of String. Otherwise, casts Object to 
  // a String and returns it.
  private static Object lastObj = null;
  private static int lastIndex  = 0;
  //
  private static String popNextNodeString(Stack nodeStack) {
    
    lastObj = nodeStack.pop();
    if (lastObj==null || !(lastObj instanceof String)) return null;
    return (String)lastObj;
  }
  
  
  
  // given a single node name (ie not a full xpath with "/"s in it), looks for 
  // and removes an xpath-style index of the form "[x]", returning the resulting 
  // String
  private static int bracketIndex = 0;
  //
  private static String stripXPathIndex(String xpathSingleNodeName) {
  
    bracketIndex = xpathSingleNodeName.indexOf("[");
    if (bracketIndex > -1) {
      xpathSingleNodeName = xpathSingleNodeName.substring(0,bracketIndex);
    }
    return xpathSingleNodeName;
  }
  
  
  
  // given a DOM rooted at the rootNode, and an xpath of the form: 
  //                /root/elem_a/subelem_b/subsubelem_c/lastelem_d
  // this method starts at the last element (lastelem_d) and checks to see if 
  // that element actually exists in the DOM. If not, the last element is put 
  // onto a stack (nodesToCreate), and the process is repeated for the next 
  // element up the path (subsubelem_c). If an element in the xpath is found to 
  // be existing in the DOM, that node is returned.  The calling method can then 
  // use the stack (nodesToCreate) to create the nodes that are not present, as 
  // children of the last existing node (i.e. the return value from this method)
  //
  private static Node getLastExistingNodeInXPath(Node rootNode, String xpath) 
                                    throws DOMException, TransformerException { 

    if (   rootNode==null || xpath==null 
        || xpath.trim().equals("") || xpath.indexOf("/") < 0 
        || xpath.indexOf("*")>-1   || xpath.indexOf(rootNode.getNodeName())<0) {
        
      DOMException de1 = new DOMException(DOMException.SYNTAX_ERR, 
                              "XPATH expression does not define a unique node; "
                              +"\n xpath    = "+xpath
                              +"\n rootNode = "+rootNode);
      de1.fillInStackTrace();
      throw de1;
    }
    
    Node lastRealNode  = null;
    nodesToCreate.clear();

    // starting condition: existingPath = xpath
    // stop repeating when: lastRealNode!=null
    // each loop, do: existingPath = stepBackUpPath(existingPath, nodesToCreate)
    for (String existingPath = xpath; 
                lastRealNode==null;
                  existingPath = stepBackUpPath(existingPath, nodesToCreate)) {
    
      //check if trimmed xpath points to a real node
      lastRealNode = getNodeWithXPath(rootNode, existingPath);
      if (existingPath.indexOf("/")<0) break;
    }
    
    //lastRealNode is now the last node in the xpath that actually exists
    //note that lastRealNode's name has also been added to the nodesToCreate 
    //stack - (this is how for () loop works - does increment before next check)
    //so we need to pop & discard it:
    nodesToCreate.pop();
    
    if (lastRealNode==null) {
      DOMException de3 = new DOMException(DOMException.SYNTAX_ERR, 
                        "XPATH expression does not contain any existing nodes "
                        +"- not even the root; "
                        +"\n xpath         = "+xpath
                        +"\n rootNode name = "+rootNode.getNodeName());
      de3.fillInStackTrace();
      throw de3;
    }
    
    return lastRealNode;
  }  
  
  
  // Given a String representation of an xpath, removes the last path element, 
  // removes any [square bracket] indices, and pushes it to the Stack provided. 
  // The Stack is changed by reference -  
  // the edited xpath String is the return value for this method
  private static String stringToPush = null;
  //  
  private static String stepBackUpPath(String existingPath, Stack nodesToCreate) {
    
    stringToPush = existingPath.substring(existingPath.lastIndexOf("/") + 1);
    nodesToCreate.push(stringToPush);
    log.debug("stepBackUpPath just added to Stack: "+nodesToCreate.peek());
    log.debug("Stack is now: "+nodesToCreate.toString());
    
    return existingPath.substring(0, existingPath.lastIndexOf("/"));
  }
  
}