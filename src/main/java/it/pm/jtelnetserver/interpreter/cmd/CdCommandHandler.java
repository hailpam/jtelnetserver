
package it.pm.jtelnetserver.interpreter.cmd;

import it.pm.jtelnetserver.context.ServerSession;
import it.pm.jtelnetserver.interpreter.ClientCommand;
import it.pm.jtelnetserver.interpreter.CommandHandler;
import it.pm.jtelnetserver.interpreter.ExecutionReport;
import java.io.File;

/**
 * This command handler implements the logic to move through the system
 * directories, starting by the working directory (actually stored in the session).
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class CdCommandHandler extends CommandHandler
{
 
    private static final String lineSeparator = System.getProperty("line.separator");
    
    private static final String cmdUsage = "cd: usage:"+lineSeparator+" cd [target]"+lineSeparator
            + "  options: not admitted"+lineSeparator
            + "  target: can be"+lineSeparator
            + "    '..'        to access the parent directory"+lineSeparator
            + "    '../..'     to access the parent-parent directory (2 levels above)"+lineSeparator
            + "    'stuff'     to access stuff directory (if it is present locally)"+lineSeparator;

    public CdCommandHandler() 
    {
        super();
        // descriptions initialization
        cmdName = "cd";
        cmdDescription = "Change the current working directory (session scoped)";
    }

    @Override
    public ExecutionReport handleCommand(ClientCommand clientQuery, ServerSession clientSess) 
    {
        if(clientQuery.getCmd().equalsIgnoreCase("cd")) {
            // managing cd command
            ExecutionReport report = new ExecutionReport();
            if(clientQuery.getOptions().size() > 0 || 
                    clientQuery.getTarget().isEmpty()) {
                report.setSuccess(false);
                report.setExecutionOutput(printUsage());
                // in this version, options are not yet supported
                return report;
            }
            // analysing the target
            String output = "";
            boolean outcome = false;
            String newWorkDir = "";
            File fNerWorkDir = null;
            if(clientQuery.getTarget().contains("/") || 
                    clientQuery.getTarget().contains("..")) {
                // split produces at least 1 element for '..'
                String[] parents = clientQuery.getTarget().split("/");
                File tmpDir = clientSess.getWorkingDir();
                boolean tooDepth = false;
                for(int i = 0; i < parents.length; i++) {
                    newWorkDir = tmpDir.getParent();
                    if(newWorkDir != null && !newWorkDir.isEmpty())
                        tmpDir = new File(newWorkDir);
                    else
                        tooDepth = true;
                }
                if(!tooDepth)
                    fNerWorkDir = new File(newWorkDir);
            }else { // simple target: new directory
                if(clientQuery.getTarget().matches("[a-zA-Z0-9]+")) {
                    newWorkDir = clientSess.getWorkingDir().getAbsolutePath();
                    newWorkDir += File.separator;
                    newWorkDir += clientQuery.getTarget();
                    fNerWorkDir = new File(newWorkDir);
                }else { // exceptions like 'cd -', 'cd .'
                    String errorMsg = "telnet: ";
                    errorMsg += clientQuery.getTarget();
                    errorMsg += ": target not allowed"+lineSeparator;
                    errorMsg += printUsage();
                    report.setExecutionOutput(errorMsg);
                    report.setSuccess(false);
                    // return immediately
                    return report;
                }
            }
            // cross-checking the existence
            if(fNerWorkDir == null || !fNerWorkDir.exists()) {
                outcome = false;
                output = "telnet: ";
                output += ((newWorkDir == null || newWorkDir.isEmpty())?"N/E":newWorkDir);
                output += ": internal server error";
            }else {
                clientSess.setWorkingDir(fNerWorkDir);
                outcome = true;
                output = newWorkDir+""+lineSeparator;
                output = output.trim();
            }
            report.setSuccess(outcome);
            report.setExecutionOutput(output);
            // return the above built result
            return report;
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
