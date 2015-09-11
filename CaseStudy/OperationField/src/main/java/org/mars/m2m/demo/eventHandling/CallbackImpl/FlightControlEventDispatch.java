/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.eventHandling.CallbackImpl;

import ch.qos.logback.classic.Logger;
import javax.swing.event.EventListenerList;
import org.mars.m2m.demo.eventHandling.Listerners.FlightControlListener;
import org.mars.m2m.demo.eventHandling.eventObject.FlightControlEventObject;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class FlightControlEventDispatch 
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(FlightControlEventDispatch.class);
    
    private EventListenerList listenerList;

    public FlightControlEventDispatch() {
        this.listenerList = new EventListenerList();
    }
    
    public void addFlightControlListener(FlightControlListener listener)
    {
        if(listener != null)
        {
            listenerList.add(FlightControlListener.class, listener);
        }
    }
    
    public void removeFlightControlListener(FlightControlListener listener)
    {
        listenerList.remove(FlightControlListener.class, listener);
    }
    
    public void updateWaypoints(FlightControlEventObject eventObject)
    {
        FlightControlListener[] listeners;
        listeners = listenerList.getListeners(FlightControlListener.class);
        for(FlightControlListener listener : listeners)
        {
            listener.updateYwaypoints(eventObject);
        }
    }
}
