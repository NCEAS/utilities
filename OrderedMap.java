/**
 *  '$RCSfile: OrderedMap.java,v $'
 *  Copyright: 2000 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: brooke $'
 *     '$Date: 2003-07-17 21:31:24 $'
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

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;

/**
 *  The OrderedMap is similar to a java.util.Map, but it preserves the insertion 
 *  order of the keys, in the same way a java.util.List does. It is comparable 
 *  with the LinkedHashMap introduced in JDK1.4, but unavailable in JDK1.3
 */
public class OrderedMap extends HashMap {

  private final List ordKeyList;
  
  /** 
  *    Creates a new instance of OrderedMap
  */
  public OrderedMap() { ordKeyList = new ArrayList(); }


  /** Associates the specified value with the specified key in this map
   * (optional operation).  If the map previously contained a mapping for
   * this key, the old value is replaced.
   *
   *  @return previous value associated with specified key, or <tt>null</tt>
   * 	       if there was no mapping for key.  A <tt>null</tt> return can
   * 	       also indicate that the map previously associated <tt>null</tt>
   * 	       with the specified key, if the implementation supports
   * 	       <tt>null</tt> values.
   *
   *  @param key    key with which the specified value is to be associated. MAY
   *                *NOT* BE NULL
   *
   *  @param value  value to be associated with the specified key. MAY BE NULL
   *
   *  @throws       NullPointerException if key is null
   */
  public Object put(Object key, Object value) throws NullPointerException {
  
    checkNotNull("put()", key);
    if (!ordKeyList.contains(key)) ordKeyList.add(key);
    return super.put(key,value);
  }



  /**
  * Removes the mapping for this key from this map if present
  *
  *  @param key key whose mapping is to be removed from the map.
  *
  *  @return previous value associated with specified key, or <tt>null</tt>
  *	       if there was no mapping for key.  A <tt>null</tt> return can
  *	       also indicate that the map previously associated <tt>null</tt>
  *	       with the specified key, if the implementation supports
  *	       <tt>null</tt> values.
   *
   *  @throws       NullPointerException if key is null
  */
  public Object remove(Object key) throws NullPointerException {
  
    checkNotNull("remove(key): ", key);
    ordKeyList.remove(key);
    return super.remove(key);
  }


  // Bulk Operations

  /** This API method is not supported
   *
   *  @param m Map - the <tt>putAll</tt> method is not supported by this map.
   *   
   *  @throws UnsupportedOperationException, since the <tt>putAll</tt> method is
   * 	not supported by this map.
   */
  public void putAll(Map m) throws UnsupportedOperationException {
  
    throwUnsupportedOperationException(
            "putAll() is not supported by this Map. Use multiple put() calls");
  }

  /**
   * Removes all mappings from this map 
   */
  public void clear(){
  
    ordKeyList.clear();
    super.clear();
  }


  // Views

  /**
   * Returns a set view of the keys contained in this map.  The set is
   * backed by the map, so changes to the map are reflected in the set, and
   * vice-versa.  If the map is modified while an iteration over the set is
   * in progress, the results of the iteration are undefined.  The set
   * supports element removal, which removes the corresponding mapping from
   * the map, via the <tt>Iterator.remove</tt>, <tt>Set.remove</tt>,
   * <tt>removeAll</tt> <tt>retainAll</tt>, and <tt>clear</tt> operations.
   * It does not support the add or <tt>addAll</tt> operations.
   *
   * @return a set view of the keys contained in this map.
   */
  public Set keySet(){
    
      return new Set() {
    
      public int      size()                   { return ordKeyList.size();     }
      public boolean  isEmpty()                { return ordKeyList.isEmpty();  }
      public boolean  contains(Object o)       { return ordKeyList.contains(o);}
      public Iterator iterator()               { return ordKeyList.iterator(); }
      public Object[] toArray()                { return ordKeyList.toArray();  }
      public Object[] toArray(Object a[])      { return ordKeyList.toArray(a); }
      public boolean  containsAll(Collection c){ return ordKeyList.isEmpty();  }
      public boolean  equals(Object o)         { return ordKeyList.equals(o);  }
      public int      hashCode()               { return ordKeyList.hashCode(); }
      public boolean  add(Object o){
        throwUnsupportedOperationException(
                          "add() not supported; this Set is immutable");
        return false;
      }
      public boolean  remove(Object o){
        throwUnsupportedOperationException(
                          "remove() not supported; this Set is immutable");
        return false;
      }
      public boolean  addAll(Collection c){
        throwUnsupportedOperationException(
                          "addAll() not supported; this Set is immutable");
        return false;
      }
      public boolean  retainAll(Collection c){
        throwUnsupportedOperationException(
                          "retainAll() not supported; this Set is immutable");
        return false;
      }
      public boolean  removeAll(Collection c){
        throwUnsupportedOperationException(
                          "removeAll() not supported; this Set is immutable");
        return false;
      }
      public void     clear(){
        throwUnsupportedOperationException(
                          "clear() not supported; this Set is immutable");
      }
    };
  }

