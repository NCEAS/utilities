/**
 *  '$RCSfile: ParseLSIDException.java,v $'
 *    Purpose: An Exception thrown when an error occurs when parsing
 *             an lsid
 *  Copyright: 2009 Regents of the University of California
 *    Authors: Michael Daigle
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
 * Exception thrown when an error occurs when parsing an LSID
 */
public class ParseLSIDException extends Exception {

	private static final long serialVersionUID = 6229544481308174753L;

	/**
	 * Create a new ParseLSIDException.
	 *
	 * @param message The error or warning message.
	 */
	public ParseLSIDException(String message) {
		super(message);
	}
}
