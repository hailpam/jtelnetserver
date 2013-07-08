
package it.pm.jtelnetserver.interpreter.cmd;

import it.pm.jtelnetserver.context.ServerSession;
import it.pm.jtelnetserver.interpreter.ClientCommand;
import it.pm.jtelnetserver.interpreter.ExecutionReport;
import java.io.File;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;

/**
 * MKDIR Command handler unit testing.
 *  
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class MkdirCommandHandlerTest extends TestCase {
    
    
    private static final String lineSeparator = System.getProperty("line.separator");
    
    public MkdirCommandHandlerTest(String testName) {
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
     * Test of handleCommand method, of class MkdirCommandHandler.
     */
    public void testHandleCommand() {
        System.out.println("[Testing]::["+ MkdirCommandHandler.class.getName()+"]::[handleCommand(ClientCommand, ServerSession)]");
        String usage = "mkdir: usage:"+lineSeparator+" mkdir [target]"+lineSeparator
            + "  options: not admitted"+lineSeparator
            + "  target: can be"+lineSeparator
            + "    'stuff'     to create locally the new directory named stuff"+lineSeparator;
        ClientCommand clientQuery = new ClientCommand();
        // test invalid command
        clientQuery.setCmd("pippo");
        ServerSession clientSess = new ServerSession();
        File userHome = new File(System.getProperty("user.home"));
        clientSess.setWorkingDir(userHome);
        MkdirCommandHandler instance = new MkdirCommandHandler();
        ExecutionReport expResult = new ExecutionReport();
        expResult.setSuccess(false);
        String output = "";
        output = "telnet: "+clientQuery.getCmd()+": command not found";
        expResult.setExecutionOutput(output);
        ExecutionReport result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        // mkdir pippo
        clientQuery.setCmd("mkdir");
        clientQuery.setTarget("pippo");
        instance = new MkdirCommandHandler();
        expResult = new ExecutionReport();
        expResult.setSuccess(true);
        String newDirectory = "";
        output = "";
        output = userHome.getAbsolutePath();
        output += File.separator;
        output += "pippo";
        newDirectory = output;
        output += lineSeparator;
        expResult.setExecutionOutput(output);
        result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        File toBeDeleted = new File(newDirectory);
        if(toBeDeleted.exists())
            toBeDeleted.delete();
        // mkdir 1pippo01
        clientQuery.setCmd("mkdir");
        clientQuery.setTarget("1pippo01");
        instance = new MkdirCommandHandler();
        expResult = new ExecutionReport();
        expResult.setSuccess(true);
        newDirectory = "";
        output = "";
        output = userHome.getAbsolutePath();
        output += File.separator;
        output += "1pippo01";
        newDirectory = output;
        output += lineSeparator;
        expResult.setExecutionOutput(output);
        result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        toBeDeleted = new File(newDirectory);
        if(toBeDeleted.exists())
            toBeDeleted.delete();
        // mkdir -la 1pippo01
        clientQuery.getOptions().add("l");
        clientQuery.getOptions().add("a");
        instance = new MkdirCommandHandler();
        expResult = new ExecutionReport();
        expResult.setSuccess(false);
        output = usage;
        expResult.setExecutionOutput(output);
        result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        // mkdir -
        clientQuery = new ClientCommand();
        clientQuery.setCmd("mkdir");
        clientQuery.setTarget("-");
        instance = new MkdirCommandHandler();
        expResult = new ExecutionReport();
        expResult.setSuccess(false);
        output = "telnet: ";
        output += clientQuery.getTarget();
        output += ": target not allowed"+lineSeparator;
        output += usage;
        expResult.setExecutionOutput(output);
        result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        // mkdir ..
        clientQuery.setCmd("mkdir");
        clientQuery.setTarget("..");
        instance = new MkdirCommandHandler();
        expResult = new ExecutionReport();
        expResult.setSuccess(false);
        output = "telnet: ";
        output += clientQuery.getTarget();
        output += ": target not allowed"+lineSeparator;
        output += usage;
        expResult.setExecutionOutput(output);
        result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        // mkdir .?£
        clientQuery.setCmd("mkdir");
        clientQuery.setTarget(".?£");
        instance = new MkdirCommandHandler();
        expResult = new ExecutionReport();
        expResult.setSuccess(false);
        output = "telnet: ";
        output += clientQuery.getTarget();
        output += ": target not allowed"+lineSeparator;
        output += usage;
        expResult.setExecutionOutput(output);
        result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
    }

    /**
     * Test of printUsage method, of class MkdirCommandHandler.
     */
    public void testPrintUsage() {
        System.out.println("[Testing]::["+ MkdirCommandHandler.class.getName()+"]::[printUsage()]");
        String usage = "mkdir: usage:"+lineSeparator+" mkdir [target]"+lineSeparator
            + "  options: not admitted"+lineSeparator
            + "  target: can be"+lineSeparator
            + "    'stuff'     to create locally the new directory named stuff"+lineSeparator;
        MkdirCommandHandler instance = new MkdirCommandHandler();
        String expResult = usage;
        String result = instance.printUsage();
        assertEquals(expResult, result);
    }
    
}
