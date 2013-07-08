
package it.pm.jtelnetserver.context;

import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;

/**
 * Server Session class unit test.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class ServerSessionTest extends TestCase {
    
    private static final String lineSeparator = System.getProperty("line.separator");
    
    public ServerSessionTest(String testName) {
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
     * Test of addCommandToHistory method, of class ServerSession.
     */
    public void testAddCommandToHistory() {
        System.out.println("[Testing]::["+ ServerSession.class.getName()+"]::[addCommandToHistory(ClientCommand)]");
        String cmd = "ls -la";
        ServerSession instance = new ServerSession();
        instance.addCommandToHistory(cmd);
        StringBuilder expResult = new StringBuilder();
        Formatter historyFormatter = new Formatter(expResult);
        historyFormatter.format("%-5.5s %-150.150s"+lineSeparator, 1, "ls -la");
        assertEquals(instance.retrieveHistory(), expResult.toString());
    }

    /**
     * Test of retrieveHistory method, of class ServerSession.
     */
    public void testRetrieveHistory() {
        System.out.println("[Testing]::["+ ServerSession.class.getName()+"]::[retrieveHistory()]");
        List<String> fakeHistory = new ArrayList<String>();
        ServerSession instance = new ServerSession();
        instance.addCommandToHistory("ls -la");
        fakeHistory.add("ls -la");
        instance.addCommandToHistory("mkdir pippo");
        fakeHistory.add("mkdir pippo");
        instance.addCommandToHistory("history");
        fakeHistory.add("history");
        StringBuilder expResult = new StringBuilder();
        Formatter historyFormatter = new Formatter(expResult);
        int cntr = 1;
        Iterator<String> cmdItr = fakeHistory.iterator();
        while(cmdItr.hasNext()) {
            historyFormatter.format("%-5.5s %-150.150s"+lineSeparator, cntr, cmdItr.next());
            cntr += 1;
        }
        String result = instance.retrieveHistory();
        assertEquals(expResult.toString(), result);
    }

    /**
     * Test of closeSession method, of class ServerSession.
     */
    public void testCloseSession() {
        System.out.println("[Testing]::["+ ServerSession.class.getName()+"]::[closeSession()]");
        ServerSession instance = new ServerSession();
        long startTime = System.currentTimeMillis();
        instance.closeSession();
        assertEquals(instance.isSessionActive(), false);
        assertEquals(instance.retrieveSessionLife(), (System.currentTimeMillis() - startTime));
    }

    /**
     * Test of isSessionActive method, of class ServerSession.
     */
    public void testIsSessionActive() {
        System.out.println("[Testing]::["+ ServerSession.class.getName()+"]::[isSessionActive()]");
        ServerSession instance = new ServerSession();
        boolean expResult = true;
        boolean result = instance.isSessionActive();
        assertEquals(expResult, result);
    }

    /**
     * Test of numberOfServiceFailures method, of class ServerSession.
     */
    public void testNumberOfServiceFailures() {
        System.out.println("[Testing]::["+ ServerSession.class.getName()+"]::[numberOfServiceFailures()]");
        ServerSession instance = new ServerSession();
        int expResult = 0;
        int result = instance.numberOfServiceFailures();
        assertEquals(expResult, result);
    }

    /**
     * Test of increaseServiceFailures method, of class ServerSession.
     */
    public void testIncreaseServiceFailures() {
        System.out.println("[Testing]::["+ ServerSession.class.getName()+"]::[increaseServiceFailures()]");
        ServerSession instance = new ServerSession();
        instance.increaseServiceFailures();
        assertEquals(instance.numberOfServiceFailures(), 1);
    }

    /**
     * Test of getStatus method, of class ServerSession.
     */
    public void testGetStatus() {
        System.out.println("[Testing]::["+ ServerSession.class.getName()+"]::[getStatus()]");
        ServerSession instance = new ServerSession();
        StringBuffer expResult = new StringBuffer("");
        expResult.append("session status: "+lineSeparator);
        expResult.append(" is active :: ");
        expResult.append(true);
        expResult.append(lineSeparator);
        expResult.append(" server command(s) #:: ");
        expResult.append(0);
        expResult.append(lineSeparator);
        expResult.append(" service failure(s) #:: ");
        expResult.append(0);
        expResult.append(lineSeparator);
        expResult.append(" uptime :: ");
        String result = instance.getStatus();
        expResult.append(instance.retrieveSessionLife());
        expResult.append("ms");
        expResult.append(lineSeparator);
        assertEquals(expResult.toString(), result);
    }
    
    public void testGetWorkingDirectory () {
        System.out.println("[Testing]::["+ ServerSession.class.getName()+"]::[getWorkingDirectory()]");
        ServerSession instance = new ServerSession();
        File f = instance.getWorkingDir();
        String expRes = System.getProperty("user.home");
        assertEquals(expRes, f.getPath());
    }
    
    public void testSetWorkingDirectory() {
        System.out.println("[Testing]::["+ ServerSession.class.getName()+"]::[setWorkingDirectory()]");
        ServerSession instance = new ServerSession();
        File f = instance.getWorkingDir();
        String expRes = "";
        File[] lFiles = f.listFiles();
        for(int i = 0; i < lFiles.length; i++) {
            if(lFiles[i].isDirectory()) {
                instance.setWorkingDir(lFiles[i]);
                expRes = lFiles[i].getPath();
            }
        }
        f = instance.getWorkingDir();
        assertEquals(expRes, f.getPath());
    }
    
}
