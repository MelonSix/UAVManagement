/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.dmcore.util.dateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author AG BRIGHTER
 */
public class GetXmlDateTimeObj 
{
    XmlDateTimeType xmlDateTime;
    GregorianCalendar now;
    ObjectFactory of;
    DatatypeFactory df;
    XMLGregorianCalendar gcDate;
    XMLGregorianCalendar gcTime;
    
    public GetXmlDateTimeObj() {
    }   
    
    public XmlDateTimeType prepareXmlDateTime(GregorianCalendar date)            
    {
        try 
        {
            of = new ObjectFactory();
            xmlDateTime = of.createDateTimeType();
            now = (date != null)? date : new GregorianCalendar();
            
            df = DatatypeFactory.newInstance();

            gcDate = df.newXMLGregorianCalendarDate(
                    now.get( Calendar.YEAR ),
                    now.get( Calendar.MONTH ),
                    now.get( Calendar.DAY_OF_MONTH ),
                    DatatypeConstants.FIELD_UNDEFINED );

            gcTime = df.newXMLGregorianCalendarTime(
                    now.get( Calendar.HOUR_OF_DAY ),
                    now.get( Calendar.MINUTE ),
                    now.get( Calendar.SECOND ),
                    null,                               // no fraction
                    DatatypeConstants.FIELD_UNDEFINED );

            // Insert sub-elements into the DateTimeType element.
            xmlDateTime.setDate( gcDate );
            xmlDateTime.setTime( gcTime );
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(GetXmlDateTimeObj.class.getName()).log(Level.SEVERE, null, ex);
        }
        return xmlDateTime;
    }
    
}
