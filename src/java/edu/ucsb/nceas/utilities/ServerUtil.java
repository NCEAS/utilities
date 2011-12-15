/**
 *  '$RCSfile: ServerUtil.java,v $'
 *  Copyright: 2000 Regents of the University of California
 *    Authors: @authors@
 *    Release: @release@
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
 *  General static utilities for File operations
 */
public class ServerUtil                                         
{
	
    /**
     *  private constructor - all methods are static so there is no
     *  no need to instantiate.
     */
    private ServerUtil() {}
    
    /**
	 * Get the operating system.
	 * 
	 * @returns string representation of the operating system name
	 */
    public static String getOSName() {
    	System.getProperty("os.name");
    	return null;
    }
    
    /**
	 * Determines if this is windows OS
	 * 
	 * @returns true if OS name starts with "Windows", false otherwise.
	 */
    public static boolean isWindowsOS() {
    	String osName = getOSName();
    	return osName.startsWith("Windows");
    }


}


