
package it.pm.jtelnetserver.interpreter.cmd;

import it.pm.jtelnetserver.context.ServerSession;
import it.pm.jtelnetserver.interpreter.ClientCommand;
import it.pm.jtelnetserver.interpreter.CommandHandler;
import it.pm.jtelnetserver.interpreter.ExecutionReport;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

/**
 * This command handler implements the logic to list the file contained in the
 * working directory. It supports some options able to adapt the display according
 * to the needs.
 *
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class LsCommandHandler extends CommandHandler
{
    private static final String lineSeparator = System.getProperty("line.separator");
    
    private static final String cmdUsage = "ls: usage:"+lineSeparator+" mkdir [target]"+lineSeparator
            + "  options:"+lineSeparator
            + "    '-a'        to not ignore hidden entries (starting with .)"+lineSeparator
            + "  target: can be"+lineSeparator
            + "    'stuff'     to list with reference to a particular directory stuff"
             + " (assuming the relative path from working directory)"+lineSeparator;
     
    // a format pattern
    private static final String FORMAT_PATTERN = "%-3.3s %-1.1s %-1.1s %-1.4s "
            + "%-8.8s %-8.8s %-70.70s"+lineSeparator;

    public LsCommandHandler() 
    {
        super();
        cmdName = "ls";
        cmdDescription = "List the files/directories contained in the working "
                + "directory (session scoped)";
    }
    
    @Override
    public ExecutionReport handleCommand(ClientCommand clientQuery, ServerSession clientSess) 
    {
       if(clientQuery.getCmd().equalsIgnoreCase("ls")) {
           ExecutionReport report = new ExecutionReport();
           File dirToList = null;
           String errorMsg = "";
           boolean okHidden = false;
           if(clientQuery.getOptions().size() > 1) { // too many options
               // invalid number of options: misusage
               errorMsg = "telnet: ";
               errorMsg += clientQuery.getOriginalQuery();
               errorMsg += ": number of options not allowed"+lineSeparator;
               errorMsg += printUsage();
               // set and return the report
               report.setSuccess(false);
               report.setExecutionOutput(errorMsg);
               return report;
           }
           if(clientQuery.getOptions().size() == 1) { // only 1 option
               if(!clientQuery.getOptions().get(0).equalsIgnoreCase("a")) {
                   // invalid option
                   errorMsg = "telnet: ";
                   errorMsg += clientQuery.getOriginalQuery();
                   errorMsg += ": invalid option"+lineSeparator;
                   errorMsg += printUsage();
                   // set and return the report
                   report.setSuccess(false);
                   report.setExecutionOutput(errorMsg);
                   return report;
               }else // ok, hidden will be accepted
                   okHidden = true;
           }
           if(!clientQuery.getTarget().isEmpty()) { // list the target directory
               if(!clientQuery.getTarget().matches("[a-zA-Z0-9]+")) { // bad target
                    // invalid target
                    errorMsg = "telnet: ";
                    errorMsg += clientQuery.getTarget();
                    errorMsg += ": invalid target for listing"+lineSeparator;
                    errorMsg += printUsage();
                    // set and return the report
                    report.setSuccess(false);
                    report.setExecutionOutput(errorMsg);
                    return report;
                }
               String newDir = "";
               newDir = clientSess.getWorkingDir().getAbsolutePath();
               newDir += File.separator;
               newDir += clientQuery.getTarget();
               dirToList = new File(newDir);
               if(!dirToList.exists()) {
                   // target doesn't exist in the working directory
                    errorMsg = "telnet: ";
                    errorMsg += newDir;
                    errorMsg += ": cannot access: no such directory"+lineSeparator;
                    errorMsg += printUsage();
                    // set and return the report
                    report.setSuccess(false);
                    report.setExecutionOutput(errorMsg);
                    return report;
               }
           }
           if(dirToList == null)
               dirToList = clientSess.getWorkingDir();
           File[] listOfRes = dirToList.listFiles();
           StringBuilder lsBuilder = new StringBuilder();
           Formatter lsFormatter = new Formatter(lsBuilder);
           String type = "";
           String lastModified;
           String name = "";
           String size = "";
           String canRead = "";
           String canWrite = "";
           String canExecute = "";
           Date fileLastMod;
           SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM");
           int cntr = 0;
           for(int i = 0; i < listOfRes.length; i++) {
               if(okHidden || !listOfRes[i].isHidden()) { // hidden filtered if okHidden=false
                   type = ((listOfRes[i].isDirectory())?"d":"f");
                   canRead = ((listOfRes[i].canRead())?"r":"-");
                   canWrite = ((listOfRes[i].canWrite())?"w":"-");
                   canExecute = ((listOfRes[i].canExecute())?"e":"-");
                   name = listOfRes[i].getName();
                   // size in kilobytes
                   size = Integer.toString((int) Math.floor(listOfRes[i].length() / 1024));
                   fileLastMod = new Date(listOfRes[i].lastModified());
                   lastModified = dateFormatter.format(fileLastMod);
                   lsFormatter.format(FORMAT_PATTERN, type, canRead, canWrite, 
                           canExecute, size, lastModified, name);
                   cntr += 1;
               }
           }
           String output = "total ";
           output += cntr;
           output += lineSeparator;
           output += lsBuilder.toString();
           // according to the above processing, the report is buit and returned
           report.setSuccess(true);
           report.setExecutionOutput(output);
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
