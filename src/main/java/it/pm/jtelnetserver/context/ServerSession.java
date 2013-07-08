
package it.pm.jtelnetserver.context;

import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;


/**
 * This class has the responsibility to maintain all the information related 
 * to a certain telnet client session.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class ServerSession 
{
    private static final String lineSeparator = System.getProperty("line.separator");
    
    private static final String HISTORY_FORMAT = "%-5.5s %-150.150s"+lineSeparator;
    
    private List<String> cmdHistory;
    private boolean isActive;
    private long startTime;
    private long sessionLife;
    public int serviceFailures;
    public File workingDir;

    public ServerSession() 
    {
        super();
        cmdHistory = new ArrayList<String>();
        isActive = true;
        startTime = System.currentTimeMillis();
        serviceFailures = 0;
        // by default, set to the user's home
        workingDir = new File(System.getProperty("user.home"));
    }
    
    /**
     * Add a command to this session history
     * 
     * @param cmd A string containing the original client query
     */
    public void addCommandToHistory(String cmd) 
    {
        cmdHistory.add(cmd);
    }
    
    /**
     * It formats this session commands history providing a string containing
     * all the relevant information.
     * 
     * @return A string depicting the history of commands performed in this session
     */
    public String retrieveHistory() 
    {
        StringBuilder historyBuilder = new StringBuilder();
        Formatter historyFormatter = new Formatter(historyBuilder);
        int cntr = 1;
        Iterator<String> cmdItr = cmdHistory.iterator();
        while(cmdItr.hasNext()) {
            historyFormatter.format(HISTORY_FORMAT, cntr, cmdItr.next());
            cntr += 1;
        }
        // return the above built history
        return historyBuilder.toString();
    }
    
    /**
     * It declares this session closed.
     */
    public void closeSession() 
    {
        sessionLife = System.currentTimeMillis() - startTime;
        isActive = false;
    }
    
    /**
     * Return the activation status of this session.
     * 
     * @return True, iff the session is still active
     */
    public boolean isSessionActive() 
    {
        return isActive;
    }
    
    /**
     * It returns the number of execution failures experienced in this session.
     * 
     * @return A counter containing all the failing attempts of query in this session
     */
    public int numberOfServiceFailures() 
    {
        return serviceFailures;
    }
    
    /**
     * It increases the number of failures experienced in this session.
     */
    public void increaseServiceFailures()
    {
        serviceFailures += 1;
    }
    
    /**
     * It returns a status representation for the current session.
     * 
     * @return A string representation of the current session
     */
    public String getStatus() 
    {
        StringBuffer statusBuffer = new StringBuffer("");
        statusBuffer.append("session status: "+lineSeparator);
        statusBuffer.append(" is active :: ");
        statusBuffer.append(isActive);
        statusBuffer.append(lineSeparator);
        statusBuffer.append(" server command(s) #:: ");
        statusBuffer.append(cmdHistory.size());
        statusBuffer.append(lineSeparator);
        statusBuffer.append(" service failure(s) #:: ");
        statusBuffer.append(serviceFailures);
        statusBuffer.append(lineSeparator);
        statusBuffer.append(" uptime :: ");
        statusBuffer.append((System.currentTimeMillis() - startTime));
        statusBuffer.append("ms");
        statusBuffer.append(lineSeparator);
        // returns the above built status
        return statusBuffer.toString();
    }
    
    /**
     * It derives the session life: how long the session has been active, till now.
     * 
     * @return A long containing the session life span in milliseconds.
     */
    public long retrieveSessionLife() 
    {
        if(sessionLife == 0L)
            sessionLife = (System.currentTimeMillis() - startTime);
        return sessionLife;
    }

    /**
     * Get the working directory
     * 
     * @return The actual working directory
     */
    public File getWorkingDir() 
    {
        return workingDir;
    }

    /**
     * Set the working directory
     * 
     * @param workingDir File resource pointing to the working directory
     */
    public void setWorkingDir(File workingDir) 
    {
        this.workingDir = workingDir;
    }

    /**
     * 
     * @return 
     */
    @Override
    public String toString() 
    {
        return "ServerSession{" + "cmdHistory=" + cmdHistory + ", "
                + "isActive=" + isActive + ", "
                + "startTime=" + startTime + ", "
                + "sessionLife=" + sessionLife + ", "
                + "serviceFailures=" + serviceFailures + ", "
                + "workingDir=" + workingDir + '}';
    }

    /**
     * 
     * @return 
     */
    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 37 * hash + (this.cmdHistory != null ? this.cmdHistory.hashCode() : 0);
        hash = 37 * hash + (this.isActive ? 1 : 0);
        hash = 37 * hash + (int) (this.startTime ^ (this.startTime >>> 32));
        hash = 37 * hash + (int) (this.sessionLife ^ (this.sessionLife >>> 32));
        hash = 37 * hash + this.serviceFailures;
        hash = 37 * hash + (this.workingDir != null ? this.workingDir.hashCode() : 0);
        return hash;
    }

    /**
     * 
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) 
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ServerSession other = (ServerSession) obj;
        if (this.cmdHistory != other.cmdHistory && (this.cmdHistory == null || 
                !this.cmdHistory.equals(other.cmdHistory))) {
            return false;
        }
        if (this.isActive != other.isActive) {
            return false;
        }
        if (this.startTime != other.startTime) {
            return false;
        }
        if (this.sessionLife != other.sessionLife) {
            return false;
        }
        if (this.serviceFailures != other.serviceFailures) {
            return false;
        }
        if (this.workingDir != other.workingDir && (this.workingDir == null || 
                !this.workingDir.equals(other.workingDir))) {
            return false;
        }
        return true;
    }
    
}
