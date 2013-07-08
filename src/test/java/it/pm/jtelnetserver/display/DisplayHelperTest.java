
package it.pm.jtelnetserver.display;


import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;

/**
 * Unit test class for Display Helper.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class DisplayHelperTest extends TestCase {
    
    private static final String lineSeparator = System.getProperty("line.separator");
    
    public DisplayHelperTest(String testName) {
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
     * Test of decoratedDisplay method, of class DisplayHelper.
     */
    public void testDecoratedDisplay() {
        System.out.println("[Testing]::["+ DisplayHelper.class.getName()+"]::[decorateDisplay(String)]");
        String response = "";
        DisplayHelper instance = new DisplayHelper();
        String expResult = lineSeparator+"telnet: internal error: retry"+lineSeparator+"prompt> ";
        String result = instance.decoratedDisplay(response);
        assertEquals(expResult, result);
        //
        response = "abc";
        result = instance.decoratedDisplay(response);
        expResult = lineSeparator+"abc"+lineSeparator+"prompt> ";
        assertEquals(expResult, result);
        //
        response = null;
        expResult = lineSeparator+"telnet: internal error: retry"+lineSeparator+"prompt> ";
        result = instance.decoratedDisplay(response);
        assertEquals(expResult, result);
    }

    /**
     * Test of errorDisplay method, of class DisplayHelper.
     */
    public void testErrorDisplay() {
        System.out.println("[Testing]::["+ DisplayHelper.class.getName()+"]::[errorDisplay(String)]");
        String faultyQuery = "";
        DisplayHelper instance = new DisplayHelper();
        String expResult = lineSeparator+"telnet: N/A: command not"
            + " found or bad query"+lineSeparator+"  Usage: CMD [-OPTIONS] [TARGET]"+lineSeparator+"prompt> ";
        String result = instance.errorDisplay(faultyQuery);
        assertEquals(expResult, result);
        //
        faultyQuery = "ls - la ";
        expResult = lineSeparator+"telnet: "+faultyQuery+": command not"
            + " found or bad query"+lineSeparator+"  Usage: CMD [-OPTIONS] [TARGET]"+lineSeparator+"prompt> ";
        result = instance.errorDisplay(faultyQuery);
        assertEquals(expResult, result);
    }

    /**
     * Test of welcomeDisplay method, of class DisplayHelper.
     */
    public void testWelcomeDisplay() {
        System.out.println("[Testing]::["+ DisplayHelper.class.getName()+"]::[welcomeDisplay()]");
        DisplayHelper instance = new DisplayHelper();
        String expResult = "";
        String result = instance.welcomeDisplay();
        assert(!result.isEmpty());
    }
    
}
