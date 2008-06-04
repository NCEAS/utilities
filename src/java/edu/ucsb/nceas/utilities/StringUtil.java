/**
 *  '$RCSfile: StringUtil.java,v $'
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

import java.util.Vector;


/**
 *  General static utilities for String operations
 */
public class StringUtil                                         
{

  /**
  *  private constructor
  */
  private StringUtil() {}

  

  /**
   *  Strips all whitespace characters from the given string. Characters may 
   *  appear anywhere in string - not just a trim() function. Whitespace 
   *  characters removed are currently: ' ', '/t', '/n', '/r' 
   *
   *  @param  input the <code>String</code> whose whitespace will be stripped
   *
   *  @return the <code>String</code> after the whitespace has been stripped
   */
  private static StringBuffer sawBuff = new StringBuffer();
  //
  public static String stripAllWhiteSpace(String input) {

    if (input==null) return null;
    sawBuff.delete(0,sawBuff.length());
    char nextChar = ' ';
    for (int i=0; i<input.length(); i++) {
      nextChar = input.charAt(i);
      if (nextChar != ' ') sawBuff.append(nextChar);
    }
    return stripTabsNewLines(sawBuff.toString());
  }
  
  
  
  
  /**
   *  Strips all '/t', '/n' and '/r' characters from the given string. 
   *` Characters may appear anywhere in string - not just a trim() function. 
   *  Space characters (' ') are *not* removed
   *
   *  @param  input   the <code>String</code> whose tabs and newlines will be 
   *                  stripped
   *
   *  @return the <code>String</code> without the tabs and newlines 
   */
  private static StringBuffer stnlBuff = new StringBuffer();
  //
  public static String stripTabsNewLines(String input) {

    if (input==null) return null;
    stnlBuff.delete(0,stnlBuff.length());
    char nextChar = ' ';
    for (int i=0; i<input.length(); i++) {

      nextChar = input.charAt(i);
      switch (nextChar) {
      case '\r':
      case '\n':
      case '\t':
        break;
      default:
        stnlBuff.append(nextChar);
      }
    }
    return stnlBuff.toString();
  }
  
	/**
	 * Convert a comma delimited string into a Vector
	 * 
	 * @param list
	 *            delimited list of values
	 * @param delim
	 *            character specifying the delimiter
	 * @return a Vector holding individual values
	 */
	public static Vector<String> toVector(String list, char delim) {
		Vector<String> subList = new Vector<String>();
		if (list == null) {
			return subList;
		}

		int fromIndex = 0;
		int toIndex = -2;
		while (toIndex != list.length()) {
			toIndex = list.indexOf(delim, fromIndex);
			if (toIndex == -1) {
				toIndex = list.length();
			}
			subList.add(list.substring(fromIndex, toIndex));
			fromIndex = toIndex + 1;
		}

		return subList;
	} 
}


