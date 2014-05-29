/**
 *  '$RCSfile: FifoCache.java,v $'
 * Copyright (c) 2011 The Regents of the University of California.
 *
 *   '$Author: jones $'
 *     '$Date: 2003-08-18 18:35:49 $'
 * '$Revision: 1.1 $'
 *
 * Permission is hereby granted, without written agreement and without
 * license or royalty fees, to use, copy, modify, and distribute this
 * software and its documentation for any purpose, provided that the above
 * copyright notice and the following two paragraphs appear in all copies
 * of this software.
 *
 * IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
 * FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
 * ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
 * THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
 * PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
 * CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
 * ENHANCEMENTS, OR MODIFICATIONS.
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
