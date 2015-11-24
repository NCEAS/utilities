/**
 *  '$RCSfile: StringUtil.java,v $'
 *  Copyright: 2000 Regents of the University of California
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-12-11 23:35:47 $'
 * '$Revision: 1.8 $'
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

import java.io.IOException;
import java.io.Reader;
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
	 * Convert a delimited string into a Vector
	 * 
	 * @param list
	 *            delimited list of values
	 * @param delim
	 *            character specifying the delimiter
	 * @return a Vector holding individual values
	 */
	public static Vector<String> toVector(String list, char delim) {
		Vector<String> subList = new Vector<String>();
		if (list == null || list.length() == 0) {
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
  
	/**
	 * Replace any duplicate spaces with single spaces.
	 * 
	 * @param oldString
	 *            the string in which we want to replace duplicate spaces
	 * @return a string holding the modified oldString
	 */
	public static String replaceDuplicateSpaces(String oldString) {		
		while (oldString.contains("  ")) {
			oldString = oldString.replaceAll("  ", " ");
		}

		return oldString;
	} 
	
	/**
	 * Replace any tabs, newlines and carriage returns with single spaces.
	 * 
	 * @param oldString
	 *            the string in which we want to replace tabs, newlines and
	 *            carriage returns
	 * @return a string holding the modified oldString
	 */
	public static String replaceTabsNewLines(String oldString) {		

		oldString = oldString.replaceAll("\r", " ");
		oldString = oldString.replaceAll("\n", " ");
		oldString = oldString.replaceAll("\t", " ");

		return oldString;
	} 
	
	/**
	 * Convert a reader to a string
	 * @param reader the initialized reader
	 * @return string extracted from reader
	 */
	public static String readerToString(Reader reader) throws IOException {
		StringBuffer contents = new StringBuffer();
		int tmp = reader.read();
		while (tmp != -1) {
			contents.append((char)tmp);
			tmp = reader.read();
		}

		reader.reset();
		return contents.toString();
	}
}


