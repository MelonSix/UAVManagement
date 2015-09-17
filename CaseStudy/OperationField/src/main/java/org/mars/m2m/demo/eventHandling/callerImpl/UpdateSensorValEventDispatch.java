/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.eventHandling.callerImpl;

import ch.qos.logback.classic.Logger;
import java.util.EventListener;
import javax.swing.event.EventListenerList;
import org.mars.m2m.demo.eventHandling.Listerners.DetectedObstacleListener;
import org.mars.m2m.demo.eventHandling.Listerners.DetectedThreatListener;
import org.mars.m2m.demo.eventHandling.Listerners.FlightControlListener;
import org.mars.m2m.demo.eventHandling.eventObject.DetectedObstacleEventObject;
import org.mars.m2m.demo.eventHandling.eventObject.DetectedThreatEventObject;
import org.mars.m2m.demo.eventHandling.eventObject.FlightControlEventObject;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class UpdateSensorValEventDispatch 
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(UpdateSensorValEventDispatch.class);
    
    private final EventListenerList listenerList;

    public UpdateSensorValEventDispatch() {
        this.listenerList = new EventListenerList();
    }
    
    /**
     * Adds an update listener
     * @param <E>
     * @param listener
     * @param clazz 
     */
    public <E extends EventListener> void addListener(E listener, Class clazz)
    {
        if(listener != null)
        {
            listenerList.add(clazz, listener);
        }
    }
    
    /**
     * Removes an update listener
     * @param <E>
     * @param listener
     * @param clazz 
     */
    public <E extends EventListener> void removeListener(E listener, Class clazz)
    {
        listenerList.remove(clazz, listener);
    }
    
    /**
     * Updates the way points of a flight control device
     * @param eventObject 
     */
    public void updateWaypoints(FlightControlEventObject eventObject)
    {
        FlightControlListener[] listeners;
        listeners = listenerList.getListeners(FlightControlListener.class);
        for(FlightControlListener listener : listeners)
        {
            listener.updateYwaypoints(eventObject);
        }
    }
    
    /**
     * Updates the sensor value of the obstacle sensor
     * @param eventObject 
     */
    public void updateObstacleSensorValue(DetectedObstacleEventObject eventObject)
    {
        DetectedObstacleListener[] listeners;
        listeners = listenerList.getListeners(DetectedObstacleListener.class);
        for(DetectedObstacleListener listener : listeners)
        {
            listener.fireSensorResourceUpdate(eventObject);
        }
    }
    
    /**
     * Updates the sensor value of the threat sensor
     * @param eventObject 
     */
    public void updateThreatSensorValue(DetectedThreatEventObject eventObject)
    {
        DetectedThreatListener[] listeners;
        listeners = listenerList.getListeners(DetectedThreatListener.class);
        for(DetectedThreatListener listener : listeners)
        {
            listener.fireSensorResourceUpdate(eventObject);
        }
    }
}
