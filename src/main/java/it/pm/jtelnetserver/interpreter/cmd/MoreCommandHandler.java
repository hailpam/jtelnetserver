
package it.pm.jtelnetserver.interpreter.cmd;

import it.pm.jtelnetserver.context.ServerSession;
import it.pm.jtelnetserver.interpreter.ClientCommand;
import it.pm.jtelnetserver.interpreter.CommandHandler;
import it.pm.jtelnetserver.interpreter.ExecutionReport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 * This command handler allows to create a file resource locally to the server.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class MoreCommandHandler extends CommandHandler
{

    private static final String lineSeparator = System.getProperty("line.separator");
    
    // command usage
    private static final String cmdUsage = "more: usage:"+lineSeparator+" more [target]"+lineSeparator
            + "  options: not admitted"+lineSeparator
            + "  target: mandatory"+lineSeparator
            + "    'stuff.blah'    to display the content of the file "+lineSeparator
            + "named stuff.blah"+lineSeparator;
    // command pattern checker
    private static final String RES_PATTERN = "([a-zA-Z0-9]+\\.{1}[a-z0-9]+)";

    public MoreCommandHandler() 
    {
        super();
        cmdName = "more";
        cmdDescription = "Display the content of a locally stored file (with "
                + "respect to the working directory)";
    }
    
    @Override
    public ExecutionReport handleCommand(ClientCommand clientQuery, ServerSession clientSess) 
    {
        ExecutionReport report = new ExecutionReport();
        String errorMsg = "";
        BufferedReader contentReader = null;
        if(clientQuery.getCmd().equalsIgnoreCase("more")) {
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
            File fToRead = new File(newResource);
            if(!fToRead.exists() || fToRead.isDirectory()) {
                // an error occurred
                report.setSuccess(true);
                report.setExecutionOutput("telnet: "+newResource+" doesn't exists: "
                        + "it's not a readable");
                return report;
            }
            StringBuilder strBuilder = new StringBuilder();
            // opening the file
            try {
                // open the file and read its content
                contentReader = new BufferedReader(new FileReader(fToRead));
                String line = "";
                do {
                    line = contentReader.readLine();
                    if(line != null) {
                        strBuilder.append(line);
                        strBuilder.append("\n");
                    }
                }while(line != null);
            } catch (FileNotFoundException ex) {
                // an error occurred
                report.setSuccess(false);
                report.setExecutionOutput("telnet: "+newResource+" doesn't exists: misuse");
                return report;
            } catch (IOException ex) {
                report.setSuccess(false);
                report.setExecutionOutput("telnet: error in reading the resource: I/O error");
                return report;
            }finally {
                try {
                    contentReader.close();
                } catch (IOException ex) {
                }
            }
            report.setSuccess(true);
            if(strBuilder.toString().isEmpty())
                report.setExecutionOutput("telnet: empty file");
            else
                report.setExecutionOutput(strBuilder.toString());
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
