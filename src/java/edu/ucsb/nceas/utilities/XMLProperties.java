/**
 *  '$RCSfile: XMLProperties.java,v $'
 *  Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: brooke $'
 *     '$Date: 2005-05-18 22:52:09 $'
 * '$Revision: 1.5 $'
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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

import javax.swing.JOptionPane;

import javax.xml.transform.TransformerException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import org.apache.log4j.Logger;


/**
 *  This class is designed to retrieve and store <code>String</code> properties
 *  information in an XML file. The concept is similar to that of a Properties
 *  file except that using the XML format allows for a hierarchy of properties
 *  and repeated properties.
 *
 *  All 'keys' are XPath expressions defining element names, while values are
 *  always stored as XML text nodes. The XML file is parsed and stored in memory
 *  as a DOM object.
 */
public class XMLProperties
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
   * XML file used to store properties
   */
  private File xmlPropsFile;

  /**
   * XML input stream used to retrieve properties
   */
  private InputStream xmlPropsSource;

  //output format used by store() method
  private static String OUTPUT_FORMAT = "UTF-8";

//  private static Logger log = Logger.getLogger(XMLProperties.class.getName());

  /**
   *  Creates a new, empty XML properties object
   */
  public XMLProperties() {}


  /**
   *  Reads properties from the XML input stream.
   *
   *  @param xmlPropsFile   <code>File</code> from which the properties
   *                          XML is to be read and parsed
   *
   *  @throws IOException if File cannot be opened or processed
   */
  public void load(File xmlPropsFile) throws IOException {

    this.xmlPropsFile = xmlPropsFile;
    InputStream xmlPropsSource = new FileInputStream(xmlPropsFile);
    this.load(xmlPropsSource);
  }


  /**
   *  Reads properties from the XML input stream.
   *
   *  @param xmlPropsSource   <code>InputStream</code> from which the properties
   *                          XML is to be read and parsed
   *
   *  @throws IOException if InputStream cannot be opened or processed
   */
  public void load(InputStream xmlPropsSource) throws IOException {

    this.xmlPropsSource = xmlPropsSource;

    init(xmlPropsSource);
  }



  private void init(InputStream xmlPropsSource) throws IOException {

    Reader sr = new InputStreamReader(xmlPropsSource);
    root = XMLUtilities.getXMLReaderAsDOMTreeRootNode(sr);
  }

  /**
   * Gets the value corresponding to an xpath key string
   *
   * @param keyXPath  XPath pointing to an element or elements, each of which
   *                  may contain a single string value, but no additional
   *                  non-text elements
   *
   * @return          an array of strings containing the retrieved values, or
   *                  null if no values retrieved
   *
   *  @throws <code>javax.xml.transform.TransformerException</code> if there is
   *                    a problem executing the XPATH expression
   */
  private final StringBuffer propResultBuff = new StringBuffer();
  //
  public String[] getProperty(String keyXPath) throws TransformerException {

    NodeList nl = XMLUtilities.getNodeListWithXPath(root, keyXPath);

//    log.debug(
//      "XMLProperties.getProperty(): getNodeListWithXPath() returned NodeList: "
//                                                                          +nl);
    if (nl==null) {
//      log.debug(
//        "XMLProperties.getProperty(): getNodeListWithXPath() returned NULL");
      return null;
    }

    int totChildren = nl.getLength();

    if (totChildren < 1) {
//      log.debug(  "XMLProperties.getProperty(): getNodeListWithXPath() returned "
//                            +totChildren+" children; returning new String[0]");
      return null;
    }

//    log.debug(  "XMLProperties.getProperty(): getNodeListWithXPath() returned "
//                            +totChildren+" children");

    List resultList = new ArrayList();
    Node nextChild = null;
    for (int i = 0; i < nl.getLength(); i++) {

      NodeList cnl = nl.item(i).getChildNodes();

      if (cnl==null || cnl.getLength() < 1) continue;

      propResultBuff.delete(0, propResultBuff.length());
      for (int ci = 0; ci < cnl.getLength(); ci++) {
        nextChild = cnl.item(ci);
        if (nextChild.getNodeType() == Node.TEXT_NODE
            || nextChild.getNodeType() == Node.CDATA_SECTION_NODE) {
          propResultBuff.append(nextChild.getNodeValue().trim());
        }
      }
      resultList.add(propResultBuff.toString());
    }

    return (String[])(resultList.toArray(new String[resultList.size()]));
  }


  /**
   * used to set a value corresponding to 'key'; value is changed in DOM
   * structure in memory. Returns the previous <code>String</code> value for
   * this parameter, or null if it didn't exist
   *
   * @param key the xpath identifying the element name.
   * @param value new value to be inserted in ith key
   * @return the previous <code>String</code> value for this parameter, or null
   *   if it didn't exist
   * @throws TransformerException
   */
  public String setProperty(String key, String value)
                                                  throws TransformerException {
    String[] resultArray = getProperty(key);

    XMLUtilities.addTextNodeToDOMTree(root, key, value);

    return (resultArray!=null)? resultArray[0] : null;
  }



  /**
   *  Writes this property XML DOM to the original location in XML format.
   *  The stream is written using UTF-8 character encoding.
   *  After the entries have been written, the output stream is flushed and
   *  closed.
   *
   *  @throws IOException if original file cannot be found or written to
   */
  public void store() throws IOException {

    FileOutputStream out = null;

    if (xmlPropsFile==null) {
      throw new IOException(
          "Cannot find properties file - xmlPropsFile==null");

    } else if (!xmlPropsFile.canWrite()) {

      throw new IOException(
          "Cannot write to properties file (xmlPropsFile.canWrite() is false): "
                                    + xmlPropsFile.getName());
    } else {

      try {
        out = new FileOutputStream(xmlPropsFile);
        store(out);
      } finally {
        if (out != null) {
          try {
            out.flush();
            out.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }


  /**
   *  Writes this property XML DOM to the output stream in XML format, suitable
   *  for loading into a Properties object using the load method. The stream is
   *  written using the UTF-8 character encoding.
   *  After the entries have been written, the output stream is flushed. The
   *  output stream remains open after this method returns.
   *
   *  @param out    the <code>OutputStream</code> to which the output will be
   *                written
   */
  public void store(OutputStream out) {

    // out.println("<?xml version=\"1.0\"?>");
    PrintWriter pw = new PrintWriter(out, true);
    this.list(pw);
  }


  /**
   * Prints this property list out to the specified output stream. This method
   * is useful for debugging.
   *
   * @param out PrintWriter
   */
  public void list(PrintWriter out) {

    XMLUtilities.print(root, out, OUTPUT_FORMAT);
  }


  /**
   * Prints this property list out to the specified output stream. This method
   * is useful for debugging.
   *
   * @param out PrintStream
   */
  public void list(PrintStream out) {

    this.store(out);
  }


  /**
   * Returns an enumeration of all the XPath keys in this properties object
   *
   * @return Iterator
   */
  public Iterator propertyNames() {

    OrderedMap nvpMap = XMLUtilities.getDOMTreeAsXPathMap(root);
    if (nvpMap==null) return emptyIterator;
    Set nvpSet = nvpMap.keySet();
    if (nvpSet==null) return emptyIterator;
    return nvpSet.iterator();
  }

   // an empty class implementing the Iterator interface
   private Iterator emptyIterator = new Iterator() {

     public boolean hasNext() { return false; }
     public Object next()     { return null; }
     public void remove()     {}
   };
}


