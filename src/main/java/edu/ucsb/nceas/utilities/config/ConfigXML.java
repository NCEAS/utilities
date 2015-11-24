/**
 *  '$RCSfile: ConfigXML.java,v $'
 *  Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author$'
 *     '$Date$'
 * '$Revision$'
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

package edu.ucsb.nceas.utilities.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import edu.ucsb.nceas.utilities.config.exception.ElementNotFoundException;
import edu.ucsb.nceas.utilities.config.exception.IndexTooLargeException;



/**
 * This class is designed to store configuration information in
 * an XML file. The concept is similar to that of a Properties
 * file except that using the XML format allows for a hierarchy
 * of properties and repeated properties.
 *
 * All 'keys' are element names, while values are always stored
 * as XML text nodes. The XML file is parsed and stored in
 * memory as a DOM object.
 *
 * Note that nodes are specified by node tags rather than paths
 */
public class ConfigXML
{

  /**
   * root node of the in-memory DOM structure
   */
  private Node root;

  /**
   * Document node of the in-memory DOM structure
   */
  private Document doc;

  /**
   * XML file name in string form
   */
  private String fileName;

  /**
   * Print writer (output)
   */
  private PrintWriter out;

  private static final String configDirectory = ".morpho";

  /**
   * Set up a DOM parser for reading an XML document
   *
   * @return a DOM parser object for parsing
   */
  private static DocumentBuilder createDomParser() throws Exception
  {
    DocumentBuilder parser = null;

    try
    {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      parser = factory.newDocumentBuilder();
      if (parser == null)
      {
        throw new Exception("Could not create Document parser in " +
                            "MonarchUtil.DocumentBuilder");
      }
    }
    catch (ParserConfigurationException pce)
    {
      throw new Exception("Could not create Document parser in " +
                            "MonarchUtil.DocumentBuilder: " + pce.getMessage());
    }

    return parser;
  }

  /**
   * String passed to the creator is the XML config file name
   *
   * @param filename name of XML file
   */
  public ConfigXML(String filename) throws FileNotFoundException, Exception
  {
    this.fileName = filename;

    DocumentBuilder parser = createDomParser();
    File XMLConfigFile = new File(filename);
    InputSource in;
    FileInputStream fs;
    fs = new FileInputStream(filename);
    in = new InputSource(fs);

    try
    {
      doc = parser.parse(in);
      fs.close();
    } catch(Exception e1) {
      throw e1;
    }
    root = doc.getDocumentElement();
  }

   /**
   * String passed to the creator is the XML config file name
   *
   * @param input stream containing the XML configuration data
   */
  public ConfigXML(InputStream configStream) throws FileNotFoundException,
                                                    Exception
  {
    DocumentBuilder parser = createDomParser();
    InputSource in;
    in = new InputSource(configStream);

    try
    {
      doc = parser.parse(in);
      configStream.close();
    } catch(Exception e1) {
      throw e1;
    }
    root = doc.getDocumentElement();
  }

  /**
   * Gets the value(s) corresponding to a key string (i.e. the
   * value(s) for a named parameter.
   *
   * @param key 'key' is element name.
   * @return Returns a Vector of strings because may have repeated elements
   * @throws ElementNotFoundException if key is not found
   */
  public Vector get(String key) throws ElementNotFoundException
  {
    NodeList nl = doc.getElementsByTagName(key);
    Vector result = new Vector();
    if (nl.getLength() < 1)
    {
      throw new ElementNotFoundException("Element " + key + " not found");
    }
    for (int i = 0; i < nl.getLength(); i++)
    {
      Node cn = nl.item(i).getFirstChild(); // assume 1st child is text node
      if ((cn != null) && (cn.getNodeType() == Node.TEXT_NODE))
      {
        String temp = cn.getNodeValue();
        result.addElement(temp.trim());
      }
    }
    return result;
  }

  /**
   * Gets the value(s) corresponding to a key string (i.e. the
   * value(s) for a named parameter.
   *
   * @param key 'key' is element name.
   * @param i zero based index of elements with the name stored in key
   * @return String value of the ith element with name in 'key'
   * @throws ElementNotFoundException if key is not found at position i
   */
  public String get(String key, int i) throws ElementNotFoundException
  {
    NodeList nl = doc.getElementsByTagName(key);
    String result = null;
    if (nl.getLength() < 1)
    {
      throw new ElementNotFoundException("Element " + key +
                                         " not found at position " + i);
    }
    if (nl.getLength() < i)
    {
      throw new ElementNotFoundException("Element " +
                                         key + " not found at position " + i);
    }
    Node cn = nl.item(i).getFirstChild(); // assume 1st child is text node
    if ((cn != null) && (cn.getNodeType() == Node.TEXT_NODE))
    {
      result = (cn.getNodeValue().trim());
    }
    return result;
  }

