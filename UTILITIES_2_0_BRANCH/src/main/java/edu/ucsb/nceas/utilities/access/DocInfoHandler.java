/**
 *  '$RCSfile$'
 *    Purpose: a class to handle document info request xml strings
 *  Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: Chad Berkley
 *
 *   '$Author$'
 *     '$Date$'
 * '$Revision$'
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

package edu.ucsb.nceas.utilities.access;

import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/** 
 * A Class implementing callback methods for the SAX parser to
 * call when processing the XML messages from the replication handler
 */
public class DocInfoHandler extends DefaultHandler {
	
	private Hashtable<String, String> _docinfo = new Hashtable<String, String>();
	private String _currentTag = null;
	private XMLAccessDAO _currentAccessDAO = null;
	private String _accessPermOrder;
	private String _accessFileId;
	private String _subTreeId;
	private String _guid;
	private boolean _inPrincipal = false;
	private boolean _inPermission = false;
	private boolean _inAllow = false;
	private boolean _inDeny = false;
  
	private Vector<XMLAccessDAO> xmlAccessDAOList = new Vector<XMLAccessDAO>();
	private Stack<XMLAccessDAO> xmlAccessDAOStack = new Stack<XMLAccessDAO>();
	
	private String chars = "";
	
	/* Get the text value of READ, WRITE, CHMOD or ALL. */
	public static String txtValue(int permission) {
		StringBuffer txtPerm = new StringBuffer();
		
		if ((permission & AccessControlInterface.ALL) == AccessControlInterface.ALL) {
			return AccessControlInterface.ALLSTRING;
		}		
		if ((permission & AccessControlInterface.CHMOD) == AccessControlInterface.CHMOD) {
			txtPerm.append(AccessControlInterface.CHMODSTRING);
		}
		if ((permission & AccessControlInterface.READ) == AccessControlInterface.READ) {
			if (txtPerm.length() > 0)
				txtPerm.append(",");
			txtPerm.append(AccessControlInterface.READSTRING);
		}
		if ((permission & AccessControlInterface.WRITE) == AccessControlInterface.WRITE) {
			if (txtPerm.length() > 0)
				txtPerm.append(",");
			txtPerm.append(AccessControlInterface.WRITESTRING);
		}

		return txtPerm.toString();
	}

	
    /** Get the int value of READ, WRITE, CHMOD or ALL. */
	public static int intValue(String permission) {
	    
		int thisPermission = 0;
		try
		{
		    thisPermission = new Integer(permission).intValue();
		    if(thisPermission >= 0 && thisPermission <= 7)
		    {
		        return thisPermission;
		    }
		    else
		    {
		        thisPermission = -1;
		    }
		}
		catch(Exception e)
		{ //do nothing.  this must be a word
		}
		
		if (permission.toUpperCase().contains(AccessControlInterface.CHMODSTRING)) {
			thisPermission |= AccessControlInterface.CHMOD;
		} 
		if (permission.toUpperCase().contains(AccessControlInterface.READSTRING)) {
			thisPermission |= AccessControlInterface.READ;
		} 
		if (permission.toUpperCase().contains(AccessControlInterface.WRITESTRING)) {
			thisPermission |= AccessControlInterface.WRITE;
		} 
		if (permission.toUpperCase().contains(AccessControlInterface.ALLSTRING)) {
			thisPermission |= AccessControlInterface.ALL;
		}

		return thisPermission;
	}
	public DocInfoHandler() {
	}

	public DocInfoHandler(String guid) {
		_guid = guid;
	}
  
   /**
	 * capture the name of the tag.
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
	    chars = "";
		_currentTag = localName;
		if (_currentTag.equals("access")) {
			if (_accessPermOrder == null) {
				_accessPermOrder = attributes.getValue("order");			
			}
			if (_accessFileId == null) {
				_accessFileId = attributes.getValue("accessfileid");			
			}
			if (_subTreeId == null) {
				_subTreeId = attributes.getValue("subtreeid");			
			}
		} else if (_currentTag.equals(AccessControlInterface.ALLOW)) {
			_inAllow = true;
		} else if (_currentTag.equals(AccessControlInterface.DENY)) {
			_inDeny = true;
		} else if (_currentTag.equals(AccessControlInterface.PERMISSION)) {
			_inPermission = true;			
		} else if (_currentTag.equals(AccessControlInterface.PRINCIPAL)) {
			// new principal means new DAO for storing it
			_inPrincipal = true;
			_currentAccessDAO = new XMLAccessDAO();
			_currentAccessDAO.setGuid(_guid);
			_currentAccessDAO.setPermOrder(_accessPermOrder);
			_currentAccessDAO.setAccessFileId(_accessFileId);
			_currentAccessDAO.setSubTreeId(_subTreeId);
			if (_inAllow) {
				_currentAccessDAO.setPermType(AccessControlInterface.ALLOW);
			}
			if (_inDeny) {
				_currentAccessDAO.setPermType(AccessControlInterface.DENY);
			}
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
			    
	    _docinfo.put(_currentTag, chars);
    	
        if (_currentTag.equals(AccessControlInterface.PRINCIPAL) && _inPrincipal) {
            if (_currentAccessDAO != null) {
                _currentAccessDAO.setPrincipalName(chars);
            }
        } else if (_currentTag.equals(AccessControlInterface.PERMISSION) && _inPermission) {
            if (_currentAccessDAO != null) {
                String permString = chars;
                Long permLong = Long.valueOf(intValue(permString));
                //add this permission for each DAO in the stack
                for (int i = 0; i < xmlAccessDAOStack.size(); i++) {
                    xmlAccessDAOStack.get(i).addPermission(permLong);
                }
            }
        }
		
		if (localName.equals(AccessControlInterface.ALLOW)) {
			_inAllow = false;
		} else if (localName.equals(AccessControlInterface.DENY)) {
			_inDeny = false;
		} else if (_currentTag.equals(AccessControlInterface.PRINCIPAL)) {
			_inPrincipal = false;
			xmlAccessDAOStack.push(_currentAccessDAO);
		} else if (_currentTag.equals(AccessControlInterface.PERMISSION)) {
			_inPermission = false;
		}
		//end of a section for allow/deny
		if (
			localName.equals(AccessControlInterface.ALLOW)
			||
			localName.equals(AccessControlInterface.DENY)
			) {
			//get all the DAOs for this section and add them to the overall list
			xmlAccessDAOList.addAll(xmlAccessDAOStack);
			xmlAccessDAOStack.clear();
		}
	}
  
  /**
	 * put the content and the name of the tag into the hashtable. the name of
	 * the tag is the key.
	 */
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    chars += new String(ch, start, length);
  }
  
  public Hashtable<String,String> getDocInfo()
  {
    return _docinfo;
  }
  
  public Vector<XMLAccessDAO> getAccessControlList() {
	  return xmlAccessDAOList;
  }
}
