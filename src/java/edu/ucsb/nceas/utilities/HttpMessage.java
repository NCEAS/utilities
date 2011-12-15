/**
 *  '$RCSfile: HttpMessage.java,v $'
 *  Copyright: 2011 Regents of the University of California 
 *
 *   '$Author: barteau $'
 *     '$Date: 2007-09-28 22:58:34 $'
 * '$Revision: 1.4 $'
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

import java.io.*;
import java.net.*;
import java.util.*;

import HTTPClient.NVPair;
import java.util.Map.Entry;

public class HttpMessage
{
  private URL servlet = null;
  private String argString = null;
  private static String cookie = null;
  private OutputStream out = null;
  private URLConnection con = null;

  public HttpMessage(URL servlet)
  {
    this.servlet = servlet;
  }

  /**
   * Performs a GET request to the previously given servlet
   * with no query string
   */
  public InputStream sendGetMessage() throws IOException
  {
    return sendGetMessage(null);
  }

  /**
   * Performs a GET request to the previously given servlet
   * Builds a query string from the supplied Properties list.
   */
  public InputStream sendGetMessage(Properties args) throws IOException
  {
    argString = "";//default

    if (args != null) {
      argString = "?" + toEncodedString(args);
    }
    URL url = new URL(servlet.toExternalForm() + argString);

    // turn off caching
    con = url.openConnection();
    con.setUseCaches(false);

    return con.getInputStream();
  }

  /**
   * Open a new post connection, preparing the request headers, including
   * cookies
   */
  private void openPostConnection() throws IOException
  {
    // Open the connection
    con = servlet.openConnection();

    // Write any cookies in the request
    if (cookie != null) {
      int k = cookie.indexOf(";");
      if (k > 0) {
        cookie = cookie.substring(0, k);
      }
      con.setRequestProperty("Cookie", cookie);
    }

    // add so Metacat can determine where requests come from
    con.setRequestProperty("User-Agent", "HttpMessage/1.0" );

    // prepare for both input and output
    con.setDoInput(true);
    con.setDoOutput(true);
    // turn off caching
    con.setUseCaches(false);
  }

  /**
     * Sends post data using multipart/form-data encoding. This method can send
     * large data files because the files are streamed directly from disk to the
     * HttpURLConnection.  Assuming that we are using the HTTClient or another
     * similar library that provides a streaming HttpURLConnection, then the
     * data is sent to the connection as it is read from disk (in contrast to the
     * default Sun HttpURLConnection that reads the whole data stream into memory
     * before sending it.
     *
     * @param args a property file containing the name-value pairs that are to be
     *             sent to the server
     * @param fileNames a property file containing the name for a formfield
     *                  that represents a file and the filename (as the property
     *                  value)
     * @param fileDate  the inputStream for the file data to be sent to Metacat
     * @param size      the size of the data being sent to Metacat
     * @return the response stream that comes from the server
     * @exception IOException If any file operation fails.
     */
    public InputStream sendPostData(Properties args, Properties fileNames,
                                    InputStream fileData, int size)
                       throws IOException
    {
      openPostConnection();

      // Prepare the parameters
      int len = args.size();
      NVPair[] opts = new NVPair[len];
      Enumeration names = args.propertyNames();
      for (int i=0; i<len; i++) {
        String name = (String)names.nextElement();
        String value = args.getProperty(name);
        opts[i] = new NVPair(name, value);
      }
      // Prepare the data files
      len = fileNames.size();
      NVPair[] data = new NVPair[len];
      Enumeration dataNames = fileNames.propertyNames();
      for (int i=0; i<len; i++) {
        String name = (String)dataNames.nextElement();
        String value = fileNames.getProperty(name);
        data[i] = new NVPair(name, value);
      }

      // Create the multipart/form-data form object
      MultipartForm myform = new MultipartForm(opts, data, fileData, size);

      // Set some addition request headers
      ((HttpURLConnection)con).setRequestMethod("POST");
      String ctype = myform.getContentType();
      ((HttpURLConnection)con).setRequestProperty("Content-Type", ctype);
      long contentLength = myform.getLength();
      ((HttpURLConnection)con).setRequestProperty("Content-Length",
               new Long(contentLength).toString());

      // Open the output stream and write the encoded data to it
      out = con.getOutputStream();
      myform.writeEncodedMultipartForm(out);

      // close the connection and return the response stream
      InputStream res = closePostConnection();
      return res;
    }

  /**
   * Sends post data using multipart/form-data encoding. This method can send
   * large data files because the files are streamed directly from disk to the
   * HttpURLConnection.  Assuming that we are using the HTTClient or another
   * similar library that provides a streaming HttpURLConnection, then the
   * data is sent to the connection as it is read from disk (in contrast to the
   * default Sun HttpURLConnection that reads the whole data stream into memory
   * before sending it.
   *
   * @param args a property file containing the name-value pairs that are to be
   *             sent to the server
   * @param fileNames a property file containing the name for a formfield
   *                  that represents a file and the filename (as the property
   *                  value)
   * @return the response stream that comes from the server
   * @exception IOException If any file operation fails.
   */
  public InputStream sendPostData(Properties args, Properties fileNames)
                     throws IOException
  {
    openPostConnection();

    // Prepare the parameters
    int len = args.size();
    NVPair[] opts = new NVPair[len];
    Enumeration names = args.propertyNames();
    for (int i=0; i<len; i++) {
      String name = (String)names.nextElement();
      String value = args.getProperty(name);
      opts[i] = new NVPair(name, value);
    }
    // Prepare the data files
    len = fileNames.size();
    NVPair[] data = new NVPair[len];
    Enumeration dataNames = fileNames.propertyNames();
    for (int i=0; i<len; i++) {
      String name = (String)dataNames.nextElement();
      String value = fileNames.getProperty(name);
      data[i] = new NVPair(name, value);
    }

    // Create the multipart/form-data form object
    MultipartForm myform = new MultipartForm(opts, data);

    // Set some addition request headers
    ((HttpURLConnection)con).setRequestMethod("POST");
    String ctype = myform.getContentType();
    ((HttpURLConnection)con).setRequestProperty("Content-Type", ctype);
    long contentLength = myform.getLength();
    ((HttpURLConnection)con).setRequestProperty("Content-Length",
             new Long(contentLength).toString());

    // Open the output stream and write the encoded data to it
    out = con.getOutputStream();
    myform.writeEncodedMultipartForm(out);

    // close the connection and return the response stream
    InputStream res = closePostConnection();
    return res;
  }

  /**
   * Sends post data using url encoding.  This method is used most of the time
   * and is for typical paramameter lists where the data is not extensive.
   *
   * @param args a property file containing the name-value pairs that are to be
   *             sent to the server
   * @return the response stream that comes from the server
   * @exception IOException If any file operation fails.
   */
  public InputStream sendPostData(Properties args) throws IOException
  {
    openPostConnection();
    out = new DataOutputStream(con.getOutputStream());
    Enumeration names = args.propertyNames();
    while (names.hasMoreElements()) {
      String name = (String)names.nextElement();
      String value = args.getProperty(name);
      sendNameValuePair(name, value);
      if (names.hasMoreElements()) {
        ((DataOutputStream)out).writeBytes("&");
        out.flush();
      }
    }
    InputStream res = closePostConnection();
    return res;
  }
  
    /**
     * This is an alternative method of sending post data.  This method allows multiple
     * parameters with the same name.
     * @param args Properties of arguments where,
     *    key = param value
     *    value = param name
     * @return InputStream
     * @throws java.io.IOException Input/Output Exception
     */
  public InputStream sendPostParameters(Properties args) throws IOException  {
      String                        paramName, paramValue;
      Iterator                      iterIt;
      Map.Entry                     entry;
      InputStream                   result;
      
    openPostConnection();
    out = new DataOutputStream(con.getOutputStream());
    iterIt = args.entrySet().iterator();
    while (iterIt.hasNext()) {
        entry = (Entry) iterIt.next();
        paramValue = (String) entry.getKey();
        paramName = (String) entry.getValue();
        sendNameValuePair(paramName, paramValue);
        if (iterIt.hasNext()) {
            ((DataOutputStream)out).writeBytes("&");
            out.flush();
        }
    }
    result = closePostConnection();
    return(result);
  }

  /**
   * Utility method to URL encode and send a single name-value pair
   */
  private void sendNameValuePair(String name, String data) throws IOException
  {
    ((DataOutputStream)out).writeBytes(URLEncoder.encode(name));
    ((DataOutputStream)out).writeBytes("=");
    ((DataOutputStream)out).writeBytes(URLEncoder.encode(data));
    out.flush();
  }

  /**
   * Clean up the post connection, save any cookies, close the output stream
   *
   * @return the response stream that comes from the server
   * @exception IOException If any file operation fails.
   */
  private InputStream closePostConnection() throws IOException
  {
    // Open the response stream
    InputStream response;
    response = con.getInputStream();
    // Read any cookies in the response
    String temp = con.getHeaderField("Set-Cookie");
    if (temp != null) {
      cookie = temp;
      int k = cookie.indexOf(";");
      if (k > 0) {
        cookie = cookie.substring(0, k);
      }
    }
    out.close();
    // Return the response stream
    return response;
  }

  /**
   * Performs a POST request with no query parameters
   * @deprecated Replaced by #sendPostData(Properties args)
   */
  public InputStream sendPostMessage() throws IOException
  {
    return sendPostMessage(null);
  }

  /**
   * Sends post data using url encoding.  This method is used most of the time
   * and is for typical paramameter lists where the data is not extensive.
   *
   * @param args a property file containing the name-value pairs that are to be
   *             sent to the server
   * @return the response stream that comes from the server
   * @exception IOException If any file operation fails.
   * @see #sendPostData(Properties args)
   * @deprecated Replaced by #sendPostData(Properties args)
   */
  public InputStream sendPostMessage(Properties args) throws IOException
  {
    return sendPostData(args);
  }

  /**
   * Converts a Properties list to a URL-encoded query string
   */
  private String toEncodedString(Properties args)
  {
    StringBuffer buf = new StringBuffer();
    Enumeration names = args.propertyNames();
    while (names.hasMoreElements())
    {
      String name = (String) names.nextElement();
      String value = args.getProperty(name);
        buf.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value));
      if (names.hasMoreElements())
          buf.append("&");
    }
    return buf.toString();
  }

  /**
   * return the cookie that this message object contains
   */
  public static String getCookie()
  {
    return cookie;
  }

  /**
   * return the cookie that this message object contains
   */
  public static void setCookie(String newCookie)
  {
    cookie = newCookie;
  }
}
