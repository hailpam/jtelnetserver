
package it.pm.jtelnetserver.context;

import it.pm.jtelnetserver.util.ServiceException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class maintains the overall server context with all the related 
 * information.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class ServerContext 
{
    private static ServerContext instance;
    
    private Map<String, ServerSession> clientSessions;
    private String serverInfo;
    private long startTime;
    
    private ServerContext() 
    {
        super();
        clientSessions  = new HashMap<String, ServerSession>();
        serverInfo = "";
        startTime = System.currentTimeMillis();
    }
    
    /**
     * It provides access to the singleton instance of the Server Context.
     * 
     * @return The singleton instance of the Server Context
     */
    public static synchronized ServerContext getInstance() 
    {
        if(instance == null)
            instance = new ServerContext();
        return instance;
    }
    
    /**
     * It derives the number of session managed till now.
     * 
     * @return The number of session managed
     */
    public int sessionManaged()
    {
        return clientSessions.size();
    }
    
    /**
     * It calculates and returns the server up-time in milliseconds.
     * 
     * @return The actual server up-time
     */
    public long serverUpTime() 
    {
        return (System.currentTimeMillis() - startTime);
    }
    
    /**
     * It builds and returns the server info.
     * 
     * @return A string representing the major local machine info
     */
    public String serverInfo() 
    {
        if(serverInfo.isEmpty()) {
            serverInfo += "OS :: ";
            serverInfo += System.getProperty("os.name");
            serverInfo += ", ";
            serverInfo += "architecture :: ";
            serverInfo += System.getProperty("os.arch");
            serverInfo += ", version:: ";
            serverInfo += System.getProperty("os.version");
        }
        return serverInfo;
    }
    
    /**
     * It retrieves (if it exists) the server session with the specific session Id.
     * In case no session Id is registered with that session Id, the return value
     * is NULL.
     * 
     * @param sessionId A string uniquely defining the session Id.
     * @return The server session associated with the provided session Id.
     */
    public ServerSession getServerSession(String sessionId) 
    {
        return this.clientSessions.get(sessionId);
    }
    
    /**
     * It sets the server session opened to serve a specific client, by using the
     * provided session Id that identifies uniquely that session.
     * 
     * @param sessionId The session Id uniquely identifying the server session
     * @param session The server session opened to serve a specific client
     * 
     * @throws ServiceException In case bad Inputs are provided
     */
    public void setSessionId(String sessionId, ServerSession session) throws ServiceException 
    {
        if(sessionId == null || sessionId.isEmpty() || session == null) 
            throw new ServiceException("Invalid Input parameters: NULL or Blank values"
                    + " not accepted");
        this.clientSessions.put(sessionId, session);
    }
    
}
