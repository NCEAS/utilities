/*
 * IterateTaskTest.java
 * NetBeans JUnit based test
 *
 * Created on March 19, 2003, 11:24 AM
 */

package sync4j.test.tools.ant;

import java.io.File;
import java.util.Map;
import java.util.Iterator;
import java.util.StringTokenizer;
import junit.framework.*;
import org.apache.tools.ant.Main;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Ant;
import org.apache.tools.ant.taskdefs.Property;

/**
 *
 * @author brooke
 */
public class IterateTaskTest extends TestCase {
  
  private static IterateTask iterateTask;

  static {
    iterateTask = new IterateTask();
    assertNotNull(iterateTask);
  }
  
  public IterateTaskTest(java.lang.String testName) {
    super(testName);
  }
  
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
  
  public static Test suite() {
    TestSuite suite = new TestSuite(IterateTaskTest.class);
    
    return suite;
  }
  
  /** Test of setItems method, of class sync4j.test.tools.ant.IterateTask. */
  public void testSetItems() {
    System.out.println("testSetItems");
    assertNotNull(iterateTask);
    try {
      iterateTask.setItems("test,test,test");
    } catch (Exception e){
      e.printStackTrace();
      fail("Exception doing setItems(test,test,test)");
    }
  }
  
  /** Test of setTarget method, of class sync4j.test.tools.ant.IterateTask. */
  public void testSetTarget() {
    System.out.println("testSetTarget");
    assertNotNull(iterateTask);
    try {
      iterateTask.setTarget("myTarget");
    } catch (Exception e){
      e.printStackTrace();
      fail("Exception doing setTarget(myTarget)");
    }
  }
  
  /** Test of setInheritAll method, of class sync4j.test.tools.ant.IterateTask. */
  public void testSetInheritAll() {
    System.out.println("testSetInheritAll");
    assertNotNull(iterateTask);
    try {
      iterateTask.setInheritAll(true);
    } catch (Exception e){
      fail("Exception doing setInheritAll(true)");
    }
  }
  
  /** Test of setProperty method, of class sync4j.test.tools.ant.IterateTask. */
  public void testSetProperty() {
    System.out.println("testSetProperty");
    
    try {
      iterateTask.setProperty("myProperty");
    } catch (Exception e){
      e.printStackTrace();
      fail("Exception doing setProperty(myProperty)");
    }
  }
  
  /** Test of setDelimiter method, of class sync4j.test.tools.ant.IterateTask. */
  public void testSetDelimiter() {
    System.out.println("testSetDelimiter");
    assertNotNull(iterateTask);
    try {
      iterateTask.setDelimiter(",");
    } catch (Exception e){
      e.printStackTrace();
      fail("Exception doing setDelimiter(,)");
    }
  }

}
