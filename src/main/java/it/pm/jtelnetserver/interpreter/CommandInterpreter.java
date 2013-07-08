
package it.pm.jtelnetserver.interpreter;

import it.pm.jtelnetserver.context.ServerContext;
import it.pm.jtelnetserver.context.ServerSession;
import it.pm.jtelnetserver.util.ServiceException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class defines the real command line interpreter. It takes in input the 
 * query forwarded by the client and executes it against the local machine; it assumes 
 * that the query has been already validated.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class CommandInterpreter 
{
    private static final String lineSeparator = System.getProperty("line.separator");
    
    private static final Logger theLogger = Logger.getLogger(CommandInterpreter.class.getName());
    
    // an help template
    private static final String HELP_TEMPLATE = lineSeparator+"JTelnetServer v1.0 Command Interpreter"+lineSeparator
            + "These shell commands are defined internally. Hereafter a list of the "
            + "available commands."+lineSeparator+lineSeparator
            + "${LIST_OF_COMMANDS}";
    // a format pattern
    private static final String FORMAT_PATTERN = "%-15.15s %-150.150s"+lineSeparator;
    
    // it is the head pointer to the chain of specific handlers
    private CommandHandler chainOfHandlers;
    // a map containing the registered commands
    private Map<String, String> specHandlers;
    // it maintains the actual session identifier
    private String sessionId;
    // it maintains a reference to the server session
    private ServerSession actualSession;
    // it caches the help output (it is built only the first time)
    private String helpOutput;
    
    public CommandInterpreter() 
    {
        super();
        specHandlers = new HashMap<String, String>();
        sessionId = "";
        helpOutput = "";
        actualSession = null;
        // fill-in the specific handlers with the basic commands (not piped)
        specHandlers.put("help", "Display the list of available commands.");
        specHandlers.put("history", "Display the list of commands forwarded over the session.");
        specHandlers.put("status", "Display the session status (useful information are reported).");
        specHandlers.put("quit", "Quit releasing the session.");
    }
    
    /**
     * It implements the command interpreter logic. Trusting the pipeline of handlers
     * it simply implements an smart analyze and forward logic.
     * 
     * @param clientQuery A validated and parsed client query
     * 
     * @return A string containing the command output
     * 
     * @throws ServiceException In case the command is not properly initialized
     */
    public String interpretCommand(ClientCommand clientQuery) throws ServiceException
    {
        if(clientQuery == null)
            throw new ServiceException("Client Query object not initialized");
        if(clientQuery.getCmd().isEmpty())
            throw new ServiceException("Client Query object not properly initialized"
                    + ": blank command. Unable to serve the request.");
        // command interpretation: it checks what it can do (help, history, status)
        if(clientQuery.getCmd().equalsIgnoreCase("help")) {
            return handleHelpRequest();
        }else if(clientQuery.getCmd().equalsIgnoreCase("history")) {
            return handlerHistoryRequest();
        }else if(clientQuery.getCmd().equalsIgnoreCase("status")) {
            return handleStatusRequest();
        }else if(clientQuery.getCmd().equalsIgnoreCase("quit")) {
            return handleQuitRequest();
        }else { // the command must be piped
            // retrieve the server session from the context
            if(actualSession == null)
                actualSession = ServerContext.getInstance().getServerSession(sessionId);
            // start the command execution
            ExecutionReport cmdRep = chainOfHandlers.handleCommand(clientQuery, actualSession);
            // interpret the result
            if(!cmdRep.isSuccess())
                actualSession.increaseServiceFailures();
            actualSession.addCommandToHistory(clientQuery.getOriginalQuery());
            // return the actual command output
            return cmdRep.getExecutionOutput();
        }
    }
    
    /**
     * Registration mechanism for specific command handlers. 
     * 
     * @param cmdHanlder A specific command handler to be chained with the existing ones
     * 
     * @throws ServiceException In case the handler is not initialized
     */
    public void addCommandHandler(CommandHandler cmdHanlder) throws ServiceException 
    {
        if(cmdHanlder == null)
            throw new ServiceException("NULL handler: unable to proceed in "
                    + "command registration");
        if(cmdHanlder.getCmdName().isEmpty() || cmdHanlder.getCmdDescription().isEmpty())
            throw new ServiceException("BAD handler: unable to proceed in "
                    + "command registration. Name and/or Description not filled");
        specHandlers.put(cmdHanlder.getCmdName(), cmdHanlder.getCmdDescription());
        // pipes the command handlers: by 5doing so a management pipe is built
        // from the head pointer
        if(chainOfHandlers == null)
            chainOfHandlers = cmdHanlder;
        else
            chainOfHandlers.setSuccessor(cmdHanlder);
    }

    /**
     * Get the session identifier for the actual working session.
     * 
     * @return A string containing the session Id
     */
    public String getSessionId() 
    {
        return sessionId;
    }

    /**
     * Set the session identifier for the actual working session
     * 
     * @param sessionId A string containing the session Id
     */
    public void setSessionId(String sessionId) 
    {
        this.sessionId = sessionId;
    }
    
    /**
     * Get the actual Server Session.
     * 
     * @return The working (for the interpreter) server session
     */
    public ServerSession getActualSession() 
    {
        return actualSession;
    }

    /**
     * Set the actual Server Session
     * 
     * @param actualSession The server session on which the interprete works
     */
    public void setActualSession(ServerSession actualSession) 
    {
        this.actualSession = actualSession;
    }
    
    /**
     * Internal Utility method to serve the commands history request
     * 
     * @return A string containing the command history
     */
    private String handlerHistoryRequest() 
    {
        // retrieve current session
        if(actualSession == null)
            actualSession = ServerContext.getInstance().getServerSession(sessionId);
        actualSession.addCommandToHistory("history");
        // wraps out the content provided by the serve ssession object
        return actualSession.retrieveHistory();
    }
    
    /**
     * Internal Utility method to serve the help request.
     * 
     * @return A string containing the help lines
     */
    private String handleHelpRequest() 
    {
        String helpDisplay = "";
        // upon the first call, it builds the string
        if(helpOutput.isEmpty()) {
            if(specHandlers.size() == 0) { // no command registered
                helpDisplay = HELP_TEMPLATE.replace("${LIST_OF_COMMANDS}", " interpreter: "
                        + "internal problem: no command registered");
            }else { // command registered
                StringBuilder helpBuilder = new StringBuilder();
                Formatter helpFormatter = new Formatter(helpBuilder);
                Iterator mapItr = specHandlers.entrySet().iterator();
                while(mapItr.hasNext()) {
                    Map.Entry pair = (Map.Entry) mapItr.next();
                    helpFormatter.format(FORMAT_PATTERN, pair.getKey(), pair.getValue());
                }
                helpDisplay = HELP_TEMPLATE.replace("${LIST_OF_COMMANDS}", helpBuilder.toString());
                helpOutput = helpDisplay;
            }
        }else
            helpDisplay = helpOutput;
        actualSession.addCommandToHistory("help");
        // return the above built help display
        return helpDisplay;
    }
    
    /**
     * Internal Utility method to serve the server status requests forwarded by
     * the clients.
     * 
     * @return A string containing the server status
     */
    private String handleStatusRequest() 
    {
        // retrieve current session
        if(actualSession == null)
            actualSession = ServerContext.getInstance().getServerSession(sessionId);
        actualSession.addCommandToHistory("status");
        // wraps out the content provided by the serve ssession object
        return actualSession.getStatus();
    }
    
    /**
     * Internal Utility to server the server quit request.
     * 
     * @return A string containing the content to show to the client
     */
    private String handleQuitRequest() 
    {
        StringBuffer strBuilder = new StringBuffer("");
        strBuilder.append("telnet: releasing the session: exiting...");
        strBuilder.append("${RELEASE}");
        // return the above built string
        return strBuilder.toString();
    }
    
}
