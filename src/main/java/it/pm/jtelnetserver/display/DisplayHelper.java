
package it.pm.jtelnetserver.display;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class works as an helper to build well formatted display to return to
 * the session client.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class DisplayHelper 
{
    private static final String lineSeparator = System.getProperty("line.separator");
    
    private static final String responseTemplate = lineSeparator+"${RESPONSE}"+lineSeparator+"prompt> ";
    private static final String errorTemplate = lineSeparator+"telnet: ${QUERY}: command not"
            + " found or bad query"+lineSeparator+"  Usage: CMD [-OPTIONS] [TARGET]"+lineSeparator+"prompt> ";
    private static final String welcomeTemplate = lineSeparator+"Welcome to JTelnetServer v1.0"+lineSeparator
            + "Session Info:"+lineSeparator
            + " Local Time: ${DATE_AND_TIME}"+lineSeparator
            + " Connection Data"+lineSeparator
            + "  IP Address: ${HOST_IP}"+lineSeparator
            + "  Hostname: ${HOST_NAME}"+lineSeparator
            + "  OS Host: ${HOST_OS}"+lineSeparator+lineSeparator
            + "prompt> ";
    
    private static final Logger theLogger = Logger.getLogger(DisplayHelper.class.getName());

    public DisplayHelper() 
    {
        super();
    }
    
    /**
     * It aid in decorating the response provided by the command handler
     * 
     * @param response Response string provided by the command handler
     * @return A decorated version from the command handler response
     */
    public String decoratedDisplay(String response) 
    {
        String display = responseTemplate;
        if(response == null || response.isEmpty()) {
            display = display.replace("${RESPONSE}", "telnet: internal error: retry");
        }else
            display = display.replace("${RESPONSE}", response);
        // return the built display    
        return display;
    }

    /**
     * It builds an error display to be shown to the final user, in case the user
     * queries the system with an invalid combination.
     * 
     * @param faultyQuery The client query which hasn't passed the validation step
     * @return An error display ready to be displayed
     */
    public String errorDisplay(String faultyQuery) 
    {
        String display = errorTemplate;
        if(faultyQuery == null || faultyQuery.isEmpty()) {
            display = display.replace("${QUERY}", "N/A");
        }else
            display = display.replace("${QUERY}", faultyQuery);
        // return the above built display
        return display;
    }
    
    /**
     * It builds a welcome display to be shown to the client upon its first 
     * connection.
     * 
     * @return A string with a welcome display
     */
    public String welcomeDisplay() 
    {
        String display = welcomeTemplate;
        String ipAddress = "";
        String hostName = "";
        String osInfo = "";
        Date now = new Date();
        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
            hostName = InetAddress.getLocalHost().getHostName();
            osInfo = System.getProperty("os.name");
            osInfo += " ";
            osInfo += System.getProperty("os.arch");
            osInfo += " ";
            osInfo += System.getProperty("os.version");
            display = display.replace("${HOST_IP}", ipAddress);
            display = display.replace("${HOST_NAME}", hostName);
            display = display.replace("${HOST_OS}", osInfo);
            display = display.replace("${DATE_AND_TIME}", now.toString());
        } catch (UnknownHostException ex) {
            theLogger.log(Level.SEVERE, "Error Occurred in retrieving host info", ex);
            display = display.replace("${HOST_IP}", "N/A");
            display = display.replace("${HOST_NAME}", "N/A");
            display = display.replace("${HOST_OS}", "N/A");
            display = display.replace("${DATE_AND_TIME}", now.toString());
        }
        // return the above built display
        return display;
    }
    
    public String test() 
    {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("pippo");
        strBuilder.append(System.getProperty("line.separator"));
        strBuilder.append("pluto");
        strBuilder.append(System.getProperty("line.separator"));
        strBuilder.append("paperino");
        strBuilder.append(System.getProperty("line.separator"));
        return strBuilder.toString();
    }
    
}
