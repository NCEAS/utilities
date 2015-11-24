/**
 *  '$RCSfile: IOUtil.java,v $'
 *  Copyright: 2011 Regents of the University of California 
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: brooke $'
 *     '$Date: 2003-09-15 16:18:50 $'
 * '$Revision: 1.5 $'
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

import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.PrintWriter;


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
//            System.err.println("IOUtil.getAsStringBuffer(): Error reading Reader: "
//                                                            +ioe.getMessage());
            throw ioe;
        } finally {
            if (closeWhenFinished) {
                try { 
                    if (reader!=null) reader.close();
                } catch (IOException ce) { ce.printStackTrace(); }
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
//            System.err.println("IOUtil.writeToWriter(): Error writing to Writer: "
//                                                            +ioe.getMessage());
            throw ioe;
        } finally {
            if (closeWhenFinished) {
                try { 
                    if (writer!=null) writer.close();
                } catch (IOException ce) { ce.printStackTrace(); }
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
  
  
  
  
  /**
   *  Read a Stack Trace from the argument Throwable object and return it as a 
   *  String for logging etc.
   *
   *  @param  e   The <code>Throwable</code> source for the stack trace
   *
   *  @return     The string contain the stack trace read from input
   */  
  public static String getStackTraceAsString(Throwable e) {
  
    StringWriter sw = new java.io.StringWriter();
    PrintWriter  pw = new java.io.PrintWriter( sw );
    ((Exception) e).printStackTrace( pw );
    return sw.toString();
  }
}


