package it.pm.jtelnetserver;

import it.pm.jtelnetserver.thread.ConnectorThread;
import java.io.File;


/**
 * Server Launcher main class.
 *
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class ServerLauncher 
{
   
    public static void main( String[] args )
    {
        String pathToCfgFile = "";
        boolean setCfgProperties = false;
        if(args.length > 0  && args[0] != null && args[1] != null) {
            System.out.println("[ServerLauncher][main] Found 1 argument :: "+args[0]);
            System.out.println("[ServerLauncher][main] Building the path to Configuration File");
            pathToCfgFile = System.getProperty("user.dir");
            pathToCfgFile += File.separator;
            pathToCfgFile += args[0];
            System.out.println("[ServerLauncher][main] Path built\n\t"+pathToCfgFile);
            File test = new File(pathToCfgFile+File.separator+args[1]);
            if(test.exists()) {
                System.out.println("[ServerLauncher][main] Found Configuration File"
                        + "\n\t"+test.getAbsolutePath());
                setCfgProperties = true;
            }
        }
        // starting the connector thread
        ConnectorThread connector = new ConnectorThread();
        if(setCfgProperties) {
            connector.setConfigPath(pathToCfgFile);
            connector.setConfigFile(args[1]);
        }
        System.out.println("Starting the Connector Thread...");
        new Thread(connector).start();
    }
    
}
