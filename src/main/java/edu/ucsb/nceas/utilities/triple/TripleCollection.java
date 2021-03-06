/**
 *  '$RCSfile: TripleCollection.java,v $'
 *  Copyright: 2000 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
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

package edu.ucsb.nceas.utilities.triple;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Vector;

/**
 * This class implements a collection of triples which make up all of the
 * relationships in a package.
 */
public class TripleCollection
{
  private Vector<Triple> triples = new Vector<Triple>();
  /**
   * Default Constructor
   */
  public TripleCollection()
  {
    
  }
  
  /**
   * Copy constructor.  instantiate this object from the given TripleCollection
   */
  public TripleCollection(TripleCollection tc)
  {
    this.triples = tc.getCollection();
  }
  
  /**
   * read an xml file, build the collection from any triples in the xml file.
   */
  public TripleCollection(Reader xml)
  {
    TripleParser tp = new TripleParser(xml);
    this.triples = tp.getTriples().getCollection();
  }

  
  /**
   * create a collection of triples from a vector of Triple objects.
   */
  public TripleCollection(Vector<Triple> triples)
  {
//    this.triples = new Vector(triples);   //DFH
    this.triples = (Vector<Triple>)triples.clone();
  }
  
  /**
   * Add a single triple to this collection
   */
  public void addTriple(Triple triple)
  {
    this.triples.addElement(triple);
  }
  
  /**
   * returns true if the collection contains triple, false otherwise
   */
  public boolean containsTriple(Triple triple)
  {
    return triples.removeElement(triple);
  }
  
  /**
   * removes the specified triple from the collection and returns it.  If the
   * triple was not in the collection null is returned.
   */ 
  public Triple removeTriple(Triple triple)
  {
    boolean removed = triples.removeElement(triple);
    if(removed)
    {
      return triple;
    }
    else
    {
      return null;
    }
  }
  
  /**
   * returns a vector of triples with the given subject
   */
  public Vector<Triple> getCollectionBySubject(String subject)
  {
    Vector<Triple> trips = new Vector<Triple>();
    for(int i=0; i<triples.size(); i++)
    {
      Triple trip = new Triple(triples.elementAt(i));
      if(trip.getSubject().equals(subject))
      {
        trips.addElement(trip);
      }
    }
    return trips;
  }
  
  /**
   * returns a vector of triples with the given relationship
   */
  public Vector<Triple> getCollectionByRelationship(String relationship)
  {
    Vector<Triple> trips = new Vector<Triple>();
    for(int i=0; i<triples.size(); i++)
    {
      Triple trip = new Triple(triples.elementAt(i));
      if(trip.getRelationship().equals(relationship))
      {
        trips.addElement(trip);
      }
    }
    return trips;
  }
  
  /**
   * returns a vector of triples with the given object
   */
  public Vector<Triple> getCollectionByObject(String object)
  {
    Vector<Triple> trips = new Vector<Triple>();
    for(int i=0; i<triples.size(); i++)
    {
      Triple trip = new Triple(triples.elementAt(i));
      if(trip.getObject().equals(object))
      {
        trips.addElement(trip);
      }
    }
    return trips;
  }
  
  /**
   * return a vector of Triple objects that represent this collection.
   */
  public Vector<Triple> getCollection()
  {
    return this.triples;
  }
  
  public String toXML()
  {
    return toXML(null);
  }
  
  /**
   * returns a string representation of this collection in xml format.
   * the xml looks like this:
   * <pre>
   * &lt;triple&gt;&lt;subject&gt;some content 1&lt;/subject&gt;
   * &lt;relationship&gt;some content 2&lt;/relationship&gt;&lt;object&gt;
   * some content 3&lt;/object&gt;&lt;/triple&gt;&lt;triple&gt;
   * &lt;subject&gt;some content 1&lt;/subject&gt;&lt;relationship&gt;
   * some content 2&lt;/relationship&gt;&lt;object&gt;some content 3
   * &lt;/object&gt;&lt;/triple&gt;
   * .....
   * &lt;triple&gt;&lt;subject&gt;some content 1&lt;/subject&gt;
   * &lt;relationship&gt;some content 2&lt;/relationship&gt;&lt;object&gt;
   * some content 3&lt;/object&gt;&lt;/triple&gt;
   * </pre>
   */
  public String toXML(String root)
  {
    StringBuffer sb = new StringBuffer();
    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    if(root != null)
    {
      sb.append("<" + root + ">");
    }
    
    for(int i=0; i<triples.size(); i++)
    {
      sb.append(((Triple)triples.elementAt(i)).toXML());
    }
    
    if(root != null)
    {
      sb.append("</" + root + ">");
    }
    
    return sb.toString();
  }
  
