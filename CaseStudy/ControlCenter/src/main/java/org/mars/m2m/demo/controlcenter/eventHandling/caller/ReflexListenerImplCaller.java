/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mars.m2m.demo.controlcenter.eventHandling.caller;

import ch.qos.logback.classic.Logger;
import java.util.ArrayList;
import org.mars.m2m.demo.controlcenter.eventHandling.Listerners.ReflexListener;
import org.mars.m2m.dmcore.model.ReportedLwM2MClient;
import org.slf4j.LoggerFactory;

/**
 *
 * @author AG BRIGHTER
 */
public class ReflexListenerImplCaller 
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ReflexListenerImplCaller.class);
    private ArrayList<ReflexListener> listeners;
    
    public ReflexListenerImplCaller() {
        this.listeners = new ArrayList<>();
    }
    
    public void addReflexListener(ReflexListener reflexListener)
    {
        if(this.listeners != null && !this.listeners.contains(reflexListener))
        {
            this.listeners.add(reflexListener);
        }
    }
    
    public void removeReflexListener(ReflexListener reflexListener)
    {
        if(this.listeners != null && this.listeners.contains(reflexListener))
        {
            this.listeners.remove(reflexListener);
        }
    }
    
    /**
     * Sends the scouting plan using a registered reflex listener
     * @param device 
     */
    public void assignScout(final ReportedLwM2MClient device)
    {
        if(this.listeners != null)
        {
            for(ReflexListener listener : this.listeners)
            {
                listener.sendScoutingPlan(device);
            }
        }
    }
    
    /**
     * Calls a registered callback method to send the observation request to the resource
     * @param device 
     */
    public void sendObservationRequest(final ReportedLwM2MClient device)
    {
        if(this.listeners != null)
        {
            for(ReflexListener listener : this.listeners)
            {
                listener.sendObservationRequest(device);
            }
        }
    }
}
