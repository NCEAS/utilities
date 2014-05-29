/**
 *  '$RCSfile: LSIDUtil.java,v $'
 *  Copyright: 2011 Regents of the University of California 
 *    Authors: Jing Tao, Michael Daigle
 *
 *   '$Author: leinfelder $'
 *     '$Date: 2008-10-02 15:59:09 $'
 * '$Revision: 1.17 $'
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

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class LSIDUtil 
{
	private static final char COLON = ':';
	private static final char DOT = '.';
	private static final String LSIDPREFIX = "urn:lsid:";
    /**
     * This class contains utilities method for LSID
     */
	
	/**
	 * Transform a LSID to document id (metacat). If the given LISD is a invalid one,
	 * null will be returned.
	 * LISD has two types, one is including revision, the other is excluding revision.
	 * The returned the docid will be also with revision or without revision.
	 *  @param LSID   the given LISD string  
     *  @return the docid transformed from the given LSID
     *
	 */
	public static String transformToDocID(String LSID)
	{
		//LISD looks like urn:lsid:gamma.msi.ucsb.edu/OepnAuth/:286:9
		// or urn:lsid:gamma.msi.ucsb.edu/OepnAuth/:286:11:1.
		// So first we should remove the substring which is before the third ":"
		String docid = null;
		int counter =0;
		int targetIndex =3;
		int firstColonIndex = 0;
		if (LSID != null)
		{
			try
			{
				if(LSID.startsWith(LSIDPREFIX))
				{
					for(int i=0; i<LSID.length(); i++)
					{
						char singleChar = LSID.charAt(i);
						// count how many COLON was hitted
						if (singleChar == COLON)
						{
							counter++;
							//System.out.println("The counter is "+counter);
						}
						// when counter equals 3,  the docid will be the left part.
						if(counter == targetIndex)
						{
						   docid = LSID.substring(1+i);
						   docid = docid.replace(COLON, DOT);
						   //System.out.println("in third counter break "+docid);
						   break;
						}
					}
				}
			}
			catch(Exception e)
			{
				docid = null;
			}
		}
		return docid;
	}
	
	/**
	 * Parse the lsid string into an LSID object
	 * 
	 * @param lsidString
	 *            string representation of lsid
	 * @return an LSID object
	 */
	public static LSID parseLSID(String lsidString) throws ParseLSIDException {
		LSID lsid = new LSID();
		
		String regex = "urn:lsid:.+:.+:.*";
		
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(lsidString);
		
		if (!matcher.matches()) {
			throw new ParseLSIDException("Could not parse lsid: " + lsidString);
		}
		
		String[] splitLSID = lsidString.split(":");
		
		lsid.setAuthority(splitLSID[2]);
		lsid.setNamespace(splitLSID[3]);
		lsid.setObjectId(splitLSID[4]);
		
		if (splitLSID.length > 4 && splitLSID[5] != "") {
			Long version = Long.valueOf(splitLSID[5]);
			lsid.setVersion(version);
		}
				
		return lsid;
	}
	
	/**
	 * Get the doc id from an lsid with an option to include the revision
	 * number.
	 * 
	 * @param lsid
	 *            the lsid to parse
	 * @param includeRevision
	 *            if set to true, include the revision part of the doc id,
	 *            otherwise don't
	 * @return the docid
	 */
	public static String getDocId(String lsid, boolean includeRevision) throws ParseLSIDException {
		LSID parsedLSID = parseLSID(lsid);
		
		String docid = parsedLSID.getNamespace() + "." + parsedLSID.getObjectId();
		if (includeRevision && parsedLSID.getVersion() != null ) {
			docid += "." + parsedLSID.getVersion();
		}
		
		return docid;
	}
}
