
package it.pm.jtelnetserver.util;

/**
 * It is a container for a default configuration.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public final class DefaultConfigurator 
{
    // to avaoid to be initialized anyway
    private DefaultConfigurator() 
    {
        throw new AssertionError("Default Configurator is not instantiable!");
    }
    
    // server host
    public static final String SERVER_HOST = "127.0.0.1";
    // server port
    public static final Integer SERVER_PORT = 10000;
    // home directory
    public static final String SERVER_HOME = System.getProperty("user.home");
    // thread pool maximum size by default
    public static final Integer MAX_NR_THREADS = 100;
    // server authentication enabled|disabled
    public static final Boolean SERVER_AUTHENTICATION = false;
    
}
