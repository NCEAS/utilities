/**
 *  '$RCSfile: ServerUtil.java,v $'
 *  Copyright: 2000 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-06-04 18:51:16 $'
 * '$Revision: 1.1.2.1 $'
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


