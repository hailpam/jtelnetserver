
package it.pm.jtelnetserver.util;

/**
 * It defines the server specific exception type (it should be raised 
 * systematically by each server module/component).
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class ServiceException extends Exception 
{

    public ServiceException(String errMsg) 
    {
        super(errMsg);
    }
    
}
