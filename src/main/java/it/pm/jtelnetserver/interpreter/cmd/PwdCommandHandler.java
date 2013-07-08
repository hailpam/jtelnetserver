
package it.pm.jtelnetserver.interpreter.cmd;

import it.pm.jtelnetserver.context.ServerSession;
import it.pm.jtelnetserver.interpreter.ClientCommand;
import it.pm.jtelnetserver.interpreter.CommandHandler;
import it.pm.jtelnetserver.interpreter.ExecutionReport;

/**
 * This class manages the client request to show the current directory.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class PwdCommandHandler extends CommandHandler
{
    private static final String lineSeparator = System.getProperty("line.separator");
    
    private static final String cmdUsage = "pwd: usage:"+lineSeparator+" pwd"+lineSeparator
            + "  options: not admitted"+lineSeparator
            + "  target: not admitted"+lineSeparator;

    public PwdCommandHandler() 
    {
        super();
        cmdName = "pwd";
        cmdDescription = "Display the current directory as an absolute path";
    }
    
    @Override
    public ExecutionReport handleCommand(ClientCommand clientQuery, ServerSession clientSess) 
    {
        ExecutionReport report = new ExecutionReport();
        if(clientQuery.getCmd().equalsIgnoreCase("pwd")) {
            if(clientQuery.getOptions().size() > 0 || !clientQuery.getTarget().isEmpty()) {
               // invalid number of options: misusage
               String errorMsg;
               errorMsg = "telnet: ";
               errorMsg += clientQuery.getOriginalQuery();
               errorMsg += ": number of options not allowed: command misuse"+lineSeparator;
               errorMsg += printUsage();
               // set and return the report
               report.setSuccess(false);
               report.setExecutionOutput(errorMsg);
               return report;
            }
            // it is possible to show the current directory
            report.setSuccess(true);
            report.setExecutionOutput(clientSess.getWorkingDir().getAbsolutePath());
            return report;
        }else if(successor != null) {
           // forward to the next piped handler 
           return successor.handleCommand(clientQuery, clientSess);
        }else {
            report.setSuccess(false);
            report.setExecutionOutput("telnet: "+clientQuery.getCmd()+": command not found");
            // notify the failure
            return report;
        }
    }

    @Override
    protected String printUsage() 
    {
        return cmdUsage;
    }
    
}
