/**
 *  '$RCSfile: OrderedMapTest.java,v $'
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

package edu.ucsb.nceas.utilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;
import edu.ucsb.nceas.utilities.OrderedMap;


/**
 * A JUnit test for testing the OrderedMap object.
 */
public class OrderedMapTest extends TestCase
{
    private static Map   orderedMap;
    private static final String[][] testData = {
      {"Key0", "Value0"},
      {"Key1", "Value1"},
      {"Key2", "Value2"},
      {"Key3", "Value3"},
      {"Key4", "Value4"},
      {"Key5",  null   }
    };
    
    /**
    * Constructor to build the test
    *
    * @param name the name of the test method
    */
    public OrderedMapTest(String name) {  super(name); }

    /**
    * NOTE - this gets called before *each* *test* 
    */
    public void setUp() {
    }
    
    /**
    * Release any objects after tests are complete
    */
    public void tearDown() {}
    

////////////////////////////////////////////////////////////////////////////////
//                    S T A R T   T E S T   M E T H O D S                     //
////////////////////////////////////////////////////////////////////////////////






    public void testOrderedMap()
    {
        System.out.println("constructor test");
        orderedMap = new OrderedMap();
        assertNotNull(orderedMap);
        System.out.println("OrderedMap() object created OK...");
    }

    public void testPut()
    {
        System.out.println("put() test");
        //...on existing OrderedMap:
        assertNotNull(orderedMap);
        
        assertEquals(0, orderedMap.size());
 
        Object result = null;
        result = orderedMap.put(testData[0][0], testData[0][1]);
        System.out.println("added "+testData[0][0]+"="+testData[0][1]);
        assertNull(result);

        result = orderedMap.put(testData[1][0], testData[1][1]);
        System.out.println("added "+testData[1][0]+"="+testData[1][1]);
        assertNull(result);
        
        result = orderedMap.put(testData[2][0], testData[2][1]);
        System.out.println("added "+testData[2][0]+"="+testData[2][1]);
        assertNull(result);
        
        result = orderedMap.put(testData[3][0], testData[3][1]);
        System.out.println("added "+testData[3][0]+"="+testData[3][1]);
        assertNull(result);
        
        result = orderedMap.put(testData[4][0], testData[4][1]);
        System.out.println("added "+testData[4][0]+"="+testData[4][1]);
        assertNull(result);

        assertEquals(5, orderedMap.size());
        
        //this should replace previous value and not mess up the order
        result = orderedMap.put(testData[2][0], "new_Value2");
        System.out.println("added "+testData[2][0]+"="+"new_Value2");
        assertNotNull(result);
        assertEquals(testData[2][1], result);
        assertEquals("new_Value2", orderedMap.get(testData[2][0]));
        
        assertEquals(5, orderedMap.size());

        result = orderedMap.put(testData[2][0], testData[2][1]);
        System.out.println("added "+testData[2][0]+"="+testData[2][1]);
        assertNotNull(result);
        assertEquals("new_Value2", result);
        assertEquals(testData[2][1], orderedMap.get(testData[2][0]));
        
        assertEquals(5, orderedMap.size());

        try {
            orderedMap.put(null, "bogus");
        } catch (NullPointerException e) {
            System.out.println("*EXPECTED* NullPointerException doing orderedMap.put(NULL, value)"+e);
        } catch (Exception ex) {
          ex.printStackTrace();
          fail("unexpected Exception doing get(NULL) "+ex);
        }
      try {
          orderedMap.put(testData[5][0], null);
      } catch (Exception ex) {
        ex.printStackTrace();
        fail("unexpected Exception doing get(NULL) "+ex);
      }

      assertEquals(6, orderedMap.size());
    }

    public void testKeySet(){
        
      System.out.println("testing orderedMap.keySet()...");

      assertNotNull(orderedMap);

      Iterator it = orderedMap.keySet().iterator();
      assertNotNull(it);
      
      Object next = null;
      for (int i=0; it.hasNext(); i++) {
        
        next = it.next();
        assertNotNull(next);
        System.out.println("-> "+(String)next);
        assertEquals(testData[i][0], next);
      }
    }
    
