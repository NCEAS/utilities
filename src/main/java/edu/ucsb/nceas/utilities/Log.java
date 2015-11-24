/**
 *  '$RCSfile: Log.java,v $'
 *  Copyright: 2011 Regents of the University of California 
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: higgins $'
 *     '$Date: 2003-09-21 23:37:29 $'
 * '$Revision: 1.1 $'
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

import javax.swing.JOptionPane;

/**
 * The Log is a utility class for logging messages to stdout, stderr,
 * a file, or a dialog box.  By default you can call just the static method
 * Log.debug() to log messages.  If you want to turn debugging off or 
 * change the default severity, use Log.getLog() to get the Log instance, 
 * and then call setDubug() and setDebugLevel(), respectively.          
 *
 * This class was originally created for use in Morpho.
 * It is a very simple logging tool compared to the more sophisticated ones
 * (e.g. log4j, or the classes added to Java 1.4), but it is quite useful with
 * a minimum of overhead
 */
public class Log
{
  private static Log log = null;
  private static boolean debug = true;
  private static int debugLevel = 9;

  /**
   * Creates a new instance of Log. Private because we don't want it to
   * be called because this is a singleton.
   */
  private Log()
  {
  }

  /**
   * Get the single instance of the Log
   *
   * @return a pointer to the single instance of the Log
   */ 
  public static Log getLog() {
    
    if (log==null) { 
      log = new Log(); 
    }
    return log;
  }
 
  /**
   * Turn debugging on or off
   */
  public static void setDebug(boolean shouldDebug)
  {
      debug = shouldDebug;
      if (debug) {
          debug(20, "Debugging turned on");
      } else {
          debug(20, "Debugging turned off");
      }
  }

  /**
   * Set the threshold severity for debugging output
   */
  public static void setDebugLevel(int level)
  {
        debugLevel = level;
        debug(20, "Debug level set to: " + debugLevel);
  }

  /**
   * Print debugging messages based on severity level, where severity level 1
   * are the most critical and higher numbers are more trivial messages.
   * Messages with severity 1 to 4 will result in an error dialog box for the
   * user to inspect.  Those with severity 5-9 result in a warning dialog
   * box for the user to inspect.  Those with severity greater than 9 are
   * printed only to standard error.
   * Setting the debugLevel to 0 in the configuration file turns all messages
   * off.
   *
   * @param severity the severity of the debug message
   * @param message the message to log
   */
  public static void debug(int severity, String message)
  {
    if (debug) {
      if (debugLevel > 0 && severity <= debugLevel) {
        // Show a dialog for severe errors
        if (severity < 5) {
          JOptionPane.showMessageDialog(null, message, "Error!",
                                        JOptionPane.ERROR_MESSAGE);
        } else if (severity < 10) {
          JOptionPane.showMessageDialog(null, message, "Warning!",
                                        JOptionPane.WARNING_MESSAGE);
        }

        // Everything gets printed to standard error
        System.err.println(message);
      }
    }
  } 
}
