/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.pm.jtelnetserver.interpreter;

import junit.framework.TestCase;

/**
 *
 * @author hailpam
 */
public class CommandParserTest extends TestCase {
    
    public CommandParserTest(String testName) {
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
     * Test of isValidQuery method, of class CommandParser.
     */
    public void testIsValidQuery() {
        System.out.println("[Testing]::["+ CommandParserTest.class.getName()+"]::[isValidQuery(String)]");
        String query = "gedit -   lo aaa.txt";
        CommandParser instance = new CommandParser();
        boolean expResult = false;
        boolean result = instance.isValidQuery(query);
        assertEquals(expResult, result);
        query = "1234";
        result = instance.isValidQuery(query);
        assertEquals(expResult, result);
        query = "ls - ";
        result = instance.isValidQuery(query);
        assertEquals(expResult, result);
        query = "ls - la";
        result = instance.isValidQuery(query);
        assertEquals(expResult, result);
        query = "ls -la C:\\Documents\\1_AAA\\2_BBB-1";
        result = instance.isValidQuery(query);
        expResult = true;
        assertEquals(expResult, result);
        query = "ls -la /home//1_AAA/2_BBB-1";
        result = instance.isValidQuery(query);
        assertEquals(expResult, result);
        query = "gedit -a /home//1_AAA/2_BBB-1/pippo.properties.*.aa1";
        result = instance.isValidQuery(query);
        assertEquals(expResult, result);
    }

    /**
     * Test of buildCommand method, of class CommandParser.
     */
    public void testBuildCommand() {
        System.out.println("[Testing]::["+ CommandParserTest.class.getName()+"]::[buildCommand(String)]");
        String query = "\"ll -la \\\\stuff/1-2_1-test_1.txt.*\"";
        CommandParser instance = new CommandParser();
        ClientCommand expResult = null;
        ClientCommand result = instance.buildCommand(query);
        assert(result != null);
        query = "gedit -a /home//1_AAA/2_BBB-1/pippo.properties.*.aa1";
        result = instance.buildCommand(query);
        assert(result != null);
        query = "ll /home//1_AAA/2_BBB-1/";
        result = instance.buildCommand(query);
        assert(result != null);
        query = "ll -la";
        result = instance.buildCommand(query);
        assert(result != null);
        query = "123456";
        instance.isValidQuery(query);
        result = instance.buildCommand(query);
        assert(result == null);
    }
}
