
package it.pm.jtelnetserver.interpreter.cmd;

import it.pm.jtelnetserver.context.ServerSession;
import it.pm.jtelnetserver.interpreter.ClientCommand;
import it.pm.jtelnetserver.interpreter.CommandHandler;
import it.pm.jtelnetserver.interpreter.ExecutionReport;
import java.io.File;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;

/**
 * CD command handler unit testing.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class CdCommandHandlerTest extends TestCase {
   
    private static final String lineSeparator = System.getProperty("line.separator");
    
    public CdCommandHandlerTest(String testName) {
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
     * Test of handleCommand method, of class CdCommandHandler.
     */
    public void testHandleCommand() {
        System.out.println("[Testing]::["+ CdCommandHandler.class.getName()+"]::[handleCommand(ClientCommand, ServerSession)]");
        // settings
        String usage = "cd: usage:"+lineSeparator+" cd [target]"+lineSeparator
            + "  options: not admitted"+lineSeparator
            + "  target: can be"+lineSeparator
            + "    '..'        to access the parent directory"+lineSeparator
            + "    '../..'     to access the parent-parent directory (2 levels above)"+lineSeparator
            + "    'stuff'     to access stuff directory (if it is present locally)"+lineSeparator;
        ClientCommand clientQuery = new ClientCommand();
        clientQuery.setCmd("cd");
        clientQuery.getOptions().add("l");
        clientQuery.getOptions().add("a");
        clientQuery.setTarget("..");
        File userHome = new File(System.getProperty("user.home"));
        ServerSession clientSess = new ServerSession();
        clientSess.setWorkingDir(userHome);
        CommandHandler instance = new CdCommandHandler();
        ExecutionReport expResult = new ExecutionReport();
        expResult.setSuccess(false);
        expResult.setExecutionOutput(usage);
        ExecutionReport result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        //
        clientQuery = new ClientCommand();
        clientQuery.setCmd("cd");
        result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        // test cd ..
        clientQuery.setTarget("..");
        expResult.setSuccess(true);
        expResult.setExecutionOutput(userHome.getParent());
        result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        // test cd ../..
        clientSess.setWorkingDir(userHome);
        clientQuery.setTarget("../..");
        expResult.setSuccess(true);
        String tmpPath = userHome.getParent();
        File tmpFPath = new File(tmpPath);
        expResult.setExecutionOutput(tmpFPath.getParent());
        result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        // test cd ../../..
        clientSess.setWorkingDir(userHome);
        expResult.setSuccess(false);
        clientQuery.setTarget("../../..");
        String tmpPath1 = tmpFPath.getParent();
        File tmpFPath1 = new File(tmpPath1);
        String output = "telnet: ";
        output += "N/E";
        output += ": internal server error";
        expResult.setExecutionOutput(output);
        result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        // test cd .
        clientSess.setWorkingDir(userHome);
        expResult.setSuccess(false);
        clientQuery.setTarget(".");
        String errorMsg = "telnet: ";
        errorMsg += ".";
        errorMsg += ": target not allowed"+lineSeparator;
        errorMsg += usage;
        expResult.setExecutionOutput(errorMsg);
        result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        // test cd -
        clientSess.setWorkingDir(userHome);
        expResult.setSuccess(false);
        clientQuery.setTarget("-");
        errorMsg = "telnet: ";
        errorMsg += "-";
        errorMsg += ": target not allowed"+lineSeparator;
        errorMsg += usage;
        expResult.setExecutionOutput(errorMsg);
        result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        // test cd 1
        clientSess.setWorkingDir(userHome);
        expResult.setSuccess(false);
        clientQuery.setTarget("1");
        output = "telnet: ";
        output += userHome;
        output += File.separator;
        output += "1";
        output += ": internal server error";
        expResult.setExecutionOutput(output);
        result = instance.handleCommand(clientQuery, clientSess);
        assertEquals(expResult, result);
        // test cd + an existing directory (if any)
        File[] listOfFiles =  userHome.listFiles();
        String dirToJump = "";
        for(int i = 0; i < listOfFiles.length; i++) {
            if(listOfFiles[i].isDirectory() && !listOfFiles[i].isHidden() && 
                    listOfFiles[i].getName().matches("[a-zA-Z0-9]+")) {
                dirToJump = listOfFiles[i].getName();
                break;
            }
        }
        if(!dirToJump.isEmpty()) {
            clientSess.setWorkingDir(userHome);
            expResult.setSuccess(true);
            clientQuery.setTarget(dirToJump);
            output = userHome.getPath();
            output += File.separator;
            output += dirToJump;
            output += lineSeparator;
            output = output.trim();
            expResult.setExecutionOutput(output);
            result = instance.handleCommand(clientQuery, clientSess);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of printUsage method, of class CdCommandHandler.
     */
    public void testPrintUsage() {
        System.out.println("[Testing]::["+ CdCommandHandler.class.getName()+"]::[printUsage()]");
        CdCommandHandler instance = new CdCommandHandler();
        String expResult = "cd: usage:"+lineSeparator+" cd [target]"+lineSeparator
            + "  options: not admitted"+lineSeparator
            + "  target: can be"+lineSeparator
            + "    '..'        to access the parent directory"+lineSeparator
            + "    '../..'     to access the parent-parent directory (2 levels above)"+lineSeparator
            + "    'stuff'     to access stuff directory (if it is present locally)"+lineSeparator;
        String result = instance.printUsage();
        assertEquals(expResult, result);
    }
    
}
