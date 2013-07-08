
package it.pm.jtelnetserver.thread;

import it.pm.jtelnetserver.context.ServerContext;
import it.pm.jtelnetserver.display.DisplayHelper;
import it.pm.jtelnetserver.interpreter.ClientCommand;
import it.pm.jtelnetserver.interpreter.CommandInterpreter;
import it.pm.jtelnetserver.interpreter.CommandParser;
import it.pm.jtelnetserver.interpreter.cmd.CdCommandHandler;
import it.pm.jtelnetserver.interpreter.cmd.LsCommandHandler;
import it.pm.jtelnetserver.interpreter.cmd.MkdirCommandHandler;
import it.pm.jtelnetserver.interpreter.cmd.MoreCommandHandler;
import it.pm.jtelnetserver.interpreter.cmd.PwdCommandHandler;
import it.pm.jtelnetserver.interpreter.cmd.RmCommandHandler;
import it.pm.jtelnetserver.interpreter.cmd.TouchCommandHandler;
import it.pm.jtelnetserver.util.ServiceException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements a Runnable entity, so it can be detached as a Thread.
 * It will be used in the thread pool to server individually a client session.
 *  
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class ServiceThread implements Runnable
{
    // logger
    private static final Logger theLogger = Logger.getLogger(ServiceThread.class.getName());

    // it maintains the client session socket
    private Socket sessionSocket;
    // thread Id
    private String threadId;
    // display helper: manages the display
    private DisplayHelper display;
    // parser: manages the parsing activities
    private CommandParser parser;
    // interpreter: manages the interpretation of the commands
    private CommandInterpreter interpreter;
    
    // in case of session abort by client, this will be false
    private boolean sessionOpen;
    
    // stream to read the client commands
    private BufferedReader cmdReader;
    // stream to write the server responses
    private PrintWriter responseWriter;
    
    public ServiceThread(Socket clientChannel, String threadIdentity) 
    {
        super();
        sessionSocket = clientChannel;
        threadId = threadIdentity;
        sessionOpen = true;
        // initializing the command interpreter facilities
        display = new DisplayHelper();
        parser = new CommandParser();
        interpreter = new CommandInterpreter();
    }
    
    public void run() 
    {
        theLogger.info("Entering the Client Worker Thread :: "+threadId);
        try {
            registerCommands();
        } catch (ServiceException ex) {
            theLogger.log(Level.SEVERE, "Unable to register the commmands: impossible to proceed", 
                    ex);
            try {
                sessionSocket.close();
            } catch (IOException ex1) {
                theLogger.log(Level.SEVERE, "Error Caught in closing the client socket", 
                        ex1);
            }
            return;
        }
        try {
            // initializing the streams
            cmdReader = new BufferedReader(
                    new InputStreamReader(sessionSocket.getInputStream()));
            responseWriter = new PrintWriter(sessionSocket.getOutputStream(), true);
            // show welcome display
            responseWriter.print(display.welcomeDisplay());
            responseWriter.flush();
            // server while: it waits command forwarded by the client
            theLogger.info("Thread correctly initialized :: READY to accept commands...");
            String clientQuery = "";
            String clientResponse = "";
            boolean parsingOutcome;
            while(sessionOpen) {
                clientQuery = cmdReader.readLine();
                theLogger.info("Treating the command query :: " +clientQuery);
                // normal management flow
                parsingOutcome = parser.isValidQuery(clientQuery);
                if(!parsingOutcome) {
                    responseWriter.print(display.errorDisplay(clientQuery));
                    responseWriter.flush();
                    continue;
                }
                // command validated
                ClientCommand cmd = parser.buildCommand(clientQuery);
                if(cmd == null) {
                    responseWriter.print(display.errorDisplay(clientQuery));
                    responseWriter.flush();
                    continue;
                }
                // command correctly built
                clientResponse = interpreter.interpretCommand(cmd);
                if(clientResponse.contains("${RELEASE}")) {
                    clientResponse = clientResponse.replace("${RELEASE}", "");
                    sessionOpen = false;
                    theLogger.info("Session released as requested by the client");
                }
                responseWriter.print(display.decoratedDisplay(clientResponse));
                responseWriter.flush();
            }
        } catch (IOException ex) {
             theLogger.log(Level.SEVERE, "Error Caught: I/O exception, unable to proceed", 
                        ex);
        } catch (ServiceException ex) {
            theLogger.log(Level.SEVERE, "Error Caught: Interpreter Unable to interpret the Query", 
                        ex);
        }finally {
            try {
                sessionSocket.close();
                responseWriter.close();
            } catch (IOException ex) {
                 theLogger.log(Level.SEVERE, "Error Caught in closing the client socket", 
                        ex);
            }
        }
    }
    
    /**
     * Internal Utility method to register the commands.
     * 
     * @throws ServiceException In case some step goes wrong in the registration
     */
    private void registerCommands() throws ServiceException
    {
        // TODO : to be enhanced with an automatic mechanism
        LsCommandHandler lsCmd = new LsCommandHandler();
        MkdirCommandHandler mkdirCmd = new MkdirCommandHandler();
        CdCommandHandler cdCmd = new CdCommandHandler();
        PwdCommandHandler pwdCmd = new PwdCommandHandler();
        TouchCommandHandler touchCmd = new TouchCommandHandler();
        RmCommandHandler rmCmd = new RmCommandHandler();
        MoreCommandHandler moreCmd = new MoreCommandHandler();
        // create the processing pipe by registering the commands
        interpreter.addCommandHandler(lsCmd);
        interpreter.addCommandHandler(cdCmd);
        interpreter.addCommandHandler(mkdirCmd);
        interpreter.addCommandHandler(pwdCmd);
        interpreter.addCommandHandler(touchCmd);
        interpreter.addCommandHandler(rmCmd);
        interpreter.addCommandHandler(moreCmd);
        // set up the session for interpreter
        interpreter.setActualSession(ServerContext.getInstance().getServerSession(threadId));
        interpreter.setSessionId(threadId);
    }
    
}
