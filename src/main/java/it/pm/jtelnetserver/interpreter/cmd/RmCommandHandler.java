
package it.pm.jtelnetserver.interpreter.cmd;

import it.pm.jtelnetserver.context.ServerSession;
import it.pm.jtelnetserver.interpreter.ClientCommand;
import it.pm.jtelnetserver.interpreter.CommandHandler;
import it.pm.jtelnetserver.interpreter.ExecutionReport;
import java.io.File;


/**
 * This command handler allows to create a file resource locally to the server.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class RmCommandHandler extends CommandHandler
{
    
    private static final String lineSeparator = System.getProperty("line.separator");
    
    // command usage
    private static final String cmdUsage = "rm: usage:"+lineSeparator+" rm [target]"+lineSeparator
            + "  options: not admitted"+lineSeparator
            + "  target: mandatory"+lineSeparator
            + "    'stuff.blah'    to remove the file named stuff.blah"+lineSeparator;
    // command pattern checker
    private static final String RES_PATTERN = "([a-zA-Z0-9]+.[a-z0-9]+)";

    public RmCommandHandler() 
    {
        super();
        cmdName = "rm";
        cmdDescription = "Remove a file resource locally (with respect to the working"
                + " directory) created.";
    }
    
    @Override
    public ExecutionReport handleCommand(ClientCommand clientQuery, ServerSession clientSess) 
    {
        ExecutionReport report = new ExecutionReport();
        String errorMsg = "";
        if(clientQuery.getCmd().equalsIgnoreCase("rm")) {
            if(clientQuery.getOptions().size() > 0 || clientQuery.getTarget().isEmpty()) {
               // invalid number of options: misusage
               errorMsg = "telnet: ";
               errorMsg += clientQuery.getOriginalQuery();
               errorMsg += ": number of options not allowed, mandatory target: command misuse"+lineSeparator;
               errorMsg += printUsage();
               // set and return the report
               report.setSuccess(false);
               report.setExecutionOutput(errorMsg);
               return report;
            }
            if(!clientQuery.getTarget().matches(RES_PATTERN)) {
                // invalid number of options: misusage
               errorMsg = "telnet: ";
               errorMsg += clientQuery.getOriginalQuery();
               errorMsg += ": target must be a canonical filename: command misuse"+lineSeparator;
               errorMsg += printUsage();
               // set and return the report
               report.setSuccess(false);
               report.setExecutionOutput(errorMsg);
               return report;
            }
            // create the resource locally
            String newResource = clientSess.getWorkingDir().getAbsolutePath();
            newResource += File.separator;
            newResource += clientQuery.getTarget();
            File fToRemove = new File(newResource);
            if(!fToRemove.exists()) {
                // an error occurred
                report.setSuccess(true);
                report.setExecutionOutput("telnet: "+newResource+" doesn't exists: misuse");
                return report;
            }
            fToRemove.delete();
            report.setSuccess(false);
            report.setExecutionOutput("telnet: "+newResource+" removed: ls to confirm");
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
