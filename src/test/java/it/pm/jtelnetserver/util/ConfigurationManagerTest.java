
package it.pm.jtelnetserver.util;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;

/**
 * Configuration Manager Unit Test class
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class ConfigurationManagerTest extends TestCase {
    
    
    public ConfigurationManagerTest(String testName) {
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
     * Test of getProperty method, of class ConfigurationManager.
     */
    public void testGetProperty_UnitializedSingleton() {
        System.out.println("[Testing]::["+ ConfigurationManager.class.getName()+"]::[getProperty(String)]");
        String expResult = "Configuration Manager not yet Initialized";
        String result = "";
        try {
            // Singleton should be not yet initialized
            ConfigurationManager.getInstance().getProperty("serverhome");
        } catch (ServiceException ex) {
            result = ex.getMessage();
            assertEquals(expResult, result);
        }
    }
    
    /**
     * Test of getInstance method, of class ConfigurationManager.
     */
    public void testGetInstance() {
        System.out.println("[Testing]::["+ ConfigurationManager.class.getName()+"]::[getInstance]");
        ConfigurationManager result = ConfigurationManager.getInstance();
        assertTrue(result instanceof ConfigurationManager);
    }

    /**
     * Test of init method, of class ConfigurationManager.
     */
    public void testInit_0args() {
        System.out.println("[Testing]::["+ ConfigurationManager.class.getName()+"]::[init()]");
        boolean expResult = true;
        assertEquals(expResult, ConfigurationManager.getInstance().init());
    }

    /**
     * Test of init method, of class ConfigurationManager.
     */
    public void testInit_String_String() {
        System.out.println("[Testing]::["+ ConfigurationManager.class.getName()+"]::[init(String,String)]");
        boolean expResult = false;
        // internal checks - test
        assertEquals(expResult, ConfigurationManager.getInstance().init(null, null));
        assertEquals(expResult, ConfigurationManager.getInstance().init("", ""));
        // real arguments but fakes
        expResult = true;
        String pathTo = "pippo";
        String fileName = "pluto";
        assertEquals(expResult, ConfigurationManager.getInstance().reset());
        assertEquals(expResult, ConfigurationManager.getInstance().init(pathTo, fileName));
        // real path and file
        File f = new File(ClassLoader.getSystemResource(".").getPath());
        File f1 = new File(f.getParent());
        File f2 = new File(f1.getParent());
        File f3 = new File(f2.getPath()+File.separator+"stuff");
        pathTo = f3.getPath();
        fileName = "fortesting.properties";
        assertEquals(expResult, ConfigurationManager.getInstance().reset());
        assertEquals(expResult, ConfigurationManager.getInstance().init(pathTo, fileName));
    }
    
    /**
     * Test of getProperty method, of class ConfigurationManager.
     */
    public void testGetProperty_ItializedSingleton() {
        System.out.println("[Testing]::["+ ConfigurationManager.class.getName()+"]::[getProperty(String)]");
        String expResult = "";
        try {
            // Singleton Initialized
            assert(!ConfigurationManager.getInstance().getProperty("serverhome").equals(expResult));
        } catch (ServiceException ex) {
            Logger.getLogger(ConfigurationManagerTest.class.getName()).log(Level.SEVERE, "Exception Caught during the Tests", ex);
        }
        // cross-check a real property retrieval
        expResult = "localhost";
        try {
            // Singleton Initialized
            assertEquals(expResult, ConfigurationManager.getInstance().getProperty("host"));
        } catch (ServiceException ex) {
            Logger.getLogger(ConfigurationManagerTest.class.getName()).log(Level.SEVERE, "Exception Caught during the Tests", ex);
        }
    }
    
}
