/**
 *  '$RCSfile: Options.java,v $'
 *  Copyright: 2003 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *
 *   '$Author: daigle $'
 *     '$Date: 2008-05-05 17:11:40 $'
 * '$Revision: 1.4.2.1 $'
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;

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
    private static SortedProperties appConfig = null;
    private static File propertyFile    = null;

    /**
     * Private constructor because this is a Singleton.
     *
     * @param propertyFile the file from which to read properties
     * @throws an IOException if the property file can't be read
     */
    private Options(File propFile) throws IOException
    {
        // locate, open, and read the properties
        appConfig = new SortedProperties(propFile.getPath());
        propertyFile = propFile;
        appConfig.load();
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
     * Reloads options. This method will overwrite the current options with new options
     * from property file. This method should be used when the property file is changed by user.
     * @return the single instance of the Options
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
     * property file. This method will also call a save method to save
     * the new values into properties file. 
     * @param key  the option name
     * @param value  the new option value
     */
    public synchronized void setOption(String key, String value)
    {
        appConfig.setProperty(key, value);
        storePropertyFile();
    }
    
    /**
     * Reset value for given option name or set a new option pair in the
     * property file. This method will NOT save the values to a properties
     * file.  This method should be used when many changes will be made to 
     * the properties at one time and you don't want to keep writing the file.
     * You must call persistOptions() once you've made all your changes 
     * @param key  the option name
     * @param value  the new option value
     */
    public synchronized void setOptionNoPersist(String key, String value)
    {
        appConfig.setProperty(key, value);
    }
    
    /**
     * Save the properties to a properties file. 
     */
    public synchronized void persistOptions()
    {
        storePropertyFile();
    }
    
    /**
     * Return a list of the options that are configured.
     * 
     * @return an Enumeration of the names of the options available
     */
    public Enumeration<String> propertyNames() {
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
