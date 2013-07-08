
package it.pm.jtelnetserver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Configuration Manager aids in managing the server's settings. It provides 
 * a Singleton instance safely accessible by multiple active entities.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public final class ConfigurationManager 
{
    private static ConfigurationManager instance;
    private static final Logger theLogger = Logger.getLogger(ConfigurationManager.class.getName());
    
    private boolean initialized;
    private Properties srvProps;
    
    private ConfigurationManager() 
    {
        super();
        initialized = false;
        srvProps = new Properties();
    }
    
    /**
     * Internal utility method to load default properties in case a failure in 
     * loading 'server.properties' configuration file is caught.
     */
    private void loadDefaultProperties() 
    {
        theLogger.info("Loaded Default Configuration for Telnet Server");
        srvProps.setProperty("server.host", DefaultConfigurator.SERVER_HOST);
        srvProps.setProperty("server.port", Integer.toString(DefaultConfigurator.SERVER_PORT));
        srvProps.setProperty("server.home", DefaultConfigurator.SERVER_HOME);
        srvProps.setProperty("server.thread.max", Integer.toString(DefaultConfigurator.MAX_NR_THREADS));
        srvProps.setProperty("server.auth", Boolean.toString(DefaultConfigurator.SERVER_AUTHENTICATION));
    }
    
    /**
     * Internal utility to initialize the server properties.
     */
    private void initializationHelper(String resource) 
    {
        // read properties file
        InputStream iStream = null;
        if(resource.isEmpty())
             iStream =ClassLoader.getSystemResourceAsStream("server.properties");
        else {
            try {
                iStream = new FileInputStream(resource);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ConfigurationManager.class.getName()).log(Level.SEVERE, "Resource not found", ex);
                // load default properties to let the server start anyway
                loadDefaultProperties();
                theLogger.log(Level.WARNING, "Server Default Properties have been loaded");
                // return immediately after having loaded the default configuration
                return;
            }
        }
        // if it's not able to locate server.properties
        if(iStream == null) {
            // load default properties to let the server start anyway
            loadDefaultProperties();
            theLogger.log(Level.WARNING, "Server Default Properties have been loaded");
            return;
        }
        // load properties using the input stream
        try {
            srvProps.load(iStream);
        } catch (IOException ex) {
            theLogger.log(Level.WARNING, "Error occurred in loading 'server.properties'", ex);
            // load default properties to let the server start anyway
            loadDefaultProperties();
            theLogger.log(Level.WARNING, "Server Default Properties have been loaded");
        }
    }
    
    /**
     * Unique point of access to Configuration Manager instance.
     * 
     * @return ConfigurationManager unique instance
     */
    public static synchronized ConfigurationManager getInstance() 
    {
        if(instance == null)
            instance = new ConfigurationManager();
        return instance;
    }
   
    /**
     * It resets the previously loaded configuration.
     * 
     * @return true, iff the configuration has been reset
     */
    public boolean reset() 
    {
        if(initialized) {
            initialized = false;
            srvProps = new Properties();
        }
        return !initialized;
    }
    
    /**
     * Initialize the Configuration Manager assuming a default path for the 
     * property file (classpath).
     * 
     * @return true, iff ConfigurationManager is initialized
     */
    public boolean init() 
    {
        if(!initialized) {
            initialized = true;
            // call initialization helper
            initializationHelper("");
        }
        return initialized;
    }
    
    /**
     * Initialize the Configuration Manager combining the path and file name 
     * provided in input to point to the property file.
     * 
     * @param pathTo Path to the configuration file
     * @param fileName Properties file name
     * @return true, iff ConfigurationManager is initialized
     */
    public boolean init(String pathTo, String fileName) 
    {
        if(pathTo == null || fileName == null)
            return false;
        if(pathTo.isEmpty() || fileName.isEmpty())
            return false;
        if(!initialized) {
            initialized = true;
            String propRes = "";
            if(pathTo.charAt(pathTo.length() - 1) == File.separatorChar)
                propRes = pathTo + fileName;
            else
                propRes = pathTo + File.separator + fileName;
            // call initialization helper
            initializationHelper(propRes);
        }
        return initialized;
    }
    
    /**
     * Retrieve the server property identified by the provided key. In case the 
     * property is not present/recognizable it returns a blank string.
     * 
     * @param propKey Server property key
     * @return The value, iff the property is present in the configuration file
     */
    public String getProperty(String propKey) throws ServiceException
    {
        if(propKey.isEmpty())
            return "";
        if(!initialized) {
            theLogger.log(Level.SEVERE, "Configuration Manager not yet Initialized");
            throw new ServiceException("Configuration Manager not yet Initialized");
        }
        // wraps a call to property getter method: decoding
        String settingValue = "";
        if(propKey.equals("host")) {
            settingValue = srvProps.getProperty("server.host");
        }else if(propKey.equals("port")) {
            settingValue = srvProps.getProperty("server.port");
        }else if(propKey.equals("serverhome")) {
            settingValue = srvProps.getProperty("server.home");
        }else if(propKey.equals("poolsize")) {
            settingValue = srvProps.getProperty("server.thread.max");
        }else if(propKey.equals("auth")) {
            settingValue = srvProps.getProperty("server.auth");
        }
        // return the retrieved value
        return ((settingValue == null)?"":settingValue);
    }
    
}
