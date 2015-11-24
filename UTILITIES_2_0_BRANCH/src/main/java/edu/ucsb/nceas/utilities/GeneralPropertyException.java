/**
 *  '$RCSfile: GeneralPropertyException.java,v $'
 *    Purpose: An Exception thrown when an error occurs because an 
 *             AccessionNumber was invalid or used incorrectly
 *  Copyright: 2011 Regents of the University of California 
 *    Authors: Matt Jones
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-07-07 04:27:27 $'
 * '$Revision: 1.2 $'
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
 * Exception that handles general properties exceptions
 */
public class GeneralPropertyException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3811607639186674808L;

  /**
   * Create a new AccessionNumberException.
   *
   * @param message The error or warning message.
   */
  public GeneralPropertyException(String message) {
    super(message);
  }
}
