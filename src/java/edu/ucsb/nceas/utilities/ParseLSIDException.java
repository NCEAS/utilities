/**
 *  '$RCSfile: PropertyExistsException.java,v $'
 *    Purpose: An Exception thrown when an error occurs because an 
 *             AccessionNumber was invalid or used incorrectly
 *  Copyright: 2008 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: Matt Jones
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-07-11 20:36:39 $'
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

/**
 * Exception thrown when an error occurs because property already exists. 
 * Typically this is used when a property is being added and there is a 
 * conflict
 */
public class ParseLSIDException extends Exception {

	private static final long serialVersionUID = 6229544481308174753L;

	/**
	 * Create a new AccessionNumberException.
	 *
	 * @param message The error or warning message.
	 */
	public ParseLSIDException(String message) {
		super(message);
	}
}
