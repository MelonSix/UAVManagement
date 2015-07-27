/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.dmcore.util.dateTime;

/**
 *
 * @author AG BRIGHTER
 * Produces the timestamp
 */
public class OneM2mTimestamp {
    private static GetXmlDateTimeObj gtXmlObj = new GetXmlDateTimeObj();
    
    /**
     * pattern is \d{4}(0[1-9]|1[0-2])(0[1-9]|[1-2]\d|30|31)T([01]\d|2[0-3])[0-5]\d[0-5]\d <br/>
     * e.g. 19901223T125711
     * @return 
     */
    public static String getTimeStamp()
    {
        String timestamp = null;
        XmlDateTimeType dt = gtXmlObj.prepareXmlDateTime();
        timestamp = dt.getDate().toString().replace("-", "")+"T"+dt.getTime().toString().replace(":", "");
        return timestamp;
    }
    
}
