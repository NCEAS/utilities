/**
 *  '$RCSfile: Options.java,v $'
 *  Copyright: 2003 Regents of the University of California
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-07-07 04:27:27 $'
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * A class that reads options from a properties file.  This is a Singleton
 * because there will never be a need for more than one instance of this
 * class.  The class needs to be initialized using a java properties file, 
 * and from that point forward can be accessed using the static getInstance()
 * method.
 */
public class Options 
{
    private static Options options = null;
    private static Properties appConfig = null;
    private static File propertyFile    = null;

    /**
     * Private constructor because this is a Singleton.
     *
     * @param propertyFile the file from which to read properties
     * @throws an IOException if the property file can't be read
     */
    private Options(File propertyFile) throws IOException
    {
        // locate, open, and read the properties
        appConfig = new Properties();
        Options.propertyFile = propertyFile;
        FileInputStream fis = new FileInputStream(propertyFile);
        appConfig.load(fis);
        fis.close();
    }

    /**
     * Initialize the single instance of the Options, 
     * creating it if needed from the given propertyFile.
     *
     * @param propertyFile the file from which to read properties
     * @return the single instance of the Options
     * @throws an IOException if the property file can't be read
     */
    public static Options initialize(File propertyFile) throws IOException
    {
        if (options == null) {
            options = new Options(propertyFile);
        }
        return options;
    }
    
    /**
     * Reloads opitions. This method will overwrite the current options with new options
     * from property file. This method should be used when the property file is changed by user.
     * @return
     *              the single instance of the Options
     * @throws IOException if the property file can't be read
     */
    public static Options reload() throws IOException
    {
    	options =new Options(propertyFile);
    	return options;
    }

    /**
     * Get the single instance of the Options, assuming it has
     * already been initialized.  If the Options instance has not 
     * yet been created yet (using the method "initialize(File)", 
     * then this method returns null.
     *
     * @return the single instance of the Options
     */
    public static Options getInstance()
    {
        return options;
    }

    /**
     * Get an option value from the properties file
     *
     * @param optionName the name of the option requested
     * @return the String value for the given property, or null if not found
     */
    public synchronized String getOption(String optionName) {
        String value = (String)appConfig.get(optionName);
  
        return value;
    }
    
    /**
     * Reset value for given option name or set a new option pair in the
     * peroperty file. This method will also call a save method to save
     * the new values into properties file. Note: the comment in property
     * file will be gone and order of keys will be change
     * @param key  the option name
     * @param value  the new option value
     */
    public synchronized void setOption(String key, String value)
    {
        appConfig.setProperty(key, value);
        storePropertyFile();
    }
    
    /**
     * Return a list of the options that are configured.
     * 
     * @return an Enumeration of the names of the options available
     */
    public Enumeration propertyNames() {
        return appConfig.propertyNames();
    }
    
    /**
     * This method will store the new property value into file
     */
    private void storePropertyFile()
    {
        try
        {
            FileOutputStream writer = new FileOutputStream(propertyFile);
            appConfig.store(writer, null);
            writer.close();
        }
        catch (IOException e)
        {
            System.err.println("Could save the new property into file");
        }
    }
}
