/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.dmcore.util;

import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.UUID;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.Operation;
import org.mars.m2m.dmcore.onem2m.enumerationTypes.ResponseStatusCode;
import org.mars.m2m.dmcore.util.dateTime.GetXmlDateTimeObj;
import org.mars.m2m.dmcore.util.dateTime.XmlDateTimeType;

/**
 *
 * @author AG BRIGHTER
 */
public class DmCommons {
    
    private static final GetXmlDateTimeObj gtXmlObj = new GetXmlDateTimeObj();
    
    /**
     * pattern is \d{4}(0[1-9]|1[0-2])(0[1-9]|[1-2]\d|30|31)T([01]\d|2[0-3])[0-5]\d[0-5]\d <br/>
     * e.g. 19901223T125711
     * @return 
     */
    public static String getOneM2mTimeStamp()
    {
        String timestamp = null;
        try 
        {
            XmlDateTimeType dt = gtXmlObj.prepareXmlDateTime(null);
            timestamp = dt.getDate().toString().replace("-", "") + "T" + dt.getTime().toString().replace(":", "");
        } catch (java.lang.NullPointerException e) {
        }
        return timestamp;
    }
    
    public static String setOneM2mTimeStamp(GregorianCalendar date)
    {
        String timestamp = null;
        XmlDateTimeType dt = gtXmlObj.prepareXmlDateTime(date);
        timestamp = dt.getDate().toString().replace("-", "")+"T"+dt.getTime().toString().replace(":", "");
        return timestamp;
    }
    
    /**
     * For determining the operation in a request
     * @param val The integer correspondent of CRUDN operation in a request primitive
     * @return The corresponding enumeration instance of the operation
     */
    public synchronized static Operation determineOneM2mOperation(BigInteger val)
    {
        switch(val.intValue())
        {
            case 1:
                return Operation.CREATE;
            case 2:
                return Operation.RETRIEVE;
            case 3:
                return Operation.UPDATE;
            case 4:
                return Operation.DELETE;
            case 5:
                return Operation.NOTIFY;
            default:
                return null;
        }
    }
    
    /**
     * For returning a unique string as an ID
     * @return 
     */
    public static String generateID()
    {
        return UUID.randomUUID().toString();
    }
    
    /**
     * For selecting appropriate OneM2M status code for an HTTP returned status code
     * @param statusCode The returned HTTP status code
     * @return  The OneM2M status code
     */
    public static ResponseStatusCode getOneM2mStatusCode(int statusCode)
    {
        switch(statusCode)
        {
            case 200:
                return ResponseStatusCode.OK;
            case 201:
                return ResponseStatusCode.CREATED;
            case 202:
                return ResponseStatusCode.ACCEPTED;
            case 203:
                return ResponseStatusCode.OK;
            /**
             * TODO: Other HTTP status code cases yet to be included
             */    
            default:
                return ResponseStatusCode.BAD_REQUEST;
        }
    }
}
