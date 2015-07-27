/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.uavendpoint.Validation;

import org.eclipse.leshan.util.Validate;
import org.mars.m2m.uavendpoint.Exceptions.DeviceStarterDetailsException;

/**
 *
 * @author AG BRIGHTER
 */
public final class StarterValidator {
    
    /**
     * Validates the string variables of the starter details of a device
     * @param string the string variable to be validated
     * @throws org.mars.m2m.uavendpoint.Exceptions.DeviceStarterDetailsException
     */
    public static void notNull(String string) throws DeviceStarterDetailsException
    {
        try
        {
            Validate.notEmpty(string, "Device starter details error");
        }
        catch(IllegalArgumentException ex)
        {
            throw new DeviceStarterDetailsException(ex.getMessage());
        }
    }
    
    /**
     * Validates the assigned port number of the device. <br/> It has to be within the ephemeral port number range
     * @param portnumber the port number to be validated
     */
    public static void notWellKnownPort(int portnumber) throws DeviceStarterDetailsException
    {
        if(!checkRange(portnumber, 1024, 65535, false, false))
        {
            throw new DeviceStarterDetailsException("Port number out of ephemeral range");
        }
    }
    
    /**
     * Checks if a number is not positive
     * @param value the number to be checked
     * @return true if it is not positive or false if it is positive
     */
    
    public static boolean notPositive(int value)
    {
        if(value < 1)
            return true;
        else
            return false;
    }
    
    /**
     * Checkes whether a value falls within a specified range
     * @param value the value to be checked
     * @param lowerBound the lower bound for the check
     * @param upperBound the upper bound for the check
     * @param lowerInclusive whether the value can be equal to the lower bound
     * @param upperInclusive whether the value can be equal to the upper bound
     * @return true if it falls within the range or false
     */
    public static boolean checkRange(int value, int lowerBound, int upperBound, boolean lowerInclusive, boolean upperInclusive)
    {
        if(lowerInclusive && upperInclusive) // x1 <= value <
        {    
            return (value >= lowerBound && value <= upperBound)? true : false;        
        }
        else
            if(lowerInclusive && !upperInclusive)
            {
                return (value >= lowerBound && value < upperBound)? true : false;
            }
        else
                if(!lowerInclusive && upperInclusive)
                {
                    return (value > lowerBound && value <= upperBound)? true : false;
                }
        else
                {    
                    return (value > lowerBound && value < upperBound)? true : false;
                }
    }
    
}
