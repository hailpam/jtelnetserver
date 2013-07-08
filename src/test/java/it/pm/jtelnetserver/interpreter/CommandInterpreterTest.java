
package it.pm.jtelnetserver.interpreter;

import it.pm.jtelnetserver.context.ServerSession;
import it.pm.jtelnetserver.interpreter.cmd.CdCommandHandler;
import it.pm.jtelnetserver.interpreter.cmd.LsCommandHandler;
import it.pm.jtelnetserver.interpreter.cmd.MkdirCommandHandler;
import it.pm.jtelnetserver.util.ServiceException;
import java.io.File;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import junit.framework.TestCase;

/**
 * Command Interpreter class unit testing.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class CommandInterpreterTest extends TestCase {
    
    public CommandInterpreterTest(String testName) {
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
     * Test of interpretCommand method, of class CommandInterpreter.
     */
    public void testInterpretCommand()  {
        System.out.println("[Testing]::["+ CommandInterpreter.class.getName()+"]::[interpretCommand(ClientCommand)]");
        // test NULL check
        ClientCommand clientQuery = null;
        CommandInterpreter instance = new CommandInterpreter();
        instance.setSessionId("1");
        String expResult = "Client Query object not initialized";
        String result = "";
        try {
            instance.interpretCommand(clientQuery);
        } catch (ServiceException ex) {
            result = ex.getMessage();
            assertEquals(result, expResult);
        }
        // test Blank command Id
        clientQuery = new ClientCommand();
        expResult = "Client Query object not properly initialized"
                    + ": blank command. Unable to serve the request.";
        try {
            instance.interpretCommand(clientQuery);
        } catch (ServiceException ex) {
            result = ex.getMessage();
            assertEquals(result, expResult);
        }
        // test help command
        String helpDisplayTemplate =  "\nJTelnetServer v1.0 Command Interpreter\n"
            + "These shell commands are defined internally. Hereafter a list of the "
            + "available commands.\n\n"
            + "${LIST_OF_COMMANDS}";
        ServerSession fakeSession = new ServerSession();
        instance.setActualSession(fakeSession);
        clientQuery.setCmd("help");
        expResult = helpDisplayTemplate.replace("${LIST_OF_COMMANDS}", " interpreter: "
                        + "internal problem: no command registered");
        try {
            result = instance.interpretCommand(clientQuery);
        } catch (ServiceException ex) {
            fail();
        }
        assert(!result.equalsIgnoreCase(expResult));
        // test history command
        clientQuery.setCmd("history");
        try {
            result = instance.interpretCommand(clientQuery);
        } catch (ServiceException ex) {
            fail();
        }
        expResult = fakeSession.retrieveHistory();
        assertEquals(expResult, result);
        System.out.println(result); // TODO - string wrong
        // test status command
        clientQuery.setCmd("status");
        try {
            result = instance.interpretCommand(clientQuery);
        } catch (ServiceException ex) {
            fail();
        }
        expResult = fakeSession.getStatus();
        assertEquals(expResult, result);
        // test some piped commands
    }

    /**
     * Test of addCommandHandler method, of class CommandInterpreter.
     */
    public void testAddCommandHandler() {
        System.out.println("[Testing]::["+ CommandInterpreter.class.getName()+"]::[addCommandHandler(CommandHandler)]");
        // NULL command handler in input
        CommandHandler cmdHanlder = null;
        CommandInterpreter instance = new CommandInterpreter();
        String expResult = "NULL handler: unable to proceed in "
                    + "command registration";
        String result = "";
        try {
            instance.addCommandHandler(cmdHanlder);
        } catch (ServiceException ex) {
            result = ex.getMessage();
            assertEquals(expResult, result);
        }
        // test a concrete registration (by using specific commands)
        LsCommandHandler lsCmd = new LsCommandHandler();
        MkdirCommandHandler mkdirCmd = new MkdirCommandHandler();
        CdCommandHandler cdCmd = new CdCommandHandler();
        expResult = "BAD handler: unable to proceed in "
                    + "command registration. Name and/or Description not filled";
        try {
            lsCmd.setCmdName("");
            instance.addCommandHandler(lsCmd);
        } catch (ServiceException ex) {
            result = ex.getMessage();
            assertEquals(expResult, result);
        }
        // correct handlers
        lsCmd = new LsCommandHandler();
        try {
            instance.addCommandHandler(lsCmd);
            instance.addCommandHandler(cdCmd);
            instance.addCommandHandler(mkdirCmd);
        } catch (ServiceException ex) {
            // registration should work correctly
            fail();
        }
       ServerSession clientSess = new ServerSession();
       File userHome = new File(System.getProperty("user.home"));
       clientSess.setWorkingDir(userHome);
       instance.setActualSession(clientSess);
       instance.setSessionId("1");
       ClientCommand clientCmd = new ClientCommand();
       clientCmd.setCmd("pippo");
       clientCmd.setOriginalQuery("pippo");
       expResult = "telnet: pippo: command not found";
       try {
            result = instance.interpretCommand(clientCmd);
            assertEquals(expResult, result);
        } catch (ServiceException ex) {
            // should work properly
            fail();
        }
       // forward a ls request
       clientCmd.setCmd("ls");
       clientCmd.setOriginalQuery("ls");
       try {
            result = instance.interpretCommand(clientCmd);
            assert(!result.isEmpty());
        } catch (ServiceException ex) {
            // should work properly
            fail();
        }
       // forward a mkdir request
       clientCmd.setCmd("mkdir");
       clientCmd.setOriginalQuery("mkdir PippoTest");
       clientCmd.setTarget("PippoTest");
       try {
            result = instance.interpretCommand(clientCmd);
            assert(!result.isEmpty());
        } catch (ServiceException ex) {
            // should work properly
            fail();
        }
       // forward a ls request
       clientCmd.setCmd("ls");
       clientCmd.setOriginalQuery("ls");
       clientCmd.setTarget("");
       try {
            result = instance.interpretCommand(clientCmd);
            assert(!result.isEmpty());
        } catch (ServiceException ex) {
            // should work properly
            fail();
        }
       // forward a cd request
       clientCmd.setCmd("cd");
       clientCmd.setOriginalQuery("PippoTest");
       clientCmd.setTarget("PippoTest");
       try {
            result = instance.interpretCommand(clientCmd);
            assert(!result.isEmpty());
        } catch (ServiceException ex) {
            // should work properly
            fail();
        }
       // forward a cd request
       clientCmd.setCmd("cd");
       clientCmd.setOriginalQuery("PippoTest");
       clientCmd.setTarget("PippoTest");
       try {
            result = instance.interpretCommand(clientCmd);
            assert(!result.isEmpty());
        } catch (ServiceException ex) {
            // should work properly
            fail();
        }
       // forward a cd .. request
       clientCmd.setCmd("cd");
       clientCmd.setOriginalQuery("..");
       clientCmd.setTarget("..");
       try {
            result = instance.interpretCommand(clientCmd);
            assert(!result.isEmpty());
        } catch (ServiceException ex) {
            // should work properly
            fail();
        }
       File toBeDeleted = new File(userHome.getAbsolutePath()+File.separator+"PippoTest");
       toBeDeleted.delete();
       // forward a ls request
       clientCmd.setCmd("ls");
       clientCmd.setOriginalQuery("ls");
       clientCmd.setTarget("");
       try {
            result = instance.interpretCommand(clientCmd);
            assert(!result.isEmpty());
        } catch (ServiceException ex) {
            // should work properly
            fail();
        }
    }

    /**
     * Test of getSessionId method, of class CommandInterpreter.
     */
    public void testGetSessionId() {
        System.out.println("[Testing]::["+ CommandInterpreter.class.getName()+"]::[getSessionId()]");
        CommandInterpreter instance = new CommandInterpreter();
        String expResult = "";
        String result = instance.getSessionId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSessionId method, of class CommandInterpreter.
     */
    public void testSetSessionId() {
        System.out.println("[Testing]::["+ CommandInterpreter.class.getName()+"]::[setSessionId()]");
        String sessionId = "blah";
        CommandInterpreter instance = new CommandInterpreter();
        instance.setSessionId(sessionId);
        String expResult = "blah";
        assertEquals(expResult, instance.getSessionId());
    }

    /**
     * Test of getActualSession method, of class CommandInterpreter.
     */
    public void testGetActualSession() {
        System.out.println("[Testing]::["+ CommandInterpreter.class.getName()+"]::[getActualSession()]");
        CommandInterpreter instance = new CommandInterpreter();
        ServerSession expResult = null;
        ServerSession result = instance.getActualSession();
        assertEquals(expResult, result);
    }

    /**
     * Test of setActualSession method, of class CommandInterpreter.
     */
    public void testSetActualSession() {
        System.out.println("[Testing]::["+ CommandInterpreter.class.getName()+"]::[setActualSession()]");
        ServerSession actualSession = new ServerSession();
        CommandInterpreter instance = new CommandInterpreter();
        instance.setActualSession(actualSession);
        ServerSession expResult = new ServerSession();
        assertEquals(expResult.getStatus(), instance.getActualSession().getStatus());
    }
    
}
