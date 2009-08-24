/**
 *  '$RCSfile: DBUtil.java,v $'
 *  Copyright: 2008 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-12-19 21:52:12 $'
 * '$Revision: 1.4 $'
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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *  General static utilities for IO operations
 */
public class DBUtil                                         
{
    
    /**
     *  constructor
     */
    private DBUtil() {}

	/**
	 * Method to get Connection object. This bypasses the db connection pool. 
	 * This method is for administrative purposes only, and should not be 
	 * called while the system is in production usage.
	 * 
	 * @param jdbcConnectString database connection criteria
	 * @param user database user name
	 * @param password database password
	 * @returns Connection object
	 */
	public static Connection getConnection(String jdbcConnectString,
			String user, String password) throws SQLException {
		
		Connection connection = 
			DriverManager.getConnection(jdbcConnectString, user, password);

		return connection;
	}

	/**
	 * Checks to see if a table exists. This bypasses the db connection pool. 
	 * This method is for administrative purposes only, and should not be 
	 * called while the system is in production usage.
	 * 
	 * @param connection an existing connection to the database
	 * @param tableName the name of the table to check
	 * @returns boolean which is true if column is found, false otherwise
	 */
	public static boolean tableExists(Connection connection, String tableName)
			throws SQLException {

		// Gets the database metadata
		DatabaseMetaData dbMetaData = connection.getMetaData();
		
		// Specify the type of object; in this case we want tables
		String[] types = { "TABLE" };
		ResultSet resultSet = dbMetaData.getTables(null, null, "%", types);

		// Get the table names
		while (resultSet.next()) {
			// Get the table name
			String dbResultSet = resultSet.getString(3);
			if (dbResultSet!= null && dbResultSet.equalsIgnoreCase(tableName)) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Checks to see if a column exists for a given table. This bypasses 
	 * the db connection pool. This method is for administrative purposes 
	 * only, and should not be called while the system is in production usage.
	 * 
	 * @param connection an existing connection to the database
	 * @param tableName the name of the table to check
	 * @param columnName the name of the column we are looking for
	 * @returns boolean which is true if column is found, false otherwise
	 */
	public static boolean columnExists(Connection connection, String tableName,
			String columnName) throws SQLException {

		// Gets the database metadata
		DatabaseMetaData dbMetaData = connection.getMetaData();
		
		ResultSet resultSet = dbMetaData.getColumns(null, null,
				tableName, null);
		while (resultSet.next()) {
			String dbColumnName = resultSet.getString("COLUMN_NAME");
			if (dbColumnName != null && dbColumnName.equalsIgnoreCase(columnName)) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Checks to see if an index exists for a given table. This bypasses 
	 * the db connection pool. This method is for administrative purposes 
	 * only, and should not be called while the system is in production usage.
	 * 
	 * @param connection an existing connection to the database
	 * @param tableName the name of the table to check
	 * @param indexName the name of the index we are looking for
	 * @returns boolean which is true if column is found, false otherwise
	 */
	public static boolean indexExists(Connection connection, String tableName, String indexName) throws SQLException {
		// Gets the database metadata
		DatabaseMetaData dbMetaData = connection.getMetaData();
		
		ResultSet resultSet = dbMetaData.getIndexInfo(null, null, tableName,
				false, false);
		while (resultSet.next()) {
			String dbIndexName = resultSet.getString("INDEX_NAME");
			if (dbIndexName != null && dbIndexName.equalsIgnoreCase(indexName)) {
				return true;
			}
		}

		return false;
	}
	
	public static ResultSet select (Connection conn, PreparedStatement ps) throws SQLException {
		ResultSet resultSet = null;
		
		return resultSet;
	}
}


