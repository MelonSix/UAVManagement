/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.Exceptions;

/**
 *
 * @author AG BRIGHTER
 */
public class DeviceStarterDetailsException extends Exception  {
    
    private static final long serialVersionUID = 19974972098766112L;
    
    /**
     * 
     */
    public DeviceStarterDetailsException()
    {        
    }
    
    public DeviceStarterDetailsException(String message)
    {
        super(message);
    }
    
    public DeviceStarterDetailsException(Throwable cause)
    {
        super(cause);
    }
    
    public DeviceStarterDetailsException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public DeviceStarterDetailsException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace)
    {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
