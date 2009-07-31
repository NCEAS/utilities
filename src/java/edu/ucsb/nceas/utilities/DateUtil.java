/**
 *  '$RCSfile: DateUtil.java,v $'
 *  Copyright: 2000 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: daigle $'
 *     '$Date: 2009/03/23 21:53:59 $'
 * '$Revision: 1.11 $'
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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 *  General static utilities for File operations
 */
public class DateUtil                                         
{   
    /**
     *  constructor
     */
    private DateUtil() {}

    /**
	 * Get the human readable date from a calendar object formatted
	 * as "d MMM yyyy hh:mm:ss aaa z".
	 * 
	 * @param cal the calendar object we want to check
	 * @param dstAdjust boolean that says whether to adjust for daylight savings or not
	 * @param format the string representation of the date format
	 * 
	 * @returns String representation of the calendar
	 */
    public static String getHumanReadable(Calendar cal) throws UtilException {
    	return getHumanReadable(cal, "mm/dd/yy HH:mm:ss z");
    }
    
    /**
	 * Get the human readable date from a calendar object formatted
	 * with the given format.
	 * 
	 * @param cal the calendar object we want to check
	 * @param dstAdjust boolean that says whether to adjust for daylight savings or not
	 * @param format the string representation of the date format
	 * 
	 * @returns String representation of the calendar
	 */
    public static String getHumanReadable(Calendar cal, String format) throws UtilException {
    	SimpleDateFormat sdf = null;
    	
    	if (cal == null) {
    		throw new UtilException("DateUtil.getHumanReadable -  cannot have a null calendar object");
    	}
    	try {
    		sdf = new SimpleDateFormat(format);
    	} catch (IllegalArgumentException iae) {
    		throw new UtilException("DateUtil.getHumanReadable - illegal date format: " + format 
    				+ " : " + iae.getMessage());
    	}
    	
    	return sdf.format(cal.getTime());
    }
    
    /**
	 * Get the human readable date from a timestamp object formatted
	 * as "d MMM yyyy hh:mm:ss aaa z".
	 * 
	 * @param timestamp the timestamp object we want to check
	 * @param dstAdjust boolean that says whether to adjust for daylight savings or not
	 * @param format the string representation of the date format
	 * 
	 * @returns String representation of the calendar
	 */
    public static String getHumanReadable(Timestamp timestamp) throws UtilException {
    	return getHumanReadable(timestamp, "mm/dd/yy HH:mm:ss z");
    }
    
    /**
	 * Get the human readable date from a timestamp object formatted
	 * with the given format.
	 * 
	 * @param timestamp the timestamp object we want to check
	 * @param dstAdjust boolean that says whether to adjust for daylight savings or not
	 * @param format the string representation of the date format
	 * 
	 * @returns String representation of the calendar
	 */
    public static String getHumanReadable(Timestamp timestamp, String format) throws UtilException {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(timestamp.getTime());
    	return getHumanReadable(cal, format);   	
    }
    
    /**
	 * Convert a human readable date string to a Calendar object using the
	 * provided format
	 * 
	 * @param dateString
	 *            the human readable date
	 * @param format
	 *            the format to use to parse the date
	 * @return the calendar object for the provided date
	 */
    public static Calendar humanReadableToCalendar(String dateString, String format) throws UtilException {
		Calendar startCal = null;
    	
    	try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			dateFormat.setLenient(false);
			Date startDate = dateFormat.parse(dateString);
			startCal = Calendar.getInstance();
			startCal.setTime(startDate);
		} catch (ParseException pe) {
			throw new UtilException("DateUtil.humanReadableToCalendar - parsing error when parsing date: "
					+ dateString + " with format : " + format + " : " + pe.getMessage());
		}
		
		return startCal;
    }
}


