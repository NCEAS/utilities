/*

 Copyright (c) 2001-2002 sync4j project
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows
    these conditions in the documentation and/or other materials
    provided with the distribution.

 3. The name "sync4j" must not be used to endorse or promote products
    derived from this software without prior written permission.

 4. Products derived from this software may not be called "sync4j", nor
    may "sync4j" appear in their name, without prior written permission.

 In addition, we request (but do not require) that you include in the
 end-user documentation provided with the redistribution and/or in the
 software itself an acknowledgement equivalent to the following:

     "This product includes software developed by the
      sync4j project."

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE SYNC4J AUTHORS OR THE PROJECT
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 */
package sync4j.test.tools.ant;

import java.util.Map;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Ant;
import org.apache.tools.ant.taskdefs.Property;


/** 
 * This class is a Jakarta Ant custom task that repeatedly calls a target
 * passing different values from a delimited string.
 *
 * <p>For example:
 * <pre>
 * &lt;project name="Test" default="main" basedir="."&gt;
 *   &lt;taskdef name="iterate" classname="sync4j.test.tools.ant.IterateTask"/&gt;
 *
 *   &lt;target name="main"&gt;
 *     &lt;iterate target="hello" items="1,2,3" property="i" &gt;
 *     &lt;/iterate&gt;
 *   &lt;/target&gt;
 *
 *   &lt;target name="hello"&gt;
 *     &lt;echo message="${i}" /&gt;
 *   &lt;/target&gt;
 * &lt;/project&gt;
 * </pre>
 *
 * <p>The above example will call the hello target three times, each time
 * passing a value from the item list. In this case the hello target will
 * echo 1, then 2 and then 3.
 *
 * <p>A more useful example is the ability to compile multiple source
 * directories into multiple jar files. For example:
 *
 * <pre>
 * &lt;target name="build" depends="init"&gt;
 *
 *   &lt;!-- iterate through the ${build.modules} variable, compiling each module specified --&gt;
 *   &lt;iterate target="javac" items="myModule,myModule/mySubModule" property="iterate.module"/&gt;
 * &lt;/target&gt;
 *
 * &lt;target name="javac" depends="checkSource, cleanBuild, prepareBuild" if="compile.source.exist"&gt;
 *
 *   &lt;javac  srcdir="${iterate.module}/src" destdir="${iterate.module}/build"&gt;
 *     &lt;include name="**\/*.java"/&gt;
 *   &lt;/javac&gt;
 *
 *   &lt;!-- create a jar file for each module--&gt;
 *   &lt;mkdir dir="${iterate.module}/lib"/&gt;
 *   &lt;jar jarfile="${iterate.module}/lib/classes.jar"&gt;
 *     &lt;fileset dir="${iterate.module}/build"/&gt;
 *   &lt;/jar&gt;
 * &lt;/target&gt;
 * </pre>
 *
 * <p>The about example does the following:
 * <ul>
 *   <li>compiles the myModule/src directory into myModule/lib/classes.jar
 *   <li>compiles the myModule/mySubModule/src directory into myModule/mySubModule/lib/classes.jar
 * <ul>
 *
 * <p>List of attributes:
 * <table border>
 * <tr><th>Attribute</th><th>Description</th><th>Required</th></tr>
 * <tr><td>target</td><td>This is the name of the target to call.</td><td>Yes</td></tr>
 * <tr><td>property</td><td>The name of the property in which each value from the item list will be stored for each iteration. This allows the called target to access each item from the item list.</td><td>Yes</td></tr>
 * <tr><td>items</td><td>A delimited list of items strings.</td><td>Yes</td></tr>
 * <tr><td>iheritAll</td><td>Boolean to enable/disable the called target from inheriting all the properties from the environment.</td><td>No</td></tr>
 * <tr><td>delimiter</td><td>The delimiter that is used to delimited the strings in the item list.</td><td>No</td></tr>
 * </table>

 * @author Stefano Fornari
 * @version $Id: IterateTask.java,v 1.1 2003-03-20 02:29:32 brooke Exp $
 */
public class IterateTask extends Task {

  /** Default constructor.
   */
  public IterateTask() {}

  /** Set the item list string. The item list can contain one or more
   *  delimited strings. The specified target will be called once for each
   *  string in the item list.
   *
   *  <p>The delimiter can be changed by specifying the
   *  delimiter attribute.
   *
   * @param items Delimited string of items.
   */
  public void setItems(String items) {
      this.items = items;
  }

  /** Set the Ant target name that will be called repeatedly, once for each
   *  item in the item list.
   *
   * @param targetName The name of the target to call repeatedly.
   */
  public void setTarget(String targetName) {
    this.targetName = targetName;
  }

  /** Sets the inherit all flag. If the value is true, then the target that
   *  is called will inherit all the properties. Default is true.
   *
   * @param inheritAll Inherit flag.
   */
  public void setInheritAll(boolean inheritAll) {
    this.inheritAll = inheritAll;
  }

  /** Set the Property. The property attribute is the name of the property
   *  that will contain each item in the item list.
   *
   * @param property Property Name
   */
  public void setProperty(String property) {
    this.property = property;
  }

  /** Set the delimiter that will be used to delimit the strings in the item
   *  list, the default is comma ",".
   *
   * @param delimiter Delimiter charater.
   */
  public void setDelimiter(String delimiter) {
    this.delimiter = delimiter;
  }

  /** Ant execute service method, called when the task is executed.
   *
   * @exception BuildException When required attributes are not set
   */
  public void execute() throws BuildException {

    validateAttributes();

    // initialise the target
    task.setDir(project.getBaseDir());
    task.setAntfile(project.getProperty("ant.file"));
    task.setTarget(targetName);
    task.setInheritAll(inheritAll);

    // call the target for each item in the item list
    StringTokenizer st = new StringTokenizer(items, delimiter);
    while (st.hasMoreTokens()) {
      project.setProperty(property, (st.nextToken().trim()));
      task.execute();
    }
  }

  /** Ant init service method, to initialise the task.
   *
   * @exception BuildException Build exception.
   */
  public void init() throws BuildException {
    super.init();

    // initialise the called target
    System.err.println("### init() starting");
    task = (Ant) project.createTask("ant");
    System.err.println("### init() task="+task);
    task.setOwningTarget(target);
    System.err.println("### init() setOwningTarget to: "+target);
    task.setTaskName(targetName);
    task.setLocation(location);
    task.init();
  }

  /** Ant create param service method, which is called for each embedded param
   *  element.
   *
   * @return Property object.
   */
  public Property createParam() {
    return task.createProperty();
  }

  
  // ----------------------------------------------------------- Private methods
  
  private void validateAttributes() throws BuildException {
      if (isEmpty(targetName)) {
          throw new BuildException("Attribute target is required.", location);
      }

      if (isEmpty(property)) {
          throw new BuildException("Attribute property is required.", location);
      }

      if (items == null) {
          throw new BuildException("Attribute items is required.", location);
      }
  }
  
  private boolean isEmpty(String s) {
      return ((s == null) || (s.length() == 0));
  }
  
  // -------------------------------------------------------------- Private data
  
  private String items;
  private String targetName;
  private boolean inheritAll = true;
  private Ant task;
  private String property;
  private String delimiter = ",";

}



