/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.pm.jtelnetserver.interpreter;

import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import sun.misc.Cleaner;

/**
 *
 * @author hailpam
 */
public class ClientCommandTest extends TestCase {
    
    public ClientCommandTest(String testName) {
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
     * Test of getCmd method, of class ClientCommand.
     */
    public void testGetCmd() {
        System.out.println("[Testing]::["+ ClientCommand.class.getName()+"]::[getCmd()]");
        ClientCommand instance = new ClientCommand();
        String expResult = "";
        String result = instance.getCmd();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCmd method, of class ClientCommand.
     */
    public void testSetCmd() {
        System.out.println("[Testing]::["+ ClientCommand.class.getName()+"]::[setCmd(String)]");
        String cmd = "ls";
        ClientCommand instance = new ClientCommand();
        instance.setCmd(cmd);
        String result = instance.getCmd();
        assertEquals(cmd, result);
    }

    /**
     * Test of getOptions method, of class ClientCommand.
     */
    public void testGetOptions() {
        System.out.println("[Testing]::["+ ClientCommand.class.getName()+"]::[getOptions()]");
        ClientCommand instance = new ClientCommand();
        List expResult = new ArrayList();
        List<String> result = instance.getOptions();
        // empty list - by initialization
        assertEquals(expResult.size(), result.size());
        result.add("l");
        result.add("l");
        result.add("s");
        result.add("p");
        assert(!(expResult.size() == result.size()));
    }

    /**
     * Test of getTarget method, of class ClientCommand.
     */
    public void testGetTarget() {
        System.out.println("[Testing]::["+ ClientCommand.class.getName()+"]::[getTarget()]");
        ClientCommand instance = new ClientCommand();
        String expResult = "";
        String result = instance.getTarget();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTarget method, of class ClientCommand.
     */
    public void testSetTarget() {
        System.out.println("[Testing]::["+ ClientCommand.class.getName()+"]::[setTarget()]");
        String target = "pippo.txt";
        ClientCommand instance = new ClientCommand();
        instance.setTarget(target);
        String result = instance.getTarget();
        assertEquals(target, result);
    }

    /**
     * Test of toString method, of class ClientCommand.
     */
    public void testToString() {
        System.out.println("[Testing]::["+ ClientCommand.class.getName()+"]::[toString()]");
        ClientCommand instance = new ClientCommand();
        instance.setCmd("ls");
        instance.getOptions().add("l");
        instance.getOptions().add("a");
        String expResult = "ClientCommand{" + "cmd=ls, options=[l, a], target=}";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
}
