/**
 *  '$RCSfile: FifoCache.java,v $'
 *  Copyright: 2003 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *
 *   '$Author: jones $'
 *     '$Date: 2003-08-18 18:35:49 $'
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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * A Queue implementing a FIFO (first-in-first-out) algorithm for controlling
 * access to the objects in the cache.  Items are added to the tail of the
 * queue and removed from the head.  The queue has a capacity, and if the 
 * capacity has been reached then the object at the head is dequeued before
 * adding the item at the tail. Items are added with an associated identifier,
 * and can beaccessed by that identifier.  If an item is added with an 
 * identifier that has been used already, then the object is replaced with the
 * new object (it behaves like a hash key).
 */
public class FifoCache
{
    private static int DEFAULT_CAPACITY = 8;
    private int capacity = 0;
    private List keys;
    private Hashtable objects;

    /**
     *  Construct a FifoCache with a default capacity
     */
    public FifoCache()
    {
        this(DEFAULT_CAPACITY);
    }

    /**
     *  Construct a FifoCache with the given capacity
     *
     *  @param capacity the number of elements to keep in the cache
     */
    public FifoCache(int capacity)
    {
        this.capacity = capacity;
        keys = new ArrayList(capacity);
        objects = new Hashtable();
    }

    /**
     * Add an object to the cache with the given identifier
     *
     * @param identifier the unique label for this cache item
     * @param o the object to be stored in the cache
     */
    public void add(String identifier, Object o)
    {
        // Replace the object if the identifier exists
        if (objects.containsKey(identifier)) {
            objects.put(identifier, o);
            return;
        }

        // Check to see if the cache is full first, and if so, remove the head
        if (keys.size() >= this.capacity) {
            remove();
        }

        // Now add the new object with its identifier
        keys.add(identifier);
        objects.put(identifier, o);
    }

    /**
     * Get the oldest item in the cache.
     *
     * @return the object at the head of the cache
     */
    public Object get()
    {
        return objects.get(keys.get(0));
    }

    /**
     * Get the item in the cache associated with the given identifier
     *
     * @return the object with the given identifier, or null if it doesn't exist
     */
    public Object get(String identifier)
    {
        return objects.get(identifier);
    }

    /**
     * Remove the oldest cache item from the list
     */
    public void remove()
    {
        String identifier = (String)keys.remove(0);
        objects.remove(identifier);
    }

    /**
     * Determine if the cache is empty.
     *
     * @return true if the cache has no items in it
     */
    public boolean isEmpty()
    {
        return keys.isEmpty();
    }
}
