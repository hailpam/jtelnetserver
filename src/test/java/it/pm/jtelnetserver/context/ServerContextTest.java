
package it.pm.jtelnetserver.context;

import junit.framework.TestCase;

/**
 * Server Context class unit test.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class ServerContextTest extends TestCase {
    
    public ServerContextTest(String testName) {
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
     * Test of getInstance method, of class ServerContext.
     */
    public void testGetInstance() {
        System.out.println("[Testing]::["+ ServerContext.class.getName()+"]::[getInstance()]");
        ServerContext result = ServerContext.getInstance();
        assert(result instanceof ServerContext);
    }

    /**
     * Test of sessionManaged method, of class ServerContext.
     */
    public void testSessionManaged() {
        System.out.println("[Testing]::["+ ServerContext.class.getName()+"]::[sessionManaged()]");
        int result = ServerContext.getInstance().sessionManaged();
        assert(result >= 0);
    }

    /**
     * Test of serverUpTime method, of class ServerContext.
     */
    public void testServerUpTime() {
        System.out.println("[Testing]::["+ ServerContext.class.getName()+"]::[serverUpTime()]");
        long expResult = 0L;
        long result = ServerContext.getInstance().serverUpTime();
        assert(result >= expResult);
    }

    /**
     * Test of serverInfo method, of class ServerContext.
     */
    public void testServerInfo() {
        System.out.println("[Testing]::["+ ServerContext.class.getName()+"]::[serverInfo()]");
        String expResult = "";
        expResult += "OS :: ";
        expResult += System.getProperty("os.name");
        expResult += ", ";
        expResult += "architecture :: ";
        expResult += System.getProperty("os.arch");
        expResult += ", version:: ";
        expResult += System.getProperty("os.version");
        String result = ServerContext.getInstance().serverInfo();
        assertEquals(expResult, result);
        System.out.println(result);
    }

    /**
     * Test of getServerSession method, of class ServerContext.
     */
    public void testGetServerSession() {
        System.out.println("[Testing]::["+ ServerContext.class.getName()+"]::[getServerSession()]");
        String sessionId = "2";
        ServerSession expResult = null;
        ServerSession result = ServerContext.getInstance().getServerSession(sessionId);
        assertEquals(expResult, result);
    }

    /**
     * Test of setSessionId method, of class ServerContext.
     */
    public void testSetSessionId() throws Exception {
        System.out.println("[Testing]::["+ ServerContext.class.getName()+"]::[setSessionId()]");
        String sessionId = "1";
        ServerSession session = new ServerSession();
        ServerSession expRes = null;
        ServerContext.getInstance().setSessionId(sessionId, session);
        expRes = ServerContext.getInstance().getServerSession(sessionId);
        assertEquals(session, expRes);
    }
    
}