  /**
   * used to set a value corresponding to 'key'; value is changed
   * in DOM structure in memory
   *
   * @param key 'key' is element name.
   * @param i index in set of elements with 'key' name
   * @param value new value to be inserted in ith key
   * @throws ElementNotFoundException if key is not found at position i
   */
  public void set(String key, int i, String value)
         throws ElementNotFoundException
  {
    boolean result = false;
    NodeList nl = doc.getElementsByTagName(key);
    if (nl.getLength() <= i) {
      throw new ElementNotFoundException("Cannot set key " + key +
                                         " at position " + i +
                                         " either because it does not exist " +
                                         "or because the there are not " + i +
                                         " elements by that name.");
    } else {
      Node cn = nl.item(i).getFirstChild(); // assumed to be a text node
      if (cn == null) {
        // No text node, so append one with the value
        Node newText = doc.createTextNode(value);
        nl.item(i).appendChild(newText);
      } else if (cn.getNodeType() == Node.TEXT_NODE) {
        // found the text node, so change its value
        cn.setNodeValue(value);
      }
    }
  }

  /**
   * Inserts another node before the first element with
   * the name contained in 'key', otherwise appends it
   * to the end of the config file (last element in root node)
   *
   * @param key element name which will be duplicated
   * @param value value for new element
   */
  public void insert(String key, String value)
  {
    // Create the new element, with its text value child
    Node newElem = doc.createElement(key);
    Node newText = doc.createTextNode(value);
    newElem.appendChild(newText);

    // Determine if there are existing elements of the same name
    NodeList nl = doc.getElementsByTagName(key);

    // If so, insert new element before existing
    if (nl.getLength() > 0) {
      Node nnn = nl.item(0);
      Node parent = nnn.getParentNode();
      //insert newElem before nnn
      parent.insertBefore(newElem, nnn);

    // Otherwise, append new element to end of root
    } else {
      root.appendChild(newElem);
    }
  }

  /**
   * Add a sub field to an existing config field
   *
   * @param parentName name of parent element
   * @param i index of parent element
   * @param childName element name of new child
   * @param value value of new child
   * @throws IndexTooLargeException if i is larger than the number of nodes
   *         of parentName
   */
  public void addSubField(String parentName, int i, String childName, String value)
                       throws IndexTooLargeException
  {
    NodeList nl = doc.getElementsByTagName(parentName);
    if (nl.getLength() > 0)
    {
      if (nl.getLength() <= i)
      {
        throw new IndexTooLargeException("Error setting XMLConfig value: " +
                                         "index too large");
      }
      else
      {
        Node parent = nl.item(i);
        Node newElem = doc.createElement(childName);
        Node newText = doc.createTextNode(value);
        //add text to element
        newElem.appendChild(newText);
        //add newElem to parent
        parent.appendChild(newElem);
      }
    }
  }

  /**
   * deletes indicated field
   *
   * @param nodeName field to delete
   * @param i node index
   * @throws IndexTooLargeException if i is larger than the number of nodeName
   * nodes.
   */
  public void delete(String nodeName, int i) throws IndexTooLargeException
  {
    NodeList nl = doc.getElementsByTagName(nodeName);
    if (nl.getLength() > 0)
    {
      if (nl.getLength() <= i)
      {
        throw new IndexTooLargeException("Error removing XMLConfig value: " +
                                         "index too large");
      }
      else
      {
        Node nnn = nl.item(i);
        Node parent = nnn.getParentNode();
        parent.removeChild(nnn);
      }
    }
  }


  /**
   * removes all subfields within specified super field
   *
   * @param parentName the super field
   * @param i index of super field
   */
  public void deleteSubFields(String parentName, int i)
         throws IndexTooLargeException
  {
    NodeList nl = doc.getElementsByTagName(parentName);
    if (nl.getLength() > 0)
    {
      if (nl.getLength() <= i)
      {
        throw new IndexTooLargeException("Error setting XMLConfig value: " +
                                 "index too large");
      }
      else
      {
        Node parent = nl.item(i);
        NodeList nlchildren = parent.getChildNodes();
        int numchildren = nlchildren.getLength();
        for (int k = 0; k < numchildren; k++)
        {
          Node temp = nlchildren.item(0);
          parent.removeChild(temp);
        }
      }
    }
  }

