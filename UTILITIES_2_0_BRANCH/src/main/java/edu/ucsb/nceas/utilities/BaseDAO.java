/**
 *  '$RCSfile$'
 *    Purpose: A Class that holds the data from the scheduled_task 
 *             table in the database. 
 *  Copyright: 2009 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: Michael Daigle
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
package edu.ucsb.nceas.utilities;

import java.sql.Timestamp;

public class BaseDAO {
	
	private Long _id;
	private Timestamp _createTime;
	private Timestamp _modTime;
	private String _status;
	
	public Long getId() {
		return _id;
	}
	
	public void setId(Long id) {
		_id = id;
	}
	
	public Timestamp getCreateTime() {
		return _createTime;
	}
	
	public void setCreateTime(Timestamp createTime) {
		_createTime = createTime;
	}
	
	public Timestamp getModTime() {
		return _modTime;
	}
	
	public void setModTime(Timestamp modTime) {
		_modTime = modTime;
	}
	
	public String getStatus() {
		return _status;
	}
	
	public void setStatus(String status) {
		_status = status;
	}
	
}