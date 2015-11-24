/**
 *  '$RCSfile: PropertyExistsException.java,v $'
 *    Purpose: An Exception thrown when an error occurs because an 
 *             AccessionNumber was invalid or used incorrectly
 *  Copyright: 2008 Regents of the University of California
 *    Authors: Matt Jones
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-07-11 20:36:39 $'
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

/**
 * Exception thrown when an error occurs because property already exists. 
 * Typically this is used when a property is being added and there is a 
 * conflict
 */
public class PropertyExistsException extends GeneralPropertyException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1596055823732831833L;

	/**
	 * Create a new AccessionNumberException.
	 *
	 * @param message The error or warning message.
	 */
	public PropertyExistsException(String message) {
		super(message);
	}
}
