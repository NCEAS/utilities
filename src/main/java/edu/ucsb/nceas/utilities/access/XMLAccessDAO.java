/**
 *  '$RCSfile$'
 *    Purpose: A Class that represents an XML Text node and its contents,
 *             and can build itself from a database connection
 *  Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: Matt Jones
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

import java.sql.Date;

import edu.ucsb.nceas.utilities.BaseDAO;

/**
 * A Class that represents an XML access rule. It include principal and 
 * permission
 */
public class XMLAccessDAO extends BaseDAO {

	private String _guid = null;
	private String _accessFileId = null;
	private String _principalName = null;
	private Long _permission = null;
	private String _permType = null;
	private String _permOrder = null;
	private Date _beginTime = null;
	private Date _endTime = null;
	private Long _ticketCount = null;
	private String _subTreeId = null;
	private String _startNodeId = null;
	private String _endNodeId = null;
	
	public String getGuid() {
		return _guid;
	}
	
	public void setGuid(String guid) {
		if (guid != null && guid.equals("")) {
			guid = null;
		} else {
			_guid = guid;
		}
	}

	public String getAccessFileId() {
		return _accessFileId;
	}
	
	public void setAccessFileId(String accessFileId) {
		if(accessFileId != null && accessFileId.equals("")) {
			_accessFileId = null;
		} else {
			_accessFileId = accessFileId;
		}
	}
	
	public String getPrincipalName() {
		return _principalName;
	}
	
	public void setPrincipalName(String principalName) {
		if (principalName != null && principalName.equals("")) {
			_principalName = null;
		} else {
			_principalName = principalName;
		}
	}
	
	public Long getPermission() {
		return _permission;
	}
	
	public void setPermission(Long permission) {
		if (_permission == null) {
			_permission = new Long(0);
		}
		_permission = permission;
	}
	
	public void addPermission(Long permission) {
		if (_permission != null) {
			_permission |= permission;
		} else {
			_permission = permission;
		}
	}
	
	public String getPermType() {
		return _permType;
	}
	
	public void setPermType(String permType) {
		if (permType != null && permType.equals("")) {
			_permType = null;
		} else {
			_permType = permType;
		}
	}
	
	public String getPermOrder() {
		return _permOrder;
	}
	
	public void setPermOrder(String permOrder) {
		if (permOrder != null && permOrder.equals("")) {
			_permOrder = null;
		} else {
			_permOrder = permOrder;
		}
	}
	
	public Date getBeginTime() {
		return _beginTime;
	}
	
	public void setBeginTime(Date beginTime) {
		_beginTime = beginTime;
	}
	
	public Date getEndTime() {
		return _endTime;
	}
	
	public void setEndTime(Date endTime) {
		_endTime = endTime;
	}
	
	public Long getTicketCount() {
		return _ticketCount;
	}
	
	public void setTicketCount(Long ticketCount) {
		_ticketCount = ticketCount;
	}
	
	public String getSubTreeId() {
		return _subTreeId;
	}
	
	public void setSubTreeId(String subTreeId) {
		if (subTreeId != null && subTreeId.equals("")) {
			_subTreeId = null;
		} else {
			_subTreeId = subTreeId;
		}
	}
	
	public String getStartNodeId() {
		return _startNodeId;
	}
	
	public void setStartNodeId(String startNodeId) {
		if (startNodeId != null && startNodeId.equals("")) {
			_startNodeId = null;
		} else {
			_startNodeId = startNodeId;
		}
	}
	
	public String getEndNodeId() {
		return _endNodeId;
	}
	
	public void setEndNodeId(String endNodeId) {
		if (endNodeId != null && endNodeId.equals("")) {
			endNodeId = null;
		} else {
			_endNodeId = endNodeId;
		}
	}

}
