
package it.pm.jtelnetserver.interpreter;

import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;

/**
 * Execution Report unit test class.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class ExecutionReportTest extends TestCase {
    
    public ExecutionReportTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getExecutionOutput method, of class ExecutionReport.
     */
    public void testGetExecutionOutput() {
        System.out.println("[Testing]::["+ ExecutionReport.class.getName()+"]::[getExecutionOutput()]");
        ExecutionReport instance = new ExecutionReport();
        String expResult = "";
        String result = instance.getExecutionOutput();
        assertEquals(expResult, result);
    }

    /**
     * Test of setExecutionOutput method, of class ExecutionReport.
     */
    public void testSetExecutionOutput() {
        System.out.println("[Testing]::["+ ExecutionReport.class.getName()+"]::[setExecutionOutput()]");
        String executionOutput = "blah";
        ExecutionReport instance = new ExecutionReport();
        instance.setExecutionOutput(executionOutput);
        String expectedRes = instance.getExecutionOutput();
        assertEquals(expectedRes, executionOutput);
    }

    /**
     * Test of toString method, of class ExecutionReport.
     */
    public void testToString() {
        System.out.println("[Testing]::["+ ExecutionReport.class.getName()+"]::[toString()]");
        ExecutionReport instance = new ExecutionReport();
        String expResult = "ExecutionReport{" + "executionOutput=blah}";
        instance.setExecutionOutput("blah");
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
    public void testSetSuccess() {
        System.out.println("[Testing]::["+ ExecutionReport.class.getName()+"]::[testSetSuccess()]");
        ExecutionReport instance = new ExecutionReport();
        boolean expRes = true;
        instance.setSuccess(true);
        boolean result = instance.isSuccess();
        assertEquals(expRes, result);
    }
    
    public void testIsSuccess() {
        System.out.println("[Testing]::["+ ExecutionReport.class.getName()+"]::[testIsSuccess()]");
        ExecutionReport instance = new ExecutionReport();
        boolean expRes = false;
        boolean result = instance.isSuccess();
        assertEquals(expRes, result);
    }
    
}
