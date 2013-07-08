
package it.pm.jtelnetserver.interpreter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * It implements a command parsing facility. Each time a string (representing 
 * a used command to be executed locally on the server) is forwarded, it is 
 * parsed in order to validate the query and to build the corresponding command
 * container object.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class CommandParser 
{
    private static final String REG_EXPRESSION = "([a-z]+\\s?(-[a-zA-Z]+)?\\s?([a-zA-Z0-9/\\*\\.\\\\_:~-]+)?)";
    
    private Pattern cmdPattern;
    private Matcher queryMatcher;

    private String lastInvalidQuery;
    
    public CommandParser() 
    { 
        super(); 
        lastInvalidQuery = "";
        cmdPattern = Pattern.compile(REG_EXPRESSION);
    }
    
    /**
     * It defines the validation logic against the received String.
     * 
     * @param query A string representing the user query
     * @return  true, iff the query can be follow up as a command
     */
    public boolean isValidQuery(String query) 
    {
        if(query == null)
            return false;
        if(query.isEmpty())
            return false;
        // it doesn't contain blank spaces but contains options
        queryMatcher = cmdPattern.matcher(query);
        if(!queryMatcher.matches()) {
            lastInvalidQuery = query;
            return false;
        }
        // the query is valid, it can be processed
        return true;
    }
    
    /**
     * It builds the client command to be forwarded to the interpreter. It
     * returns NULL if an invalid query (previously validated) is passed as input.
     * 
     * @param query A validated string representing the user query
     * @return A client command built by the client query
     */
    public ClientCommand buildCommand(String query) 
    {
        if(query == null)
            return null;
        if(query.isEmpty() || query.equals(lastInvalidQuery))
            return null;
        ClientCommand cCmd = new ClientCommand();
        cCmd.setOriginalQuery(query);
        String[] cComponents = query.split("\\s");
        for(int i = 0; i < cComponents.length; i++) {
            if(i == 0)
                cCmd.setCmd(cComponents[i]);
            else if(cComponents[i].matches("(-[a-zA-Z]+)")) { // options
                String tmp = cComponents[i].replace("-", "");
                for(int j = 0; j < tmp.length(); j++) {
                    cCmd.getOptions().add(""+tmp.charAt(j));
                }
            }else
                cCmd.setTarget(cComponents[i]);
        }
        // return the command as above built
        return cCmd;
    }
    
}
