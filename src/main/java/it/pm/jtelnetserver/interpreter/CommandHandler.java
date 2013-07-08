
package it.pm.jtelnetserver.interpreter;

import it.pm.jtelnetserver.context.ServerSession;
import it.pm.jtelnetserver.util.ServiceException;

/**
 * Abstract class that defines the interface and a basic behaviour of each specific
 * version of command handlers.
 * 
 * @author Paolo Maresca <plo.marescsa@gmail.com>
 */
public abstract class CommandHandler 
{
    // command successor in the pipe
    protected CommandHandler successor;
    // it defines the command name 
    protected String cmdName;
    // it defines the command description
    protected String cmdDescription;
    
    /**
     * It sets the next handler in the chain that will be built.
     * 
     * @param nextHandler A valid command handler to be elected as successor
     */
    public void setSuccessor(CommandHandler nextHandler) throws ServiceException
    {
        if(nextHandler == null)
            throw new ServiceException("NULL successor in Input: Unable to proceed");
        // pipes the successor
        if(successor == null)
            successor = nextHandler;
        else
            this.successor.setSuccessor(nextHandler);
    }
    
    /**
     * This method needs to be implemented by each specific/child handler. It 
     * defines the buisness logic useful to manage the query forwarded by 
     * the client.
     * 
     * @param clientQuery A command containing the validated and parsed query
     * @param clientSess The current client session
     * 
     * @return The actual result of execution of this command
     */
    public abstract ExecutionReport handleCommand(ClientCommand clientQuery,
                                                    ServerSession clientSess);
    
    /**
     * Internal utility method to print the usage of the specific command handler.
     * 
     * @return A string containing the usage information
     */
    protected abstract String printUsage();

    /**
     * Get the Command Name.
     * 
     * @return A string containing the command name
     */
    public String getCmdName() 
    {
        return cmdName;
    }

    /**
     * Set the Command Name.
     * 
     * @param cmdName A string containing the commmand name
     */
    public void setCmdName(String cmdName) 
    {
        this.cmdName = cmdName;
    }

    /**
     * Get the Command Description.
     * 
     * @return A string containing the command description
     */
    public String getCmdDescription() 
    {
        return cmdDescription;
    }

    /**
     * Set the Command Description.
     * 
     * @param cmdDescription A string containing the command description
     */
    public void setCmdDescription(String cmdDescription) 
    {
        this.cmdDescription = cmdDescription;
    }

    /**
     * Stringify the Command Handler and its internal status.
     * 
     * @return A string representing the command handler
     */
    @Override
    public String toString() {
        return "CommandHandler{" + "successor=" + successor + ", cmdName=" + cmdName + ", cmdDescription=" + cmdDescription + '}';
    }
    
}