  /**
   * Assume that there is some parent node which has a subset of
   * child nodes that are repeated e.g.
   * <parent>
   *    <name>xxx</name>
   *    <value>qqq</value>
   *    <name>yyy</value>
   *    <value>www</value>
   *    ...
   * </parent>
   *
   * this method will return a Hashtable of names-values of parent
   * @param field the name of the field in which the name/value pair resides
   * @param name the name sub field in the name/value pair
   * @param value the value sub field in the name/value pair
   * @throws ElementNotFoundException if the parentName field is not found
   */
  public Hashtable getNameValuePairs(String field, String name,
                                     String value)
                                     throws ElementNotFoundException
  {
    try
    {
      Hashtable h = getNameValuePairs(field, name, value, 0);
      return h;
    }
    catch(IndexTooLargeException e)
    {
      throw new ElementNotFoundException("The element " + field +
                                         " does not exist.");
    }
  }

  /**
   * Assume that there is some parent node which has a subset of
   * child nodes that are repeated e.g.
   * <parent>
   *    <name>xxx</name>
   *    <value>qqq</value>
   *    <name>yyy</value>
   *    <value>www</value>
   *    ...
   * </parent>
   *
   * this method will return a Hashtable of names-values of parent
   * @param field the name of the parent field of the name/value pair
   * @param name the name node in the name/value pair
   * @param value the value node in the name/value pair
   * @param i the index of the parent that you want in the node list.
   * @throws IndexTooLargeException if there are less than i parentName nodes
   */
  public Hashtable getNameValuePairs(String field, String name,
                                     String value, int i)
                                     throws IndexTooLargeException
  {
    String keyval = "";
    String valval = "";
    Hashtable ht = new Hashtable();
    NodeList nl = doc.getElementsByTagName(field);
    if (nl.getLength() > 0)
    {
      if(nl.getLength() <= i)
      {
        throw new IndexTooLargeException("There are not " + i + " nodes in " +
                                         "the resultset.");
      }
      // always use the first parent
      NodeList children = nl.item(i).getChildNodes();
      if (children.getLength() > 0)
      {
        for (int j = 0; j < children.getLength(); j++)
        {
          Node cn = children.item(j);
          if ((cn.getNodeType() == Node.ELEMENT_NODE)
              && (cn.getNodeName().equalsIgnoreCase(name)))
          {
            Node ccn = cn.getFirstChild();        // assumed to be a text node
            if ((ccn != null) && (ccn.getNodeType() == Node.TEXT_NODE))
            {
              keyval = ccn.getNodeValue();
            }
          }
          if ((cn.getNodeType() == Node.ELEMENT_NODE)
              && (cn.getNodeName().equalsIgnoreCase(value)))
          {
            Node ccn = cn.getFirstChild();        // assumed to be a text node
            if ((ccn != null) && (ccn.getNodeType() == Node.TEXT_NODE))
            {
              valval = ccn.getNodeValue();
              ht.put(keyval, valval);
            }
          }
        }
      }
    }
    return ht;
  }

  /**
   *  utility routine to return the value(s) of a node defined by
   *  a specified XPath
   * @param pathstring the path to the requested nodes
   */
  public Vector getValuesForPath(String pathstring)
         throws ElementNotFoundException, Exception
  {
    Vector val = new Vector();
    if (!pathstring.startsWith("/"))
    {
      pathstring = "//*/"+pathstring;
    }

    try
    {
      NodeList nl = null;
      nl = XPathAPI.selectNodeList(doc, pathstring);
      if ((nl!=null)&&(nl.getLength()>0))
      {
        // loop over node list is needed if node is repeated
        for (int k=0;k<nl.getLength();k++) {
          Node cn = nl.item(k).getFirstChild();  // assume 1st child is text node
          if ((cn!=null)&&(cn.getNodeType() == Node.TEXT_NODE)) {
            String temp = cn.getNodeValue().trim();
            val.addElement(temp);
          }
        }
      }
      else
      {
        throw new ElementNotFoundException("The path " + pathstring +
                                           " was not found.");
      }
    }
    catch (Exception e)
    {
      throw e;
    }
    return val;
  }

    /**
   * gets the content of a tag in a given xml file with the given path
   * @param path the path to get the content from
   * @throws javax.xml.transform.TransformerException
   * @throws ElementNotFoundException
   */
  public NodeList getPathContent(String path)
         throws javax.xml.transform.TransformerException,
                ElementNotFoundException
  {
    NodeList docNodeList = XPathAPI.selectNodeList(doc, path);
    if(docNodeList == null || docNodeList.getLength() == 0)
    {
      throw new ElementNotFoundException("Path " + path + " was not found.");
    }

    return docNodeList;
  }

  /**
   * Save the configuration file
   * @throws Exception if any errors occur during the save
   */
  public void save() throws Exception
  {
    saveDOM(root);
  }

