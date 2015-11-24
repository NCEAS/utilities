/**
 *  '$RCSfile: DateUtil.java,v $'
 * Copyright (c) 2011 The Regents of the University of California.
 * All rights reserved.
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: daigle $'
 *     '$Date: 2009/03/23 21:53:59 $'
 * '$Revision: 1.11 $'
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
    	return getHumanReadable(cal, "MM/dd/yy HH:mm:ss z");
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
    	return getHumanReadable(timestamp, "MM/dd/yy HH:mm:ss z");
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


