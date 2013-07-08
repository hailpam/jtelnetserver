/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.pm.jtelnetserver.interpreter.cmd;

import it.pm.jtelnetserver.context.ServerSession;
import it.pm.jtelnetserver.interpreter.ClientCommand;
import it.pm.jtelnetserver.interpreter.ExecutionReport;
import java.io.File;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;

/**
 * LS Command Handler unit testing.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class LsCommandHandlerTest extends TestCase {
    
    private static final String lineSeparator = System.getProperty("line.separator");
    
    public LsCommandHandlerTest(String testName) {
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
     * Test of handleCommand method, of class LsCommandHandler.
     */
    public void testHandleCommand() {
        System.out.println("[Testing]::["+ LsCommandHandler.class.getName()+"]::[handleCommand(ClientQuery,ServerSession)]");
        String usage = "ls: usage:"+lineSeparator+" mkdir [target]"+lineSeparator
            + "  options:"+lineSeparator
            + "    '-a'        to not ignore hidden entries (starting with .)"+lineSeparator
            + "  target: can be"+lineSeparator
            + "    'stuff'     to list with reference to a particular directory stuff"
             + " (assuming the relative path from working directory)"+lineSeparator;
        ClientCommand clientQuery = new ClientCommand();
        clientQuery.setCmd("ls");
        clientQuery.getOptions().add("a");
        clientQuery.getOptions().add("l");
        clientQuery.setOriginalQuery("ls -al");
        ServerSession clientSess = new ServerSession();
        File userHome = new File(System.getProperty("user.home"));
        clientSess.setWorkingDir(userHome);
        LsCommandHandler instance = new LsCommandHandler();
        ExecutionReport expResult = new ExecutionReport();
        expResult.setSuccess(false);
        String errorMsg = "telnet: ";
        errorMsg += clientQuery.getOriginalQuery();
        errorMsg += ": number of options not allowed"+lineSeparator;
        errorMsg += usage;
        expResult.setExecutionOutput(errorMsg);
        ExecutionReport result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        // ls -a pippo
        clientQuery = new ClientCommand();
        clientQuery.setCmd("ls");
        clientQuery.getOptions().add("a");
        clientQuery.setTarget("pippo");
        clientQuery.setOriginalQuery("ls -al pippo");
        instance = new LsCommandHandler();
        expResult = new ExecutionReport();
        expResult.setSuccess(false);
        errorMsg = "telnet: ";
        errorMsg += userHome.getAbsolutePath();
        errorMsg += File.separator;
        errorMsg += clientQuery.getTarget();
        errorMsg += ": cannot access: no such directory"+lineSeparator;
        errorMsg += usage;
        expResult.setExecutionOutput(errorMsg);
        result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        // ls -a 1pippo01
        clientQuery.setTarget("1pippo01");
        clientQuery.setOriginalQuery("ls -al 1pippo01");
        instance = new LsCommandHandler();
        expResult = new ExecutionReport();
        expResult.setSuccess(false);
        errorMsg = "telnet: ";
        errorMsg += userHome.getAbsolutePath();
        errorMsg += File.separator;
        errorMsg += clientQuery.getTarget();
        errorMsg += ": cannot access: no such directory"+lineSeparator;
        errorMsg += usage;
        expResult.setExecutionOutput(errorMsg);
        result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        // ls -a .£$pippo
        clientQuery.setTarget(".£$pippo");
        clientQuery.setOriginalQuery("ls -al .£$pippo");
        instance = new LsCommandHandler();
        expResult = new ExecutionReport();
        expResult.setSuccess(false);
        errorMsg = "telnet: ";
        errorMsg += clientQuery.getTarget();
        errorMsg += ": invalid target for listing"+lineSeparator;
        errorMsg += usage;
        expResult.setExecutionOutput(errorMsg);
        result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        // ls pippo
        clientQuery.setTarget("pippo");
        clientQuery.setOriginalQuery("ls -al pippo");
        instance = new LsCommandHandler();
        expResult = new ExecutionReport();
        expResult.setSuccess(false);
        errorMsg = "telnet: ";
        errorMsg += userHome.getAbsolutePath();
        errorMsg += File.separator;
        errorMsg += clientQuery.getTarget();
        errorMsg += ": cannot access: no such directory"+lineSeparator;
        errorMsg += usage;
        expResult.setExecutionOutput(errorMsg);
        result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        // test ls + an existing directory (if any)
        File[] listOfFiles =  userHome.listFiles();
        String dirToJump = "";
        for(int i = 0; i < listOfFiles.length; i++) {
            if(listOfFiles[i].isDirectory() && !listOfFiles[i].isHidden()) {
                dirToJump = listOfFiles[i].getName();
                break;
            }
        }
        if(!dirToJump.isEmpty()) {
            clientQuery.setTarget(dirToJump);
            clientQuery.setOriginalQuery("ls -a"+dirToJump);
            instance = new LsCommandHandler();
            result = instance.handleCommand(clientQuery, clientSess);
            assert(!result.getExecutionOutput().isEmpty());
            // test simly ls with the found directory (filtering the hidden files)
            clientQuery = new ClientCommand();
            clientQuery.setCmd("ls");
            clientQuery.setTarget(dirToJump);
            clientQuery.setOriginalQuery("ls "+dirToJump);
            result = instance.handleCommand(clientQuery, clientSess);
            assert(!result.getExecutionOutput().isEmpty());
        }
        // test ls
        clientSess.setWorkingDir(userHome);
        clientQuery.setTarget("");
        clientQuery.setCmd("ls");
        clientQuery.setOriginalQuery("ls");
        instance = new LsCommandHandler();
        result = instance.handleCommand(clientQuery, clientSess);
        assert(!result.getExecutionOutput().isEmpty());
        // test ls
        clientQuery.setCmd("ls");
        clientQuery.getOptions().add("a");
        clientQuery.setOriginalQuery("ls -a");
        instance = new LsCommandHandler();
        result = instance.handleCommand(clientQuery, clientSess);
        assert(!result.getExecutionOutput().isEmpty());
        // ls 
    }

    /**
     * Test of printUsage method, of class LsCommandHandler.
     */
    public void testPrintUsage() {
        System.out.println("[Testing]::["+ LsCommandHandler.class.getName()+"]::[printUsage()]");
        String usage = "ls: usage:"+lineSeparator+" mkdir [target]"+lineSeparator
            + "  options:"+lineSeparator
            + "    '-a'        to not ignore hidden entries (starting with .)"+lineSeparator
            + "  target: can be"+lineSeparator
            + "    'stuff'     to list with reference to a particular directory stuff"
             + " (assuming the relative path from working directory)"+lineSeparator;
        LsCommandHandler instance = new LsCommandHandler();
        String expResult = usage;
        String result = instance.printUsage();
        assertEquals(expResult, result);
    }
    
}
