
package it.pm.jtelnetserver.interpreter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class wraps the result 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class ClientCommand 
{
    // encapsulate the client command
    private String cmd;
    // contains the command options
    private List<String> options;
    // encapsulate the command target
    private String target;
    // original query forwarded by the client
    private String origQuery;

    public ClientCommand() 
    {
        cmd = "";
        options = new ArrayList<String>();
        target = "";
        origQuery = "";
    }

    /**
     * Get the Command
     * 
     * @return The command
     */
    public String getCmd() 
    {
        return cmd;
    }
    
    /**
     * Set the Command
     * 
     * @param cmd Command to be set
     */
    public void setCmd(String cmd) 
    {
        this.cmd = cmd;
    }

    /**
     * Get the Option List
     * 
     * @return The list of options for this command
     */
    public List<String> getOptions() 
    {
        return options;
    }

    /**
     * Get the Target for this command
     * 
     * @return The target for this command
     */
    public String getTarget() 
    {
        return target;
    }

    /**
     * Set the Target for this command
     * 
     * @param target Target to be set
     */
    public void setTarget(String target) 
    {
        this.target = target;
    }

    /**
     * Get the original query forwarded by the client.
     * 
     * @return A string containing the original query
     */
    public String getOriginalQuery() {
        return origQuery;
    }

    /**
     * Set the original query forwarded by the client.
     * 
     * @param origQuery A string containing the original query
     */
    public void setOriginalQuery(String origQuery) {
        this.origQuery = origQuery;
    }

    /**
     * Stringify this class
     * 
     * @return A string representation for this class
     */
    @Override
    public String toString() 
    {
        return "ClientCommand{" + "cmd=" + cmd + ", options=" + options + ", target=" + target + '}';
    }
    
}
