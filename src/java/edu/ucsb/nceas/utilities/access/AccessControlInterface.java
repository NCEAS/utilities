/**
 *  '$RCSfile$'
 *    Purpose: A Class that loads eml-access.xml file containing ACL 
 *             for a metadata document into relational DB
 *  Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: Jivka Bojilova
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

/**
 * This interface will handle the access control for different documents:
 * the package-orented, single eml2 document and other single file
 */
public interface AccessControlInterface
{
  public static final String CHMODSTRING = "CHANGEPERMISSION";
  public static final String WRITESTRING = "WRITE";
  public static final String READSTRING  = "READ";
  public static final String ALLSTRING   = "ALL";
  public static final int CHMOD = 1;
  public static final int WRITE = 2;
  public static final int READ = 4;
  public static final int ALL = 7;
  public static final String ALLOWFIRST="allowFirst";
  public static final String DENYFIRST="denyFirst";
  public static final String ALLOW="allow";
  public static final String DENY="deny";
  public static final String PUBLIC="public";
  public static final String ACLID="acl";
  public static final String ACCESS="access";
  public static final String PERMISSION="permission";
  public static final String PRINCIPAL="principal";
  
 
}