  public void testValues(){
  
    System.out.println("testing orderedMap.values()...");

    assertNotNull(orderedMap);

    Iterator it = orderedMap.values().iterator();
    assertNotNull(it);
    
    Object next = null;
    for (int i=0; it.hasNext(); i++) {
    
      next = it.next();
      System.out.println("-> "+(String)next);
      assertEquals(testData[i][1], next);
    }
  }
  

  public void testPutAll(){

    System.out.println("testing putAll()...");

    assertNotNull(orderedMap);

    try {
        orderedMap.putAll(new HashMap());
    } catch (UnsupportedOperationException e) {
        System.out.println("*EXPECTED* UnsupportedOperationException doing orderedMap.putAll()"+e);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail("unexpected Exception doing putAll() "+ex);
    }
  }

  public void testEntrySet(){

    System.out.println("testing entrySet()...");

    assertNotNull(orderedMap);

    try {
        orderedMap.entrySet();
    } catch (UnsupportedOperationException e) {
        System.out.println("*EXPECTED* UnsupportedOperationException doing orderedMap.entrySet()"+e);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail("unexpected Exception doing entrySet() "+ex);
    }
  }


  public void testGet(){
  
    assertNotNull(orderedMap);

    assertEquals(orderedMap.get(testData[0][0]), testData[0][1]);
    assertEquals(orderedMap.get(testData[1][0]), testData[1][1]);
    assertEquals(orderedMap.get(testData[2][0]), testData[2][1]);
    assertEquals(orderedMap.get(testData[3][0]), testData[3][1]);
    assertEquals(orderedMap.get(testData[4][0]), testData[4][1]);
    
    try {
        orderedMap.get(null);
    } catch (NullPointerException e) {
        System.out.println("*EXPECTED* NullPointerException doing orderedMap.get(NULL)"+e);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail("unexpected Exception doing get(NULL) "+ex);
    }
  }


  public void testToString(){

    assertNotNull(orderedMap);

    String stringVal = orderedMap.toString();
    assertNotNull(stringVal);
  
    assertEquals(
    "\n\n* * * Begin OrderedMap * * *\n\n"
    +testData[0][0]+"\t = \t"+testData[0][1]+"\n"
    +testData[1][0]+"\t = \t"+testData[1][1]+"\n"
    +testData[2][0]+"\t = \t"+testData[2][1]+"\n"
    +testData[3][0]+"\t = \t"+testData[3][1]+"\n"
    +testData[4][0]+"\t = \t"+testData[4][1]+"\n"
    +testData[5][0]+"\t = \t"+"**NULL VALUE**"+"\n"
    +"\n* * * End OrderedMap * * *\n",
    stringVal);
  }



  public void testRemove(Object key){
  
    assertNotNull(orderedMap);

    assertEquals(orderedMap.get(testData[2][0]), testData[2][1]);
    
    assertEquals(orderedMap.remove(testData[2][0]), testData[2][1]);

    System.out.println("removed key2/val2; orderedMap now:\n"+orderedMap.keySet());
    
    Iterator it = orderedMap.keySet().iterator();
    assertNotNull(it);
  
    Object next = null;
    while (it.hasNext()) {
      next = it.next();
      assertNotNull(next);
      System.out.println("-> "+(String)next);
      assertTrue(!testData[2][0].equals(next));
    }
  }

  public void testClear(){

    assertNotNull(orderedMap);

    assertNotNull(orderedMap.keySet());
  
    orderedMap.clear();
    
    assertEquals(0, orderedMap.keySet().size());

    assertEquals(0, orderedMap.values().size());
  
  }

    
////////////////////////////////////////////////////////////////////////////////
//                      E N D   T E S T   M E T H O D S                       //
////////////////////////////////////////////////////////////////////////////////


    public static void main(String args[]) {
        junit.textui.TestRunner.run(OrderedMapTest.class);
        System.exit(0);
    }
}


