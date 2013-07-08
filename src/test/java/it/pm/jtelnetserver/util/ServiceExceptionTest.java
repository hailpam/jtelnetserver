
package it.pm.jtelnetserver.util;

import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;

/**
 * Service Exception Unit Test class
 *  
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class ServiceExceptionTest extends TestCase {
    
    public ServiceExceptionTest(String testName) {
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

    public void testException() {
        // blank string
        System.out.println("[Testing]::["+ ServiceException.class.getName()+"]::[getMessage]");
        try {
            throw new ServiceException("");
        }catch(ServiceException ex) {
            assertEquals(ex.getMessage(), "");
        }
        // message string
        try {
            throw new ServiceException("test");
        }catch(ServiceException ex) {
            assertEquals(ex.getMessage(), "test");
        }
    }
}
