
package it.pm.jtelnetserver.thread;

import it.pm.jtelnetserver.context.ServerContext;
import it.pm.jtelnetserver.context.ServerSession;
import it.pm.jtelnetserver.util.ConfigurationManager;
import it.pm.jtelnetserver.util.ServiceException;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements a Runnable entity, so it can be detached as a Thread.
 * It waits new client connections and upon a new client connection, it dispatches
 * such request to one of the worker threads in the pool.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class ConnectorThread implements Runnable
{
    // logger
    private static final Logger theLogger = Logger.getLogger(ConnectorThread.class.getName());
    
    // control variable
    boolean isAlive;
    // server socket
    private ServerSocket connector;
    // pool of thread executor
    private ExecutorService poolExecutor;
    // thread counter
    private int threadDetached;
    // path pointing to the configuration file
    private String configPath;
    // configuration file to be used
    private String configFile;

    public ConnectorThread() 
    {
        super();
        isAlive = true;
        threadDetached = 0;
        configPath = "";
        configFile = "";
    }
    
    public void run() 
    {
        if(!configPath.isEmpty() && !configFile.isEmpty())
            ConfigurationManager.getInstance().init(configPath, configFile);
        else
            ConfigurationManager.getInstance().init();
        try {
            String serverInfo = "\n+--";
            serverInfo += "Server Info\n";
            serverInfo += "     "+ServerContext.getInstance().serverInfo();
            serverInfo += "\n     home :: ";
            serverInfo += ConfigurationManager.getInstance().getProperty("serverhome");
            serverInfo += "\n     sessions # :: ";
            serverInfo += "     "+ServerContext.getInstance().sessionManaged();
            serverInfo += "\n+--";
            theLogger.info(serverInfo);
            poolExecutor = Executors.newFixedThreadPool(Integer.parseInt(
                    ConfigurationManager.getInstance().getProperty("poolsize")));
            connector = new ServerSocket(Integer.parseInt(
                    ConfigurationManager.getInstance().getProperty("port")));
            theLogger.info("Server is Running");
            theLogger.info("Listening on Port :: " +
                    ConfigurationManager.getInstance().getProperty("port"));
            // main loop: wait for connections and dispatctch them to worker threads
            while(isAlive) {
                // wait untill a new client connections is ready to be dispatched
                Socket clientSession = connector.accept();
                // creating the client-server session descriptor
                threadDetached += 1;
                String threadIdentity = "Thread_#";
                threadIdentity += threadDetached;
                ServerSession session = new ServerSession();
                File serverHome = new File(ConfigurationManager.getInstance().getProperty("serverhome"));
                session.setWorkingDir(serverHome);
                // fill-in the server context with a newer client session
                ServerContext.getInstance().setSessionId(threadIdentity, session);
                // dispatch the connection to the client
                poolExecutor.execute(new ServiceThread(clientSession, threadIdentity));
            }
        } catch (IOException ex) {
            theLogger.log(Level.SEVERE, "Error occurred in creating the binding socket", 
                    ex);
        } catch (ServiceException ex) {
            theLogger.log(Level.SEVERE, "Error occurred in retrieving the info "
                    + "from Configuration Manager", 
                    ex);
        }
    }

    /**
     * Get the control status.
     * 
     * @return True, iff the Thread is alive
     */
    public boolean isIsAlive() 
    {
        return isAlive;
    }

    /**
     * Set the configuration path.
     * 
     * @param configPath A string containing the configuration path
     */
    public void setConfigPath(String configPath) 
    {
        this.configPath = configPath;
    }

    /**
     * Set the configuration file.
     * 
     * @param configFile A string containing the name of the configuration file
     */
    public void setConfigFile(String configFile) 
    {
        this.configFile = configFile;
    }
    
    /**
     * This method must be invoked only in case it's needed to shutdown the
     * server connector.
     */
    public void shutDownConnector() 
    {
        // close the socket
        try {
            connector.close();
        } catch (IOException ex) {
            theLogger.log(Level.SEVERE, "Error occurred in closing the connector"
                    , ex);
        }finally {
            // retry to close if not yet closed
            if(!connector.isClosed()) {
                try {
                    connector.close();
                } catch (IOException ex) {
                     theLogger.log(Level.SEVERE, "RETRY :: Error occurred in closing the connector"
                    , ex);
                }
                // exit from while
                if(connector.isClosed())
                    isAlive = false;
            }
        }
    }
    
}
