
package it.pm.jtelnetserver.interpreter.cmd;

import it.pm.jtelnetserver.context.ServerSession;
import it.pm.jtelnetserver.interpreter.ClientCommand;
import it.pm.jtelnetserver.interpreter.CommandHandler;
import it.pm.jtelnetserver.interpreter.ExecutionReport;
import java.io.File;

/**
 * This command handler implements the logic to create a new directory in the
 * actual working directory.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class MkdirCommandHandler extends CommandHandler
{
    
    private static final String lineSeparator = System.getProperty("line.separator");

    private static final String cmdUsage = "mkdir: usage:"+lineSeparator+" mkdir [target]"+lineSeparator
            + "  options: not admitted"+lineSeparator
            + "  target: can be"+lineSeparator
            + "    'stuff'     to create locally the new directory named stuff"+lineSeparator;

    public MkdirCommandHandler() 
    {
        super();
        // descriptions initialization
        cmdName = "mkdir";
        cmdDescription = "Create a new directory in the working directory (session scoped)";
    }
    
    @Override
    public ExecutionReport handleCommand(ClientCommand clientQuery, ServerSession clientSess) 
    {
        if(clientQuery.getCmd().equalsIgnoreCase("mkdir")) {
            ExecutionReport report = new ExecutionReport();
            if(clientQuery.getOptions().size() > 0 || 
                    clientQuery.getTarget().isEmpty()) {
                report.setSuccess(false);
                report.setExecutionOutput(printUsage());
                // in this version, options are not yet supported
                return report;
            }
            // analysing the target
            if(!clientQuery.getTarget().matches("[a-zA-Z0-9]+")) {
                String errorMsg = "telnet: ";
                errorMsg += clientQuery.getTarget();
                errorMsg += ": target not allowed"+lineSeparator;
                errorMsg += printUsage();
                report.setExecutionOutput(errorMsg);
                report.setSuccess(false);
                // return immediately
                return report;
            }else { // it's a valid target
                String output = "";
                boolean outcome = false;
                String newDir = clientSess.getWorkingDir().getAbsolutePath();
                newDir += File.separator;
                newDir += clientQuery.getTarget();
                File toBeCreated = new File(newDir);
                toBeCreated.mkdir();
                if(toBeCreated.exists()) {
                    outcome = true;
                    output = newDir;
                    output += lineSeparator;
                }else { // error in creation
                    outcome = false;
                    output = "telnet: ";
                    output += ((newDir == null || newDir.isEmpty())?"N/E":newDir);
                    output += ": internal server error in creation";
                }
                // fill-in the report and return it
                report.setExecutionOutput(output);
                report.setSuccess(outcome);
                return report;
            }
        }else if(successor != null) {
             // forward to the next piped handler 
            return successor.handleCommand(clientQuery, clientSess);
        }else {
            ExecutionReport report = new ExecutionReport();
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