  /**
   * returns a nicely formatted string representation of this collection in xml
   * the xml looks like this:
   * <pre>
   * &lt;triple&gt;
   *   &lt;subject&gt;some content 1&lt;/subject&gt;
   *   &lt;relationship&gt;some content 2&lt;/relationship&gt;
   *   &lt;object&gt;some content 3&lt;/object&gt;
   * &lt;/triple&gt;
   * &lt;triple&gt;
   *   &lt;subject&gt;some content 1&lt;/subject&gt;
   *   &lt;relationship&gt;some content 2&lt;/relationship&gt;
   *   &lt;object&gt;some content 3&lt;/object&gt;
   * &lt;/triple&gt;
   * .....
   * &lt;triple&gt;
   *   &lt;subject&gt;some content 1&lt;/subject&gt;
   *   &lt;relationship&gt;some content 2&lt;/relationship&gt;
   *   &lt;object&gt;some content 3&lt;/object&gt;
   * &lt;/triple&gt;
   * </pre>
   * without the formatting (i.e. no indents or extra spaces)
   */
  public String toFormatedXML()
  {
    StringBuffer sb = new StringBuffer();
    for(int i=0; i<triples.size(); i++)
    {
      sb.append(((Triple)triples.elementAt(i)).toFormatedXML());
    }
    return sb.toString();
  }
  
  /**
   * return this collection as a string
   */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    for(int i=0; i<triples.size(); i++)
    {
      Triple t = new Triple((Triple)triples.elementAt(i));
      sb.append(t.toString());
      if(i != triples.size()-1)
      {
        sb.append(",");
      }
    }
    sb.append("]");
    return sb.toString();
  }
  
  public static void main(String[] args)
  {
    if(args.length == 0)
    {
      System.out.println("usage: TripleCollection <xml_file>");
      return;
    }
    
    String filename = args[0];
    
    try
    {
      Reader xml = new InputStreamReader(new FileInputStream(filename), Charset.forName("UTF-8"));
      TripleCollection tc = new TripleCollection(xml);
      System.out.println("Triples are:" );
      System.out.println(tc.toString());
      //test the getCollectionBy methods
      Vector v = tc.getCollectionBySubject("1.s");
      System.out.println(v.toString());
      v = tc.getCollectionByObject("2.o");
      System.out.println(v.toString());
      v = tc.getCollectionByRelationship("1.r");
      System.out.println(v.toString());
      //test addTriple
      Triple t = new Triple("3.s", "3.r", "3.o");
      tc.addTriple(t);
      System.out.println(tc.toString());
      //test removeTriple
      Triple u = tc.removeTriple(t);
      System.out.println(u.toString());
      System.out.println(tc.toString());
      Triple x = new Triple();
      u = tc.removeTriple(x);
      if(u != null)
        System.out.println(u.toString());
      else
        System.out.println("u == null");
      System.out.println(tc.toString());
      //check containsTriple
      tc.addTriple(t);
      if(tc.containsTriple(t))
        System.out.println("it's there");
      else
        System.out.println("it's not");
      tc.removeTriple(t);
      if(tc.containsTriple(t))
        System.out.println("it's there");
      else
        System.out.println("it's not");
      //test toXML
      tc.addTriple(t);
      System.out.println(tc.toXML());
      System.out.println(tc.toFormatedXML());
    }
    catch(Exception e)
    {
      System.out.println("error in main");
      e.printStackTrace(System.out);
    }
    System.out.println("Done with " + args[0]);
  }
}