  /**
   * This method wraps the 'print' method to send DOM back to the
   * XML document (file) that was used to create the DOM. i.e.
   * this method saves changes to disk
   *
   * @param nd node (usually the document root)
   * @throws Exception if the config file cannot be written
   */
  private void saveDOM(Node nd) throws Exception
  {
    File outfile = new File(fileName);
    if (!outfile.canWrite())
    {
      throw new Exception("Cannot write the config file " + fileName);
    }
    else
    {
      out = new PrintWriter(new FileWriter(fileName));
      out.println("<?xml version=\"1.0\"?>");
      print(nd);
      out.close();
    }
  }

  /**
   * This method can 'print' any DOM subtree. Specifically it is
   * set (by means of 'out') to write the in-memory DOM to the
   * same XML file that was originally read. Action thus saves
   * a new version of the XML doc
   *
   * @param node node usually set to the 'doc' node for complete XML file
   * re-write
   */
  private void print(Node node)
  {

    // is there anything to do?
    if (node == null)
    {
      return;
    }

    int type = node.getNodeType();
    switch (type)
    {
      // print document
    case Node.DOCUMENT_NODE:
    {

      out.println("<?xml version=\"1.0\"?>");
      print(((Document) node).getDocumentElement());
      out.flush();
      break;
    }

      // print element with attributes
    case Node.ELEMENT_NODE:
    {
      out.print('<');
      out.print(node.getNodeName());
      Attr attrs[] = sortAttributes(node.getAttributes());
      for (int i = 0; i < attrs.length; i++)
      {
        Attr attr = attrs[i];
        out.print(' ');
        out.print(attr.getNodeName());
        out.print("=\"");
        out.print(normalize(attr.getNodeValue()));
        out.print('"');
      }
      out.print('>');
      NodeList children = node.getChildNodes();
      if (children != null)
      {
        int len = children.getLength();
        for (int i = 0; i < len; i++)
        {
          print(children.item(i));
        }
      }
      break;
    }

      // handle entity reference nodes
    case Node.ENTITY_REFERENCE_NODE:
    {
      out.print('&');
      out.print(node.getNodeName());
      out.print(';');

      break;
    }

      // print cdata sections
    case Node.CDATA_SECTION_NODE:
    {
      out.print("<![CDATA[");
      out.print(node.getNodeValue());
      out.print("]]>");

      break;
    }

      // print text
    case Node.TEXT_NODE:
    {
      out.print(normalize(node.getNodeValue()));
      break;
    }

      // print processing instruction
    case Node.PROCESSING_INSTRUCTION_NODE:
    {
      out.print("<?");
      out.print(node.getNodeName());
      String data = node.getNodeValue();
      if (data != null && data.length() > 0)
      {
        out.print(' ');
        out.print(data);
      }
      out.print("?>");
      break;
    }
    }

    if (type == Node.ELEMENT_NODE)
    {
      out.print("</");
      out.print(node.getNodeName());
      out.print('>');
    }
    out.flush();
  }

  /** Returns a sorted list of attributes. */
  private Attr[] sortAttributes(NamedNodeMap attrs)
  {

    int len = (attrs != null) ? attrs.getLength() : 0;
    Attr array[] = new Attr[len];
    for (int i = 0; i < len; i++)
    {
      array[i] = (Attr) attrs.item(i);
    }
    for (int i = 0; i < len - 1; i++)
    {
      String name = array[i].getNodeName();
      int index = i;
      for (int j = i + 1; j < len; j++)
      {
        String curName = array[j].getNodeName();
        if (curName.compareTo(name) < 0)
        {
          name = curName;
          index = j;
        }
      }
      if (index != i)
      {
        Attr temp = array[i];
        array[i] = array[index];
        array[index] = temp;
      }
    }

    return (array);

  } // sortAttributes(NamedNodeMap):Attr[]

  /** Normalizes the given string. */
  private String normalize(String s)
  {
    StringBuffer str = new StringBuffer();

    int len = (s != null) ? s.length() : 0;
    for (int i = 0; i < len; i++)
    {
      char ch = s.charAt(i);
      switch (ch)
      {
      case '<':
      {
        str.append("&lt;");
        break;
      }
        case '>':
      {
        str.append("&gt;");
        break;
      }
      case '&':
      {
        str.append("&amp;");
        break;
      }
      case '"':
      {
        str.append("&quot;");
        break;
      }
      case '\r':
      case '\n':
      {
        // else, default append char
      }
      default:
      {
        str.append(ch);
      }
      }
    }

    return (str.toString());

  }

  /**
   * Determine the home directory in which configuration files should be located
   *
   * @return String name of the path to the configuration directory
   */
  public static String getConfigDirectory() {
    return System.getProperty("user.home") + File.separator + configDirectory;
  }
}
