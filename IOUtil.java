/**
 *  '$RCSfile: IOUtil.java,v $'
 *  Copyright: 2000 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: tao $'
 *     '$Date: 2003-09-02 23:31:05 $'
 * '$Revision: 1.3 $'
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

import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 *  General static utilities for IO operations
 */
public class IOUtil                                         
{

    private static Class classRef = IOUtil.class;
    
    /**
     *  constructor
     */
    private IOUtil() {}

    /**
     *  reads character data from the <code>Reader</code> provided, using a 
     *  buffered read. Returns data as a <code>StringBufer</code>
     *
     *  @param  reader              <code>Reader</code> object to be read
     *
     *  @param  closeWhenFinished   <code>boolean</code> value to indicate 
     *                              whether Reader should be closed when reading
     *                              finished
     *
     *  @return                     <code>StringBuffer</code> containing  
     *                              characters read from the <code>Reader</code>
     *
     *  @throws IOException if there are problems accessing or using the Reader.
     */
    public static StringBuffer getAsStringBuffer(   Reader reader, 
                                                    boolean closeWhenFinished) 
                                                            throws IOException
    {
        if (reader==null) return null;
        
        StringBuffer sb = new StringBuffer();
        try {
            char[] buff = new char[4096];
            int numCharsRead;

            while ((numCharsRead = reader.read( buff, 0, buff.length ))!=-1) {
                sb.append(buff, 0, numCharsRead);
            }
        } catch (IOException ioe) {
            System.err.println("IOUtil.getAsStringBuffer(): Error reading Reader: "
                                                            +ioe.getMessage());
            throw ioe;
        } finally {
            if (closeWhenFinished) {
                try { 
                    if (reader!=null) reader.close();
                } catch (IOException ce) {  
                    System.err.println("IOUtil.getAsStringBuffer(): closing Reader: "
                                                            +ce.getMessage());
                }
            }
        }
        return sb;
    }

    /**
     *  reads character data from the <code>Reader</code> provided, using a 
     *  buffered read. Returns data as a <code>String</code>
     *
     *  @param  reader              <code>Reader</code> object to be read
     *
     *  @param  closeWhenFinished   <code>boolean</code> value to indicate 
     *                              whether Reader should be closed when reading
     *                              finished
     *
     *  @return                     <code>String</code> containing  
     *                              characters read from the <code>Reader</code>
     *
     *  @throws IOException if there are problems accessing or using the Reader.
     */
    public static String getAsString( Reader reader, 
                                      boolean closeWhenFinished) 
                                      throws IOException
    {
        StringBuffer sb = IOUtil.getAsStringBuffer(reader, closeWhenFinished);
        return sb.toString();
    }
    
    /**
     *  reads character data from the <code>StringBuffer</code> provided, and 
     *  writes it to the <code>Writer</code> provided, using a buffered write. 
     *
     *  @param  buffer              <code>StringBuffer</code> whose contents are 
     *                              to be written to the <code>Writer</code>
     *
     *  @param  writer              <code>java.io.Writer</code> where contents 
     *                              of StringBuffer are to be written
     *
     *  @param  closeWhenFinished   <code>boolean</code> value to indicate 
     *                              whether Reader should be closed when reading
     *                              finished
     *
     *  @return                     <code>StringBuffer</code> containing  
     *                              characters read from the <code>Reader</code>
     *
     *  @throws IOException if there are problems accessing or using the Writer.
     */
    public static void writeToWriter(StringBuffer buffer, Writer writer,
                                                  boolean closeWhenFinished) 
                                                            throws IOException
    {
        if (writer==null) {
            throw new IOException("IOUtil.writeToWriter(): Writer is NULL!");
        }
        
        char[] bufferChars = new char[buffer.length()];
        buffer.getChars(0,buffer.length(),bufferChars,0);
        
        try {
            writer.write(bufferChars);
            writer.flush();
        } catch (IOException ioe) {
            System.err.println("IOUtil.writeToWriter(): Error writing to Writer: "
                                                            +ioe.getMessage());
            throw ioe;
        } finally {
            if (closeWhenFinished) {
                try { 
                    if (writer!=null) writer.close();
                } catch (IOException ce) {  
                    System.err.println("IOUtil.writeToWriter(): closing Writer: "
                                                            +ce.getMessage());
                }
            }
        }
    }
    
    
    /**
     *  Gets a resource as a <code>InputStreamReader</code>, given the 
     *  <em>CLASSPATH-RELATIVE</em> name of the file resource to be read
     *
     *  @param cpRelativeFilename   <em>CLASSPATH-RELATIVE</em> name of the file 
     *                              resource to be read
     *
     *  @return                     <code>InputStreamReader</code> for reading  
     *                              from the file resource
     */
    public static InputStreamReader getResourceAsInputStreamReader(
                                                    String cpRelativeFilename) {
                                                    
        InputStream iStream = classRef.getResourceAsStream(cpRelativeFilename);
        return new InputStreamReader(iStream);
    }
    
    
    /**
     *  Given an array of <code>String</code> objects, returns the array 
     *  elements as a single string, formatted as a "list" for printing to 
     *  command line or logging
     *
     *  @param  stringArray         an array of <code>String</code> objects
     *
     *  @return                     the array elements in a single string, 
     *                              formatted as a "list" for printing to 
     *                              command line or logging
     */
    public static String getStringArrayAsString(String[] stringArray) 
    {
        if (stringArray==null) {
            return "\n* * * RECEIVED NULL ARRAY! * * *";
        }
        if (stringArray.length<1) {
            return "\n* * * RECEIVED EMPTY ARRAY! (length=0) * * *";
        }
        if (!(stringArray[0] instanceof String)) {
            return "\n* * * ARRAY DOES NOT CONTAIN STRINGS! * * *";
        }
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("\n---------------------------\n");
        for (int i = 0; i < stringArray.length; i++) {
            buffer.append(" element["+i+"] => ");
            buffer.append(stringArray[i]);
            buffer.append("\n");
        }
        buffer.append("---------------------------\n");
        
        return buffer.toString();
    }
   
   /**
     *  Read character data from a input stream and return a string.
     *
     *  @param  input               The input stream
     *
     *  @return                     The string contain data read from input
     *                              stream
     */  
  public static String getInputStreamAsString(InputStream input)
  {
    String response = null;
    try
    {
      InputStreamReader returnStream = new InputStreamReader(input);
      StringWriter sw = new StringWriter();
      int len;
      char[] characters = new char[512];
      while ((len = returnStream.read(characters, 0, 512)) != -1)
      {
        sw.write(characters, 0, len);
      }
      returnStream.close();
      response = sw.toString();
      sw.close();
    }
    catch(Exception e)
    {
      return null;
    }
  
    return response;
  }
}