  //
  private Collection orderedValues = new ArrayList();
  //
  /** 
   *  returns a <code>java.util.Collection</code> view of the values in 
   *  this OrderedMap
   *   
   *  @return   a <code>java.util.Collection</code> view of the values in this
   *            OrderedMap
   */
  public Collection values(){
  
    orderedValues.clear();
    Iterator it = ordKeyList.iterator();
    while (it.hasNext()) {
      orderedValues.add(super.get(it.next()));
    }
    return orderedValues;
  }
  
  //
  StringBuffer toStringBuff = new StringBuffer();
  //
  /**
   *  Returns a string representation of the keys & values contained in this map
   *
   *  @return a string representation of the keys & values contained in this map
   */
  public String toString(){
  
    Iterator it = keySet().iterator();
    if (it==null) return null;
    
    toStringBuff.delete(0,toStringBuff.length());
    
    toStringBuff.append("\n\n* * * Begin OrderedMap * * *\n\n");

    Object nextKey = null;
    Object nextVal = null;
    
    while (it.hasNext()) {
      
      nextKey = it.next();
      
      if (nextKey==null) {
        toStringBuff.append("**NULL KEY**\n");
        continue;
      } else {
        toStringBuff.append(nextKey);
      }
      
      toStringBuff.append("\t = \t");

      nextVal = get(nextKey);
      
      if   (nextVal==null) toStringBuff.append("**NULL VALUE**");
      else toStringBuff.append(nextVal);

      toStringBuff.append("\n");
    }
    toStringBuff.append("\n* * * End OrderedMap * * *\n");
    return toStringBuff.toString();
  }


  /**
   *  simply calls the <tt>entrySet</tt> method in the superclass - <em>but 
   *  note that *ORDER IS NOT GUARANTEED IN THE SET*</em>. 
   *  Javadoc for this method from Map:
   *  
   *  Returns a collection view of the mappings contained in this map. Each 
   *  element in the returned collection is a Map.Entry. The collection is 
   *  backed by the map, so changes to the map are reflected in the collection, 
   *  and vice-versa. The collection supports element removal, which removes the 
   *  corresponding mapping from the map, via the Iterator.remove, 
   *  Collection.remove, removeAll, retainAll, and clear operations. It does not 
   *  support the add or addAll operations.
   *
   *  @return a collection view of the mappings contained in this map
   */
  public Set entrySet(){ return super.entrySet(); }

    
  /**
   * throws a NullPointerException if arg is null
   *
   *  @param origin String describing calling function - used for debugging in 
   *                Exception message
   *
   *  @param arg    the Object to be tested for null value
   *
   *  @throws        NullPointerException if arg is null
   */
  private void checkNotNull(String origin, Object arg)
                                          throws NullPointerException {
  
    if (arg==null) {
      throwNullPointerException(origin+"\n* * * Argument must not be null!\n");
    }
  }

  /**
   * throws a NullPointerException if key or value argument is null (or if both
   *  are null) 
   *
   *  @param origin   String describing calling function - used for debugging in 
   *                  Exception message
   *
   *  @param arg      the "key" Object to be tested for null value
   *
   *  @param arg      the "value" Object to be tested for null value
   *
   *  @throws         NullPointerException if key or value argument is null (or 
   *                  if both are null) 
   */
  private void checkNotNull(String origin, Object key, Object value) 
                                          throws NullPointerException {
  
    if (key==null || value==null) {
      String keyNull = (key==null)?   "* * * Key must not be null!\n"   : "";
      String valNull = (value==null)? "* * * Value must not be null!\n" : "";
      throwNullPointerException(origin+" received key=" +key+" & value="+value
                                                        +"\n"+keyNull+valNull);
    }
  }
  
  /**
   *  convenience method to throw a NullPointerException, with the String 
   *  message provided
   *
   *  @param msg    The String message to use in constructing the 
   *                NullPointerException
   *
   *  @throws       NullPointerException
   */
  private void throwNullPointerException(String msg) 
                                          throws NullPointerException {
    
    NullPointerException npe = new NullPointerException(msg);
    npe.fillInStackTrace();
    throw npe;
  }
  
  /**
   *  convenience method to throw a UnsupportedOperationException, with the  
   *  String message provided
   *
   *  @param msg    The String message to use in constructing the 
   *                UnsupportedOperationException
   *
   *  @throws       UnsupportedOperationException
   */
  private void throwUnsupportedOperationException(String msg)
                                          throws UnsupportedOperationException {
    
    UnsupportedOperationException e = new UnsupportedOperationException(msg);
    e.fillInStackTrace();
    throw e;
  }
}

