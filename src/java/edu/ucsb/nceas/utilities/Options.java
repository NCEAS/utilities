/**
 *  '$RCSfile: Options.java,v $'
 *  Copyright: 2003 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *
 *   '$Author: tao $'
 *     '$Date: 2005-08-29 22:52:02 $'
 * '$Revision: 1.2 $'
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private Properties appConfig = null;
    private File propertyFile    = null;

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
        this.propertyFile = propertyFile;
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
    
    /*
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
